/**
 * Esta classe � usada como excess�o para todo tipo de erro que possa ocorrer
 * na valida��o da m�quina ou na valida��o/processamento do Input.
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