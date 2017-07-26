//Vincent Ching-Roa
//Last edit: 07/25/2017
//Description: 
//Controller objects which performs analytic solutions
//Laser properties are contained within the controller
//Future plan: Might be better to move these properties(s, w) to the source

/*READ 
 * COORDINATE SYSTEM
 * x+ right
 * y+ down
 * z+ into the page
 */

/*Culver, J. P., Ntziachristos, V., Holboke, M. J., & Yodh, A. G. (2001). 
 * 	Optimization of optode arrangements for diffuse optical tomography:
 * 	A singular-value analysis. Optics Letters, 26(10), 701. 
 * 	doi:10.1364/ol.26.000701
 */

//Durduran, T., Choe, R., Baker, W.B. and Yodh, A.G., 2010. 
//	Diffuse optics for tissue monitoring and tomography. 
//	Reports on Progress in Physics, 73(7), p.076701.

package controller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import complex.Complex;
import probe.Source;
import probe.Probe;
import probe.Detector;
import phantom.Phantom;
import phantom.Voxel;
//import Jama.SingularValueDecomposition;

import org.ujmp.core.*;
import org.ujmp.core.doublematrix.calculation.general.decomposition.SVD;


public class Controller {
	//Mode
	int mode; //0 for semi-infinite 1 for infinite slab
	
	//Light and laser properties 
	double c = 3*Math.pow(10,10); //speed of light
	double s; //intensity
	double w; //omega frequency 70MHz in most cases

	double v; //speed of light in the medium

	//Green's function stuff
	double d; //slab thickness for infinite slab

	Complex unit_i = new Complex(0,1);	
	
	//Setters for all controller constants above
	
	public void set_s(double s){
		this.s=s;
	}
	
	public void set_w(double w){
		this.w=w;
	}
	
	public void set_d(double d){ //not necessary for semi-infinite
		this.d=d;
	}
	
	public Controller(int mode, double s, double w){ //0 for semi-infinite 1 for infinite slab configuration
		if(mode!=0 && mode!=1){
			this.mode = 0;
			return;
		}
		this.mode = mode;
		this.s = s;
		this.w = w;
		
	}

	/*Greens function for infinite slab boundary condition
	 * Returns the absolute value of the solution.
	 * Refer to Durduran's paper
	 */
	public double GInfiniteSlab(double position1[], double position2[], double mua, double musp, double n){
		
		//Calculating v, ltr, D, Reff and zb
		double v = c/n; //speed of light in the medium
		double ltr = 1/(musp+mua); //transport free mean path
		double D = v*ltr/3; // photon diffusion coefficient
		double Reff = -1.440*Math.pow(n,-2)+0.710/n+0.668+0.0636*n; //effective reflection coefficient approximation Durduran p.7
		double zb = 2*ltr*(1+Reff)/(3*(1-Reff)); //zb position for Greens calculation or extrapolated zero
		
		//Calculating k
		Complex mu_a_complex = new Complex(mua,0);
		Complex v_complex = new Complex(v,0);
		Complex w_complex = new Complex(w,0);
		Complex D_complex = new Complex(D,0);
		
		Complex ktemp1 = mu_a_complex.times(v_complex);
		Complex ktemp2 = unit_i.times(w_complex);
		Complex ktemp3 = ktemp1.minus(ktemp2);
		Complex ktemp4 = ktemp3.div(D_complex);
		Complex k = ktemp4.sqrt();
		
		
		//Sx,sy,sz = source positions; dx,dy,dz = target positions
		double sx = position1[0];
		double sy = position1[1];
		double sz = position1[2];
		
		double dx = position2[0];
		double dy = position2[1];
		double dz = position2[2];
		
		double rho = Math.sqrt(
				Math.pow(sx-dx, 2)
				+ Math.pow(sy-dy, 2)
				); //cylindrical distance between source and target 
		
		double z = dz-sz; //depth difference between source and target;
		
		int limit = 10;
		
		Complex output_complex = new Complex(0,0);
		//tfw no operator overloading :(
		
		for(int i = -limit; i<=limit; i++){
			Complex z_plus = new Complex(2*i*(d+2*zb)+ltr,0);
			Complex z_minus = new Complex(2*i*(d+2*zb)-2*zb-ltr,0);
			
			Complex r_plus = new Complex(Math.sqrt(Math.pow(rho, 2) + Math.pow((z-z_plus.real()), 2)),0);
			Complex r_minus = new Complex(Math.sqrt(Math.pow(rho, 2) + Math.pow((z-z_minus.real()), 2)),0);
			
//			output_complex = 
//					output_complex.plus(((((k.chs()).times(r_plus)).exp()).div(r_plus)).minus((((k.chs()).times(r_minus)).exp()).div(r_minus)));
			Complex p1 = k.chs().times(r_plus);
			Complex p2 = k.chs().times(r_minus);
			Complex p3 = p1.exp();
			Complex p4 = p2.exp();
			Complex p5 = p3.div(r_plus);
			Complex p6 = p4.div(r_minus);
			Complex p7 = p5.minus(p6);
			output_complex = output_complex.plus(p7);
			
		}
		
		double constant = (v*s/(4d*Math.PI*D));
		return constant*output_complex.abs();
	}
	
