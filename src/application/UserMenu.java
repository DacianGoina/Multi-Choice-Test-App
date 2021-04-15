package application;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
/**
 * 
 * @author Dacian
 * <p> Class for User Menu - main menu for current user
 * <p> Contains extra buttons (options) for users who have accountType = "ADMIN"
 */
public class UserMenu {
	private Button createQuestion = new Button("Creare întrebare");
	private Button editQuestion = new Button("Editare întrebare");
	private Button startTest = new Button("Pornește test");
	private Button groupQuestions = new Button("Grupare întrebări");
	private Button userInfo = new Button("Informații cont");
	private Button changePasswd = new Button("Schimbă parola");
	private Button logoutButton = new Button("Deconectare");
	private Label emailInCornerLabel = new Label();
	
	
	public Scene openUserMenu(Stage primaryStage, double windowWidth, double windowHeight, String userEmail) {
		StackPane root = new StackPane();
		Scene a = new Scene(root, windowWidth, windowHeight);
		
		//System.out.println(javafx.scene.text.Font.getFamilies().toString());
		
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
		
		User currentUser = DBOperations.getUserInfo(userEmail);
		System.out.println(currentUser.toString());
		
		createQuestion.setId("createQuestion");
		editQuestion.setId("editQuestion");
		groupQuestions.setId("groupQuestions");
		startTest.setId("startTest");
		userInfo.setId("userInfo");
		changePasswd.setId("changePasswd");
		logoutButton.setId("logoutButton");
		emailInCornerLabel.setId("emailInCornerLabel");
		
		
		createQuestion.setFocusTraversable(false);
		editQuestion.setFocusTraversable(false);
		startTest.setFocusTraversable(false);
		groupQuestions.setFocusTraversable(false);
		userInfo.setFocusTraversable(false);
		changePasswd.setFocusTraversable(false);
		logoutButton.setFocusTraversable(false);
		
		// Show username in top - center
		// The var's name remained from older variant
		emailInCornerLabel.setText("Bine ai revenit, " + currentUser.getUserName());
		
		
		// Group questions
		groupQuestions.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene(new GroupQuestionMenu().OpenEditMenu(primaryStage, windowWidth, windowHeight, currentUser));
		});
		
		//Create question
		createQuestion.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene((new CreateQuestionMenu()).openCreateQuestionMenu(primaryStage,windowWidth, windowHeight,currentUser));
		});
		
		editQuestion.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene(new EditQuestionMenu().openEditQuestionMenu(primaryStage, windowWidth, windowHeight, currentUser));
		});
		
		// Start Test
		startTest.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene(new StartTestMenu().openPanelOne(primaryStage, windowWidth, windowHeight, currentUser));
		});
		
		// User Info Button event
		userInfo.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene((new UserInfoPanel()).openUserInfoPanel(primaryStage,windowWidth, windowHeight,currentUser));
		});
		
		// Change Password Button event
		changePasswd.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene((new ChangePassword()).openChangePassWord(primaryStage,windowWidth, windowHeight,currentUser));
		});
		
		// Logout Button event
		logoutButton.setOnMouseClicked(e -> {
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene((new MainMenu()).showMainMenu(primaryStage,windowWidth, windowHeight));
		});
		
		
		a.getStylesheets().add(getClass().getResource("style_UserMenu.css").toExternalForm());
		
		root.getChildren().addAll(startTest,userInfo,changePasswd,logoutButton,emailInCornerLabel);
		if(currentUser.getAccountType().equals("ADMIN")) { // if (user is ADMIN)
			root.getChildren().addAll(createQuestion,editQuestion,groupQuestions);
		}
		
		return a;
	}
}
