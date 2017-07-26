//Vincent Ching-Roa
//Last edit: 07/25/2017
//Description: 
//Probe gene to be used with genetic algorithm
//Log: changed mate method from static to instance based
//	   implemented Gene abstract class

package genetic;
import probe.*;


public class ProbeGene extends Gene{
	
	public ProbeGene(Probe childProbe) {
		super();
	}

	public ProbeGene() {
		super();
	}

	//because im a bad coder and i dont know design patterns to go around constructor overriding
	//"constructor"/initializer for randomizing probe elements
	@Override
	public void initialize(){
		probe = new Probe(0,0,0);
		pos = new double[numSource+numDetector][2];
		for (int j = 0; j < numSource+numDetector; j++){
			
			double randx = rand.nextInt(801)*0.01-4;
			double randy = rand.nextInt(401)*0.01-2;
			
			pos[j][0] = randx;
			pos[j][1] = randy;

		}
		
		for(int k = 0 ; k < numSource; k++){
			Source source = new Source(pos[k][0],pos[k][1],0);
			probe.addSource(source);
			
		}
		
		for(int k = numSource; k < numSource+numDetector; k++){
			Detector detector = new Detector(pos[k][0],pos[k][1],0);
			probe.addDetector(detector);
		}
		this.res();
	}
	
	//mating algorithm. random point crossover with Gene partner
	@Override
	public ProbeGene mate(Gene partner){
		Probe childProbe = new Probe(0,0,0);
		ProbeGene child = new ProbeGene(childProbe);
		
		for(int i = 0; i <numDetector+numSource; i++){
			int parentChoice = rand.nextInt(2);
			if(parentChoice==0){
				child.pos[i][0]=this.pos[i][0];
				child.pos[i][1]=this.pos[i][1];
			} else {
				child.pos[i][0]=partner.pos[i][0];
				child.pos[i][1]=partner.pos[i][1];
			}
		}
		
		for(int k = 0 ; k < numSource; k++){
			Source source = new Source(child.pos[k][0],child.pos[k][1],0);
			child.probe.addSource(source);
		}
		
		for(int k = numSource; k < numSource+numDetector; k++){
			Detector detector = new Detector(child.pos[k][0],child.pos[k][1],0);
			child.probe.addDetector(detector);
		}
		
		child.mutate();
		return child;
	}
	
	//mutation algorithm
	@Override
	public void mutate(){
		
		for(int i = 0; i < numDetector+numSource; i++){
			if(rand.nextDouble()<=(mutationRate[0]/100d)){
				boolean valid = false;
				while(!valid){
					int mutationDegreeX = rand.nextInt(2*mutationRate[1]+1)-mutationRate[1];
					int mutationDegreeY = rand.nextInt(2*mutationRate[1]+1)-mutationRate[1];
					pos[i][0] = pos[i][0]+mutationDegreeX*0.01;
					pos[i][1] = pos[i][1]+mutationDegreeY*0.01;
					valid = true;
				}
			}
		}
		
		probe.clear();
		
		for(int k = 0 ; k < numSource; k++){
			Source source = new Source(pos[k][0],pos[k][1],0);
			probe.addSource(source);
		}
		
		for(int k = numSource; k < numSource+numDetector; k++){
			Detector detector = new Detector(pos[k][0],pos[k][1],0);
			probe.addDetector(detector);
		}
		this.res();
	}
	
	//gene factory... gotta learn how to do this properly....
	@Override
	public Gene factory(){
		return new ProbeGene();
	}
	
	

}
