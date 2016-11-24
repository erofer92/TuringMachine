/**
 * Todo elemento possui um s�mbolo e um n�mero para represent�-lo.
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
