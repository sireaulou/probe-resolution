//Vincent Ching-Roa
//Last edit: 07/24/2017
//Description: 
//Voxel object with position vector and optical properties mua and musp (cm^-1) and n (refractive index) 

package phantom;
public class Voxel {
	
	double position[] = new double[3]; //x,y,z relative to field.
	double mua; //absorption coefficient in cm^-1 
	double musp; //reduced scattering coefficient in cm^1-
	double n; //refractive index 
	
	//Voxel constructor with position and optical properties input
	public Voxel(double x, double y, double z, double mua, double musp, double n){
		position[0]=x;
		position[1]=y;
		position[2]=z;
		this.mua = mua;
		this.musp = musp;
		this.n = n;
	}
	
	
	//Accessor(s) 
	public double[] getPosition(){
		return position;
	}
	public double getMua(){
		return mua;
	}
	public double getMusp(){
		return musp;
	}
	public double getN(){
		return n;
	}
	
	//toString Override for troubleshooting
	@Override
	public String toString(){
		String output;
		output = "x: "+position[0]+" y: "+position[1]+" z: "+position[2];
		return output;
	}

}
