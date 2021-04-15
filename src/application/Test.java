package application;


/**
 * 
 * @author Dacian
 * <p> Test class - an objects contain information about a test: timeLimit, numberOfQuestions, evaluationType
 */
public class Test {
	private int timeLimit; // va fi 0 daca nu s-a setat o limita de timp
	private int numberOfQuestions;
	private String evaluationType;
	
	public Test(int timeLimit, int numberOfQuestions, String evaluationType) {
		super();
		this.timeLimit = timeLimit;
		this.numberOfQuestions = numberOfQuestions;
		this.evaluationType = evaluationType;
	}
	@Override
	public String toString() {
		return "Test [timeLimit=" + timeLimit + ", numberOfQuestions=" + numberOfQuestions + ", evaluationType="
				+ evaluationType + "]";
	}
	
	public int getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}
	public void setNumberOfQuestions(int numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}
	public String getEvaluationType() {
		return evaluationType;
	}
	public void setEvaluationType(String evaluationType) {
		this.evaluationType = evaluationType;
	}
	
	
}
