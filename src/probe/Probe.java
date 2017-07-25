//Vincent Ching-Roa
//Last edit: 07/24/2017
//Description: 
//Probe object which holds sources and detectors
//Also contains its own scan path

package probe;
import java.util.LinkedList;
import java.util.List;

public class Probe {
	List<Source> sourceList;
	List<Detector> detectorList;
	double position[][]; //t;x,y,z
	double xFOV;
	double yFOV;
	double zFOV;
	
	//Probe Constructor with set positions and takes all sources and detectors
	//from previous probe
	public Probe(Probe template, double [][] position){
		this.addSource(template.getSourceList());
		this.addDetector(template.getDetectorList());
		this.position = position;
	}

	
	//Static Probe Constructor with static position parameters. 
	public Probe(double startX, double startY, double startZ){
		position = new double[1][3];
		position[0][0]=startX;
		position[0][1]=startY;
		position[0][2]=startZ;
		sourceList = new LinkedList<Source>();
		detectorList = new LinkedList<Detector>();
	}
	
	//Rectangular Probe Path Field Constructor
	//Parameters for initial position, 2D field of view and dx and dy of probe movement
	public Probe(
			double startX, double startY, double startZ,
			double xFOV, double yFOV,
			double dx, double dy){
		this.xFOV = xFOV;
		this.yFOV = yFOV;
		
		int xSteps = (int)(xFOV/dx);
		int ySteps = (int)(yFOV/dy);
		
		int t = (xSteps+1)*(ySteps+1);
		
		position = new double[t][3];

		int current_t = 0;
		for(int j = 0; j <= ySteps; j++){
			for(int i = 0; i <= xSteps; i++){
				position[current_t][0] = startX + i*dx;
				position[current_t][1] = startY + j*dy;
				position[current_t][2] = startZ;

//				//TROUBLESHOOTING
//				System.out.println(current_t);
//				System.out.println(position[current_t][0]);
//				System.out.println(position[current_t][1]);
//				System.out.println();
		
				current_t++;

			}
		}
		
		sourceList = new LinkedList<Source>();
		detectorList = new LinkedList<Detector>();
	}
	
	//Accessors start
	public double[][] getPosition(){
		return position;
	}

	public void addSource(Source source){
		sourceList.add(source);
	}
	
	public void addSource(double x, double y, double z){
		Source s = new Source(x,y,z);
		sourceList.add(s);
	}

	public void addSource(List<Source> sources){
		sourceList.addAll(sources);
	}

	public void addDetector(Detector detector){
		detectorList.add(detector);
	}
	
	public void addDetector(double x, double y, double z){
		Detector d = new Detector(x,y,z);
		detectorList.add(d);
	}

	public void addDetector(List<Detector> detectors){
		detectorList.addAll(detectors);
	}

	public List<Source> getSourceList(){
		return sourceList;
	}

	public List<Detector> getDetectorList(){
		return detectorList;
	}
	
	public void setPosition(double[][] pos){
		this.position = pos;
	}
	
	public void clear(){
		sourceList.clear();
		detectorList.clear();
	}
	//Accessors end 
	
	//Generates scan path as double[][]
	public static double[][] generateScan(double startX, double startY, double startZ,
			double xFOV, double yFOV,
			double dx, double dy){
		
		int xSteps = (int)(xFOV/dx);
		int ySteps = (int)(yFOV/dy);
		
		int t = (xSteps+1)*(ySteps+1);
		
		double[][] position = new double[t][3];

		int current_t = 0;
		for(int j = 0; j <= ySteps; j++){
			for(int i = 0; i <= xSteps; i++){
				position[current_t][0] = startX + i*dx;
				position[current_t][1] = startY + j*dy;
				position[current_t][2] = startZ;
				current_t++;

			}
		}
		
		return position;
	}
	
	//Premade design methods
	//transforms a probe into one of the premade probe designs. clear all probe elements beforehand
	public void linear7x4(){
		this.clear();
		Source middleSource = new Source(0, 1, 0);
		this.addSource(middleSource);
		for(int i = 1; i <= 3; i ++){
			Source sourceRight = new Source(i*0.7,-1,0);
			Source sourceLeft = new Source(-i*0.7,-1,0);
			this.addSource(sourceRight);
			this.addSource(sourceLeft);
		}
		for(int j = 0; j < 4; j ++){
			Detector det = new Detector(-3+j*2,1,0);
			this.addDetector(det);
		}
	}
	
	public void fan7x4(){
		this.clear();
		this.addSource(-2.07,-0.69,0);
		this.addSource(-1.244,-0.985,0);
		this.addSource(-0.5556,-0.95,0);
		this.addSource(0,-0.7321,0);
		this.addSource(0.5556,-0.95,0);
		this.addSource(1.244,-0.985,0);
		this.addSource(2.07,-0.69,0);
		this.addDetector(-3,1,0);
		this.addDetector(-1,1,0);
		this.addDetector(1,1,0);
		this.addDetector(3,1,0);
	}
	
	public void linear4x4(){
		this.clear();
		for(int i = 1; i <= 2; i ++){
			Source sourceRight = new Source(i-0.5,-1.25,0);
			Source sourceLeft = new Source(-i-0.5,-1.25,0);
			this.addSource(sourceRight);
			this.addSource(sourceLeft);
		}
		for(int j = 1; j <= 2; j ++){
			Detector detRight = new Detector(j-0.5,1.25,0);
			Detector detLeft = new Detector(-j-0.5,1.25,0);
			this.addDetector(detRight);
			this.addDetector(detLeft);
		}
	}
	
}
