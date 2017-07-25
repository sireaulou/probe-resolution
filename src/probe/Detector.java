//Vincent Ching-Roa
//Last edit: 07/24/2017
//Description: 
//Detector object. Only holds position for now

package probe;

public class Detector {
	double position[] = new double[3]; //x,y,z relative to probe 0,0,0
	
	//detector constructor with position parameters relative to the probe 
	public Detector(double x ,double y, double z){
		position[0] = x;
		position[1] = y;
		position[2] = z;
	}
	
	//Accessor(s)
	public double[] getPosition(){ 
		return position;
	}
	public double getX(){
		return position[0];
	}
	public double getY(){
		return position[1];
	}
	public double getZ(){
		return position[2];
	}
}