	/*Greens function for semi-infinite boundary condition
	 * Returns the absolute value of the solution.
	 * Refer to Durduran's paper
	 */
	public double GSemiInfinite(double position1[], double position2[],double mua, double musp, double n){
		
		//Calculating v, ltr, D, Reff and zb
		double v = c/n; //speed of light in the medium
		double ltr = 1/(musp+mua); //transport free mean path
		double D = v*ltr/3; // photon diffusion coefficient
		double Reff = -1.440*Math.pow(n,-2)+0.710/n+0.668+0.0636*n; //effective reflection coefficient approximation Durduran p.7
		double zb = 2*ltr*(1+Reff)/(3*(1-Reff)); //zb position for Greens calculation or extrapolated zero
		
		//Calculating k
		Complex mu_a_complex = new Complex(mua,0);
		Complex v_complex = new Complex(v,0);
		Complex w_complex = new Complex(w,0);
		Complex D_complex = new Complex(D,0);
		
		Complex ktemp1 = mu_a_complex.times(v_complex);
		Complex ktemp2 = unit_i.times(w_complex);
		Complex ktemp3 = ktemp1.minus(ktemp2);
		Complex ktemp4 = ktemp3.div(D_complex);
		Complex k = ktemp4.sqrt();
		
		//Sx,sy,sz = source positions; dx,dy,dz = target positions
		double sx = position1[0];
		double sy = position1[1];
		double sz = position1[2];
		
		double dx = position2[0];
		double dy = position2[1];
		double dz = position2[2];
		
		double rho = Math.sqrt(
				Math.pow(sx-dx, 2)
				+ Math.pow(sy-dy, 2)
				); //cylindrical distance between source and target 
		
		double z = dz-sz; //depth difference between source and target;
		
		Complex r_1 = new Complex(Math.sqrt(Math.pow(rho, 2) + Math.pow(z - ltr, 2)),0);
		Complex r_b = new Complex(Math.sqrt(Math.pow(rho, 2) + Math.pow(z + 2*zb+ltr, 2)),0);
		
		//Complex output = ((((k.chs()).times(r_1)).exp()).div(r_1)).minus((((k.chs()).times(r_b)).exp()).div(r_b));
		Complex p1 = k.chs().times(r_1);
		Complex p2 = k.chs().times(r_b);
		Complex p3 = p1.exp();
		Complex p4 = p2.exp();
		Complex p5 = p3.div(r_1);
		Complex p6 = p4.div(r_b);
		Complex output = p5.minus(p6);
		
		double constant = (v*s/(4d*Math.PI*D));
		constant = 1;
		return constant*output.abs();
	}

