//Vincent Ching-Roa
//Last edit: 07/25/2017
//Description: 
//Genetic algorithm simulations... wooo!

package simulations;

import genetic.GeneticAlgorithm;
import genetic.ProbeGeneSymmetrical;

public class GeneticAlgorithmSimulation {
	public static void main(String [] args){
		String file = "geneticAlgorithm//Symmetrical_GA_RR_7x4a.dat";
//		//LRS Template
//		GeneticAlgorithm<ProbeGeneSymmetrical> genAlgo =
//				new GeneticAlgorithm<ProbeGeneSymmetrical>(
//						file, 7, 4, 1000,
//						new int[]{3, 200}, 1.6, 20, 0.01d);

//		//RR Template
		GeneticAlgorithm<ProbeGeneSymmetrical> genAlgo =
				new GeneticAlgorithm<ProbeGeneSymmetrical>(
						file, 7, 4, 1000,
						new int[]{3, 200},new int[]{600, 800},new int[]{0, 3}
						, 1.6, 20, 0.01d);
		
		genAlgo.start(new ProbeGeneSymmetrical());
	}
}
