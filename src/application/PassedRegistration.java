package application;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * 
 * @author Dacian
 * <p> Class for PassedRegistration page
 */
public class PassedRegistration {
	private Label successfulRegistrationLabel = new Label("Înregistrare reușită! Acum te poți loga");
	private Button backToMainMenu = new Button("Înapoi la meninul principal");
	
	public Scene openPassedRegistration(Stage primaryStage, double windowWidth, double windowHeight) {
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
		
		successfulRegistrationLabel.setId("successfulRegistrationLabel");
		backToMainMenu.setId("backToMainMenu");
		
		backToMainMenu.setFocusTraversable(false);
		
		a.getStylesheets().add(getClass().getResource("style_PassedRegistration.css").toExternalForm());
		
		// Back to use menu
		backToMainMenu.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene((new MainMenu()).showMainMenu(primaryStage,windowWidth, windowHeight));
		});
		
		
		root.getChildren().addAll(successfulRegistrationLabel, backToMainMenu);
		
		return a;
	}
}
