//Vincent Ching-Roa
//Last edit: 07/25/2017
//Description: 
//Symmetrical probe gene to be used with genetic algorithm
//Log: changed mate method from static to instance based

package genetic;
import probe.*;


public class ProbeGeneSymmetrical extends Gene{
	
	public ProbeGeneSymmetrical(Probe childProbe){
		super();
	}
	
	public ProbeGeneSymmetrical() {
		super();
	}

	//because im a bad coder and i dont know design patterns to go around constructor overriding
	//"constructor"/initializer for randomizing probe elements
	@Override
	public void initialize(){
		probe = new Probe(0,0,0);
		pos = new double[numSource+numDetector][2];
		
		int detectorShift;

		if(haveOddSources){
			detectorShift = 1;
		} else {
			detectorShift = 0;
		}

		for(int j = 0; j < numSource/2; j++){
			double randx = rand.nextInt(401)*0.01;
			double randy = rand.nextInt(401)*0.01-2;
			pos[2*j][0] = randx;
			pos[2*j][1] = randy;
			pos[2*j+1][0] = -randx;
			pos[2*j+1][1] = randy;
		}
		if(haveOddSources){
			pos[numSource-1][0]=0;
			pos[numSource-1][1]=0;
		}
		for(int j = numSource/2; j < numSource/2+numDetector/2; j++){
			double randx = rand.nextInt(401)*0.01;
			double randy = rand.nextInt(401)*0.01-2;
			pos[2*j+detectorShift][0] = randx;
			pos[2*j+detectorShift][1] = randy;
			pos[2*j+1+detectorShift][0] = -randx;
			pos[2*j+1+detectorShift][1] = randy;
		}
		if(haveOddDetectors){
			pos[numSource+numDetector-1][0]=0;
			pos[numSource+numDetector-1][1]=0;
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
	
	//calculate the extrapolated resolution
	@Override
	public double res(){
		res = controller.fitness2(controller.svdDirectNoOutputLimit(probe,phantom), alpha, L);
		return res;
	}
	//calculate the two-output fitness 
	@Override
	public double [] fitness(){
		fitness = controller.fitness(controller.svdDirectNoOutputLimit(probe,phantom), alpha, L);
		return fitness;
	}
	
	//mating algorithm. random point crossover with Gene partner
	@Override
	public ProbeGeneSymmetrical mate(Gene partner){
		Probe childProbe = new Probe(0,0,0);
		ProbeGeneSymmetrical child = new ProbeGeneSymmetrical(childProbe);
		
		int detectorShift;

		if(haveOddSources){
			detectorShift = 1;
		} else {
			detectorShift = 0;
		}

		for(int j = 0; j < numSource/2; j++){
			int parentChoice = rand.nextInt(2);
			if(parentChoice==0){
				child.pos[2*j][0]=this.pos[2*j][0];
				child.pos[2*j][1]=this.pos[2*j][1];
				child.pos[2*j+1][0]=this.pos[2*j+1][0];
				child.pos[2*j+1][1]=this.pos[2*j+1][1];
			} else {
				child.pos[2*j][0]=partner.pos[2*j][0];
				child.pos[2*j][1]=partner.pos[2*j][1];
				child.pos[2*j+1][0]=partner.pos[2*j+1][0];
				child.pos[2*j+1][1]=partner.pos[2*j+1][1];
			}
		}
		if(haveOddSources){
			if(rand.nextInt(2)==0){
				child.pos[numSource-1][0]=this.pos[numSource-1][0];
				child.pos[numSource-1][1]=this.pos[numSource-1][1];
			} else {
				child.pos[numSource-1][0]=partner.pos[numSource-1][0];
				child.pos[numSource-1][1]=partner.pos[numSource-1][1];
			}
		}
		for(int j = numSource/2; j < numSource/2+numDetector/2; j++){
			int parentChoice = rand.nextInt(2);
			if(parentChoice==0){
				child.pos[2*j+detectorShift][0]=this.pos[2*j+detectorShift][0];
				child.pos[2*j+detectorShift][1]=this.pos[2*j+detectorShift][1];
				child.pos[2*j+detectorShift+1][0]=this.pos[2*j+detectorShift+1][0];
				child.pos[2*j+detectorShift+1][1]=this.pos[2*j+detectorShift+1][1];
			} else {
				child.pos[2*j+detectorShift][0]=partner.pos[2*j+detectorShift][0];
				child.pos[2*j+detectorShift][1]=partner.pos[2*j+detectorShift][1];
				child.pos[2*j+detectorShift+1][0]=partner.pos[2*j+detectorShift+1][0];
				child.pos[2*j+detectorShift+1][1]=partner.pos[2*j+detectorShift+1][1];
			}
		}
		if(haveOddDetectors){
			if(rand.nextInt(2)==0){
				child.pos[numSource+numDetector-1][0]=this.pos[numSource+numDetector-1][0];
				child.pos[numSource+numDetector-1][1]=this.pos[numSource+numDetector-1][1];
			} else {
				child.pos[numSource+numDetector-1][0]=partner.pos[numSource+numDetector-1][0];
				child.pos[numSource+numDetector-1][1]=partner.pos[numSource+numDetector-1][1];
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
		
		int detectorShift;

		if(haveOddSources){
			detectorShift = 1;
		} else {
			detectorShift = 0;
		}

		for(int j = 0; j < numSource/2; j++){
			if(rand.nextDouble()<=(mutationRate[0]/100d)){
				int mutationDegreeX = rand.nextInt(2*mutationRate[1]+1)-mutationRate[1];
				int mutationDegreeY = rand.nextInt(2*mutationRate[1]+1)-mutationRate[1];
				pos[2*j][0] = pos[2*j][0]+mutationDegreeX*0.01;
				pos[2*j][1] = pos[2*j][1]+mutationDegreeY*0.01;
				pos[2*j+1][0] = -pos[2*j][0];
				pos[2*j+1][1] = pos[2*j][1];
			}
		}
		if(haveOddSources){
			if(rand.nextDouble()<=(mutationRate[0]/100d)){
				int mutationDegreeX = rand.nextInt(2*mutationRate[1]+1)-mutationRate[1];
				int mutationDegreeY = rand.nextInt(2*mutationRate[1]+1)-mutationRate[1];
				pos[numSource-1][0] = pos[numSource-1][0]+mutationDegreeX*0.01;
				pos[numSource-1][1] = pos[numSource-1][1]+mutationDegreeY*0.01;
			}
		}
		for(int j = numSource/2; j < numSource/2+numDetector/2; j++){
			if(rand.nextDouble()<=(mutationRate[0]/100d)){
				int mutationDegreeX = rand.nextInt(2*mutationRate[1]+1)-mutationRate[1];
				int mutationDegreeY = rand.nextInt(2*mutationRate[1]+1)-mutationRate[1];
				pos[2*j+detectorShift][0] = pos[2*j+detectorShift][0]+mutationDegreeX*0.01;
				pos[2*j+detectorShift][1] = pos[2*j+detectorShift][1]+mutationDegreeY*0.01;
				pos[2*j+detectorShift+1][0] = -pos[2*j+detectorShift][0];
				pos[2*j+detectorShift+1][1] = pos[2*j+detectorShift][1];
			}
		}
		if(haveOddDetectors){
			if(rand.nextDouble()<=(mutationRate[0]/100d)){
				int mutationDegreeX = rand.nextInt(2*mutationRate[1]+1)-mutationRate[1];
				int mutationDegreeY = rand.nextInt(2*mutationRate[1]+1)-mutationRate[1];
				pos[numSource+numDetector-1][0] = pos[numSource+numDetector-1][0]+mutationDegreeX*0.01;
				pos[numSource+numDetector-1][1] = pos[numSource+numDetector-1][1]+mutationDegreeY*0.01;
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
			return new ProbeGeneSymmetrical();
		}
	
}
