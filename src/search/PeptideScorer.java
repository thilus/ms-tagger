package search;

import java.util.List;

/**
 * This class represents the scoring system for the peptide identification process.
 * @author Thilo Muth
 *
 */
public class PeptideScorer {	
	
	
	/**
	 * This method calculates the mass delta score:
	 * Sum of the logs of the mass differences (multiplied by  -1) + log of the delta.
	 * @return score The calculated score
	 */
	public static double calcDeltaScore(List<Double> massDiffs, double delta){
		double score = 0.0;
		double sum = 0.0;		
		
		for (double massDiff : massDiffs) {
			sum += Math.log10(massDiff);
		}
		
		// Score function
		score = (sum * (-1)) + (Math.log10(delta)) *(-5);	
		
		return score;		
	}
	
	/**
	 * This method calculates the intensity score:
	 * Sum of the logs of the intensities divided by the peptide sequence length.
	 * @return score The calculated score
	 */
	public static double calcIntScore(List<Double> intensities, int pepLength){
		double score = 0.0;
		double sum = 0.0;		
		
		for (double intensity : intensities) {
			sum += intensity;			
		}
		
		// Score function
		score = sum / pepLength;		
		
		return score;
	}

}
