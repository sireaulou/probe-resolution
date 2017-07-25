//Vincent Ching-Roa
//Last edit: 07/24/2017
//Description: 
//Phantom voxel field. 
//Should add support for non-rectangular field generation or import 


package phantom;
import java.util.LinkedList;
import java.util.List;

public class Phantom {
	List<Voxel> voxelList = new LinkedList<Voxel>(); //voxel container 
	
	//Blank phantom constructor
	public Phantom(){
	}
	
	//Homogeneous rectangular voxel field constructor
	public Phantom(
			double startX, double startY, double startZ,
			int numX, int numY, int numZ,
			double dx, double dy, double dz,
			double mua, double musp, double n)
	{
		for(int i = 0; i < numY; i++){
			for(int j = 0; j < numZ; j++){
				for(int k = 0; k < numX; k++){
					Voxel newVoxel = new Voxel(
							startX + k*dx,
							startY + i*dy,
							startZ + j*dz,
							mua,musp,n);
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

}
