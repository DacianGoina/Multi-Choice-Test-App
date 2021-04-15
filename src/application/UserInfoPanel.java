package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
/**
 * 
 * @author Dacian
 * <p> Class for UserInfoPanel - show info about current user
 */
public class UserInfoPanel {
	private Label info = new Label();
	private Label header = new Label("Informații cont");
	private Button backToUserMenu = new Button("Înapoi");
	
	public Scene openUserInfoPanel(Stage primaryStage, double windowWidth, double windowHeight, User currentUser) {
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
		
		
		try {
			FileInputStream input;
			input = new FileInputStream("./info_icon.png");
			Image image = new Image(input);
		    ImageView imageView = new ImageView(image);
		    imageView.setStyle("-fx-translate-x:200px; -fx-translate-y:1px");
		    root.getChildren().add(imageView);
		} catch (FileNotFoundException e1) {
			System.out.println("Nu am gasit imaginea!");
			e1.printStackTrace();
		}
		
		info.setId("info");
		header.setId("header");
		backToUserMenu.setId("backToUserMenu");
		
		backToUserMenu.setFocusTraversable(false);
		
		double averageScore = 0;
		double testsCount = currentUser.getTestsCount();
		double totalScore = currentUser.getTotalScore();
		
		if(testsCount != 0)
			averageScore = totalScore/testsCount;
		else
			averageScore = 0.0;
		
		String trunc;
		trunc = new DecimalFormat("#.##").format(averageScore);
		
		info.setLineSpacing(20);
		//System.out.println(currentUser.getRegistrationDate());
		
		String dateInfo[] = currentUser.getRegistrationDate().toString().split("-");
		String registrationDate = dateInfo[2] + "/" + dateInfo[1] + "/" + dateInfo[0];
		
		info.setText("Email: "+ currentUser.getEmail() + "\n"+"Nume de utilizator: "+ currentUser.getUserName()+"\n"
				 +"Data înregistrării: "+ registrationDate + "\n" + "Tip cont: "+currentUser.getAccountType()
				 + "\n\n" + "Teste efectuate: " + currentUser.getTestsCount() + "\n" + "Medie punctaje: "  + trunc);
		
		
		// Back To User Menu Button event
		backToUserMenu.setOnMouseClicked(e->{
			primaryStage.setScene((new UserMenu()).openUserMenu(primaryStage,windowWidth, windowHeight,currentUser.getEmail()));
		});
		
		a.getStylesheets().add(getClass().getResource("style_UserInfoPanel.css").toExternalForm());
		root.getChildren().addAll(header,info,backToUserMenu);
		return a;
	}
}
