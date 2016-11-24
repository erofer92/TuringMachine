/**
 * Classe feita para validar a Maquina de Turing especificada.
 * 
 * @author Eron
 */
public class Validator {
	
	private int counter = 0;
	
	private String transitionRules;
	private String[] transitionRulesArray;
	
	private Element blank;
	private Element delimiter;
	private Element initialState;
	
	private Element[] alphabet;
	private Element[] states;
	private Element[] finalStates;
	private Element[] tapeAlphabet;
	private Element[] fullTapeAlphabet;
	private Element[] directions;
	
	/**
	 * Define os simbolos do alfabeto.
	 * Os simbolos podem ser caracteres ou strings (sem espaço).
	 * Todos os simbolos do alfabeto devem ser separados por um espaço.
	 * Ex1: a b c d
	 * Ex2: red green blue
	 * Ex3: b ba d ss
	 * 
	 * @param alphabet
	 * @throws InvalidMachineException
	 */
	public void validateAlphabet(String alphabet) throws InvalidMachineException {
		
		String []alphabetArray = alphabet.split(" ");
		
		if (this.hasDuplicatedSymbols(alphabetArray))
			throw new InvalidMachineException("Duplicated Symbols in the Alphabet!");
		
		this.alphabet = new Element[alphabetArray.length];
		
		for (int i = 0; i < alphabetArray.length; i++)
			this.alphabet[i] = new Element(alphabetArray[i], new Integer(this.counter++));
	}
	
	/**
	 * Define os simbolos do alfabeto da fita.
	 * Dentre eles, deve ser definido o delimitador e o branco.
	 * Os simbolos do alfabeto da fita devem ser diferentes dos simbolos do
	 * alfabeto e podem, da mesma forma que o alfabeto, ser caracteres ou strings (sem espaÃ§o).
	 * Todos os simbolos do alfabeto da fita devem ser separados por um espaÃ§o.
	 * Ex1: a b c d
	 * Ex2: red green blue
	 * Ex3: b ba d ss
	 * 
	 * @param tapeAlphabet
	 * @throws InvalidMachineException
	 */
	public void validateTapeAlphabet(String tapeAlphabet) throws InvalidMachineException {
		
		String[] tapeAlphabetArray = tapeAlphabet.split(" ");
		
		if (this.hasDuplicatedSymbolsInTape(tapeAlphabetArray))
			throw new InvalidMachineException("Duplicated Tape Alphabet Symbol!");

		this.tapeAlphabet = new Element[tapeAlphabetArray.length];
		this.fullTapeAlphabet = new Element[tapeAlphabetArray.length + this.alphabet.length];
		
		// Preenche a fita apenas com os símbolos que não fazem parte do alfabeto.
		for (int i = 0; i < tapeAlphabetArray.length; i++)
			this.tapeAlphabet[i] = new Element(tapeAlphabetArray[i], new Integer(this.counter++));

		// Preenche a fita com todos os simbolos.
		// Tanto os que estão no alfabeto, quanto os que estão no alfabeto da fita.
		{
			for (int i = 0; i < this.alphabet.length; i++)
				this.fullTapeAlphabet[i] = this.alphabet[i];
			
			for (int i = 0; i < this.tapeAlphabet.length; i++)
				this.fullTapeAlphabet[this.alphabet.length + i] = this.tapeAlphabet[i];
		}
	}
	
	/**
	 * Define o simbolo que representa o branco.
	 * Ele deve fazer parte do alfabeto da fita.
	 * O simbolo pode ser um caractere ou uma string (sem espaço).
	 * 
	 * @param blank
	 * @throws InvalidMachineException
	 */
	public void validateBlank(String blank) throws InvalidMachineException {
		
		if (!this.isPartOfTapeAlphabet(blank))
			throw new InvalidMachineException("Blank isn't part of Tape Alphabet!");
		
		this.blank = searchTapeAlphabetSymbol(blank);
	}
	
	/**
	 * Define o simbolo que representa o delimitador.
	 * Ele deve fazer parte do alfabeto da fita.
	 * O simbolo pode ser um caractere ou uma string (sem espaço).
	 * 
	 * @param delimiter
	 * @throws InvalidMachineException
	 */
	public void validateDelimiter(String delimiter) throws InvalidMachineException {
		
		if (!isPartOfTapeAlphabet(delimiter))
			throw new InvalidMachineException("Delimiter isn't part of Tape Alphabet!");
		
		this.delimiter = searchTapeAlphabetSymbol(delimiter);
	}
	
