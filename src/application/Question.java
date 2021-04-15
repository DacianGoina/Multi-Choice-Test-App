package application;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/**
 * 
 * @author Dacian
 * <p> Question Class
 * <p> A question contain: questionID, questionBody and 5 pairs statement - validity
 */
public class Question implements Comparable<Question>{
	private String ID;
	private String questionBody;
	private String S1;
	private boolean S1V;
	private String S2;
	private boolean S2V;
	private String S3;
	private boolean S3V;
	private String S4;
	private boolean S4V;
	private String S5;
	private boolean S5V;
	public Question(String iD, String questionBody, String s1, boolean s1v, String s2, boolean s2v, String s3,
			boolean s3v, String s4, boolean s4v, String s5, boolean s5v) {
		super();
		this.ID = iD;
		this.questionBody = questionBody;
		this.S1 = s1;
		this.S1V = s1v;
		this.S2 = s2;
		this.S2V = s2v;
		this.S3 = s3;
		this.S3V = s3v;
		this.S4 = s4;
		this.S4V = s4v;
		this.S5 = s5;
		this.S5V = s5v;
	}
	@Override
	public String toString() {
		return "Question [ID=" + ID + ", questionBody=" + questionBody + ", S1=" + S1 + ", S1V=" + S1V + ", S2=" + S2
				+ ", S2V=" + S2V + ", S3=" + S3 + ", S3V=" + S3V + ", S4=" + S4 + ", S4V=" + S4V + ", S5=" + S5
				+ ", S5V=" + S5V + "]";
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getQuestionBody() {
		return questionBody;
	}
	public void setQuestionBody(String questionBody) {
		this.questionBody = questionBody;
	}
	public String getS1() {
		return S1;
	}
	public void setS1(String s1) {
		S1 = s1;
	}
	public boolean isS1V() {
		return S1V;
	}
	public void setS1V(boolean s1v) {
		S1V = s1v;
	}
	public String getS2() {
		return S2;
	}
	public void setS2(String s2) {
		S2 = s2;
	}
	public boolean isS2V() {
		return S2V;
	}
	public void setS2V(boolean s2v) {
		S2V = s2v;
	}
	public String getS3() {
		return S3;
	}
	public void setS3(String s3) {
		S3 = s3;
	}
	public boolean isS3V() {
		return S3V;
	}
	public void setS3V(boolean s3v) {
		S3V = s3v;
	}
	public String getS4() {
		return S4;
	}
	public void setS4(String s4) {
		S4 = s4;
	}
	public boolean isS4V() {
		return S4V;
	}
	public void setS4V(boolean s4v) {
		S4V = s4v;
	}
	public String getS5() {
		return S5;
	}
	public void setS5(String s5) {
		S5 = s5;
	}
	public boolean isS5V() {
		return S5V;
	}
	public void setS5V(boolean s5v) {
		S5V = s5v;
	}
	
	// Shuffle question's variants
	public void shuffleVariants() {
		List<QuestionVariant> L = new LinkedList<>();
		
		L.add(new QuestionVariant(this.S1,this.S1V));
		L.add(new QuestionVariant(this.S2,this.S2V));
		L.add(new QuestionVariant(this.S3,this.S3V));
		L.add(new QuestionVariant(this.S4,this.S4V));
		L.add(new QuestionVariant(this.S5,this.S5V));
		
		Collections.shuffle(L);
		
		
		this.setS1(L.get(0).getText());
		this.setS1V(L.get(0).isValidity());
		
		this.setS2(L.get(1).getText());
		this.setS2V(L.get(1).isValidity());
		
		this.setS3(L.get(2).getText());
		this.setS3V(L.get(2).isValidity());
		
		this.setS4(L.get(3).getText());
		this.setS4V(L.get(3).isValidity());
		
		this.setS5(L.get(4).getText());
		this.setS5V(L.get(4).isValidity());
		
	}
	
	@Override
	public int compareTo(Question o) {
		if( this.getID().equals(o.getID()) && this.getQuestionBody().equals(o.getQuestionBody()) && this.getS1().equals(o.getS1()) 
			&& this.getS2().equals(o.getS2()) && this.getS3().equals(o.getS3()) && this.getS4().equals(o.getS4()) && 
			this.getS5().equals(o.getS5()) && this.isS1V() == o.isS1V() && this.isS2V() == o.isS2V() && this.isS3V() == o.isS3V()
			&& this.isS4V() == o.isS4V() && this.isS5V() == o.isS5V())
			return 0;
		
		return 1;
	}
	
}
