//Vincent Ching-Roa
//Last edit: 08/7/2017
//Description: 
//Gene abstract class... maybe interface would've been better
//Fixed silly comparator code

package genetic;

import java.util.Random;

import controller.Controller;
import phantom.Phantom;
import probe.Probe;

public abstract class Gene implements Comparable<Gene>{
	//static objects (bad practice but gotta save up on stuff)
	static Phantom phantom;
	static Controller controller;
	static int numSource;
	static int numDetector;
	static int [] mutationRate;
	static double alpha, L;
	static boolean haveOddSources;
	static boolean haveOddDetectors;
	static boolean initialized = false;
	static Random rand = new Random();
	//object vars
	Probe probe; 
	//position array. 1st index: ith element of the probe. 2nd index: [0]=x, [1]=y
	double[][] pos; 
	double res; //extrapolated resolution - currently using
	double [] fitness; //two output fitness - not currently used
	
	//set static properties. need to run this first in a genetic algorithm simulation
	public static void setGlobalProps(
			int ns, int nd,
			Phantom p, Controller c,
			double a, double len,
			int [] mr){
		numSource = ns;
		numDetector = nd;
		phantom = p;
		controller = c;
		alpha = a;
		L = len;
		mutationRate = mr;
		if(numSource%2==0){
			haveOddSources=false;
		} else {
			haveOddSources=true;
		}
		if(numDetector%2==0){
			haveOddDetectors=false;
		} else {
			haveOddDetectors=true;
		}
		initialized = true;
	}
	
	//constructor used to import probe (with blank position array)
	//used mostly internally
	public Gene(Probe probe){
		this.probe = probe;
		this.pos = new double[numSource+numDetector][2];
	}
	
	//random probe constructor
	public Gene() {
		initialize();
	}
	
	//comparator
	@Override
	public int compareTo(Gene other) {
		//extrapolated res fitness
		if(this.res==0||other.res==0){
			this.res();
			other.res();
		}
		
		if(this.res>other.res){
			return 1;
		} else if (this.res<other.res){
			return -1;
		} else {
			return 0;
		}
		
//		two-output fitness: refer to my documentation
//		if(this.fitness[0]==0||other.fitness[0]==0){
//			this.fitness();
//			other.fitness();
//		}
//		if(this.fitness[0]>other.fitness[0]){
//			return -1;
//		} else if (this.fitness[0]<other.fitness[0]){
//			return 1;
//		} else {
//			if(this.fitness[1]>other.fitness[1]){
//				return -1;
//			} else if (this.fitness[1]<other.fitness[1]){
//				return 1;
//			} else {
//				return 0;
//			}
//		}
	}	
	
	//calculate the extrapolated resolution
	public double res(){
		if(!initialized){
			return 0;
		}
		res = controller.fitness2(controller.svdDirectNoOutputLimit(probe,phantom), alpha, L);
		return res;
	}
	//calculate the two-output fitness 
	public double [] fitness(){
		if(!initialized){
			return new double[]{0,0};
		}
		fitness = controller.fitness(controller.svdDirectNoOutputLimit(probe,phantom), alpha, L);
		return fitness;
	}

	
	public abstract void initialize();
	public abstract Gene mate(Gene partner); //mating algorithm. random point crossover with Gene partner
	public abstract void mutate(); //mutation algorithm
	public abstract Gene factory(); //gene factory... gotta learn how to do this properly....
}
