package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spectrum {
	
	/**
	 * List containing the peaks of the spectrum.
	 */
	private List<Peak> peakList;	
	
	/**
	 * Variable containing the precursor m/z.
	 */
	private double precursorMz;
	
	/**
	 * Variable containing the charge.
	 */
	private int charge;
	
	private String filename;
	
	private boolean identified;
	
	/**
	 * Map contains the ranks of the ranked intensities.
	 */
	private Map<Double, Integer> rankMap;
	
	/**
	 * The spectrum has peaks, the precursor mz and the charge as parameters.
	 * @param filename
	 * @param peakList
	 * @param precursorMz
	 * @param charge
	 */
	public Spectrum(String filename, List<Peak> peakList, HashMap<Double, Integer> rankMap, double precursorMz, int charge) {
		super();
		this.filename = filename;
		this.peakList = peakList;
		this.rankMap = rankMap;
		this.precursorMz = precursorMz;
		this.charge = charge;
	}
	
	public String getFilename() {
		return filename;
	}

	public List<Peak> getPeakList() {
		return peakList;
	}

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public double getPrecursorMz() {
		return precursorMz;
	}

	public void setPrecursorMz(double precursorMz) {
		this.precursorMz = precursorMz;
	}

	public boolean isIdentified() {
		return identified;
	}

	public void setIdentified(boolean identified) {
		this.identified = identified;
	}
	
	/**
	 * Returns the reversed rank of the intensity.
	 * @param intensity
	 * @return reversed rank Integer
	 */
	public int getIRR(double intensity){
		return rankMap.get(intensity);
	}
	
}
