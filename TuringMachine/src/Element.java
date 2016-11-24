/**
 * Todo elemento possui um símbolo e um número para representá-lo.
 * 
 * @author Eron
 */
public class Element {

	private String string;
	private Integer integer;
	
	public Element (String string, Integer integer) {
		this.string = string;
		this.integer = integer;
	}
	
	public Integer getInteger() {
		return this.integer;
	}
	
	public String getString() {
		return this.string;
	}
	
	public Integer getBinary() {
		String binary = Integer.toBinaryString(this.integer);
		return Integer.parseInt(binary);
	}
}
