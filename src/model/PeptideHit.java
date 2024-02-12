package model;

public class PeptideHit {
	
	private int scan;
	private String sequence;
	private String accession;
	private int matchedPeaks;
	private double massDiff;	
	private double score;
	private double intensityScore;
	private int start;
	private int end;	
	private double mh;
	private double delta;
	
	public PeptideHit() {	
	}

	public int getScan() {
		return scan;
	}

	public void setScan(int scan) {
		this.scan = scan;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getAccession() {
		return accession;
	}

	public void setAccession(String accession) {
		this.accession = accession;
	}

	public int getMatchedPeaks() {
		return matchedPeaks;
	}

	public void setMatchedPeaks(int matchedPeaks) {
		this.matchedPeaks = matchedPeaks;
	}

	public double getMassDiff() {
		return massDiff;
	}

	public void setMassDiff(double massDiff) {
		this.massDiff = massDiff;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getIntensityScore() {
		return intensityScore;
	}

	public void setIntensityScore(double intensityScore) {
		this.intensityScore = intensityScore;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public double getMh() {
		return mh;
	}

	public void setMh(double mh) {
		this.mh = mh;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}
	
	
}