	/**
	 * Define os estados que a máquina terá.
	 * Deve existir no mínimo 1 estado.
	 * A contagem de estados se inicia em zero (0) e vai até a quantidade de estados menos 1.
	 * Ex: 0, 1, 2, ..., n - 1. Onde 'n' é a quantidade de estados.
	 * 
	 * @param statesQtt
	 * @throws InvalidMachineException
	 */
	public void validateStates(String states) throws InvalidMachineException {
		
		String[] statesArray = states.split(" ");
		
		if (statesArray.length < 1)
			throw new InvalidMachineException("Insufficient States Quantities!");
		
		for (int i = 0; i < statesArray.length; i++)
			if (isPartOfTapeAlphabet(statesArray[i]))
				throw new InvalidMachineException("The symbol used for the state has already been used for an element of the alphabet/tape alphabet (" + statesArray[i] + ")");
		
		this.states = new Element[statesArray.length];

		for (int i = 0; i < statesArray.length; i++)
			this.states[i] = new Element(statesArray[i], new Integer(this.counter++));
	}
	
	/**
	 * Define o estado inicial.
	 * Deve existir apenas 1 estado inicial.
	 * Esse estado inicial deve estar dentro do intervalo de estados definidos.
	 * Se Q = {0, 1, 2, ..., n - 1}, tal que n = qtd de estados - 1, o estado inicial deve pertencer a este conjunto.
	 * 
	 * @param initialState
	 * @param states
	 * @throws InvalidMachineException
	 */
	public void validateInitialState(String initialState) throws InvalidMachineException {
		
		if (initialState.split(" ").length > 1)
			throw new InvalidMachineException("Too many Initial States.");
		
		this.initialState = this.searchState(initialState);
		
		if (this.initialState == null)
			throw new InvalidMachineException("Invalid Initial State Symbol.");
	}
	
	/**
	 * Define os estados finais.
	 * Deve existir no mínimo 1 estado final.
	 * Os estados finais devem estar dentro do intervalo de estados definidos.
	 * Se Q = {0, 1, 2, ..., n - 1}, tal que n = qtd de estados - 1, os estados finais devem pertencer a este conjunto. 
	 * 
	 * @param finalStates
	 * @param states
	 * @throws InvalidMachineException
	 */
	public void validateFinalStates(String finalStates) throws InvalidMachineException {
		
		String[] finalStatesArray = finalStates.split(" ");
		
		if (finalStatesArray.length < 1)
			throw new InvalidMachineException("Insufficient Final States");
		
		this.finalStates = new Element[finalStatesArray.length];
		
		for (int i = 0; i < finalStatesArray.length; i++) {
			
			this.finalStates[i] = searchState(finalStatesArray[i]);
			
			if (this.finalStates[i] == null)
				throw new InvalidMachineException("Invalid Final State: " + finalStatesArray[i]);
		}	
	}
	
	/**
	 * Define as direções.
	 * 
	 * @param left
	 * @param right
	 * @param halt
	 * @throws InvalidMachineException
	 */
	public void validateDirections(String left, String right, String halt) throws InvalidMachineException {

		if (isPartOfTapeAlphabet(left) || isPartOfStates(left) || 
			isPartOfTapeAlphabet(right) || isPartOfStates(right) ||
			isPartOfTapeAlphabet(halt) || isPartOfStates(halt))
			throw new InvalidMachineException("Some of the direction symbols has already been used in the alphabet, tape alphabet or states!");
		
		this.directions = new Element[3];
		
		this.directions[0] = new Element(left, new Integer(this.counter++));
		this.directions[1] = new Element(right, new Integer(this.counter++));
		this.directions[2] = new Element(halt, new Integer(this.counter++));
	}
	
	/**
	 * Define as regras de transição.
	 * As regras devem ser separadas por espaço e seus elementos por virgula.
	 * O formato de uma regra de transição Ã© "q0,x,q1,y,L", onde 'q0' é o estado atual,
	 * 'q1' é o próximo estado, 'x' é o símbolo lido, 'y' é o simbolo escrito e
	 * 'L' é a direção que o cabeçote da máquina se deslocará.
	 *  
	 * @param transitionRules
	 * @throws InvalidMachineException
	 */
	public void validateTransitionRules(String transitionRules) throws InvalidMachineException {
		
		String[] transitionRulesArray = transitionRules.split(" ");
		
		if (this.hasUnknownSymbols(transitionRulesArray))
			throw new InvalidMachineException("Unknown Symbols in Transition Rules!");
		
		if (this.hasntHaltSymbol(transitionRulesArray))
			throw new InvalidMachineException("Hasn't Halt in Transition Rules!");
		
		if (this.haltWithoutFinalState(transitionRulesArray))
			throw new InvalidMachineException("Halt in States that aren't Final State!");
		
		if (this.hasDuplicatedSymbols(transitionRulesArray))
			throw new InvalidMachineException("Duplicated Transition Rules!");
		
		this.transitionRules = transitionRules;
		this.transitionRulesArray = transitionRulesArray;
	}
	
