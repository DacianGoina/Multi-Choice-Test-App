package application;

import javafx.stage.Stage;
/**
 * @author Dacian
 * <p> OpenApp - launch application
 */

public class OpenApp {
	private double windowHeight;
	private double windowWidth;

	public OpenApp(double windowWidth, double windowHeight) {
		super();
		this.windowHeight = windowHeight;
		this.windowWidth = windowWidth;
	}

	public double getWindowHeight() {
		return windowHeight;
	}
	public void setWindowHeight(double windowHeight) {
		this.windowHeight = windowHeight;
	}

	public double getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(double windowWidth) {
		this.windowWidth = windowWidth;
	}
	
	
	public void openWindow(Stage primaryStage) {
			try {
				primaryStage.setResizable(false);
				primaryStage.setTitle("Aplicație pentru prelucrare și simularea testelor grilă");
				primaryStage.setWidth(windowWidth);
				primaryStage.setHeight(windowHeight);
				primaryStage.setScene((new MainMenu()).showMainMenu(primaryStage,windowWidth, windowHeight));
				primaryStage.show();
				System.out.println(primaryStage.getWidth() + " " + primaryStage.getHeight());
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
	}
}
