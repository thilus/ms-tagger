package tag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Database;
import model.Sequence;

/**
 * This class does the tag-against-database matching, returning a series of sequences that possibly can hold PSMs.
 * @author Thilo Muth
 *
 */
public class TagMatcher {	
	
	/**
	 * This variable contains the sequence tag.
	 */
	private Tag tag;
	
	/**
	 * This variable contains the FASTA file.
	 */
	private File fastaFile;
	
	/**
	 * This variable contains the database.
	 */
	private Database database;
	
	/**
	 * The constructor retrieves the tag and the fasta file as parameters.
	 * @param tag Tag
	 * @param fastaFile File
	 */
	public TagMatcher(Tag tag, File fastaFile) {
		this.tag = tag;
		this.fastaFile = fastaFile;
		database = this.read();		
	}
	
	/**
	 * This method parses proteinIDs and sequences from the FASTA file.
	 * 
	 * @return Database db
	 */
	public Database read() {
		Database db;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fastaFile));
			String nextLine;
			nextLine = reader.readLine();
			boolean firstline = true;
			ArrayList<String> proteinIDs = new ArrayList<String>();
			ArrayList<String> proteinSeqs = new ArrayList<String>();
			int proteinCounter = 0;
			StringBuffer stringBf = new StringBuffer();
			while (nextLine != null) {
					if(nextLine.trim().length() > 0){
						if (nextLine.charAt(0) == '>') {	
							nextLine = nextLine.substring(1);
							if(firstline){
								proteinCounter++;
								proteinIDs.add(nextLine);
							} else {								
								proteinSeqs.add(stringBf.toString());
								stringBf = new StringBuffer(); 
								proteinCounter++;
								proteinIDs.add(nextLine);
							}							
						} else {
							stringBf.append(nextLine);		
						}	
					}
					nextLine = reader.readLine();
					firstline = false;	
			}
			proteinSeqs.add(stringBf.toString());
			
			// List of sequences
			List<Sequence> sequences = new ArrayList<Sequence>();
			
			// Iterate over the proteinIDs
			for(int i = 0; i < proteinIDs.size(); i++){
				sequences.add(new Sequence(proteinIDs.get(i), proteinSeqs.get(i)));
			}
			
			// Instantiate the Database object.
			db = new Database(fastaFile.getName(), sequences);			
			reader.close();

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return db;
	}
	
	/**
	 * Match the tag against the database sequences.
	 * @return sequences List<Sequence>
	 */
	public List<Sequence> getMatchedSequences() {
		// Get the sequences from the fasta database.
		List<Sequence> sequences = null; // TODO: get the sequences!
		List<Sequence> matchedSequences = new ArrayList<Sequence>();

		// Reverse the end tag for the matching.
		String reverseTag = new StringBuffer(tag.toString()).reverse().toString();
		for (Sequence seq : sequences) {
			String proteinSeq = seq.getSequenceAsString();
			if (proteinSeq.contains(reverseTag)) {

				// Set the match start to occurrence of the first letter of the tag.
				seq.setMatchStart(proteinSeq.indexOf(reverseTag));

				// Set the match end to occurrence of the last letter of the tag.
				seq.setMatchEnd(proteinSeq.indexOf(reverseTag) + reverseTag.length());

				// Add match.
				matchedSequences.add(seq);
			}
		}
		return matchedSequences;
	}
	
}
