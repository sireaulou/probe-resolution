//Vincent Ching-Roa
//Last edit: 07/24/2017
//Description: 
//Voxel object with position vector and optical properties mua and musp (cm^-1) and n (refractive index) 
//Log: 7/27 added voxel dimension for conversion factor
package phantom;
public class Voxel {
	
	double position[] = new double[3]; //x,y,z relative to field.
	double mua; //absorption coefficient in cm^-1 
	double musp; //reduced scattering coefficient in cm^1-
	double n; //refractive index 
	double voxelDim; //voxel dimensions (length of a side)[if not cubic then do (total volume)^(1/3)]
	
	//Voxel constructor with position and optical properties input
	public Voxel(double x, double y, double z, double voxelDim, double mua, double musp, double n){
		position[0]=x;
		position[1]=y;
		position[2]=z;
		this.voxelDim = voxelDim;
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
	public double getDim(){
		return voxelDim;
	}
	
	//toString Override for troubleshooting
	@Override
	public String toString(){
		String output;
		output = "x: "+position[0]+" y: "+position[1]+" z: "+position[2];
		return output;
	}

}
