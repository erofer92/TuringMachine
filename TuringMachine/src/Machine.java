import java.util.Vector;

/**
    T = (Q, Î£, Î“, q0, Î´)
    Q:  NÃºmero de estados;
    Î£:  NÃºmero de sÃ­mbolos do alfabeto;
    Î“:  NÃºmero de sÃ­mbolos da fita;
    q0: CÃ³digo do estado inicial;
    Î´:  CodificaÃ§Ã£o da funÃ§Ã£o de transiÃ§Ã£o
    
    âˆ‚(an,qm) = (ap, qr, ds)
    an: CÃ³digo do sÃ­mbolo lido;
    qm: CÃ³digo do estado atual da mÃ¡quina;
    ap: CÃ³digo do sÃ­mbolo escrito;
    qr: CÃ³digo do novo estado;
    ds: CÃ³digo da direÃ§Ã£o (esq, dir, halt)
    
    Ex:
    Q:  q0 q1 q2 q3 q4 q5 q6 ... ;
    Î£:  a b c d e ... ;
    Î“:  A B C D E ... ;
    q0: q0;
    Î´:  (a, Q, b, W, L/R) ... ;
*/

public class Machine {
	
	private int machineHead;
	private boolean valid;

	private Validator machineValidator;
	
	private String transitionRules;
	private String[] transitionRulesArray;
	
	private Element blank;
	private Element delimiter;
	private Element initialState;
	private Element left;
	private Element right;
	private Element halt;
	
	private Element[] alphabet;
	private Element[] states;
	private Element[] finalStates;
	private Element[] tapeAlphabet;
	private Element[] fullTapeAlphabet;
	private Element[] directions;
	
	private Vector<Element> defaultTape;
	private Vector<Element> inputTape;
	private Vector<Element> outputTape;
	private Vector<Element> completeTape;
	private Vector<String> binaryTape;
	private Vector<String> binaryTape2;
	
	public Machine (String alphabet, String tapeAlphabet, String blank, String delimiter,
			String states, String initialState, String finalStates, String left, String right, String halt, String transitionRules)
			throws InvalidMachineException {
		
		this.machineHead = 0;
		this.machineValidator = new Validator();
		this.valid = false;
		this.setProperties(alphabet, tapeAlphabet, blank, delimiter,
				states, initialState, finalStates, left, right, halt, transitionRules);
	}

	/* ---------------------------------------- */
	/* ---------------------------------------- */
	/* ----------- MÃ‰TODOS PADRÃƒO ------------- */
	/* ---------------------------------------- */
	/* ---------------------------------------- */
	
	private void setProperties(String alphabet, String tapeAlphabet, String blank, String delimiter,
			String states, String initialState, String finalStates, String left, String right, String halt, String transitionRules)
			throws InvalidMachineException {

		this.defaultTape = new Vector<Element>();
		this.inputTape = new Vector<Element>();
		this.outputTape = new Vector<Element>();
		this.completeTape = new Vector<Element>();
		this.binaryTape = new Vector<String>();
		
		this.setAlphabet(alphabet);
		this.setTapeAlphabet(tapeAlphabet);
		this.setBlank(blank);
		this.setDelimiter(delimiter);
		this.setStates(states);
		this.setInitialState(initialState);
		this.setFinalState(finalStates);
		this.setDirections(left, right, halt);
		this.setTransitionRules(transitionRules);
		this.fillTape();
		this.valid = true;
	}
	
	private void setAlphabet(String alphabet) throws InvalidMachineException {
		this.machineValidator.validateAlphabet(alphabet);
		this.alphabet = this.machineValidator.getAlphabet();
	}
	
	private void setTapeAlphabet(String tapeAlphabet) throws InvalidMachineException {
		this.machineValidator.validateTapeAlphabet(tapeAlphabet);
		this.tapeAlphabet = this.machineValidator.getTapeAlphabet();
		this.fullTapeAlphabet = this.machineValidator.getFullTapeAlphabet();
	}
	
	private void setBlank(String blank) throws InvalidMachineException {
		this.machineValidator.validateBlank(blank);
		this.blank = this.machineValidator.getBlank();
	}
	
	private void setDelimiter(String delimiter) throws InvalidMachineException {
		this.machineValidator.validateDelimiter(delimiter);
		this.delimiter = this.machineValidator.getDelimiter();
	}
	
	private void setStates(String states) throws InvalidMachineException {
		this.machineValidator.validateStates(states);
		this.states = this.machineValidator.getStates();
	}
	
