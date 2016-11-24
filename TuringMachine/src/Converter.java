/**
 * Esta classe é usada para converter um Integer em forma de string um Binário em forma de string.
 *  
 * @author Eron
 */
public class Converter {
	
	public static String toBinaryString(String str, int bitsQtt) {
		
		if (str.length() == bitsQtt)
			return str;
		else if (str.length() > bitsQtt)
			return null;
		
		StringBuilder binaryString = new StringBuilder();
		
		for (int i = 0; i < bitsQtt - str.length(); i++)
			binaryString.append("0");
		
		binaryString.append(str);
		
		return binaryString.toString();
	}
}
