package search;

import graph.Graph;
import io.MascotGenericFile;
import io.MascotGenericFileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Spectrum;
import model.TagHit;
import tag.Masses;
import tag.PreProcessor;
import tag.Tag;
import tag.TagGenerator;

public class Starter {
	
    /**
     * Default constructor.
     */
    public Starter() {
    }
	
	public static void main(String[] args) throws IOException {
		// File input from args
		File mgfFile = new File("test//test.mgf");
		
		/**if(args[0] != null) {
			mgfFile = new File(args[0]);
		} else {
			throw new IOException("Spectrum input file is missing...");
		}*/	
		
		// The MGF reader.
		MascotGenericFileReader reader = new MascotGenericFileReader(mgfFile);
		// Init masses.
		Masses.init();		
		
		// Read spectrum files.
		List<MascotGenericFile> spectrumFiles = reader.getSpectrumFiles();
		
		// Init the starter
		Starter starter = new Starter();
		
		System.out.println("Starting job: Reading " + spectrumFiles.size() + " spectrum files and generating tags... ");
		
		// Get the tag hits
		long start = System.currentTimeMillis();
		List<TagHit> taghits = starter.getTagHits(spectrumFiles);
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("Running time: " + time);
		// Write the output. 
		File outFile = new File(mgfFile.getAbsolutePath() + ".out");
		starter.writeToFile(taghits, outFile);
		
		System.out.println("Finished tag generation... Output written to " + outFile.getName());
		// Second loop is for the tag to database matching.
		// File fasta = new File("C:/Users/TMuth/Downloads/human/human.fasta");
		/*for (MascotGenericFile mgf : spectrumFiles) {
			
			// Preprocessing the spectra. 
			PreProcessor preProcessor = new PreProcessor(mgf);
			
			// Spectrum
			Spectrum expSpectrum = preProcessor.format();
			
			// Get the rounded precursor from the spectrum.
			double precursorMz = round(expSpectrum.getPrecursorMz());
			
			// Check whether the map contains a key for the precursorMz
			if(tagMap.containsKey(precursorMz)){				
					Tag tag = tagMap.get(precursorMz);
					System.out.println(tag + " " + tag.getScore());
					System.out.println("<----------------DATABASE SEARCH------------------>");
					System.out.println("SCAN: " + scan);
					System.out.println("spectrum name: " + expSpectrum.getFilename());
					// Tag list for possible isoforms.
					List<Tag> tags = new ArrayList<Tag>();
					tags.add(tag);
					if(tag.hasIsoform()){			
						tags.add(tag.getIsoform());
					}		
					
					// Iterate over tags with possible isoforms.
					for (Tag isoTag : tags) {
						TagMatcher tagMatcher = new TagMatcher(isoTag, fasta);		
						List<Peak> obsPeaks = expSpectrum.getPeakList();	
						PeakComparator peakComparator = new PeakComparator(expSpectrum, 0.5);
						List<Sequence> dbSequences = tagMatcher.getMatchedSequences();
						for (int i = 0; i < dbSequences.size(); i++) {
									List<Peak> theoPeaks = peakComparator.calcTheoPeaks(dbSequences.get(i), isoTag);					
									PeptideHit hit = peakComparator.comparePeaks(dbSequences.get(i), obsPeaks, theoPeaks);
									if(hit != null){
										System.out.println("peptide hit: " + hit.getSequence());
										System.out.println("matched peaks: " + hit.getMatchedPeaks());
										System.out.println("delta: " + hit.getDelta());
										System.out.println("score: " + hit.getScore() * tag.getMatches());
										System.out.println("intensity score: " + hit.getIntensityScore());
										System.out.println("tag peaks: " + tag.getMatches());
										expSpectrum.setIdentified(true);
									}
							}
						}
					if(expSpectrum.isIdentified()) {
						count++;
					}
			}
			scan++;
		}
		System.out.println(count + " identifications in " + spectrumFiles.size() + " spectra.");*/
	}

