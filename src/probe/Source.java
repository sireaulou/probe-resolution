//Vincent Ching-Roa
//Last edit: 07/24/2017
//Description: 
//Source object. Only holds position for now.

package probe;

public class Source {
	double position[] = new double[3]; //x,y,z relative to probe 0,0,0

	//source constructor with position parameters relative to the probe
	public Source(double x, double y, double z){
		position[0] = x;
		position[1] = y;
		position[2] = z;
	}
	
	//Accessor(s)
	public double[] getPosition() {
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
