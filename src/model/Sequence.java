package model;


/**
 * Implementation of the Sequence Interface.
 * 
 * @author Thilo Muth
 *
 */
public class Sequence {
	
	/**
	 * Name of the sequence (ProteinID).
	 */
	private String name;
	
	/**
	 * Character string of the sequence.
	 */
	private char[] sequence;	
	
	/**
	 * Start position of the sequence.
	 */
	private int start;
	
	/**
	 * End position of the sequence.
	 */
	private int end;
	
	/**
	 * Start position of tag match.
	 */
	private int matchStart;
	
	/**
	 * End position of tag match.
	 */
	private int matchEnd;
	
	/**
	 * Copy Constructor
	 * @param seq
	 */
	public Sequence(Sequence seq){
		this.name = seq.getName();
		this.sequence = seq.getSequence();
		this.start = seq.getStart();
		this.end = seq.getEnd();
	}
	
	/**
	 * Constructor for the sequence implementation, where
	 * sequence is given as string
	 * @param name
	 * @param sequence
	 * @param start
	 * @param end
	 */
	public Sequence(String name, String sequence, int start, int end) {
		this.name = name;
		this.sequence = sequence.toCharArray();
		this.start = start;
		this.end = end;
	}

	/**
	 * Constructor for the sequence implementation.
	 * The whole sequence string is taken from start to end position.
	 * @param name
	 * @param sequence
	 */
	public Sequence(String name, String sequence) {
		this(name, sequence, 1, -1);
	}
	
	/**
	 * Returns the letter (amino acid) at a certain position.
	 */
	public char getLetterAt(int i) {
	    if (i < sequence.length) {	      
	      return sequence[i];
	    } else  {
	      return ' ';
	    }
	  }
	
	/**
	 * Returns the sequence start position of the sequence.
	 */
	public int getStart() {
		return start;
	}
	
	/**
	 * Finds the i'th position in the sequence, skipping the gaps.
	 * @param i
	 * @return pos Integer
	 */
	public int findPosition(int i) {
		int j = 0;
		int pos = start;
		int seqlen = sequence.length;
		while ((j < i) && (j < seqlen)) {
			pos++;
			j++;
		}

	    return pos;
	  }
	
	/**
	 * Sets the start position of the sequence.
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * Returns the end position of the sequence.
	 */
	public int getEnd() {
		return this.end;
	}
	
	/**
	 * Sets the end position of the sequence.
	 */
	public void setEnd(int end) {
		this.end = end;
	}

	/**
	 * Returns the sequence length.
	 */
	public int getLength() {
		return this.sequence.length;
	}
	
	/**
	 * Returns the sequence name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the sequence name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the sequence (for start and end position) as char array.
	 */
	public char[] getSequence(int start, int end) {
		if (start < 0) {
			start = 0;
		}
		
		if (start >= sequence.length) {
			return new char[0];
		}

		if (end >= sequence.length) {
			end = sequence.length;
		}

		char[] charseq = new char[end - start];
		System.arraycopy(sequence, start, charseq, 0, end - start);

		return charseq;
	}
	
	/**
	 * Sets the sequence as string.
	 */
	public void setSequence(String sequence) {
		 this.sequence = sequence.toCharArray();

	}
	
	/**
	 * Sets the sequence as char array.
	 * @param sequence
	 */
	public void setSequence(char[] sequence) {
		this.sequence = sequence;
	}
	
	/**
	 * Returns the sequence as a string.
	 */
	public String getSequenceAsString() {
		return new String(sequence);
	}
	
	/**
	 * Returns the sequence as char array.
	 */
	public char[] getSequence() {
		return sequence;
	}
	
	/**
	 * Returns the position of the match start position.
	 */
	public int getMatchStart() {
		return matchStart;
	}
	
	/**
	 * Sets the position of the match start position.
	 */
	public void setMatchStart(int matchStart) {
		this.matchStart = matchStart;
	}
	
	/**
	 * Returns the position of the match end position.
	 */
	public int getMatchEnd() {
		return matchEnd;
	}
	
	/**
	 * Sets the position of the match end position.
	 */
	public void setMatchEnd(int matchEnd) {
		this.matchEnd = matchEnd;
	}

}

