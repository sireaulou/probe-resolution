//Vincent Ching-Roa
//Last edit: 10/13/2017
//Description: 
//Performs genetic algorithm.
//Uses round robin selection or linear ranking selection (LRS).
//Refer to my doc for round robin selection. I should update it for LRS....
//Fixed silly LRS code (redundancy check)
//Log: 10/13 Made phantom a param (why I didn't do this in the first place i dont know)

package genetic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import controller.Controller;
import phantom.Phantom;

public class GeneticAlgorithm<PGene extends Gene> {
	//options
	int numSource; //number of sources 
	int numDetector; //number of detectors
	int poolSize; //maximum pool size
	int [] mutationRate; //mutation rate. [0] % mutation, [1] +/- bound degree of mutation
	int [] poolRemovalRange; //[0] lower, [1] upper bounds of pool removal 
	int [] offspringRange; //[0] lower, [1] upper bounds of offspring production
	double alpha,L; //alpha and L for SVD resolution analysis
	double epsilon; //termination criteria
	Queue<Double> cache; //queue memory for termination
	Gene fittest; //fittest individual in the current pool
	String file; //filename;
	int selectionMode; //0 = round robin selection. 1 = linear ranking selection.
	double totalN; //summation from 1 to poolsize for LRS probability calculation
	static Random rand = new Random(); //random number gen

	//constructor for round robin selection. SO MANY PARAMS
	public GeneticAlgorithm(String filename,
			int numSource, int numDetector,
			int poolSize, int[] mutationRate,
			int [] poolRemovalRange, int [] offspringRange,
			double alpha, double L,
			double epsilon){
		selectionMode = 0;
		this.numSource = numSource;
		this.numDetector = numDetector;
		this.poolSize =poolSize;
		this.mutationRate = mutationRate;
		this.poolRemovalRange = poolRemovalRange;
		this.offspringRange = offspringRange;
		this.alpha = alpha;
		this.L = L;
		this.epsilon=epsilon;
		cache = new LinkedList<Double>();
		this.file = filename;
	}
	
	//constructor for LRS.
	public GeneticAlgorithm(String filename,
			int numSource, int numDetector,
			int poolSize, int[] mutationRate,
			double alpha, double L,
			double epsilon){
		selectionMode = 1;
		this.numSource = numSource;
		this.numDetector = numDetector;
		this.poolSize =poolSize;
		this.mutationRate = mutationRate;
		this.alpha = alpha;
		this.L = L;
		this.epsilon=epsilon;
		cache = new LinkedList<Double>();
		this.file = filename;
		totalN = poolSize*(poolSize+1)/2;
	}
	
	//param is any type of Gene that you are using... again terrible code
	//mad cause bad
	public void start(PGene t, Phantom phantom){
		//Create phantom and controller
		//set probe gene global properties
//		Phantom phantom = new Phantom(
//				-1,-1,1.5,
//				11,11,11,
//				0.2,0.2,0.2,0.2,
//				0.05,8,1.4);
		Controller controller = new Controller(0, 1, 2d*Math.PI*70d*Math.pow(10, 6) );
		PGene.setGlobalProps(numSource, numDetector, phantom, controller, alpha, L, mutationRate);
		
		//generate pool (randomized probes)
		ArrayList<Gene> pool = new ArrayList<Gene>(poolSize);
		for(int i = 0; i < poolSize; i++){
			Gene probeGene = t.factory();
			pool.add(probeGene);
		}
		
		DecimalFormat df = new DecimalFormat(".##");
		FileWriter fstream;
		BufferedWriter out;
		try{
			//writer
			fstream = new FileWriter(file,true);
			out = new BufferedWriter(fstream);
			
			//evolutionary loop
			int generationCount = 0;
			while(true){
				//sort pool by fitness
				Collections.sort(pool);
				
				//print fittest
				out.write("Fittest "+generationCount);
				out.newLine();
				out.write("x: [");
				for(int f = 0; f<pool.get(0).pos.length; f++){
					out.write(df.format(pool.get(0).pos[f][0])+",");
				}
				out.write("];");
				out.newLine();
				out.write("y: [");
				for(int f = 0; f<pool.get(0).pos.length; f++){
					out.write(df.format(pool.get(0).pos[f][1])+",");
				}
				out.write("];");
				out.newLine();
				out.write(df.format(pool.get(0).res));
				out.newLine();
		 
				 
				
				//termination analysis
				cache.add(pool.get(0).res);
				if(cache.size()>=300){
					double runningAvg;
					double sum = 0;
					for(Double res:cache){
						sum = sum + res;
					}
					runningAvg = sum/cache.size(); 
					if(Math.abs(runningAvg-pool.get(0).res)<epsilon){
						fittest = pool.get(0);
						break;
					}
					cache.poll();
				}
				
				if(selectionMode == 0){
					//cull pool
					int cullPool = rand.nextInt(poolRemovalRange[1]-poolRemovalRange[0]+1)+poolRemovalRange[0];
					for(int cull = 0; cull < cullPool; cull ++){
							pool.remove(pool.size()-1);
					}
					
					//mating sesh
					int roundRobin = 1;
					while(pool.size()<poolSize){
						if(roundRobin>=pool.size()){
							roundRobin=1;
						}
						int offspringNum = rand.nextInt(offspringRange[1]-offspringRange[0]+1)+offspringRange[0];
						for(int o = 0; o < offspringNum; o ++){
							pool.add(pool.get(0).mate(pool.get(roundRobin)));
						}
						roundRobin++;
					}
				} else if(selectionMode == 1){
					//parent selection poolSize-1 times (elitism)
					int [][] parentPool = new int[poolSize-1][2];
					for(int n = 0; n<poolSize-1; n++){
						parentPool[n] = LRS(poolSize, totalN);
					}
					
					//mating sesh -> gen new pool
					ArrayList<Gene> newPool = new ArrayList<Gene>();
					newPool.add(pool.get(0)); // elitism
					for(int n = 0; n<poolSize-1; n++){
						newPool.add(pool.get(parentPool[n][0]).mate(pool.get(parentPool[n][0])));
					}

					//out with the old in with the new
					pool = newPool;
				}
				System.out.println(generationCount);
				out.flush();
				generationCount++;
			}
			out.close();
		} catch (Exception e){ 
			e.printStackTrace();
		}
		
	}
	
	public static int [] LRS(int poolSize,double totalN){
		//linear ranking selection 
		int d = -1;
		int v = -1;
		double check = 0;
		double prevCheck = 0;
		while(d==-1|v==-1|d==v){
			double p = rand.nextDouble();
			for(int i = 1; i < poolSize; i++){
				check = (poolSize - (double) i )/(totalN) + prevCheck;
				if(p<=check){
					if(d==-1){
						d = i-1;
						break;
					} else {
						v = i-1;
						break;
					}
				}
				prevCheck = check;
			}
		}
		return new int[]{d,v};
	}
}