	private void setInitialState(String initialState) throws InvalidMachineException {
		this.machineValidator.validateInitialState(initialState);
		this.initialState = this.machineValidator.getInitialState();
	}
	
	private void setFinalState(String finalStates) throws InvalidMachineException {
		this.machineValidator.validateFinalStates(finalStates);
		this.finalStates = this.machineValidator.getFinalStates();
	}
	
	private void setDirections(String left, String right, String halt) throws InvalidMachineException {
		this.machineValidator.validateDirections(left, right, halt);
		this.directions = this.machineValidator.getDirections();
	}
	
	private void setTransitionRules(String transitionRules) throws InvalidMachineException {
		this.machineValidator.validateTransitionRules(transitionRules);
		this.transitionRules = this.machineValidator.getTransitionRules();
		this.transitionRulesArray = this.machineValidator.getTransitionRulesArray();
	}
	
	public boolean isValid() {
		return this.valid;
	}
	
	public boolean process(String input) throws InvalidMachineException {
		
		String[] inputArray = input.split(" ");
		String[] splittedRule = null;
		
		this.machineValidator.validateInput(inputArray);
		
		this.generateInputTape(inputArray);

		this.outputTape = (Vector<Element>) inputTape.clone();
		this.machineHead = 0;
		
		Element readedElement = this.inputTape.get(0),
				currentState = this.initialState,
				writeElement = null,
				nextState = null,
				direction = null;
		int i = 0, j;
		
		while (direction != this.directions[2]) {
			i++;
			for (j = 0; j < this.transitionRulesArray.length; j++) {

				/*
				 * [0] = SÃ­mbolo lido.
				 * [1] = Estado atual.
				 * [2] = SÃ­mbolo escrito.
				 * [3] = PrÃ³ximo estado.
				 * [4] = DireÃ§Ã£o.
				 */
				splittedRule = transitionRulesArray[j].split(",");
				
				if (readedElement == this.machineValidator.searchFullTapeAlphabetSymbol(splittedRule[0]) &&
					currentState == this.machineValidator.searchState(splittedRule[1])) {
					
					writeElement = this.machineValidator.searchFullTapeAlphabetSymbol(splittedRule[2]);
					nextState = this.machineValidator.searchState(splittedRule[3]);
					direction = this.machineValidator.searchDirection(splittedRule[4]);

					System.out.println (i + ": " +readedElement.getString() + " "  + currentState.getString() + " " +
										writeElement.getString() + " " + nextState.getString() + " " + direction.getString());
					
					this.outputTape.remove(this.machineHead);
					this.outputTape.add(this.machineHead, writeElement);
					currentState = nextState;
					
					
					if (direction == this.directions[0])
						this.machineHead--;
					else if (direction == this.directions[1])
						this.machineHead++;
					else if (direction == this.directions[2]) // HALT
						break;
					
					if (this.machineHead < 0) {
						readedElement = this.blank;
						this.machineHead++;
						this.outputTape.add(this.machineHead, this.blank);
					}
					else if (this.machineHead == this.outputTape.size()) {
						readedElement = this.blank;
						this.outputTape.add(this.machineHead, this.blank);
					}
					else
						readedElement = this.outputTape.get(this.machineHead);
					
					break;
				}
			}
			
			if (j == this.transitionRulesArray.length) // NÃƒO EXISTE NENHUMA REGRA DE TRANSIÃ‡ÃƒO VÃ�LIDA
				throw new InvalidMachineException("Nenhuma regra de transiÃ§Ã£o atende o estado e elemento necessÃ¡rios! \n" +
													"Estado: " + currentState.getString() + "\n" + 
													"Elemento: " + readedElement.getString());
		}
//		this.machineValidator.isOutputValid(this.outputTape
		
//		if ()
//			throw new InvalidMachineException("Halt antes de reconhecer todo o input");
		return true;
	}
	
	private void generateInputTape(String[] input) {
		
		for (int i = 0; i < input.length; i++)
			this.inputTape.add(this.machineValidator.searchAlphabetSymbol(input[i]));
	}

	/* ---------------------------------------- */
	/* ---------------------------------------- */
	/* -------- OPERAÃ‡Ã•ES SOBRE A FITA -------- */
	/* ---------------------------------------- */
	/* ---------------------------------------- */
	
