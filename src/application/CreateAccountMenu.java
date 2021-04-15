package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 * 
 * @author Dacian
 * <p> Class for CreateAccountMenu page
 */
public class CreateAccountMenu {
	
	// GUI Objects
	private Button backToMainMenu = new Button("Înapoi");
	private Button createAccount = new Button("Creare cont");
	private TextField userField = new TextField();
	private TextField emailField = new TextField();
	private PasswordField passField = new PasswordField();
	private PasswordField repeatPassField = new PasswordField();
	private Label userFieldLabel = new Label("Nume de utilizator:");
	private Label passFieldLabel = new Label("Parolă:");
	private Label repeatPassFieldLabel = new Label("Repetă parola:");
	private Label emailFieldLabel = new Label("Adresa de email:");
	private Label successfulOperation = new Label("");
	
	
	public Scene openCreateAccountMenu(Stage primaryStage, double windowWidth, double windowHeight) {
		StackPane root = new StackPane();
		Scene a = new Scene(root, windowWidth, windowHeight);
		
		
		// When Database is down
		a.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		    	if(DBOperations.checkConnection() == false) {
					primaryStage.setScene((new MainMenu()).showMainMenu(primaryStage,windowWidth, windowHeight));
					MainMenu.noConnectionLabel.setText("Nu exista conexiune spre baza de date");
					System.out.println("ai apasat pe root");
				}
				else
					MainMenu.noConnectionLabel.setText("");
		    }
		});
	
		
		// Load image
		try {
			FileInputStream input;
			input = new FileInputStream("./register2_icon.png");
			Image image = new Image(input);
		    ImageView imageView = new ImageView(image);
		    imageView.setStyle("-fx-translate-x:230px; -fx-translate-y:-186px");
		    root.getChildren().add(imageView);
		} catch (FileNotFoundException e1) {
			System.out.println("Nu am gasit imaginea!");
			e1.printStackTrace();
		}
		
		
		// Set IDs for objects
		backToMainMenu.setId("backToMainMenu");
		createAccount.setId("createAccount");
		emailField.setId("emailField");
		userField.setId("userField");
		passField.setId("passField");
		repeatPassField.setId("repeatPassField");
		userFieldLabel.setId("userFieldLabel");
		passFieldLabel.setId("passFieldLabel");
		repeatPassFieldLabel.setId("repeatPassFieldLabel");
		emailFieldLabel.setId("emailFieldLabel");
		successfulOperation.setId("successfulOperation");
		
		backToMainMenu.setFocusTraversable(false);
		createAccount.setFocusTraversable(false);
		
		a.getStylesheets().add(getClass().getResource("style_CreateAccountMenu.css").toExternalForm());
		
		
		// backToMainMenu button
		backToMainMenu.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene((new MainMenu()).showMainMenu(primaryStage,windowWidth, windowHeight));
		});
		
		
		// createAccount button
		createAccount.setOnMouseClicked(e -> {
			System.out.println(emailField.getText());
			System.out.println(userField.getText());
			System.out.println(passField.getText());
			System.out.println(repeatPassField.getText());
			String email = emailField.getText();
			String userName = userField.getText();
			String pass = passField.getText();
			String repeatPass = repeatPassField.getText();
			
			if(email.length() > 7 && email.length() <= 70 && userName.length() > 4 && userName.length() <= 30 && pass.length() > 8 &&
					pass.length() <= 30  && repeatPass.compareTo(pass) == 0 && DBOperations.checkEmailKey(email) &&
					validateEmail(email) && validatePassword(pass) && DBOperations.checkUserNameKey(userName)) {
				DBOperations.newAccount(email, userName, pass);
				primaryStage.setScene((new PassedRegistration()).openPassedRegistration(primaryStage, windowWidth, windowHeight));
			}
			else if(email.length() <= 7 || email.length() > 70) {
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Lungime incorectă a adresei de email");
			}
			else if(!validateEmail(email)){
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Email invalid!");
			}
			else if(userName.length() <=4 || userName.length() > 30) {
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Lungime incorectă a numelui de utilizator");
			}
			else if(!DBOperations.checkUserNameKey(userName)) {
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Există deja un utilizator cu acest nume");
			}
			else if(pass.length() <=8 || pass.length() > 30) {
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Lungime incorectă a parolei");
			}
			else if(!validatePassword(pass)){
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Parola trebuie să conțină litere mari, mici, și cifre");
			}
			else if(repeatPass.compareTo(pass) != 0) {
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Vă rugăm repetați parola");
			}
			else if(!DBOperations.checkEmailKey(email)) {
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Există deja un cont cu acest email");
			}

			//validare email cu regex, validare cheie primara
		});
		
		
		// Append GUI objects to root
		root.getChildren().addAll(backToMainMenu,successfulOperation);
		root.getChildren().addAll(createAccount,emailField,userField,passField,repeatPassField);
		root.getChildren().addAll(userFieldLabel,emailFieldLabel,passFieldLabel,repeatPassFieldLabel);
		
		
		return a;
	}
	
	
	// Validate password method - requires at lest one uppercase letter, one lowercase letter, one digit
	public static boolean validatePassword(String passwd) {
		boolean digit = false;
		boolean lower = false;
		boolean upper = false;
		for(int i = 0;i<passwd.length();i++) {
			if(Character.isDigit(passwd.charAt(i)))
				digit = true;
			if(Character.isLowerCase(passwd.charAt(i)))
				lower = true;
			if(Character.isUpperCase(passwd.charAt(i)))
				upper = true;
		}
		if(digit && lower && upper)
			return true;
		return false;
	}
	
	
	// Pattern (with regex) for checking email address
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public static boolean validateEmail(String emailStr) {
		
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		     return matcher.find();
		}
}
