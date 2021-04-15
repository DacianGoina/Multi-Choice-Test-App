package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 * 
 * @author Dacian
 * <p> Class for ChangePassword page
 */
public class ChangePassword {
	
	// GUI elements
	
	private Label header = new Label("Schimbare parolă");
	private Label successfulOperation = new Label();
	
	private PasswordField passwdField = new PasswordField();
	private PasswordField newPasswdField = new PasswordField();
	private PasswordField repeatNewPasswdField = new PasswordField();
	
	private Label passwdLabel = new Label("Parolă:");
	private Label newPasswdLabel = new Label("Parolă nouă:");
	private Label repeatNewPasswdLabel = new Label("Repetă parola nouă:");
	
	private Button changePasswd = new Button("Schimbă parola");
	private Button backToUserMenu = new Button("Înapoi");
	
	
	
	public Scene openChangePassWord(Stage primaryStage, double windowWidth, double windowHeight, User currentUser) {
		StackPane root = new StackPane();
		Scene a = new Scene(root, windowWidth, windowHeight);
		
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
			input = new FileInputStream("./change_passwd_icon.png");
			Image image = new Image(input);
		    ImageView imageView = new ImageView(image);
		    imageView.setStyle("-fx-translate-x:200px; -fx-translate-y:-140px");
		    root.getChildren().add(imageView);
		} catch (FileNotFoundException e1) {
			System.out.println("Nu am gasit imaginea!");
			e1.printStackTrace();
		}
		
		// Set IDs for GUI objects
		header.setId("header");
		successfulOperation.setId("successfulOperation");
		passwdField.setId("passwdField");
		newPasswdField.setId("newPasswdField");
		repeatNewPasswdField.setId("repeatNewPasswdField");
		passwdLabel.setId("passwdLabel");
		newPasswdLabel.setId("newPasswdLabel");
		repeatNewPasswdLabel.setId("repeatNewPasswdLabel");
		changePasswd.setId("changePasswd");
		backToUserMenu.setId("backToUserMenu");
		
		changePasswd.setFocusTraversable(false);
		backToUserMenu.setFocusTraversable(false);
		
		// Back to User Menu Button event
		backToUserMenu.setOnMouseClicked(e->{
			primaryStage.setScene((new UserMenu()).openUserMenu(primaryStage,windowWidth, windowHeight,currentUser.getEmail()));
		});
	
		
		// Change password button
		changePasswd.setOnMouseClicked(e->{
			String passwd = passwdField.getText();
			String newPasswd = newPasswdField.getText();
			String repeatNewPasswd = repeatNewPasswdField.getText();
			System.out.println("apasat pt schimbare passwd");
			if(passwd.equals(currentUser.getPasswd()) && newPasswd.equals(repeatNewPasswd) && newPasswd.length() > 8  &&
					CreateAccountMenu.validatePassword(newPasswd) && newPasswd.length() <=30) {
				// parola schimbata cu succes
				DBOperations.ChangePassword(newPasswd, currentUser.getEmail());
				successfulOperation.setTextFill(Color.GREEN);
				successfulOperation.setText("Parolă schimbată cu succes!");
				passwdField.setText("");
				newPasswdField.setText("");
				repeatNewPasswdField.setText("");
				
			}
			else if(!passwd.equals(currentUser.getPasswd())) {
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Parolă curentă incorentă");
			}
			else if(!CreateAccountMenu.validatePassword(newPasswd)) {
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Noua parolă trebuie să conțină litere mari, mici, și cifre");
			}
			else if(newPasswd.length() < 9 || newPasswd.length() > 30) {
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Lungime invalidă pentru noua parolă");
			}
			else if(!newPasswd.equals(repeatNewPasswd)){
				System.out.println("am intrat pe fail");
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Repetă noua parolă");
			}

			
		});
		
		
		// Append GUI objects to root
		a.getStylesheets().add(getClass().getResource("style_ChangePassword.css").toExternalForm());
		root.getChildren().addAll(header,successfulOperation);
		root.getChildren().addAll(passwdField,newPasswdField,repeatNewPasswdField);
		root.getChildren().addAll(passwdLabel,newPasswdLabel,repeatNewPasswdLabel);
		root.getChildren().addAll(changePasswd,backToUserMenu);
		
		return a;
	}
	
}
