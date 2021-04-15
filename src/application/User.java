package application;


import java.util.Date;
/**
 * 
 * @author Dacian
 * <p> User Class
 */
public class User {
	private String email;
	private String userName;
	private String passwd;
	private String accountType;
	private int testsCount;
	private Date registrationDate;
	private double totalScore;
	public User(String email, String userName, String passwd, String accountType, int testsCount, Date registrationDate,
			double totalScore) {
		super();
		this.email = email;
		this.userName = userName;
		this.passwd = passwd;
		this.accountType = accountType;
		this.testsCount = testsCount;
		this.registrationDate = registrationDate;
		this.totalScore = totalScore;
	}
	@Override
	public String toString() {
		return "User [email=" + email + ", userName=" + userName + ", passwd=" + passwd + ", accountType=" + accountType
				+ ", testsCount=" + testsCount + ", registrationDate=" + registrationDate + ", totalScore=" + totalScore
				+ "]";
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public int getTestsCount() {
		return testsCount;
	}
	public void setTestsCount(int testsCount) {
		this.testsCount = testsCount;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	public double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}
	
	
}
