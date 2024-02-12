package model;

public class TagHit {
	
	private int scan;
	private int rank;
	private String sequence;
	private double mass;
	private double massDelta;
	private double nGap;	
	private double score;
	private int intensityRRS;
	private int matches;
	
	public TagHit() {		
	}
	
	public int getScan() {
		return scan;
	}
	public void setScan(int scan) {
		this.scan = scan;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public double getMass() {
		return mass;
	}
	
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	public double getMassDelta() {
		return massDelta;
	}

	public void setMassDelta(double massDelta) {
		this.massDelta = massDelta;
	}

	public double getnGap() {
		return nGap;
	}
	public void setnGap(double nGap) {
		this.nGap = nGap;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}

	public int getIntensityRSS() {
		return intensityRRS;
	}

	public void setIntensityRSS(int intensityRRS) {
		this.intensityRRS = intensityRRS;
	}

	public int getMatches() {
		return matches;
	}

	public void setMatches(int matches) {
		this.matches = matches;
	}	
	
	
}
