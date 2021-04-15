package application;

/**
 * 
 * @author Dacian
 * <p> QuestionVariant - auxiliary Class for shuffle
 */

public class QuestionVariant {
	private String text;
	private boolean validity;
	public QuestionVariant(String text, boolean validity) {
		super();
		this.text = text;
		this.validity = validity;
	}
	@Override
	public String toString() {
		return "QuestionVariant [text=" + text + ", validity=" + validity + "]";
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isValidity() {
		return validity;
	}
	public void setValidity(boolean validity) {
		this.validity = validity;
	}
	
	
}
