package tag;

import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import model.Peak;
import model.Spectrum;

public class TagGenerator {	
	
	/**
	 * The experimental spectrum.
	 */
	private Spectrum expSpectrum;
	
	private static final double FRAG_TOL = 0.4;
	private static final int TAG_LIMIT = 10;
	
	/**
	 * Constructor for the TagGenerator.
	 * @param expSpectrum The experimental spectrum.
	 */
	public TagGenerator(Spectrum expSpectrum) {
		this.expSpectrum = expSpectrum;
	}
	
	/**
	 * Generates the graph from the spectrum.
	 * @param spectrum
	 * @return
	 */
	public Graph generateGraph(){
		List<Peak> peakList = expSpectrum.getPeakList();		
		
		List<Vertex> vertices = new ArrayList<Vertex>();
		// Add the starting node to the graph.
		vertices.add(new Vertex(0.0, 0.0));
		
		// Add the spectrum peaks as nodes to the graph.
		for (int i = 0; i < peakList.size(); i++) {			
			vertices.add(new Vertex(peakList.get(i).getMz(), peakList.get(i).getIntensity()));			
		}		
		return new Graph(vertices);		
	}
	
	
	/**
	 * This method returns the Top10 c-terminal tags.
	 * @param graph The spectrum graph.
	 * @return top10 List<Tag>
	 */
	public List<Tag> getCTermTags(Graph graph){
		
		// Checking for the y3-ion
		List<Tag> firstList = filterY3Ions(graph);
		
		// Checking for y-ions
		List<Tag> secondList = filterYIons(graph, firstList);
		
		// Checking for b-ions
		List<Tag> thirdList = filterBIons(graph, secondList);
		
		// Checking for charged ions
		List<Tag> fourthList = filterChargedIons(graph, thirdList);
				
		// Return the top10 ranked tags.
		List<Tag> top10 = getBestRankTags(fourthList);		
		return top10;		
	}
	
	/**
	 * This method returns the highest scored tags that are below a certain tag limit.
	 * @param tags
	 * @return bestTags List<Tag>
	 */
	private List<Tag> getBestRankTags(List<Tag> tags){
		HashMap<Double, Tag> tagMap = new HashMap<Double, Tag>();		 
        List<Tag> bestTags = new ArrayList<Tag>();
		// Put the tags into a map
        for (Tag tag : tags) {
        	 tagMap.put(tag.getScore(), tag);
        }
         
        // Sort the map by the key in descending order
        Object[] key = tagMap.keySet().toArray();
        Arrays.sort(key, Collections.reverseOrder());
        for(int i = 0; i < TAG_LIMIT; i++){
        	if(i < key.length) bestTags.add(tagMap.get(key[i]));
        }
        return bestTags;
	}
	
	/**
	 * This method filters for the y3-ions.
	 * @param graph
	 * @return firstList List<Tag>
	 */
	private List<Tag> filterY3Ions(Graph graph){
		// First filtered list
		List<Tag> firstList = new ArrayList<Tag>();		
		List<Vertex> vertices = graph.getVertices();
		Set<Character> letters = Masses.letters;
		double deltaY3, y3;	
		
		// FIRST FILTERING: Checking for 3-letter y-ions
		for (Character aa1 : Masses.trypticLetters) {
			for (Character aa2 : letters) {
				for (Character aa3 : letters) {
					y3 = Masses.C_term + Masses.MAP.get(aa1) + Masses.MAP.get(aa2) + Masses.MAP.get(aa3);
					for (int i = 0; i < vertices.size(); i++) {
						deltaY3 = Math.abs(vertices.get(i).getMass() - y3);
						if(deltaY3 <= FRAG_TOL) {
							Tag pepTag = new Tag(aa1, aa2, aa3, i);
							
							// Set mass delta
							pepTag.setMassDelta(deltaY3);
							
							// Scoring y3-ion
							pepTag.setScore((Math.log10(deltaY3) * Math.log10(vertices.get(i).getIntensity())) * (-1));
							
							// Intensity reversed rank
							pepTag.setIntensityRRS(expSpectrum.getIRR(vertices.get(i).getIntensity()));
							firstList.add(pepTag);
							
							// Matches
							pepTag.setMatches(1);
						}
					}
				}
			}
		}		
		return firstList;
	}
	
