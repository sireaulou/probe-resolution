package prototype;

import controller.Controller;
import controller.ControllerParallel;
import phantom.Phantom;
import probe.Probe;

public class StreamTest {
	public static void main(String [] args){
		boolean isSame = true;
		//15.2 x 29 x 5
		Phantom phantom = new Phantom(-15.2/2,-29/2,0,30,38,10,15.2/31,29/39,5/10,0.5670,0.05,8,1.33);
		phantom.addSphericalAnomaly(-2, -1, 1.5, 1, 0.1, 10, 1.33);
		Probe probe = new Probe(0,0,0);
		probe.fan6x4();
		Controller controller = new Controller(0, 1, 2d*Math.PI*70d*Math.pow(10, 6));
		ControllerParallel controllerP = new ControllerParallel(0, 1, 2d*Math.PI*70d*Math.pow(10, 6));
		double [] svd;
		double [] svdp;
		long startTime = System.nanoTime();
		svd = controller.svdDirectNoOutput(probe,phantom);
		for(int i = 0 ; i < 10 ; i++){
			svd = controller.svdDirectNoOutput(probe,phantom);
		}
		long midTime = System.nanoTime();
		svdp = controllerP.svdDirectNoOutput(probe, phantom);
		for(int i = 0 ; i < 10 ; i++){
			svdp = controllerP.svdDirectNoOutput(probe, phantom);
		}
		long endTime = System.nanoTime();
		
		for(int i = 0; i < svd.length; i++){
			if(svd[i]!=svdp[i]){
				isSame = false;
				break;
			}
		}
		
		System.out.println("Serial:");
		System.out.println((midTime-startTime)*Math.pow(10, -9));
		System.out.println("Concurrent:");
		System.out.println((endTime-midTime)*Math.pow(10, -9));
		System.out.println("Equality:");
		System.out.println(isSame);
		
		/*
		 	Serial:
			38.663379912
			Concurrent:
			16.058767365
			Equality:
			true
		*/
	}
}
