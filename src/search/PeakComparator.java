package search;

import java.util.ArrayList;
import java.util.List;

import tag.Masses;
import tag.Tag;


import model.Peak;
import model.PeptideHit;
import model.Sequence;
import model.Spectrum;

public class PeakComparator {
	
	/**
	 * List containing the matched database sequences.
	 */
	private List<Sequence> dbSequences;

	/**
	 * The experimental spectrum.
	 */
	private Spectrum expSpectrum;
	
	/**
	 * The fragment ion tolerance.
	 */
	private double fragTol; // TODO: Use!
	
	private double delta;
	
	private final static int MINIMUM_PEAK_MATCHES = 10;
	
	private final static double PRECURSOR_TOL = 0.2;
	private final static double FRAG_TOL = 0.5;
	private PeptideHit pepHit;

	public PeakComparator(Spectrum expSpectrum, double fragTol) {
		this.expSpectrum = expSpectrum;
		this.fragTol = fragTol;
	}
	
	public List<Peak> calcTheoPeaks(Sequence seq, Tag tag){
		Masses.init();
		List<Peak> theoPeaks = new ArrayList<Peak>();
		
		// y-Ions have fixed C-terminal mass
		double yIonSum = Masses.C_term;
		double bIonSum = Masses.N_term;
		String peptide = "";
				
		this.delta = Double.NaN;
		

		// Reverse the end tag
		String tagString = new StringBuffer(tag.toString()).reverse().toString();
		if(seq.getSequenceAsString().contains(tagString)){ 
			
			seq.setMatchStart(seq.getSequenceAsString().indexOf(tagString));
			seq.setMatchEnd(seq.getSequenceAsString().indexOf(tagString) + tagString.length() - 1);
			char aa;
			double mass;
			double yDelta;
			double bDelta;
			Peak peak;
			List<Peak> yPeaks = new ArrayList<Peak>();			
			// Delta means the difference between the found y-ions and the actual precursorMz. 
			
			int i = seq.getMatchEnd();
			
			// Calculate the y-Ion peaks.
			for ( ; i > 0; i--){
				aa = seq.getLetterAt(i);
				mass = Masses.MAP.get(aa);			
				
				yIonSum += mass;
				// Break criterium
				if (yIonSum > expSpectrum.getPrecursorMz()) break;
				peptide += aa;		
				yDelta = Math.abs(yIonSum - expSpectrum.getPrecursorMz());
				peak = new Peak(yIonSum);
				yPeaks.add(peak);
				if(yDelta < PRECURSOR_TOL){
					this.delta = yDelta;
					// The y-ions must be within the precursor tolerance, than a new hit is created
					pepHit = new PeptideHit();
					pepHit.setStart(i);
					pepHit.setEnd(seq.getMatchEnd() + 1);
					theoPeaks.addAll(yPeaks);
					break;
				}				
			}			
			// Calculate the b-Ion peaks.
			for (int j = i; j < seq.getMatchEnd(); j++){
				aa = seq.getLetterAt(j);
				mass = Masses.MAP.get(aa);
				bIonSum += mass;
				if (bIonSum + Masses.N_term > expSpectrum.getPrecursorMz()) break;
				peptide += aa;	
				bDelta = Math.abs(bIonSum + Masses.N_term - expSpectrum.getPrecursorMz());
				peak = new Peak(bIonSum);
				theoPeaks.add(peak);
				if(bDelta < PRECURSOR_TOL){
					// If not already defined
					if(delta == Double.NaN){
						this.delta = bDelta;
					}					
					this.delta = bDelta;
					break;
				}
			}
			if(peptide.length() > 0){
				// Reverse the yIon sequence
				peptide = new StringBuffer(peptide).reverse().toString(); 
			}
		}
		
		return theoPeaks;
	}
	
	/**
	 * Compare the observed peaks against the theoretical peaks.
	 * @param obsPeaks
	 * @param theoPeaks
	 * @return
	 */
	public PeptideHit comparePeaks(Sequence seq, List<Peak> obsPeaks, List<Peak> theoPeaks){
		
		double massDiff;
		List<Double> massDiffs = new ArrayList<Double>();
		List<Double> intList = new ArrayList<Double>();
		int matchedPeaks = 0;
		
		for (Peak theoPeak : theoPeaks) {
			for (int i = 0; i < obsPeaks.size(); i++) {
				massDiff = Math.abs(theoPeak.getMz() - obsPeaks.get(i).getMz());
				if(massDiff < FRAG_TOL) {
					matchedPeaks++;
					massDiffs.add(massDiff);
					intList.add(obsPeaks.get(i).getIntensity());
				}
			}
		}
		
		// Matched peaks as threshold
		if(matchedPeaks > MINIMUM_PEAK_MATCHES){
			// Set the peptide sequence
			pepHit.setSequence(seq.getSequenceAsString().substring(pepHit.getStart(), pepHit.getEnd()));
			
			// Set the accession
			pepHit.setAccession(seq.getName());
			
			// Set the matched peaks.
			pepHit.setMatchedPeaks(matchedPeaks);
			
			// Set the delta.
			pepHit.setDelta(delta);
			
			// Set the score.
			pepHit.setScore(PeptideScorer.calcDeltaScore(massDiffs, delta));
			
			// Set the intensity score
			pepHit.setIntensityScore(PeptideScorer.calcIntScore(intList, pepHit.getSequence().length()));
			return pepHit;

		} else {
			return null;
		}
	}
	
	
	
	public PeptideHit getPeptideHit(){
		return pepHit;
		
	}

}
