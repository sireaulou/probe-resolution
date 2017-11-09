//Vincent Ching-Roa
//Last edit: 10/13/2017
//Description: 
//Genetic algorithm simulations... wooo!

package simulations;

import genetic.GeneticAlgorithm;
import phantom.Phantom;
import genetic.*;

public class GeneticAlgorithmSimulation {
	public static void main(String [] args){
		String file = "geneticAlgorithm//Symmetrical_GA_LRS_6x4_101217.dat";
		
		Phantom phantom = new Phantom(
				-6,-6,0,
				25,25,11,
				0.5,0.5,0.5,0.5,
				0.05,8,1.4);
		
		phantom.addSphericalAnomaly(0, 0, 1.5, 1, 0.0625, 12, 1.4);
		
		double alpha = 0.01/(0.0125/(12*12*5)); //sigma = 0.01, sig0 = 0.0125 (del mua)
		double phantomLength = 120; //12 cm in mm
		
//		//LRS Template
//		GeneticAlgorithm<ProbeGeneSymmetrical> genAlgo =
//				new GeneticAlgorithm<ProbeGeneSymmetrical>(
//						file, 6, 4, 1000,
//						new int[]{5, 200}, 1.6, 20, 0.01d);

//		//RR Template
		GeneticAlgorithm<ProbeGeneSymmetrical> genAlgo =
				new GeneticAlgorithm<ProbeGeneSymmetrical>(
						file, 6, 4, 1000,
						new int[]{5, 200},new int[]{600, 800},new int[]{0, 3}
						, alpha, phantomLength, 0.01d);
		genAlgo.start(new ProbeGeneSymmetrical(),phantom);
		
	}
}