	/**
	 * This method filters for y1- and y2-ions.
	 * @param graph
	 * @param firstList
	 * @return secondList List<Tag>
	 */
	private List<Tag> filterYIons(Graph graph, List<Tag> firstList){
		// First filtered list
		List<Tag> secondList = new ArrayList<Tag>();		
		List<Vertex> vertices = graph.getVertices();		
		double deltaY1, deltaY2, y1, y2;	
		
		// SECOND FILTERING: Checking for y-ions
		for (Tag tag : firstList) {
			for (int i = 0; i < tag.getEndIndex(); i++) {
				// y1-Ion match
				y1 = Masses.C_term + Masses.MAP.get(tag.getAa1());
				deltaY1 = Math.abs(vertices.get(i).getMass() - y1);
				if(deltaY1 <= FRAG_TOL) {
					// Scoring y1-ion
					double score = tag.getScore();
					score += (Math.log10(deltaY1) * Math.log10(vertices.get(i).getIntensity()) * (-1));
					tag.setScore(score);
					
					// Intensity reversed rank
					int rankSum = tag.getIntensityRRS() + expSpectrum.getIRR(vertices.get(i).getIntensity());
					tag.setIntensityRRS(rankSum);
					
					// Increase matches
					int matches = tag.getMatches() + 1;
					tag.setMatches(matches);
				}
				
				// y2-Ion match
				y2 = Masses.C_term + Masses.MAP.get(tag.getAa1());
				deltaY2 = Math.abs(vertices.get(i).getMass() - y2);
				if(deltaY2 <= FRAG_TOL) {
					// Scoring y2-ion
					double score = tag.getScore();
					score += (Math.log10(deltaY1) * Math.log10(vertices.get(i).getIntensity()) * (-1));
					tag.setScore(score);
					
					// Intensity reversed rank
					int rankSum = tag.getIntensityRRS() + expSpectrum.getIRR(vertices.get(i).getIntensity());
					tag.setIntensityRRS(rankSum);
					
					// Increase matches
					int matches = tag.getMatches() + 1;
					tag.setMatches(matches);
				}
			}
			secondList.add(tag);
		}
		return secondList;
	}
	
	/**
	 * This method filters for the second and third last b-ions.
	 * @param graph
	 * @param secondList
	 * @return thirdList List<Tag>
	 */
	private List<Tag> filterBIons(Graph graph, List<Tag> secondList){
		// First filtered list
		List<Tag> thirdList = new ArrayList<Tag>();		
		List<Vertex> vertices = graph.getVertices();		
		double deltabN_1, deltabN_2, bN_2, bN_1;	
		double precursorMz = expSpectrum.getPrecursorMz();
		// THIRD FILTERING: Checking for b-ions
		for (Tag tag : secondList) {			
			for (int i = vertices.size() - 1; i > 0; i--) {
				// b_N-1-Ion match				
				bN_1 = precursorMz - (Masses.C_term + Masses.MAP.get(tag.getAa1()));
				deltabN_1 = Math.abs(vertices.get(i).getMass() - bN_1);
				if(deltabN_1 <= FRAG_TOL) {
					// Scoring y2-ion
					double score = tag.getScore();
					score += (Math.log10(deltabN_1) * Math.log10(vertices.get(i).getIntensity()) * (-1));
					tag.setScore(score);	
					
					// Intensity reversed rank
					int rankSum = tag.getIntensityRRS() + expSpectrum.getIRR(vertices.get(i).getIntensity());
					tag.setIntensityRRS(rankSum);
					
					// Increase matches
					int matches = tag.getMatches() + 1;
					tag.setMatches(matches);
				}
				// b_N-2-Ion match				
				bN_2 = precursorMz - (Masses.C_term + Masses.MAP.get(tag.getAa1()) + Masses.MAP.get(tag.getAa2()));
				deltabN_2 = Math.abs(vertices.get(i).getMass() - bN_2);
				if(deltabN_2 <= FRAG_TOL) {
					// Scoring y2-ion
					double score = tag.getScore();
					score += (Math.log10(deltabN_2) * Math.log10(vertices.get(i).getIntensity()) * (-1));
					tag.setScore(score);
					
					// Intensity reversed rank
					int rankSum = tag.getIntensityRRS() + expSpectrum.getIRR(vertices.get(i).getIntensity());
					tag.setIntensityRRS(rankSum);
					
					// Increase matches
					int matches = tag.getMatches() + 1;
					tag.setMatches(matches);
				}
			}
			thirdList.add(tag);
		}
		return thirdList;
	}
	
