package tag;

public class Tag {
	
	// First letter
	private Character aa1;
	
	// Second letter
	private Character aa2;
	
	// Third letter
	private Character aa3;
	
	// Fourth letter aa4;
	private Character aa4;
	

	// Total mass difference
	private double massDelta;
	
	// Abundance of all the peaks of the tag.
	private double totalIntensity;
	
	// Tag score
	private double score;
	
	// Index of the start vertex
	private int startIndex;
	
	// Index of the end vertex
	private int endIndex;
	
	private double bIonMass;
	
	// The charge of the tag
	private int charge;

	private double yIonStartMass;
	
	private double yIonEndMass;
	
	private int matches;
	
	private boolean isoform;
	
	// Intensity reversed rank sum --> The higher the better!
	private int intensityRRS;
	
	/**
	 * Constructor for the Tag. 
	 * @param aa1 first letter
	 * @param aa2 second letter
	 * @param aa3 third letter
	 */
	public Tag(Character aa1, Character aa2, Character aa3, int endIndex) {
		super();
		this.aa1 = aa1;
		this.aa2 = aa2;
		this.aa3 = aa3;
		this.endIndex = endIndex;
		calculateIons();
	}
	
	public Tag(Character aa1, Character aa2, Character aa3, Character aa4) {
		super();
		this.aa1 = aa1;
		this.aa2 = aa2;
		this.aa3 = aa3;
		this.aa4 = aa4;
	}
	
	/**
     * Copy Constructor
     * @param tag
     */
    public Tag(Tag tag){
            this.aa1 = tag.getAa1();
            this.aa2 = tag.getAa2();
            this.aa3 = tag.getAa3();
            this.aa4 = tag.getAa4();
            this.charge = tag.getCharge();
            this.isoform = tag.hasIsoform();
            this.bIonMass = tag.getbIonMass();
            this.yIonStartMass = tag.getyIonStartMass();
            this.yIonEndMass = tag.getyIonEndMass();
            this.endIndex = tag.getEndIndex();
            this.startIndex = tag.getStartIndex();
            this.matches = tag.getMatches();
            this.score = tag.getScore();
    }

	private void calculateIons() {
		yIonStartMass = Masses.N_term + Masses.MAP.get(aa1) + Masses.MAP.get(aa2) + Masses.MAP.get(aa3);
		yIonEndMass = Masses.C_term + Masses.MAP.get(aa1) + Masses.MAP.get(aa2) + Masses.MAP.get(aa3);		
	}
	
	/**
	 * Returns the whole tag as string.
	 */
	public String toString() {		
		if(aa4 != null) {
			return aa1.toString() + aa2.toString() + aa3.toString() + aa4.toString();
		} else {
			return aa1.toString() + aa2.toString() + aa3.toString();
		}
	}
	
	public Tag getIsoform(){
		Tag isoTag = new Tag(this);
		if(this.getAa1() == 'I'){					
			isoTag.setAa1('L');
		} else if(this.getAa1() == 'L'){
			isoTag.setAa1('I');
		}
		if(this.getAa2() == 'I'){
			isoTag.setAa2('L');
		} else if(this.getAa2() == 'L'){
			isoTag.setAa2('I');
		}
		if(this.getAa3() == 'I'){
			isoTag.setAa3('L');
		} else if(this.getAa3() == 'L'){
			isoTag.setAa3('I');
		}
		if(this.getAa4() == 'I'){
			isoTag.setAa4('L');
		} else if(this.getAa4() == 'L'){
			isoTag.setAa4('I');
		}			
		return isoTag;
	}
	
	public double getValue1(){
		return Masses.MAP.get(aa1) + Masses.N_term;
	}
	
	public double getValue2(){
		return Masses.MAP.get(aa2);
	}
	
	public double getValue3(){
		return Masses.MAP.get(aa3);
	}
	
	public double getValueN(){
		return Masses.MAP.get(aa1) + Masses.C_term;
	}
	
	public double getValueNminus1(){
		return Masses.MAP.get(aa2);
	}
	
	public double getValueNminus2(){
		return Masses.MAP.get(aa3);
	}
	public Character getAa1() {
		return aa1;
	}

	public Character getAa2() {
		return aa2;
	}

	public Character getAa3() {
		return aa3;
	}
	
	public Character getAa4() {
		return aa4;
	}

	public void setAa1(Character aa1) {
		this.aa1 = aa1;
	}

	public void setAa2(Character aa2) {
		this.aa2 = aa2;
	}

	public void setAa3(Character aa3) {
		this.aa3 = aa3;
	}

	public void setAa4(Character aa4) {
		this.aa4 = aa4;
	}

	public double getMassDelta() {
		return massDelta;
	}

	public void setMassDelta(double massDelta) {
		this.massDelta = massDelta;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public double getbIonMass() {
		return bIonMass;
	}
	
	public double getyIonStartMass() {
		return yIonStartMass;
	}
	
	public double getyIonEndMass() {
		return yIonEndMass;
	}	
	
	public int getCharge() {
		return charge;
	}

	public double getTotalIntensity() {
		return totalIntensity;
	}

	public void setTotalIntensity(double totalIntensity) {
		this.totalIntensity = totalIntensity;
	}

	public int getMatches() {
		return matches;
	}

	public void setMatches(int matches) {
		this.matches = matches;
	}

	public boolean hasIsoform() {
		return isoform;
	}

	public void setIsoform(boolean isoform) {
		this.isoform = isoform;
	}

	public int getIntensityRRS() {
		return intensityRRS;
	}

	public void setIntensityRRS(int intensityRRS) {
		this.intensityRRS = intensityRRS;
	}
	
	
}
