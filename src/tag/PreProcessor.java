package tag;

import io.MascotGenericFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import model.Peak;
import model.Spectrum;

public class PreProcessor {
	
	
	private MascotGenericFile mgfFile;
	
	// TODO: Cutoff is still hard-coded. Use Signal-to-Noise ratio (?).
	private static final double CUTOFF = 2.0;

	/**
	 * The PreProcessor takes a MGF file as parameter.
	 * @param mgfFile
	 */
	public PreProcessor(MascotGenericFile mgfFile) {
		this.mgfFile = mgfFile;	
	}
	
	/**
	 * Formats the spectrum suitable to the model by using the intensity threshold.
	 * Additionally the intensities are ranked.
	 * @return Spectrum The formatted spectrum.
	 */
	public Spectrum format(){		
		List<Peak> peakList = mgfFile.getPeaks();
		List<Peak> formattedPeaks = new ArrayList<Peak>();
		List<Double> rankedList = new ArrayList<Double>();
		HashMap<Double, Integer> rankMap = new HashMap<Double, Integer>();
		double intensity = 0.0;
		// Iterate through the peaks list.
		for (Peak peak : peakList) {
			intensity = peak.getIntensity();
			// Intensity must be above the threshold		
			if (intensity > CUTOFF){				
				formattedPeaks.add(peak);
				rankedList.add(intensity);
			}
		}
		// Sort the intensities
		Double[] ranked = new Double[rankedList.size()];
		rankedList.toArray(ranked);
		Arrays.sort(ranked, Collections.reverseOrder());
		
		// Iterate through the ranked intensities and 
		for(int i = 0; i < ranked.length; i++){
			rankMap.put(ranked[i], i+1);
		}
		
		
		// Multiply the pepmass * charge
		int charge = mgfFile.getCharge();
		double precursorMZ = (mgfFile.getPrecursorMZ() * charge); 
		return new Spectrum(mgfFile.getTitle(), formattedPeaks, rankMap, precursorMZ, charge);		
	}

}