	/**
	 * Verifica se os elementos usados no input especificado fazem parte do alfabeto.
	 * O input deve ter seus simbolos separados por espaço.
	 * 
	 * @param input
	 * @throws InvalidMachineException
	 */
	public void validateInput(String[] inputArray) throws InvalidMachineException {
		
		for (int i = 0; i < inputArray.length; i++)
			if (searchAlphabetSymbol(inputArray[i]) == null)
				throw new InvalidMachineException(inputArray[i] + " isn't part of the alphabet!");
	}
	
	/*
	 * 
	 * 
	 * AUX METHODS!
	 * 
	 * 
	 */
	
	/**
	 * Verifica se existe alguma regra de transição que possui HALT mas não leva a um estado final.
	 * 
	 * @param transitionRules
	 * @return
	 */
	private boolean haltWithoutFinalState(String[] transitionRulesArray) {
		
		String[] aux;
		
		/*
		 * [0] = Símbolo lido.
		 * [1] = Estado atual.
		 * [2] = Símbolo escrito.
		 * [3] = Próximo estado.
		 * [4] = Direção.
		 */
		
		for (int i = 0, j = 0; i < transitionRulesArray.length; i++) {
			
			aux = transitionRulesArray[i].split(",");
			
			if (this.directions[2].getString() == aux[4]) {
				
				for (j = 0; j < this.finalStates.length; j++)
					if (this.finalStates[i].getString() == aux[3])
						break;
				
				if (j == this.finalStates.length) // aux[3] não é estado final e aux[4] é halt
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Verifica se não existe HALT nas regras de transição.
	 * 
	 * @param transitionRules
	 * @return
	 */
	private boolean hasntHaltSymbol(String[] transitionRulesArray) {
		
		for (int i = 0; i < transitionRulesArray.length; i++)
			if (this.directions[2].getString().equals(transitionRulesArray[i].split(",")[4]))
				return false;
		
		return true;
	}
	
	/**
	 * Verifica se há símbolos duplicados.
	 * 
	 * @param str1
	 * @return
	 */
	private boolean hasDuplicatedSymbols(String[] string) {
		
		int i, j;
		
		for (i = 0; i < string.length - 1; i++) {
			
			for (j = i + 1; j < string.length; j++)
				if (string[i].equals(string[j]))
					return true;
		}
		
		return false;
	}
	
	/**
	 * Verifica se há símbolos duplicados na fita.
	 * 
	 * @param tapeAlphabet
	 * @return
	 */
	private boolean hasDuplicatedSymbolsInTape(String[] tapeAlphabetArray) {
		
		if (hasDuplicatedSymbols(tapeAlphabetArray))
			return true;
		
		for (int i = 0; i < this.alphabet.length; i++) {
			for (int j = 0; j < tapeAlphabetArray.length; j++) {
				if (this.alphabet[i].getString().equals(tapeAlphabetArray[j]))
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Verifica se já existe um elemento do alfabeto da fita representado pelo símbolo especificado.
	 * 
	 * @param str
	 * @return
	 */
	public boolean isPartOfTapeAlphabet(String str) {
		
		for (int i = 0; i < this.fullTapeAlphabet.length; i++)
			if (this.fullTapeAlphabet[i].getString().equals(str))
				return true;
		
		return false;
	}
	
	/**
	 * Verifica se já existe um estado representado pelo símbolo especificado.
	 * 
	 * @param state
	 * @return
	 */
	public boolean isPartOfStates(String state) {
		
		for (int i = 0; i < this.states.length; i++)
			if (this.states[i].getString().equals(state))
				return true;
		
		return false;
	}
	
	/**
	 * Verifica se já existe um estado representado pelo símbolo especificado.
	 * 
	 * @param state
	 * @return
	 */
	public boolean isPartOfFinalStates(String state) {
		
		for (int i = 0; i < this.finalStates.length; i++)
			if (this.finalStates[i].getString().equals(state))
				return true;
		
		return false;
	}
	
	/**
	 * Verifica se já existe uma direção representada pelo simbolo especificado.
	 * 
	 * @param direction
	 * @return
	 */
	public boolean isPartOfDirections(String direction) {
		
		for (int i = 0; i < this.directions.length; i++)
			if (this.directions[i].getString().equals(direction))
				return true;
		
		return false;
	}
	
	/**
	 * Verifica se algum simbolo em alguma regra de transição não faz parte dos símbolos conhecidos.
	 * Todos os simbolos das regras de transição devem fazer parte dos
	 * conjuntos já conhecidos (alfabeto, alfabeto da fita, estados).
	 * 
	 * @param transitionRules
	 * @return
	 * @throws InvalidMachineException
	 */
	private boolean hasUnknownSymbols(String[] transitionRulesArray) {

		String[] aux;
		String[] elementsArray = new String[transitionRulesArray.length * 5];
		
		// preenche o array de elementos com cada elemento de cada regra de transiÃ§Ã£o
		for (int i = 0, k = 0; i < transitionRulesArray.length; i++) {
			
			aux = transitionRulesArray[i].split(",");
			
			for (int j = 0; j < 5; j++)
				elementsArray[k++] = aux[j];
		}
		
		// verifica se cada um dos elementos fazem parte dos seus respectivos conjuntos.
		for (int i = 0; i < elementsArray.length; i += 5)
			if (!isPartOfTapeAlphabet(elementsArray[i + 0]) || !isPartOfStates(elementsArray[i + 1]) || 
				!isPartOfTapeAlphabet(elementsArray[i + 2]) || !isPartOfStates(elementsArray[i + 3]) ||	
				!isPartOfDirections(elementsArray[i + 4]))
					return true;
		
		return false;
	}
	
	/**
	 * Procura por um elemento do alfabeto que tenha o símbolo especificado.
	 *  
	 * @param str
	 * @return
	 */
	public Element searchAlphabetSymbol(String str) {

		for (int i = 0; i < this.alphabet.length; i++)
			if (this.alphabet[i].getString().equals(str))
				return this.alphabet[i];
		
		return null;
	}
	
	/**
	 * Procura por um elemento do alfabeto da fita que tenha o símbolo especificado.
	 * 
	 * @param str
	 * @return
	 */
	public Element searchTapeAlphabetSymbol(String str) {
		
		for (int i = 0; i < this.tapeAlphabet.length; i++)
			if (this.tapeAlphabet[i].getString().equals(str))
				return this.tapeAlphabet[i];
		
		return null;
	}
	
	/**
	 * Procura por um elemento do alfabeto e do alfabeto da fita que tenha o símbolo especificado.
	 * 
	 * @param str
	 * @return
	 */
	public Element searchFullTapeAlphabetSymbol(String str) {
		
		for (int i = 0; i < this.alphabet.length; i++)
			if (this.alphabet[i].getString().equals(str))
				return this.alphabet[i];
		
		for (int i = 0; i < this.tapeAlphabet.length; i++)
			if (this.tapeAlphabet[i].getString().equals(str))
				return this.tapeAlphabet[i];
		
		return null;
	}

	/**
	 * Procura por um estado que tenha o símbolo especificado.
	 * 
	 * @param state
	 * @return
	 */
	public Element searchState(String state) {
		
		for (int i = 0; i < this.states.length; i++)
			if (this.states[i].getString().equals(state))
				return this.states[i];
		
		return null;
	}
	
	/** 
	 * Procura por uma direção.
	 * 
	 * @param direction
	 * @return
	 */
	public Element searchDirection(String direction) {
		
		for (int i = 0; i < this.directions.length; i++)
			if (this.directions[i].getString().equals(direction))
				return this.directions[i];
		
		return null;
	}
	
	/**
	 * Procura por um símbolo qualquer.
	 * 
	 * @param string
	 * @return
	 */
	public Element searchAnySymbol(String string) {
		
		Element element;
		
		element = searchFullTapeAlphabetSymbol(string);
		if (element != null)
			return element;
		
		element = searchState(string);
		if (element != null)
			return element;
		
		element = searchDirection(string);
		if (element != null)
			return element;
		
		return null;
	}
	
	/*
	 * 
	 * GETTERS
	 * 
	 */
	
	public Element[] getAlphabet() {
		return alphabet;
	}

	public Element[] getTapeAlphabet() {
		return tapeAlphabet;
	}

	public Element[] getFullTapeAlphabet() {
		return fullTapeAlphabet;
	}
	
	public Element getBlank() {
		return blank;
	}

	public Element getDelimiter() {
		return delimiter;
	}

	public Element[] getStates() {
		return states;
	}

	public Element getInitialState() {
		return initialState;
	}

	public Element[] getFinalStates() {
		return finalStates;
	}
	
	public Element[] getDirections() {
		return directions;
	}
	
	public String getTransitionRules() {
		return transitionRules;
	}
	
	public String[] getTransitionRulesArray() {
		return transitionRulesArray;
	}
	
	public int getCounter() {
		return this.counter;
	}
}
