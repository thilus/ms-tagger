package model;

import java.util.List;

public class Database {
	/**
	 * The filename of the FASTA-Database
	 */
	private String filename;

	/**
	 * The list of sequences.
	 */
	private List<Sequence> sequences;

	public Database(String filename, List<Sequence> sequences) {
		super();
		this.filename = filename;
		this.sequences = sequences;
	}
}