	/*Calculates weight matrix row 
	 * Row calculation involves all Greens solution for all voxels
	 * for each source-detector pairs
	 */
	public double[] weightMatrixRow(Source source, Detector detector, int t, Probe probe, Phantom phantom){
		double [] output = new double[phantom.getVoxelList().size()];
		
		double absolute_source_x = probe.getPosition()[t][0] + source.getPosition()[0];
		double absolute_source_y = probe.getPosition()[t][1] + source.getPosition()[1];
		double absolute_source_z = probe.getPosition()[t][2] + source.getPosition()[2];
		double absolute_source_position[] = {absolute_source_x,absolute_source_y,absolute_source_z};
		
		double absolute_detector_x = probe.getPosition()[t][0] + detector.getPosition()[0]; 
		double absolute_detector_y = probe.getPosition()[t][1] + detector.getPosition()[1]; 
		double absolute_detector_z = probe.getPosition()[t][2] + detector.getPosition()[2]; 
		double absolute_detector_position[] = {absolute_detector_x,absolute_detector_y,absolute_detector_z};
		
		for(int i = 0; i < phantom.getVoxelList().size(); i++){
			Voxel currentVoxel = phantom.getVoxelList().get(i);
			if(mode == 0){
				output[i] = 
						GSemiInfinite(absolute_source_position, currentVoxel.getPosition(), currentVoxel.getMua(),currentVoxel.getMusp(),currentVoxel.getN())*
						GSemiInfinite(absolute_detector_position, currentVoxel.getPosition(), currentVoxel.getMua(),currentVoxel.getMusp(),currentVoxel.getN())/
						GSemiInfinite(absolute_source_position, absolute_detector_position, currentVoxel.getMua(),currentVoxel.getMusp(),currentVoxel.getN());
			} else if (mode == 1){
				output[i] = 
						GInfiniteSlab(absolute_source_position, currentVoxel.getPosition(), currentVoxel.getMua(),currentVoxel.getMusp(),currentVoxel.getN())*
						GInfiniteSlab(absolute_detector_position,currentVoxel.getPosition(), currentVoxel.getMua(),currentVoxel.getMusp(),currentVoxel.getN())/
						GInfiniteSlab(absolute_source_position, absolute_detector_position, currentVoxel.getMua(),currentVoxel.getMusp(),currentVoxel.getN());
			}
		}
		
		return output;
	}
	
	
	
	//Performs SVD on a double[][] matrix and prints the singular values in <filename>.dat
	public double[] svdDirect(String filename, Probe probe, Phantom phantom){
		
		File varTmpDir = new File(filename);
		boolean exists = varTmpDir.exists();
		
		if(exists){
			System.out.println("Data file already exists. Pick another filename or delete the existing file");
			return new double[]{0};
		}
		
		int sdcountmax = probe.getSourceList().size()*probe.getDetectorList().size()*probe.getPosition().length;
		int numVox = phantom.getVoxelList().size();
		
		double weightMatrix[][] = new double[sdcountmax][numVox];
		
		int sdcount = 0;
		for(int t = 0; t < probe.getPosition().length; t++ ){
			for(Source source:probe.getSourceList()){
				for(Detector detector:probe.getDetectorList()){
					weightMatrix[sdcount] = weightMatrixRow(source, detector, t, probe, phantom);
					sdcount ++;
				}
			}
		}
		
		Matrix wMatrix = Matrix.Factory.importFromArray(weightMatrix);
		SVD.SVDMatrix wMatrixSVD = new SVD.SVDMatrix(wMatrix,false,false,false);
		double[] singularValues = wMatrixSVD.getSingularValues();
		printDoubleDat(singularValues,filename);
		return singularValues;

	}
	
	
	public double[] svdDirectNoOutput(Probe probe, Phantom phantom){
		
		
		int sdcountmax = probe.getSourceList().size()*probe.getDetectorList().size()*probe.getPosition().length;
		int numVox = phantom.getVoxelList().size();
		
		double weightMatrix[][] = new double[sdcountmax][numVox];
		
		int sdcount = 0;
		for(int t = 0; t < probe.getPosition().length; t++ ){
			for(Source source:probe.getSourceList()){
				for(Detector detector:probe.getDetectorList()){
					weightMatrix[sdcount] = weightMatrixRow(source,detector,t,probe,phantom);
					sdcount ++;
				}
			}
		}
		
//		Matrix wMatrix = new Matrix(weightMatrix);
//		SingularValueDecomposition wMatrixSVD = wMatrix.svd();
//		double[] singularValues = wMatrixSVD.getSingularValues();
//		return singularValues;
		
		//ujmp
		Matrix wMatrix = Matrix.Factory.importFromArray(weightMatrix);
		SVD.SVDMatrix wMatrixSVD = new SVD.SVDMatrix(wMatrix,false,false,false);
		double[] singularValues = wMatrixSVD.getSingularValues();
		return singularValues;
	}
	
