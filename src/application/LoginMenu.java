package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
 * <p> Class for Login Menu
 */
public class LoginMenu {
	// GUI objects
	private Button backToMainMenu = new Button("Înapoi");
	private Button loginButton = new Button("Autentificare");
	private TextField emailField = new TextField();
	private PasswordField passField = new PasswordField();
	private Label emailFieldLabel = new Label("Adresã de email:");
	private Label passFieldLabel = new Label("Parola:");
	private Label successfulOperation = new Label("");
	
	
	public Scene openLoginMenu(Stage primaryStage, double windowWidth,double windowHeight) {
		
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
		
		backToMainMenu.setId("backToMainMenu");
		loginButton.setId("loginButton");
		emailField.setId("emailField");
		passField.setId("passField");
		emailFieldLabel.setId("emailFieldLabel");
		passFieldLabel.setId("passFieldLabel");
		successfulOperation.setId("successfulOperation");
		
		backToMainMenu.setFocusTraversable(false);
		loginButton.setFocusTraversable(false);
		
		
		try {
			FileInputStream input;
			input = new FileInputStream("./login_icon.png");
			Image image = new Image(input);
		    ImageView imageView = new ImageView(image);
		    imageView.setStyle("-fx-translate-x:-230px; -fx-translate-y:-150px");
		    root.getChildren().add(imageView);
		} catch (FileNotFoundException e1) {
			System.out.println("Nu am gasit imaginea!");
			e1.printStackTrace();
		}
		
		a.getStylesheets().add(getClass().getResource("style_LoginMenu.css").toExternalForm());
		
		
		backToMainMenu.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene((new MainMenu()).showMainMenu(primaryStage,windowWidth, windowHeight));
		});
		
		
		loginButton.setOnMouseClicked(e -> {
			String email = emailField.getText();
			String pass = passField.getText();
			if(DBOperations.checkLogin(email, pass) == true)
				primaryStage.setScene((new UserMenu()).openUserMenu(primaryStage,windowWidth, windowHeight,email));
			else {
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Date de logare invalide");
			}
				
		});
		
		root.getChildren().addAll(backToMainMenu,successfulOperation);
		root.getChildren().addAll(loginButton,emailField,passField);
		root.getChildren().addAll(emailFieldLabel,passFieldLabel);
		
		return a;
	}
}