	/**
	 * Preenche a fita.
	 * 
	 * @throws InvalidMachineException
	 */
	private void fillTape() throws InvalidMachineException {
		// A FITA SERÃ� INICIALMENTE PREENCHIDA COM UM BRANCO, O ALFABETO, O ALFABETO DA FITA (SEM REPETIR O ALFABETO)
		// A QUANTIDADE DE ESTADOS, AS REGRAS DE TRANSIÃ‡ÃƒO E POR ÃšLTIMO UM BRANCO.
		
		this.defaultTape = new Vector<Element>();
		
		// INSERE O ALFABETO
		for (int i = 0; i < this.alphabet.length; i++)
			this.defaultTape.add(this.alphabet[i]);
		
		// INSERE O ALFABETO DA FITA
		for (int i = 0; i < this.tapeAlphabet.length; i++)
			this.defaultTape.add(this.tapeAlphabet[i]);
		
		// INSERE A QUANTIDADE DE ESTADOS (NÃƒO TODOS)
		this.defaultTape.add(this.states[this.states.length - 1]);
		
		// INSERE AS REGRAS DE TRANSIÃ‡ÃƒO
		this.defaultTape.addAll(this.transitionRulesToVector());
	}
	
	/**
	 * Gera uma fita com as regras de transiÃ§Ã£o.
	 * Essa fita adicionada a fita principal.
	 *  
	 * @return
	 * @throws InvalidMachineException
	 */
	private Vector<Element> transitionRulesToVector() throws InvalidMachineException {
		
		// CADA REGRA DE TRANSIÃ‡ÃƒO TEM 5 COMPONENTES. CADA COMPONENTE DEVE SER
		// INSERIDO NO VECTOR PARA POSTERIORMENTE O VECTOR SER INSERIDO NA FITA
		
		int i, j;
		String []elements;
		Element element;
		Vector<Element> vector = new Vector<Element>();
		
		for (i = 0; i < this.transitionRulesArray.length; i++) {
			
			// DIVIDE OS ELEMENTOS EM UM ARRAY DE STRINGS
			elements = this.transitionRulesArray[i].split(",");
			
			for (j = 0; j < elements.length; j++) {
				element = this.machineValidator.searchAnySymbol(elements[j]);
				
				if (element == null)
					throw new InvalidMachineException("Erro ao procurar pelo elemento " + elements[j] +
														" da regra de TransiÃ§Ã£o " + this.transitionRulesArray[i]);
				vector.add(element);
			}
		}
		
		return vector; // RETORNA O VECTOR COM TODAS AS REGRAS DE TRANSIÃ‡ÃƒO
	}
	
	/**
	 * Imprime todas as fitas.
	 * A sequencia de impressÃ£o Ã©: defaultTape, inputTape, outputTape, binaryTape.
	 * 
	 * @throws InvalidMachineException
	 */
	public void printAllTapes() throws InvalidMachineException {
		
		this.generateCompleteTape();
		this.generateBinaryTape();
		
		for (int i = 0; i < this.completeTape.size(); i++)
			System.out.print(this.completeTape.elementAt(i).getString() + " ");
		System.out.println();
		
		for (int i = 0; i < this.completeTape.size(); i++)
			System.out.print(this.completeTape.elementAt(i).getInteger() + " ");
		System.out.println();
		
		for (int i = 0; i < this.binaryTape.size(); i++)
			System.out.print(this.binaryTape.elementAt(i) + " ");
		System.out.println();

		System.out.println(this.binaryTape.size() * this.binaryLength());
	}

	/**
	 * Cria uma fita contendo toda a informaÃ§Ã£o da fita original, do input e do output.
	 */
	private void generateCompleteTape() {
		this.completeTape.addAll(this.defaultTape);
		this.completeTape.addAll(this.inputTape);
		this.completeTape.addAll(this.outputTape);
	}

	/**
	 * Transforma a fita completa em uma fita binÃ¡ria.
	 * 
	 * @throws InvalidMachineException
	 */
	private void generateBinaryTape() throws InvalidMachineException {
		
		String str;
		this.binaryTape = new Vector<String>();
		int length = this.binaryLength();
		
		for (int i = 0; i < this.defaultTape.size(); i++) {
			
			str = Converter.toBinaryString (this.completeTape.elementAt(i).getBinary().toString(), length);
			
			if (str == null)
				throw new InvalidMachineException("Falha na transformaÃ§Ã£o dos elementos em binÃ¡rios de tamanho fixo");
			
			this.binaryTape.add(str);
		}
	}
	
	/**
	 * Define o tamanho dos binÃ¡rios que terÃ£o na fita.
	 * 
	 * @return int
	 */
	private int binaryLength() {
		return Integer.toBinaryString(this.machineValidator.getCounter() - 1).length();
	}
}
