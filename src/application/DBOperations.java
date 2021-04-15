package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
/**
 * 
 * @author Dacian
 * <p> Class for operations with Database
 */
public class DBOperations {
	private static String dbUser = "user";
	private static String dbPasswd = "passwd";

	
	// Create tables when app starts for the first time
	public static void createMainTables() {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String USERSTable = new String("CREATE TABLE IF NOT EXISTS "
				+ "USERS ( EMAIL VARCHAR(70), USERNAME VARCHAR(30), PASSWD VARCHAR(30), "
				+ "ACCOUNTTYPE VARCHAR(5), TESTSCOUNT INT, REGISTRATIONDATE DATE, TOTALSCORE DOUBLE(2), PRIMARY KEY(EMAIL));");
		String QUESTIONSTable = new String("CREATE TABLE IF NOT EXISTS "
				+ "QUESTIONS( ID VARCHAR(7), QUESTION VARCHAR(150), S1 VARCHAR(100), S1V BOOLEAN, "
				+ "S2 VARCHAR(100), S2V BOOLEAN, S3 VARCHAR(100), S3V BOOLEAN, S4 VARCHAR(100), "
				+ "S4V BOOLEAN, S5 VARCHAR(100), S5V BOOLEAN, PRIMARY KEY (ID))");
		String GROUPS = new String("CREATE TABLE IF NOT EXISTS GROUPS (NAME VARCHAR(30))");
		String FILTERED_QUESTIONS = new String("CREATE TABLE IF NOT EXISTS  FILTERED_QUESTIONS(DOMAIN VARCHAR(30), "
				+ "ID VARCHAR(7), QUESTION VARCHAR(150), S1 VARCHAR(100), S1V BOOLEAN,  "
				+ "S2 VARCHAR(100), S2V BOOLEAN, S3 VARCHAR(100), S3V BOOLEAN, S4 VARCHAR(100), S4V BOOLEAN, "
				+ "S5 VARCHAR(100), S5V BOOLEAN)");
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			Statement st = conn.createStatement();
			st.executeUpdate(USERSTable);
			st.executeUpdate(QUESTIONSTable);
			st.executeUpdate(GROUPS);
			st.executeUpdate(FILTERED_QUESTIONS);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// Check if exists connection to DB
	public static boolean checkConnection() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			MainMenu.noConnectionLabel.setVisible(false);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			MainMenu.noConnectionLabel.setVisible(true);
			MainMenu.noConnectionLabel.setText("Nu exista conexiune spre baza de date");
			return false;
		}
	}
	
	// Check username - every username is unique
	public static boolean checkUserNameKey(String username) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st = conn.prepareStatement("SELECT USERNAME FROM USERS WHERE USERNAME = ?");
			st.setString(1, username);
			ResultSet rez = st.executeQuery();
			if(rez.toString().contains("rows: 0"))
				return true;
			return false;
			
		} catch (SQLException e) {
			System.out.println("Nu s-a adaugat contul");
			e.printStackTrace();
			return false;
		}
	}
	
	// Check email - every email address is unique (primary key for USERS table)
	public static boolean checkEmailKey(String email) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st = conn.prepareStatement("SELECT EMAIL FROM USERS WHERE EMAIL = ?");
			st.setString(1, email);
			ResultSet rez = st.executeQuery();
			if(rez.toString().contains("rows: 0"))
				return true;
			return false;
			
		} catch (SQLException e) {
			System.out.println("Nu s-a adaugat contul");
			e.printStackTrace();
			return false;
		}
	}
	
	
	 //Add new account in Database, in table USERS 
	public static void newAccount(String email, String userName, String password) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st = conn.prepareStatement("INSERT INTO USERS VALUES (?,?,?,?,?,?,?)");
			st.setString(1, email);
			st.setString(2,  userName);
			st.setString(3, password);
			st.setString(4, "USER");
			st.setInt(5,0);
			
			// Get current date
			Statement getDate = conn.createStatement();
			ResultSet rez = getDate.executeQuery("SELECT CURRENT_DATE");
			rez.next();
			
			st.setObject(6, rez.getDate(1));
			st.setDouble(7, 0.0);
			st.executeUpdate();
			System.out.println("Cont adaugat!");
			
		} catch (SQLException e) {
			System.out.println("Nu s-a adaugat contul");
			e.printStackTrace();
		}
	}
	
	// Check values from fields for login
	public static boolean checkLogin(String email, String password) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st = conn.prepareStatement("SELECT EMAIL, PASSWD FROM USERS WHERE EMAIL = ?");
			st.setString(1, email);
			ResultSet rez = st.executeQuery();
			if(rez.toString().contains("rows: 0")) {
				return false;
			}
			
			
			rez.next(); 
			String getEmail = rez.getString(1);
			String getPassword = rez.getString(2);
			System.out.println(getEmail + " " + getPassword);
			if(getEmail.equals(email) && getPassword.equals(password)) {
				System.out.println("Logare cu succes!");
				return true;
			}
			else
				return false;
		} catch (SQLException e) {
			System.out.println("Logare esuata");
			e.printStackTrace();
		}
		return false;
	}
	
	// Toate informatiile despre un user, pentru a fi trimise spre aplicatie dupa logare
	// Get user info from DB - they will be send to app
	public static User getUserInfo(String email) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st = conn.prepareStatement("SELECT * FROM USERS WHERE EMAIL= ?");
			st.setString(1, email);
			ResultSet rez = st.executeQuery();
			rez.next();
			Date registrationDate = rez.getDate(6);
			return (new User(rez.getString(1),rez.getString(2),rez.getString(3),rez.getString(4),rez.getInt(5),registrationDate,rez.getDouble(7)));
			
		} catch (SQLException e) {
			System.out.println("Nu pot obtine datele despre utilzator");
			e.printStackTrace();
		}
		return (new User("","","","",0,new Date(),0));
	}
	
	// Change password - modify in table USERS
	public static void ChangePassword(String newPasswd, String userEmail) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st = conn.prepareStatement("UPDATE USERS SET PASSWD = ? WHERE EMAIL = ?");
			st.setString(1, newPasswd);
			st.setString(2, userEmail);
			st.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nu s-a realizat schimbarea parolei");
			e.printStackTrace();
		}
	}
	
	// Add new question to Database
	public static void createQuestion(Question v) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st = conn.prepareStatement("INSERT INTO QUESTIONS VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
			
			// Set ID for this new question
			v.setID(DBOperations.validateQuestionID());
			
			st.setString(1, v.getID());
			st.setString(2, v.getQuestionBody());
			st.setString(3, v.getS1());
			st.setBoolean(4, v.isS1V());
			st.setString(5, v.getS2());
			st.setBoolean(6, v.isS2V());
			st.setString(7, v.getS3());
			st.setBoolean(8, v.isS3V());
			st.setString(9, v.getS4());
			st.setBoolean(10, v.isS4V());
			st.setString(11, v.getS5());
			st.setBoolean(12, v.isS5V());
			
			
			st.executeUpdate();
			
			System.out.println(v);
		} catch (SQLException e) {
			System.out.println("Intrebarea nu a fost adaugata");
			e.printStackTrace();
		}
	}
	
	// Create an unique ID for every question
	public static String validateQuestionID() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			
			PreparedStatement st = conn.prepareStatement("SELECT ID FROM QUESTIONS WHERE ID = ?");
			
			String id = " ";
			boolean unique = false;
			
			while(unique == false) {
				id = GenerateQuestionID.generate();
				st.setString(1, id);
				ResultSet rez = st.executeQuery();
				System.out.println(rez.toString());
				if(rez.toString().contains("columns: 1 rows: 0 pos: -1")) {
					unique = true;
				}
				
			}
			return id;
			
		} catch (SQLException e) {
			System.out.println("Problema la conectarea la baza de date");
			e.printStackTrace();
			return (" ");
		}
	}
	
	
	// Get questions from DB - questions from all domains
	public static Question[] getQuestions(Test f) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
						
			Question A[] = new Question[f.getNumberOfQuestions()];
			
			
				// Questions from all domains
				PreparedStatement st = conn.prepareStatement("SELECT * FROM questions ORDER BY RAND() LIMIT ?");
				st.setInt(1, f.getNumberOfQuestions());
				ResultSet rez = st.executeQuery();
				
				int i = 0;
				while(rez.next()) {
					A[i] = new Question(rez.getString(1),rez.getString(2),rez.getString(3),rez.getBoolean(4),rez.getString(5),
							rez.getBoolean(6),rez.getString(7),rez.getBoolean(8),rez.getString(9),rez.getBoolean(10),rez.getString(11),rez.getBoolean(12));
					i++;
				}
				
				
				for(int j = 0; j<A.length;j++)
					A[j].shuffleVariants();
			
				
			return A;
			
		} catch (SQLException e) {
			System.out.println("Nu pot obtine intrebari");
			e.printStackTrace();
			return new Question[1];
		}
		
	}
	
	// Get number of questions from DB
	public static boolean getNumberOfQuestions(int number) {
		
		// calculeaza cate intrebari am in baza de date, daca nu am testule pentru test nu ma lasa sa rulez testul
		// se foloseste pentru teste cu intrebari allDomains
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st = conn.prepareStatement("SELECT COUNT(*) FROM QUESTIONS");
			ResultSet rez = st.executeQuery();
			rez.next();
			if(rez.getInt(1) >= number)
				return true;
			
			return false;
		} catch (SQLException e) {
			System.out.println("problema");
			e.printStackTrace();
			return false;
		}
		
	}
	
	// Get questions from DB - questions from several domains
	public static Question[] getQuestionsFromGroups(List<String> domains, int numberOfQuestions) {
		
		// selecteaza intrebari grupate pe grupuri
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
						
			Question A[] = new Question[numberOfQuestions];
			
			List<Question> auxList = new LinkedList<>();
			
			// intai foloseste o lista pentru a le lua pe toate
			
			for(String i: domains) {
				PreparedStatement st = conn.prepareStatement("SELECT * FROM FILTERED_QUESTIONS WHERE DOMAIN = ?");
				st.setString(1, i);
				ResultSet rez = st.executeQuery();
				while(rez.next()) {
					auxList.add(new Question(rez.getString(2),rez.getString(3),rez.getString(4),rez.getBoolean(5),
							rez.getString(6),rez.getBoolean(7),rez.getString(8),rez.getBoolean(9),rez.getString(10),
							rez.getBoolean(11),rez.getString(12),rez.getBoolean(13)));
				}
			}
			// daca nu sunt destule intrebari in lista returneaza null - verifica cu set ca sa nu fie duplicate
			Set<Question>uniqueQuestions = new HashSet<Question>(auxList);
			if(uniqueQuestions.size() < numberOfQuestions) {
				System.out.println("Nu am destule intrebari");
				return null;
			}
			else {
				// daca sunt destul intrebari atunci trimite primele numberOfQuestions
				auxList.clear();
				for(Question i: uniqueQuestions)
					auxList.add(i);
				
				Collections.shuffle(auxList);
				for(int i = 0;i<numberOfQuestions;i++)
					A[i] = auxList.get(i);
			}
			
			for(int j = 0;j<A.length;j++)
				A[j].shuffleVariants();
			
			return A;
			
		} catch (SQLException e) {
			System.out.println("problema");
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	// Increment test number - when an user finish a test
	public static void IncrementTestsNumber(User currentUser) {
		// Incrementeaza numarul de teste al utilizatorului
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st  = conn.prepareStatement("SELECT TESTSCOUNT FROM USERS WHERE EMAIL = ?");
			
			st.setString(1,currentUser.getEmail());
			ResultSet rez = st.executeQuery();
			rez.next();
			
			int n = rez.getInt(1);
			n++;
			st  = conn.prepareStatement("UPDATE USERS SET TESTSCOUNT = ? WHERE EMAIL = ?");
			st.setInt(1, n);
			st.setString(2, currentUser.getEmail());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("nu am incrementat");
			e.printStackTrace();
		}
	}
	
	
	// Update user score - when an user finish a test
	public static void updateTotalScore(User currentUser, double Grade) {
		// Actualizeaza punctajul total obtinut (suma punctajelor obtinute la toate testele)
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st  = conn.prepareStatement("SELECT TOTALSCORE FROM USERS WHERE EMAIL = ?");
			
			st.setString(1,currentUser.getEmail());
			ResultSet rez = st.executeQuery();
			rez.next();
			
			double totalScore = rez.getDouble(1);
			totalScore = totalScore + Grade;
			st  = conn.prepareStatement("UPDATE USERS SET TOTALSCORE = ? WHERE EMAIL = ?");
			st.setDouble(1, totalScore);
			st.setString(2, currentUser.getEmail());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nu am actualizat");
			e.printStackTrace();
		}
	}
	
	// Check group name - every group name is unique
	public static boolean checkGroupName(String val) {
		
		// Returneaza true daca numele grupului nu exista
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st  = conn.prepareStatement("SELECT NAME FROM GROUPS WHERE NAME = ?");
			st.setString(1, val);
			ResultSet rez = st.executeQuery();
			System.out.println(rez.toString());
			
			// daca exista returneaza ceva cu row 1, daca nu exista se returneaza ceva cu row 0
			
			if(rez.toString().contains("rows: 0 pos: -1")) {
				return true;
			}
			return false;
			
		} catch (SQLException e) {
			System.out.println("");
			e.printStackTrace();
			return false;
		}
	}
	
	// Create new group
	public static void createNewGroup(String val) {
		
		// Create new group - add in GROUPS table
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st ;
			//st.setString(1, val);
			st = conn.prepareStatement("INSERT INTO GROUPS VALUES(?)");
			st.setString(1, val);
			st.executeUpdate();
			System.out.println("Am adaugat grupul!");
			
			
			
			
		} catch (SQLException e) {
			System.out.println("adaugare esuata");
			e.printStackTrace();
		}
	}
	
	// Check group numbers
	public static boolean checkGroupsNumber() {
		// maxim 12 grupuri simulan, daca nu trece de 11 va returna true si inca poti adauga
		// daca este 12 atunci nu mai poti adauga
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st  = conn.prepareStatement("SELECT COUNT(*) FROM GROUPS");
			
			ResultSet rez = st.executeQuery();
			rez.next();
			int n = rez.getInt(1);
			if(n <= 11)
				return true;
			return false;
			
		} catch (SQLException e) {
			System.out.println("");
			e.printStackTrace();
			return false;
		}
	}
	
	// Get Groups names as String
	public static List<String> getGroupsAsString(){
		List<String> A = new LinkedList<>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st  = conn.prepareStatement("SELECT NAME FROM GROUPS");
			ResultSet rez = st.executeQuery();
			while(rez.next())
				A.add(rez.getString(1));
			return A;
			
		} catch (SQLException e) {
			System.out.println("");
			e.printStackTrace();
			return null;
		}
		
	}
	
	// Get groups names as ObservableList - for Combobox
	public static ObservableList<String> getGroups(){
		
		// Returneaza toate numele de grupuri / domenii 
		
		ObservableList<String> A = FXCollections.observableArrayList();
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st  = conn.prepareStatement("SELECT NAME FROM GROUPS");
			ResultSet rez = st.executeQuery();
			while(rez.next())
				A.add(rez.getString(1));
			return A;
			
		} catch (SQLException e) {
			System.out.println("");
			e.printStackTrace();
			return null;
		}
		
	}
	
	// Get all questions - for Combobox
	public static ObservableList<String> getAllQuestions(){
		
		// returneaza toate intrebarile - din tabelul Questions pentru ComboBox
		
		ObservableList<String> A = FXCollections.observableArrayList();
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st  = conn.prepareStatement("SELECT ID,QUESTION FROM QUESTIONS");
			ResultSet rez = st.executeQuery();
			while(rez.next()) {
				A.add(rez.getString(1) + " | " + rez.getString(2));
			}
			return A;
		} catch (SQLException e) {
			System.out.println("");
			e.printStackTrace();
			return null;
		}
	}
	
	// Delete a group
	public static void deleteGroup(String val) {
		
		// delete a group
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st  = conn.prepareStatement("DELETE FROM GROUPS WHERE NAME = ?");
			st.setString(1, val);
			st.executeUpdate();
			
			
			// delete all questions with this domain
			st = conn.prepareStatement("DELETE FROM FILTERED_QUESTIONS WHERE DOMAIN = ?");
			st.setString(1, val);
			st.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("");
			e.printStackTrace();
			
		}
	}
	
	
	// Add question to group
	public static void addQuestionToGroup(String group, String question) {
		// adauga doar daca nu exista deja aceasta intrebare in acest grup
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			
			PreparedStatement st  = conn.prepareStatement("SELECT * FROM QUESTIONS WHERE ID = ? ");
			String id = question.substring(0, 7);
			st.setString(1, id);
			
			
			PreparedStatement aux = conn.prepareStatement("SELECT * FROM FILTERED_QUESTIONS WHERE DOMAIN = ? AND ID = ? ");
			aux.setString(1, group);
			aux.setString(2, id);
			ResultSet auxRez = aux.executeQuery();
			System.out.println("-------------------");
			System.out.println(auxRez.toString());
			System.out.println("-------------------");
			
			//System.out.println("In metoda de adaugare: " + "grup: " + group + " intrebare: "  + question);
			//System.out.println("Interogare auxiliara: " + auxRez.toString());
			if(auxRez.toString().contains("rows: 0")) {  // verifica daca exista intai
				ResultSet rez = st.executeQuery();
				rez.next();
				//System.out.println(rez.getString(1) + " " + rez.getString(2) + " " + rez.getString(3));
				
				PreparedStatement st2 = conn.prepareStatement("INSERT INTO FILTERED_QUESTIONS VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
				st2.setString(1, group);
				st2.setString(2, rez.getString(1));
				st2.setString(3, rez.getString(2));
				
				st2.setString(4, rez.getString(3));
				st2.setBoolean(5, rez.getBoolean(4));
				st2.setString(6, rez.getString(5));
				st2.setBoolean(7, rez.getBoolean(6));
				st2.setString(8, rez.getString(7));
				st2.setBoolean(9, rez.getBoolean(8));
				st2.setString(10, rez.getString(9));
				st2.setBoolean(11, rez.getBoolean(10));
				st2.setString(12, rez.getString(11));
				st2.setBoolean(13, rez.getBoolean(12));
				
				st2.executeUpdate();
				
				System.out.println("@@@@@@@@@@@@@@@@@ AM ADAUGAT INTREBAREA @@@@@@@@@@@@@@@");
				
				GroupQuestionMenu.addQuestionInfo.setTextFill(Color.GREEN);
				GroupQuestionMenu.addQuestionInfo.setText("Întrebarea a fost adaugatã în grup");
				
				GroupQuestionMenu.removeQuestionInfo.setText("");
				GroupQuestionMenu.deleteGroupSituation.setText("");
				GroupQuestionMenu.createNewGroupSituation.setText("");
			}
			
			else {
				// daca intrebarea nu a fost adaugata in grup
				GroupQuestionMenu.addQuestionInfo.setTextFill(Color.RED);
				GroupQuestionMenu.addQuestionInfo.setText("Întrebarea nu a fost adaugatã în grup");
				
				GroupQuestionMenu.removeQuestionInfo.setText("");
				GroupQuestionMenu.deleteGroupSituation.setText("");
				GroupQuestionMenu.createNewGroupSituation.setText("");
			}
			
		} catch (SQLException e) {
			//System.out.println("");
			e.printStackTrace();
			
		}
	}
	
	// Get all questions from a group - for Combobox
	public static ObservableList<String> getQuestionsFromGroup(String group) {
		// returneaza toate intrebarile dintr-un grup / domeniu
		ObservableList<String> A = FXCollections.observableArrayList();
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st  = conn.prepareStatement("SELECT ID,QUESTION FROM FILTERED_QUESTIONS WHERE DOMAIN = ?");
			st.setString(1, group);
			ResultSet rez = st.executeQuery();
			while(rez.next())
				A.add(rez.getString(1) + " | " + rez.getString(2));
			
			
			return A;
		} catch (SQLException e) {
			System.out.println("");
			e.printStackTrace();
			return null;
		}
	}
	
	// Remove a question from group
	public static void removeQuestionFromGroup(String group, String question) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st  = conn.prepareStatement("DELETE FROM FILTERED_QUESTIONS WHERE DOMAIN = ? AND ID = ?");
			String id = question.substring(0,7);
			st.setString(1, group);
			st.setString(2, id);
			st.executeUpdate();
			
			GroupQuestionMenu.removeQuestionInfo.setTextFill(Color.GREEN);
			GroupQuestionMenu.removeQuestionInfo.setText("Întrebarea a fost eliminatã din grup");
			
			GroupQuestionMenu.addQuestionInfo.setText("");
			GroupQuestionMenu.deleteGroupSituation.setText("");
			GroupQuestionMenu.createNewGroupSituation.setText("");
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		
		}
	}
	
	// Get question information - test from fields and validies
	public static void getQuestionInfo(String val) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			String id = val.substring(0,7);
			System.out.println("question id: " + id);
			PreparedStatement st  = conn.prepareStatement("SELECT * FROM QUESTIONS WHERE ID = ?");
			st.setString(1, id);
			ResultSet rez = st.executeQuery();
			rez.next();
			EditQuestionMenu.questionBody.setText(rez.getString(2));
			EditQuestionMenu.S1Field.setText(rez.getString(3));
			if(rez.getBoolean(4) == true)
				EditQuestionMenu.S1V.setText("A");
			else
				EditQuestionMenu.S1V.setText("F");
			
			EditQuestionMenu.S2Field.setText(rez.getString(5));
			if(rez.getBoolean(6) == true)
				EditQuestionMenu.S2V.setText("A");
			else
				EditQuestionMenu.S2V.setText("F");
			
			EditQuestionMenu.S3Field.setText(rez.getString(7));
			if(rez.getBoolean(8) == true)
				EditQuestionMenu.S3V.setText("A");
			else
				EditQuestionMenu.S3V.setText("F");
			
			EditQuestionMenu.S4Field.setText(rez.getString(9));
			if(rez.getBoolean(10) == true)
				EditQuestionMenu.S4V.setText("A");
			else
				EditQuestionMenu.S4V.setText("F");
			
			EditQuestionMenu.S5Field.setText(rez.getString(11));
			if(rez.getBoolean(12) == true)
				EditQuestionMenu.S5V.setText("A");
			else
				EditQuestionMenu.S5V.setText("F");
				
		} catch (SQLException e) {
			System.out.println("");
			//e.printStackTrace();
			
		}
	}
	
	
	// Delete a question - from QUESTIONS table and from all groups which contain this question
	public static void deleteQuestion(String val) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st  = conn.prepareStatement("DELETE FROM FILTERED_QUESTIONS WHERE ID = ?");
			PreparedStatement st2 = conn.prepareStatement("DELETE FROM QUESTIONS WHERE ID = ?");
			
			String id = val.substring(0,7);
			st.setString(1, id);
			st2.setString(1, id);
			
			st.executeUpdate();
			st2.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// Edit a question
	public static void editQuestion(String val) {
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test",dbUser,dbPasswd);
			PreparedStatement st  = conn.prepareStatement("UPDATE QUESTIONS SET QUESTION = ?, S1 = ?,"
					+ "S1V = ?, S2 = ?, S2V = ?, S3 = ?, S3V = ?, S4 = ?, S4V = ?, S5 = ?, S5V = ? WHERE ID = ?");
			
			PreparedStatement st2  = conn.prepareStatement("UPDATE FILTERED_QUESTIONS SET QUESTION = ?, S1 = ?,"
					+ "S1V = ?, S2 = ?, S2V = ?, S3 = ?, S3V = ?, S4 = ?, S4V = ?, S5 = ?, S5V = ? WHERE ID = ?");
			
			String id = val.substring(0,7);
			
			// Set pentru QUESTIONS
			st.setString(1, EditQuestionMenu.questionBody.getText());
			st.setString(2, EditQuestionMenu.S1Field.getText());
			//s1
			if(EditQuestionMenu.S1V.getText().equals("A")) {
				st.setBoolean(3, true);
			}
			else {
				st.setBoolean(3, false);
			}
			//s2
			st.setString(4, EditQuestionMenu.S2Field.getText());
			if(EditQuestionMenu.S2V.getText().equals("A")) {
				st.setBoolean(5, true);
			}
			else {
				st.setBoolean(5, false);
			}
			//s3
			st.setString(6, EditQuestionMenu.S3Field.getText());
			if(EditQuestionMenu.S3V.getText().equals("A")) {
				st.setBoolean(7, true);
			}
			else {
				st.setBoolean(7, false);
			}
			
			//s4
			st.setString(8, EditQuestionMenu.S4Field.getText());
			if(EditQuestionMenu.S4V.getText().equals("A")) {
				st.setBoolean(9, true);
			}
			else {
				st.setBoolean(9, false);
			}
			//s5
			st.setString(10, EditQuestionMenu.S5Field.getText());
			if(EditQuestionMenu.S5V.getText().equals("A")) {
				st.setBoolean(11, true);
			}
			else {
				st.setBoolean(11, false);
			}
			st.setString(12, id);
			
			
			// set pentru FILTERED_QUESTIONS
			st2.setString(1, EditQuestionMenu.questionBody.getText());
			st2.setString(2, EditQuestionMenu.S1Field.getText());
			//s1
			if(EditQuestionMenu.S1V.getText().equals("A")) {
				st2.setBoolean(3, true);
			}
			else {
				st2.setBoolean(3, false);
			}
			//s2
			st2.setString(4, EditQuestionMenu.S2Field.getText());
			if(EditQuestionMenu.S2V.getText().equals("A")) {
				st2.setBoolean(5, true);
			}
			else {
				st2.setBoolean(5, false);
			}
			//s3
			st2.setString(6, EditQuestionMenu.S3Field.getText());
			if(EditQuestionMenu.S3V.getText().equals("A")) {
				st2.setBoolean(7, true);
			}
			else {
				st2.setBoolean(7, false);
			}
			
			//s4
			st2.setString(8, EditQuestionMenu.S4Field.getText());
			if(EditQuestionMenu.S4V.getText().equals("A")) {
				st2.setBoolean(9, true);
			}
			else {
				st2.setBoolean(9, false);
			}
			//s5
			st2.setString(10, EditQuestionMenu.S5Field.getText());
			if(EditQuestionMenu.S5V.getText().equals("A")) {
				st2.setBoolean(11, true);
			}
			else {
				st2.setBoolean(11, false);
			}
			st2.setString(12, id);
			
			st.executeUpdate();
			st2.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
	

