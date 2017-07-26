//Vincent Ching-Roa
//Last edit: 07/24/2017
//Description: 
//Reproduction of Culver's figures. Exports data to be plotted in matlab 
//Also includes reproduction of Jingxuan's vs. Standard geometry 
//(diagonal vs. vertical probe orientation)

/*Culver, J. P., Ntziachristos, V., Holboke, M. J., & Yodh, A. G. (2001). 
 * 	Optimization of optode arrangements for diffuse optical tomography:
 * 	A singular-value analysis. Optics Letters, 26(10), 701. 
 * 	doi:10.1364/ol.26.000701
 */

//Durduran, T., Choe, R., Baker, W.B. and Yodh, A.G., 2010. 
//	Diffuse optics for tissue monitoring and tomography. 
//	Reports on Progress in Physics, 73(7), p.076701.

package simulations;
import probe.*;
import controller.Controller;
import phantom.*;

public class CulverReproductions {
	
	//Reproduction of Fig.2a and Fig.2b 
	public static void culver2(String basename,double xFOV, int numSDpairs){
		//creating the probe 
		double dx = xFOV/(numSDpairs-1);
		Probe staticProbe = new Probe(-xFOV/2,0,0,0,0,0,0);
		for(int i = 0; i<numSDpairs; i++){
			Source newSource = new Source(i*dx,6,0);
			Detector newDetector = new Detector(i*dx,0,0);	
			staticProbe.addSource(newSource);
			staticProbe.addDetector(newDetector);
		}
		
		//creating the phantom
		Phantom phantom = new Phantom(-3,3,0,30,1,1,0.2,0.2,0,0.05,8,1.4);
		
		//creating the controller
		Controller controller = new Controller(1,1,2d*Math.PI*70d*Math.pow(10, 6));
		controller.set_d(6); //for infinite slab geometry
		
		//controller analytic solution direct to SVD singular values
		controller.svdDirect(basename,staticProbe,phantom);
		System.out.println("Done!");
	}
	
	//Reproduction of Fig.3a
	public static void culver3a(String basename){
		String filename;
		//dx = [3 4.5 6 9 10 11.25 15 18 22.5] mm
		double[] dx = {0.3, 0.45, 0.6, 0.9, 1.0, 1.125, 1.5, 1.8, 2.25};
		for(int j = 0; j<9; j++){ 
			double xFOV= 18;
			int numSDpairs = (int) (xFOV/dx[j])+1;
			Probe staticProbe = new Probe(-xFOV/2,0,0,0,0,0,0);
			for(int i = 0; i<numSDpairs; i++){
				Source newSource = new Source(i*dx[j],6,0);
				Detector newDetector = new Detector(i*dx[j],0,0);	
				staticProbe.addSource(newSource);
				staticProbe.addDetector(newDetector);
			}
			
			Phantom phantom = new Phantom(-3,0,0,30,30,1,0.2,0.2,0,0.05,8,1.4);
			
			Controller controller = new Controller(1,1,2d*Math.PI*70d*Math.pow(10, 6));
			controller.set_d(6);
			filename = basename+(j+1);
			controller.svdDirect(filename,staticProbe,phantom);		
		}
		System.out.println("Done!");
	}
	
	//Reproduction of Fig.3b
	public static void culver3b(String basename){
		String filename;
		for(int j = 1; j<=8; j++){ //2cm to 16 cm
			double xFOV= 2*j;
			int numSDpairs = 30;
			double dx = xFOV/(numSDpairs-1);
			Probe staticProbe = new Probe(-xFOV/2,0,0,0,0,0,0);
			for(int i = 0; i<numSDpairs; i++){
				Source newSource = new Source(i*dx,6,0);
				Detector newDetector = new Detector(i*dx,0,0);	
				staticProbe.addSource(newSource);
				staticProbe.addDetector(newDetector);
			}
			
			Phantom phantom = new Phantom(-3,0,0,30,30,1,0.2,0.2,0,0.05,8,1.4);
			
			Controller controller = new Controller(1,1,2d*Math.PI*70d*Math.pow(10, 6));
			controller.set_d(6);
			filename = basename+j;
			controller.svdDirect(filename,staticProbe,phantom);		
		}
		System.out.println("Done!");
	}
	