	/**
     * Extracts tag hits from the given spectrum files.
     *
     * @param spectrumFiles List of spectrum files
     * @return List of TagHit objects
     */
    private List<TagHit> getTagHits(List<MascotGenericFile> spectrumFiles) {
        List<TagHit> tagHits = new ArrayList<>();
        int scan = 1;
        for (MascotGenericFile mgf : spectrumFiles) {
            PreProcessor preProcessor = new PreProcessor(mgf);
            Spectrum expSpectrum = preProcessor.format();
            if (scan % 100 == 0) System.out.println("Scan number... " + scan);
            if (expSpectrum.getCharge() < 3) {
                TagGenerator generator = new TagGenerator(expSpectrum);
                Graph graph = generator.generateGraph();
                List<Tag> bestTags = generator.getCTermTags(graph);
                if (!bestTags.isEmpty()) {
                    for (int i = 0; i < bestTags.size(); i++) {
                        Tag tag = bestTags.get(i);
                        TagHit taghit = new TagHit();
                        taghit.setScan(scan);
                        taghit.setRank(i + 1);
                        String seq = new StringBuffer(tag.toString()).reverse().toString();
                        taghit.setSequence(seq);
                        double tagMass = round(tag.getyIonEndMass());
                        taghit.setMass(tagMass);
                        taghit.setMassDelta(round(tag.getMassDelta()));
                        taghit.setScore(round(tag.getScore()));
                        taghit.setIntensityRSS(tag.getIntensityRRS());
                        taghit.setMatches(tag.getMatches());
                        taghit.setnGap(round(expSpectrum.getPrecursorMz() - tagMass));
                        tagHits.add(taghit);
                    }
                }
            }
            scan++;
        }
        return tagHits;
    }
	
	
	private HashMap<Double, Tag> getPrecursorMap(List<MascotGenericFile> spectrumFiles){
		// Precursor Map initialization
		HashMap<Double, Tag> precursorMap = new HashMap<Double, Tag>();
		int index = 1;
		// Iterate the spectra
		for (MascotGenericFile mgf : spectrumFiles) {
			// Preprocessing the spectra.
			PreProcessor preProcessor = new PreProcessor(mgf);
			
			// Spectrum
			Spectrum expSpectrum = preProcessor.format();			
			System.out.println("SCAN: " + index);
			index++;
			// Only charge 2 supported
			if(expSpectrum.getCharge() < 3){
				// Instantiate end tags list.
				List<Tag> endTags = new ArrayList<Tag>();
				TagGenerator generator = new TagGenerator(expSpectrum);
				
				// Generate the graph for the spectrum.
				Graph graph = generator.generateGraph();
				
				// Retrieve the best tags
				List<Tag> bestTags = generator.getCTermTags(graph);
								
				// Write tags to file
				
				// Iterate through the best tags
				if (bestTags.size() > 0){
					for (Tag tag : bestTags) {
						double precursorMz = round(expSpectrum.getPrecursorMz());						
						// Check whether the map has already a tag entry for the precursorMz
						if (precursorMap.containsKey(precursorMz)) {
							Tag mappedTag = precursorMap.get(precursorMz);
							// Add to the map only if the score is higher
							if (tag.getScore() > mappedTag.getScore()) {
								precursorMap.put(precursorMz, tag);
								// Leucin == Isoleucin Isoform
							} else if (tag.getScore() == mappedTag.getScore()){
								mappedTag.setIsoform(true);								
								precursorMap.put(precursorMz, mappedTag);
							}	
						} else {
							precursorMap.put(precursorMz, tag);
						}
					}
					}
			}
			
		}
		return precursorMap;
	}	
	
/**
     * Helper method to round a double value.
     *
     * @param precursorMz Double value to round
     * @return Rounded double value
     */
    private static double round(final double precursorMz) {
        BigDecimal bd = new BigDecimal(precursorMz).setScale(4, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    /**
     * Writes the list of TagHits to a file.
     *
     * @param taghits List of TagHit objects
     * @param outfile Output file
     */
    public void writeToFile(List<TagHit> taghits, File outfile) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));
            writer.write("\tScan" + " " + "Rank" + " " + "Tag" + " " + "  Score" + " " + "  RRS" + " " + " Ions" + " " + "Delta" + "\t    " + "Mass" + "\t    " + "N_Gap");
            writer.newLine();
            for (TagHit tagHit : taghits) {
                writer.write("\t" + tagHit.getScan() + "\t " + tagHit.getRank() + "\t  " + tagHit.getSequence() + "\t" + tagHit.getScore() + "\t" + tagHit.getIntensityRSS() + "\t " + tagHit.getMatches() + "\t  " + tagHit.getMassDelta() + "\t" + tagHit.getMass() + "\t" + tagHit.getnGap());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
