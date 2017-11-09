//Vincent Ching-Roa
//Last edit: 10/26/2017
//Description: 
//Phantom voxel field. 
//Should add support for non-rectangular field generation or import 
//Log: 7/27 added voxel dimension for conversion factor
//Log: 10/13 added anomalies
//Log: 10/26 add region support

package phantom;
import java.util.LinkedList;
import java.util.List;

import tools.CommonTools;

public class Phantom {
	List<Voxel> voxelList = new LinkedList<Voxel>(); //voxel container 
	
	//Blank phantom constructor
	public Phantom(){
	}
	
	//Homogeneous rectangular voxel field constructor
	public Phantom(
			double startX, double startY, double startZ,
			int numX, int numY, int numZ,
			double dx, double dy, double dz, double voxelDim,
			double mua, double musp, double n)
	{
		for(int i = 0; i < numY; i++){
			for(int j = 0; j < numZ; j++){
				for(int k = 0; k < numX; k++){
					Voxel newVoxel = new Voxel(
							startX + k*dx,
							startY + i*dy,
							startZ + j*dz,
							voxelDim,mua,musp,n);
					voxelList.add(newVoxel);
				}
			}
		}
	}
	
	//Phantom constructor with voxel list 
	public Phantom(List<Voxel> voxels){
		voxelList.addAll(voxels);
	}
	
	//Accessors 
	public List<Voxel> getVoxelList(){
		return voxelList;
	}
	
	public void addVoxel(List<Voxel> voxels){
		voxelList.addAll(voxels);
	}
	
	public void addVoxel(Voxel voxel){
		voxelList.add(voxel);
	}
	
	//count how many voxels are marked with the specified region
	public int countRegion(int region){
		int count = 0;
		for(Voxel voxel:voxelList){
			if(voxel.getRegion()==region){
				count++;
			}
		}
		return count;
	}
	
	//Add phantom anomalies (ie change regions)
	public void addSphericalAnomaly(double centerX, double centerY, double centerZ, double radius,
			double mua, double musp, double n){
		double [] sphereCenter = {centerX, centerY, centerZ};
		for(Voxel voxel:voxelList){
			if(CommonTools.distance(voxel.getPosition(), sphereCenter)<=radius){
				voxel.setMua(mua);
				voxel.setMusp(musp);
				voxel.setN(n);
				voxel.incRegion();
			}
		}
	}
	

}