	/**
	 * This method filters for the 2+ charged ions.
	 * @param graph
	 * @param thirdList
	 * @return fourthList List<Tag>
	 */
	private List<Tag> filterChargedIons(Graph graph, List<Tag> thirdList){
		// First filtered list
		List<Tag> fourthList = new ArrayList<Tag>();		
		List<Vertex> vertices = graph.getVertices();		
		double delta1CH2, delta2CH2, b1_CH2, b2_CH2;	
		double deltaY2CH2, deltaY3CH2, y2_CH2, y3_CH2;
		double precursorMz = expSpectrum.getPrecursorMz() / 2;
		int charge = 2;
		for (Tag tag : thirdList) {
			for (int i = 0; i < vertices.size(); i++) {
				// b1_CH2-Ion match			
				double mass1_CH2 = (Masses.C_term + Masses.MAP.get(tag.getAa1()) - Masses.Hydrogen) / charge;
				b1_CH2 = precursorMz - mass1_CH2;
				delta1CH2 = Math.abs(vertices.get(i).getMass() - b1_CH2);
				if(delta1CH2 <= FRAG_TOL) {
					// Scoring b1-charged2 ion
					double score = tag.getScore();
					score += (Math.log10(delta1CH2) * Math.log10(vertices.get(i).getIntensity()) * (-1));
					tag.setScore(score);	
					
					// Increase matches
					int matches = tag.getMatches() + 1;
					tag.setMatches(matches);
				}
				
				// b2_CH2-Ion match			
				double mass2_CH2 = (Masses.C_term + Masses.MAP.get(tag.getAa1()) + Masses.MAP.get(tag.getAa2()) - Masses.Hydrogen) / charge;
				b2_CH2 = precursorMz - mass2_CH2;				
				delta2CH2 = Math.abs(vertices.get(i).getMass() - b2_CH2);
				if(delta2CH2 <= FRAG_TOL) {
					// Scoring b2-charged2 ion
					double score = tag.getScore();
					score += (Math.log10(delta2CH2) * Math.log10(vertices.get(i).getIntensity()) * (-1));
					tag.setScore(score);	
					
					// Increase matches
					int matches = tag.getMatches() + 1;
					tag.setMatches(matches);
				}
				
				// y2_CH2-Ion match			
				y2_CH2 = (Masses.C_term + Masses.MAP.get(tag.getAa1()) + Masses.MAP.get(tag.getAa2()) + Masses.Hydrogen) / charge;				
				deltaY2CH2 = Math.abs(vertices.get(i).getMass() - y2_CH2);
				if(deltaY2CH2 <= FRAG_TOL) {
					// Scoring y2-charged2 ion
					double score = tag.getScore();
					score += (Math.log10(deltaY2CH2) * Math.log10(vertices.get(i).getIntensity()) * (-1));
					tag.setScore(score);	
					
					// Increase matches
					int matches = tag.getMatches() + 1;
					tag.setMatches(matches);
		
				}
				
				// y3_CH2-Ion match			
				y3_CH2 = (Masses.C_term + Masses.MAP.get(tag.getAa1()) + Masses.MAP.get(tag.getAa2()) + Masses.MAP.get(tag.getAa3()) + Masses.Hydrogen) / charge;
				deltaY3CH2 = Math.abs(vertices.get(i).getMass() - y3_CH2);
				if(deltaY3CH2 <= FRAG_TOL) {
					// Scoring y3-charged2 ion
					double score = tag.getScore();
					score += (Math.log10(deltaY3CH2) * Math.log10(vertices.get(i).getIntensity()) * (-1));
					tag.setScore(score);	
					
					// Increase matches
					int matches = tag.getMatches() + 1;
					tag.setMatches(matches);
				}
			}
			fourthList.add(tag);
		}
		return fourthList;
	}
	
