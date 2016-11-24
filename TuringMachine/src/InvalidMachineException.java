/**
 * Esta classe é usada como excessão para todo tipo de erro que possa ocorrer
 * na validação da máquina ou na validação/processamento do Input.
 * 
 * @author Eron
 */
@SuppressWarnings("serial")
public class InvalidMachineException extends Exception {
	
	private String err;
	
	public InvalidMachineException(String err) {
		this.err = err;
	}
	
	public String getError() {
		return this.err;
	}
}