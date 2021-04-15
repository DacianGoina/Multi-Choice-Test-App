package application;

import java.util.Random;
/**
 * 
 * @author Dacian
 * <p> Generate ID for question. It will contain 7 characters:  ABBBBBA,  A: ALPHA   B: ALPHANUM. 
 *  'A' characters aren't equals (aren't same characters), same rule for 'B' characters
 *  <p> 40875134976 posibilities  (over 40 billions)
 */
public class GenerateQuestionID {
	private static final String ALPHA[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S",
			"T","U","V","W","X","Y","Z"}; 
	private static final String ALPHANUM[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S",
			"T","U","V","W","X","Y","Z","0","1","2","3","4","5","6","7","8","9"};
	
	private static final int ALPHAlen = GenerateQuestionID.ALPHA.length;
	private static final int ALPHANUMlen = ALPHANUM.length;
	
	public static String generate() {
		// FORMAT tip: 1222221 1: ALPHA 2: ALPHANUM
		Random r = new Random();
		
		String ID = ALPHA[r.nextInt(ALPHAlen)] + ALPHANUM[r.nextInt(ALPHANUMlen)] + ALPHANUM[r.nextInt(ALPHANUMlen)]
				+ ALPHANUM[r.nextInt(ALPHANUMlen)] + ALPHANUM[r.nextInt(ALPHANUMlen)] + ALPHANUM[r.nextInt(ALPHANUMlen)]
				+ ALPHA[r.nextInt(ALPHAlen)];
		return ID;
	}
}