	private List<Vertex> findSeeds(Graph graph) {
		List<Vertex> seedPeaks = new ArrayList<Vertex>();
		// First filtered list
		List<Tag> firstList = new ArrayList<Tag>();		
		List<Vertex> vertices = graph.getVertices();
		Set<Character> letters = Masses.letters;
		double deltaY3, y3;	
		
		// FIND SEEDS: Look for double charged ions + the non-charged complements.		
		// TODO!		
		
		return seedPeaks;
		
	}
	
	/**
	 * This method matches the tag to the peaks of the graph.
	 * @param graph
	 * @param tag
	 * @return
	 */
	private Tag matchPeaks(Graph graph, Tag tag){
		Tag pepTag = tag;
		double m1, m2, m3, m4;
		double bIonSum = 0.0;
		List<Peak> matchedPeaks = new ArrayList<Peak>();
		double ion = 0.0;
		List<Vertex> peaks = graph.getVertices();
		
		// Calculate the b-Ion peaks.
					
		m1 = Masses.MAP.get(tag.getAa1());			
		bIonSum += m1;
		ion = Math.abs(expSpectrum.getPrecursorMz() + 1 - (bIonSum + Masses.C_term));
		for (int i = peaks.size() - 1; i > 0; i--){
			if(Math.abs(ion - peaks.get(i).getMass()) < 0.5){
				matchedPeaks.add(new Peak(peaks.get(i).getMass(), peaks.get(i).getIntensity()));
			}
		}
		
		m2 = Masses.MAP.get(tag.getAa2());			
		bIonSum += m2;
		ion = Math.abs(expSpectrum.getPrecursorMz() + 1 - (bIonSum + Masses.C_term));
		for (int i = peaks.size() - 1; i > 0; i--){
			if(Math.abs(ion - peaks.get(i).getMass()) < 0.5){
				matchedPeaks.add(new Peak(peaks.get(i).getMass(), peaks.get(i).getIntensity()));
			}
		}
		m3 = Masses.MAP.get(tag.getAa3());			
		bIonSum += m3;
		ion = Math.abs(expSpectrum.getPrecursorMz() + 1 - (bIonSum + Masses.C_term));
		for (int i = peaks.size() - 1; i > 0; i--){
			if(Math.abs(ion - peaks.get(i).getMass()) < 0.5){
				matchedPeaks.add(new Peak(peaks.get(i).getMass(), peaks.get(i).getIntensity()));
			}
		}
		m4 = Masses.MAP.get(tag.getAa4());			
		bIonSum += m4;
		ion = Math.abs(expSpectrum.getPrecursorMz() + 1 - (bIonSum + Masses.C_term));
		for (int i = peaks.size() - 1; i > 0; i--){
			if(Math.abs(ion - peaks.get(i).getMass()) < 0.5){
				matchedPeaks.add(new Peak(peaks.get(i).getMass(), peaks.get(i).getIntensity()));
			}
		}
		pepTag.setMatches(matchedPeaks.size());
		pepTag.setScore(pepTag.getScore() * pepTag.getMatches());
		return pepTag;
	}	
}