	public static double distance(double x1, double y1, double x2, double y2){
		return Math.pow(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2),0.5);
	}
	
	public double[] svdDirectNoOutputLimit(Probe probe, Phantom phantom){
		
		int numVox = phantom.getVoxelList().size();
		
		double maxDistance = 4;
		int underMax = 0;
		for (int i = 0; i < probe.getPosition().length; i++){
			for(Source source:probe.getSourceList()){
				for(Detector detector:probe.getDetectorList()){
					if(distance(source.getX(),source.getY(),detector.getX(),detector.getY())<maxDistance){
						underMax++;
					}
				}
			}
		}
		
		double weightMatrix[][] = new double[underMax][numVox];
		
		int sdcount = 0;
		for(int t = 0; t < probe.getPosition().length; t++ ){
			for(Source source:probe.getSourceList()){
				for(Detector detector:probe.getDetectorList()){
					if(distance(source.getX(),source.getY(),detector.getX(),detector.getY())<maxDistance){
						weightMatrix[sdcount] = weightMatrixRow(source,detector,t,probe,phantom);
						sdcount ++;
					}
				}
			}
		}
		if(sdcount == 0){
			return new double[]{0};
		}
//		Matrix wMatrix = new Matrix(weightMatrix);
//		SingularValueDecomposition wMatrixSVD = wMatrix.svd();
//		double[] singularValues = wMatrixSVD.getSingularValues();
//		return singularValues;
		//ujmp
		Matrix wMatrix = Matrix.Factory.importFromArray(weightMatrix);
		SVD.SVDMatrix wMatrixSVD = new SVD.SVDMatrix(wMatrix,false,false,false);
		double[] singularValues = wMatrixSVD.getSingularValues();
		return singularValues;
	}
	
	//Discretized resolution calculation based on Culver's paper
	public double res(double[] singularValues, double alpha, double L){
		double [] diff = new double[singularValues.length];
		int index = 0; 
		for(int i = 0; i < singularValues.length; i++){
			diff[i] = Math.abs(singularValues[i]-alpha);
		}
		double mindiff = diff[0];
		for(int i = 1; i < diff.length; i++){
			if(diff[i]<mindiff){
				mindiff = diff[i];
				index = i;
			}
		}
		
		double res = 2*L/(Math.pow(index+1,1d/3d));
		return res;
		
	}
	
	//GA fitness function that removes the discretization from the res fitness
	//and attempts to move the curve upwards
	public double [] fitness(double[] singularValues, double alpha, double L){ 
		if(singularValues.length==1&&singularValues[0]==0){
			return new double[]{Double.MAX_VALUE,0};
		}
		double [] diff = new double[singularValues.length];
		int index = 0; 
		for(int i = 0; i < singularValues.length; i++){
			diff[i] = Math.abs(singularValues[i]-alpha);
		}
		double mindiff = diff[0];
		for(int i = 1; i < diff.length; i++){
			if(diff[i]<mindiff){
				mindiff = diff[i];
				index = i;
			}
		}
		
		return new double[]{index+1,singularValues[index]-alpha};
		
	}
	
	//GA fitness function that removes discretization through linear extrapolation 
	public double fitness2(double[] singularValues, double alpha, double L){
		if(singularValues.length==1&&singularValues[0]==0){
			return Double.MAX_VALUE;
		}
		double [] diff = new double[singularValues.length];
		int index = 0;
		for(int i = 0; i < singularValues.length; i++){
			diff[i] = Math.abs(singularValues[i]-alpha);
		}
		double mindiff = diff[0];
		for(int i = 1; i < diff.length; i++){
			if(diff[i]<mindiff){
				mindiff = diff[i];
				index = i;
			}
		}
		
		double extIndex;
		if(singularValues[index]-alpha<0 && index!=0){
			int x1 = index;
			int x2 = index - 1;
			double y1 = singularValues[x1];
			double y2 = singularValues[x2];
			double slope = (y1-y2)/(x1-x2);
			extIndex = (alpha-y2+slope*x2)/slope;
		} else if(singularValues[index]-alpha>0){
			int x1 = index;
			int x2 = index + 1;
			double y1 = singularValues[x1];
			double y2 = singularValues[x2];
			double slope = (y1-y2)/(x1-x2);
			extIndex = (alpha-y2+slope*x2)/slope;
		} else {
			extIndex = index;
		}
		return 2*L/(Math.pow(extIndex+1,1d/3d));
	}
	
	
	

	//Print method into /temp/<filename>.dat 
	public void printDoubleDat(double [] d, String filename){
		try{
			String file = filename+".dat";
			FileWriter fstream = new FileWriter(file,true);
			BufferedWriter out = new BufferedWriter(fstream);
			for(double val:d){
//				String temp = Double.toString(val);
//				out.write(temp+" ");
				BigDecimal value = BigDecimal.valueOf(val);
				BigDecimal wantedValue = value.round(new MathContext(8, RoundingMode.HALF_UP));
				out.write(wantedValue.toPlainString()+" ");
			}
			out.close();
		} catch (Exception e){
			System.out.println(e);
		}
		
	}
	
	//---------------DEBUGGING METHODS BELOW---------------------------------------
	
		public void weightMatrixOutputDebug(String filebase, Probe probe, Phantom phantom){
			int sdcount = 0;
			for(int t = 0; t < probe.getPosition().length; t++ ){
				for(Source source:probe.getSourceList()){
					for(Detector detector:probe.getDetectorList()){
						double [] row = weightMatrixRow(source,detector,t, probe, phantom);
						String filename = filebase + Integer.toString(sdcount);
						printDoubleDatDebug(row, filename, source, detector, t, probe, phantom);
						sdcount ++;
					}
				}
			}
		}
		
		
		public void printDoubleDatDebug(double [] d, String filename, Source source, Detector detector, int t, Probe probe, Phantom phantom){
			try{
				String file = filename+".dat";
				FileWriter fstream = new FileWriter(file,true);
				BufferedWriter out = new BufferedWriter(fstream);
				
				double absolute_source_x = probe.getPosition()[t][0] + source.getPosition()[0];
				double absolute_source_y = probe.getPosition()[t][1] + source.getPosition()[1];
				double absolute_source_z = probe.getPosition()[t][2] + source.getPosition()[2];
				
				double absolute_detector_x = probe.getPosition()[t][0] + detector.getPosition()[0]; 
				double absolute_detector_y = probe.getPosition()[t][1] + detector.getPosition()[1]; 
				double absolute_detector_z = probe.getPosition()[t][2] + detector.getPosition()[2]; 
				
				String sourcePosition = absolute_source_x + " " + absolute_source_y + " " + absolute_source_z;
				String detectorPosition = absolute_detector_x + " " + absolute_detector_y + " " + absolute_detector_z;
				
				out.write(sourcePosition);
				out.newLine();
				out.write(detectorPosition);
				out.newLine();
				
				for(double val:d){
					String temp = Double.toString(val);
					out.write(temp+" ");
				}
				out.close();
			} catch (Exception e){
				System.out.println(e);
			}
		}
	//----------------------------------------------------------------------------

}
