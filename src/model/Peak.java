package model;

public class Peak {
	
	/**
	 * The m/z value.
	 */
	private double mz;
	
	/**
	 * The intensity value.
	 */
	private double intensity;
	
	/**
	 * This constructor is used for the theoretical peaks.
	 * @param mz
	 */
	public Peak(double mz) {
		super();
		this.mz = mz;		
	}
	
	/**
	 * This constructor is used for the observed peaks.
	 * @param mz
	 * @param intensity
	 */
	public Peak(double mz, double intensity) {
		super();
		this.mz = mz;
		this.intensity = intensity;
	}
	public double getMz() {
		return mz;
	}
	public double getIntensity() {
		return intensity;
	}
	
}
