package application;
	
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * 
 * @author Dacian
 * <p> Main class - launch application
 */

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		
		OpenApp A = new OpenApp(800,600);
		A.openWindow(primaryStage);
		DBOperations.createMainTables();
		
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
