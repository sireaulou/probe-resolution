//Vincent Ching-Roa
//Last edit: 10/13/2017
//Description: 
//Common tools for convenience
//might move stuff to here if i find any wandering "tools"
//trying to add JCuda for hopefully faster svd 
//nevermind might not be worth it. (refer to MATLAB cpu v gpu svd test)
//has to be a REALLY REALLY large matrix for it to make a massive difference

package tools;

//import jcuda.Pointer;
//import jcuda.jcusolver.JCusolverDn;

public class CommonTools {
	public static double distance(double [] coord1, double [] coord2){
		
		double xdiff2 = Math.pow(coord1[0]-coord2[0], 2);
		double ydiff2 = Math.pow(coord1[1]-coord2[1], 2);
		double zdiff2 = Math.pow(coord1[2]-coord2[2], 2);
		
		return Math.sqrt(xdiff2+ydiff2+zdiff2);
	}
	
//	public double [] svdCUDA(double [][] weightMatrix){
//		int M = weightMatrix.length;
//		int N = weightMatrix[0].length;
//		
//		int[] bufferSize = {0};
//		Pointer info = new Pointer();
//		Pointer buffer = new Pointer();
//		Pointer svd = new Pointer();
//		int [] h_info = {0};
//		
//		JCusolverDn.cusolverDnDgesvd_bufferSize(handle, m, n, lwork);
//		
//		JCusolverDn.cusolverDnDgesvd(handle, jobu, jobvt, m, n, A, lda, S, U, ldu, VT, ldvt, work, lwork, rwork, info)
//		
//		
//		return new double[]{};
//	}
}