	//Reproduction of Fig.4
	public static void culver4(String basename){
		String filename;
		// Depths: 0.8:0.2:3 away from the source		
		for(int j = 0; j<12; j++){
			double xFOV=9;
			int numSDpairs = 30;
			double dx = xFOV/(numSDpairs-1);
			Probe staticProbe = new Probe(-xFOV/2,0,0,0,0,0,0);
			for(int i = 0; i<numSDpairs; i++){
				Source newSource = new Source(i*dx,6,0);
				Detector newDetector = new Detector(i*dx,0,0);	
				staticProbe.addSource(newSource);
				staticProbe.addDetector(newDetector);
			}
			
			double depth = 6d - 0.8 - 0.2*j;
			
			Phantom phantom = new Phantom(-3,depth,0,30,1,1,0.2,0.2,0,0.05,8,1.4);
			
			Controller controller = new Controller(1,1,2d*Math.PI*70d*Math.pow(10, 6));
			controller.set_d(6);
			filename = basename+(j+1);
			controller.svdDirect(filename,staticProbe,phantom);
		}
		System.out.println("Done!");
	}
	
	
	public static void jingxuanVsStandard(String basename){
		//Standard
		Probe probeS = new Probe(
				-4.5d,-7.5d/2d,0,
				9d,7.5d,
				9d/12,7.5d/10d);
		Source source1s = new Source(0,0,0);

		Detector detector1s = new Detector(0,1.3d*2,0);
		Detector detector2s = new Detector(0,3.25d*2,0);
		
		probeS.addSource(source1s);
		probeS.addDetector(detector1s);
		probeS.addDetector(detector2s);
		
		//Jingxuan
		Probe probeJ = new Probe(
				-4.5d,-7.5d/2d,0,
				9d,7.5d,
				9d/12,7.5d/10d);
		Source source1j = new Source(0,0,0);
		
		Detector detector1j = new Detector(1.3d*Math.sqrt(2),1.3d*Math.sqrt(2),0);
		Detector detector2j = new Detector(3.25d*Math.sqrt(2),3.25d*Math.sqrt(2),0);
		
		probeJ.addSource(source1j);
		probeJ.addDetector(detector1j);
		probeJ.addDetector(detector2j);

		
		//phantom setup
		int numVoxelX = 10;
		int numVoxelY = 50;
		int numVoxelZ = 10;
		
		double startX = -0.8;
		double endX = 0.8;
		double dx = (endX-startX)/(numVoxelX-1);

		double startY = -10;
		double endY = 7.5;
		double dy = (endY-startY)/(numVoxelY-1);
		
		double startZ = 0.1;
		double endZ = 1.7;
		double dz = (endZ-startZ)/(numVoxelZ-1);
		
		Phantom phantom = new Phantom(
				startX, startY, startZ,
				numVoxelX, numVoxelY, numVoxelZ,
				dx, dy, dz,
				0.05, 8, 1.4);
		
		
		//controller setup 
		Controller controller = new Controller(0,1,2d*Math.PI*70d*Math.pow(10, 6)); 
		controller.set_d(6d); //not necessary for semi-infinite;

		controller.svdDirect(basename+"Standard", probeS,phantom);		
		controller.svdDirect(basename+"Jingxuan",probeJ,phantom);
		System.out.println("Done!");
	}
	
	//Run whatever you want here.
	public static void main(String [] args){
		culver2("culverReproduction//culver2_21mm_8x8",2.1,8);
		culver2("culverReproduction//culver2_21mm_80x80",2.1,80);
		culver2("culverReproduction//culver2_87mm_30x30",8.7,30);
		culver3a("culverReproduction//culver3a_");
		culver3b("culverReproduction//culver3b_");
		culver4("culverReproduction//culver4_");
	}
}
