package simulations;

import controller.Controller;
import phantom.Phantom;
import phantom.Voxel;
import probe.Probe;

public class ProbeComparison {
	public static void main(String [] args){
		//Static Probe Comparison
		Probe linear6x4 = new Probe(0,0,0);
		Probe fan6x4 = new Probe(0,0,0);
		linear6x4.linear6x4();
		fan6x4.fan6x4();
		
		Phantom phantom = new Phantom(
				-6,-6,0,
				15,15,5,
				0.8,0.8,0.8,0.8,
				0.05,8,1.4);
		
		Controller controller = new Controller(0, 1, 2d*Math.PI*70d*Math.pow(10, 6) );
		//Look back at your notes on 10/26 plz
//		double M = 6*4;
//		double N = 15*15*5;
//		double sigma = 0.01;
//		double normError = sigma*Math.sqrt(M);
//		double normSignal = Math.sqrt(something);
//		double alpha = Math.sqrt(N/M)*(normError/normSignal);
//		double L = 120;
//		System.out.println("Linear 6x4:");
//		System.out.println(controller.fitness(controller.svdDirectNoOutput(linear6x4, phantom), alpha, L));
//		System.out.println("Fan 6x4: ");
		
	}
}
