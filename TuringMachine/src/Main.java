
public class Main {
	
	public static void main(String[] args) {
		
		Machine tm = null;
		
//		String alphabet = new String("a b");
//		String tapeAlphabet = new String("x y B D");
//		String blank = new String("B");
//		String delimiter = new String("D");
//		String states = new String("0 1 2 3 4");
//		String initialState = new String("0");
//		String finalStates = new String("4");
//		String left = new String("L");
//		String right = new String("R");
//		String halt = new String("H");
//		String transitionRules = new String("y,0,y,3,R a,0,x,1,R y,1,y,1,R a,1,a,1,R b,1,y,2,L y,2,y,2,L a,2,a,2,L x,2,x,0,R y,3,y,3,R B,3,B,4,H");
//		String input = new String("a a b b");
		
		String alphabet = new String("a b c");
		String tapeAlphabet = new String("x y z B D");
		String blank = new String("B");
		String delimiter = new String("D");
		String states = new String("0 1 2 3 4 5 6");
		String initialState = new String("0");
		String finalStates = new String("6");
		String left = new String("L");
		String right = new String("R");
		String halt = new String("H");
		String transitionRules = new String("a,0,x,1,R y,0,y,4,L B,0,B,6,H y,1,y,1,R a,1,a,1,R b,1,y,2,R z,2,z,2,R b,2,b,2,R c,2,z,3,L a,3,a,3,L b,3,b,3,L y,3,y,3,L z,3,z,3,L x,3,x,0,R x,4,x,4,L B,4,B,5,R x,5,x,5,R y,5,y,5,R z,5,z,5,R B,5,x,6,H");
		String input = new String("a a b b c c");
		
		// 0,a,1,x,R 0,y,4,y,L 0,B,6,B,H 1,y,1,y,R 1,a,1,a,R 1,b,2,y,R 2,z,2,z,R 2,b,2,b,R 2,c,3,z,L 3,a,3,a,L 3,b,3,b,L 3,y,3,y,L 3,z,3,z,L 3,x,0,x,R 4,x,4,x,L 4,B,5,B,R 5,x,5,x,R 5,y,5,y,R 5,z,5,z,R 5,B,6,x,H
		try {
			
			tm = new Machine(alphabet, tapeAlphabet, blank, delimiter, states, initialState, finalStates, left, right, halt, transitionRules);
			
			System.out.println("Machine is " + (tm.isValid() ? "Valid!" : "Invalid!"));
			
			System.out.println("Input is " + (tm.process(input) ? "Valid!" : "Invalid!"));
			
			tm.printAllTapes();
		}
		catch (InvalidMachineException ime) {
			System.out.println(ime.getError());
		}
	}
	
	public void comments() {
		/*
		
		q0
		0,a,1,x,R
		0,y,4,y,L
		0,B,6,B,H
		
		q1
		1,y,1,y,R
		1,a,1,a,R
		1,b,2,y,R
		
		q2
		2,z,2,z,R
		2,b,2,b,R
		2,c,3,z,L
		
		q3
		3,a,3,a,L
		3,b,3,b,L
		3,y,3,y,L
		3,z,3,z,L
		3,x,0,x,R
		
		q4
		4,x,4,x,L
		4,B,5,B,R
		
		q5
		5,x,5,x,R
		5,y,5,y,R
		5,z,5,z,R
		5,B,6,x,H
		
		 */
		
		
		
		
		/*
		Alphabet: a b
		Tape Alphabet: x y BLANK DELIMITER
		States: 5
		Initial State: 0
		Final States: 4
		Directions: L R H
		Transition Rules: y,0,y,3,R a,0,x,1,R y,1,y,1,R a,1,a,1,R b,1,y,2,L y,2,y,2,L a,2,a,2,L x,2,x,0,R y,3,y,3,L BLANK,3,BLANK,4,H
		
		q0:
		y,0,y,3,R
		a,0,x,1,R
		
		q1:
		y,1,y,1,R
		a,1,a,1,R
		b,1,y,2,L
		
		q2:
		y,2,y,2,L
		a,2,a,2,L
		x,2,x,0,R
		
		q3:
		y,3,y,3,L
		BLANK,3,BLANK,4,H
		
		q4
		-
		======================================================================================================================
		
		Alphabet: a b
		Tape Alphabet: 0 1 BLANK DELIMITER
		States: 9
		Initial State: 0
		Directions: L R HALT
		Transition Rules: a,0,BLANK,1,R b,0,BLANK,7,R BLANK,0,1,6,HALT a,1,a,1,R b,1,b,1,R BLANK,1,BLANK,2,L a,2,BLANK,5,L b,2,BLANK,3,L BLANK,2,1,6,HALT a,3,BLANK,3,L b,3,BLANK,3,L BLANK,3,0,4,HALT a,5,a,5,L b,5,b,5,L BLANK,5,BLANK,0,R a,7,a,7,R b,7,b,7,R BLANK,7,BLANK,8,L b,8,BLANK,5,L BLANK,8,1,6,HALT a,8,BLANK,3,L
		
		q0
		a,0,BLANK,1,R 
		b,0,BLANK,7,R 
		
		q1
		a,1,a,1,R 
		b,1,b,1,R 
		BLANK,1,BLANK,2,L 
		
		q2
		a,2,BLANK,5,L 
		b,2,BLANK,3,L 
		BLANK,2,1,6,HALT 
		
		q3
		a,3,BLANK,3,L 
		b,3,BLANK,3,L 
		BLANK,3,0,4,HALT
		
		q4
		-
		
		q5
		a,5,a,5,L 
		b,5,b,5,L 
		BLANK,5,BLANK,0,R 
		
		q6
		-
		
		q7
		a,7,a,7,R 
		b,7,b,7,R 
		BLANK,7,BLANK,8,L 
		
		q8
		b,8,BLANK,5,L 
		BLANK,8,1,6,L
		a,8,BLANK,3,L
		
		legenda da regra de transição:
		∂(an,qm) = (ap, qr, ds)
	    an: Código do símbolo lido;
	    qm: Código do estado atual da máquina;
	    ap: Código do símbolo escrito;
	    qr: Código do novo estado;
	    ds: Código da direção (L, R, HALT);
		*/
	}
}