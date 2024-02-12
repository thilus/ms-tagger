package tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class holds all the masses used for the calculation of theoretical masses.
 * @author Thilo Muth
 *
 */
public class Masses {	
	
	
	public static Map<Character, Double> MAP;
	public static Set<Character> letters = new TreeSet<Character>();
	public static Set<Character> trypticLetters = new TreeSet<Character>();
	
	public static void init(){
		MAP = new HashMap<Character, Double>(20);
		// Fill the map
		// Alanine
		MAP.put('A', 71.037110);
		// Cysteine
		MAP.put('C', 160.030649);
		// Aspartic Acid
		MAP.put('D', 115.026940);
		// Glutatmatic Acid
		MAP.put('E', 129.042590);
		// Phenylalanine
		MAP.put('F', 147.068410);
		// Glycin
		MAP.put('G', 57.021460);
		// Histidine
		MAP.put('H', 137.0589116);
		// Isoleucine
		MAP.put('I', 113.084060);
		// Leucine
		MAP.put('L', 113.084060);
		// Lysine
		MAP.put('K', 128.094963);
		// Methionine
		MAP.put('M', 131.040490);
		// Asparagine
		MAP.put('N', 114.042930);
		// Proline
		MAP.put('P', 97.052760);
		// Glutamine
		MAP.put('Q', 128.058580);
		// Arginine
		MAP.put('R', 156.101110);
		// Serine
		MAP.put('S', 87.032030);
		// Threonine
		MAP.put('T', 101.047680);
		// Valine
		MAP.put('V', 99.068410);
		// Tryptophan
		MAP.put('W', 186.079310);
		// Tyrosine
		MAP.put('Y', 163.063330);
		
		// Tyrosine
		MAP.put('U', 100.0000);
		
		// Add the keys to the letters.
		letters.addAll(MAP.keySet());
		
		// Trypsin main cleaves Lysine or Arginine
		trypticLetters.add('K');
		trypticLetters.add('R');
	}
	
	
	/**
	 * The mass of Hydrogen
	 */
	public static final double Hydrogen = 1.007825;
	/**
	 * The mass of Carbon
	 */
	public static final double Carbon = 12.000000;
	/**
	 * The mass of Nitrogen
	 */
	public static final double Nitrogen = 14.003070;
	/**
	 * The mass of Oxygen
	 */
	public static final double Oxygen = 15.994910;
	/**
	 * The mass of an electron
	 */
	public static final double Electron = 0.005490;
	/**
	 * The mass of the C Terminus = Oxygen + 3* Hydrogen
	 */
	public static final double C_term = 19.017837;
	/**
	 * The mass of the N Terminus = Hydrogen
	 */
	public static final double N_term = 1.007825;
	

}


