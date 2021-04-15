package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
/**
 * 
 * @author Dacian
 * <p> Class for Main Menu
 */
public class MainMenu {
	// GUI objects
	private Button loginButton = new Button("Autentificare");
	private Button createAccountButton = new Button("Creare cont");
	private Button exitButton = new Button("Închide aplicație");

	private Label header = new Label("Aplicație pentru prelucrarea și simularea testelor grilă");
	
	public static Label noConnectionLabel = new Label();
	public static void noConnectionLabelSetProp() {
		MainMenu.noConnectionLabel.setStyle("-fx-font-size:25px;\r\n"
				+ "	-fx-font-style: italic;\r\n"
				+ "	-fx-translate-y:240px;");
	}
	public Scene showMainMenu(Stage primaryStage, double windowWidth, double windowHeight) {
		
		StackPane root = new StackPane();
		Scene a = new Scene(root, windowWidth, windowHeight);
		MainMenu.noConnectionLabel.setId("noConnectionLabel");
		
		DBOperations.checkConnection();
		MainMenu.noConnectionLabelSetProp();

		
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
		
		
		try {
			FileInputStream input;
			input = new FileInputStream("./logo_icon.png");
			Image image = new Image(input);
		    ImageView imageView = new ImageView(image);
		    imageView.setStyle("-fx-translate-x:-280px; -fx-translate-y:-50px");
		    //imagineView.setY()
		    root.getChildren().add(imageView);
		} catch (FileNotFoundException e1) {
			System.out.println("Nu am gasit imaginea!");
			e1.printStackTrace();
		}
	     
		
		loginButton.setId("loginButton");
		createAccountButton.setId("createAccountButton");
		exitButton.setId("exitButton");
		header.setId("header");
		
		loginButton.setFocusTraversable(false);
		createAccountButton.setFocusTraversable(false);
		exitButton.setFocusTraversable(false);
		
		a.getStylesheets().add(getClass().getResource("style_MainMenu.css").toExternalForm());
		
		
		// Login button event
		loginButton.setOnMouseClicked(e->{
			primaryStage.setScene((new LoginMenu()).openLoginMenu(primaryStage, windowWidth, windowHeight));
		});
		
		// Create account button event
		createAccountButton.setOnMouseClicked(e ->{
			primaryStage.setScene((new CreateAccountMenu()).openCreateAccountMenu(primaryStage, windowWidth, windowHeight));
		});
		
		// Exit button event
		exitButton.setOnMouseClicked(e -> {
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.close();
		});
		
		
		root.getChildren().addAll(loginButton,createAccountButton,exitButton,noConnectionLabel,header);
		

		
		return a;
	}
}
