package application;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EditQuestionMenu {
	private static Label header = new Label("Editare întrebare - selectează întrebare:");
	private static ComboBox<String> questionsMenu = new ComboBox<>();
	
	private static Label questionLabel = new Label("Corp întrebare:");
	private static Label statementsListLabel = new Label("Variante de răspuns");
	private static Label validityListLabel = new Label("Validitate");
	
	public static TextField questionBody = new TextField();
	
	
	private static Label S1 = new Label("V1:");
	private static Label S2 = new Label("V2:");
	private static Label S3 = new Label("V3:");
	private static Label S4 = new Label("V4:");
	private static Label S5 = new Label("V5:");
	
	// Variants fields
	public static TextField S1Field = new TextField();
	public static TextField S2Field = new TextField();
	public static TextField S3Field = new TextField();
	public static TextField S4Field = new TextField();
	public static TextField S5Field = new TextField();
	
	// Buttons for validity
	public static Button S1V = new Button("A");
	public static Button S2V = new Button("A");
	public static Button S3V = new Button("A");
	public static Button S4V = new Button("A");
	public static Button S5V = new Button("A");
	
	// Buttons for edit/delete question
	private static Button editQuestion = new Button("Editează întrebarea");
	private static Button deleteQuestion = new Button("Șterge întrebarea");
		
	// Create Question situation
	private static Label successfulOperation = new Label(""); // de pus valoare pentru fiecare caz
	
	// Back to user menu button
	private static Button backToUserMenu = new Button("Înapoi");
	
	// Load questions from DB
	public static void setQuestionsMenu() {
		questionsMenu.setItems(DBOperations.getAllQuestions());
		questionsMenu.getSelectionModel().select(0);
	}
	
	// Set IDs for GUI objects
	public static void setIds() {
		header.setId("header");
		questionsMenu.setId("questionsMenu");
		questionLabel.setId("questionLabel");
		statementsListLabel.setId("statementsListLabel");
		validityListLabel.setId("validityListLabel");
		questionBody.setId("questionBody");
		
		S1.setId("S1");
		S2.setId("S2");
		S3.setId("S3");
		S4.setId("S4");
		S5.setId("S5");
		
		S1V.setId("S1V");
		S2V.setId("S2V");
		S3V.setId("S3V");
		S4V.setId("S4V");
		S5V.setId("S5V");
		
		S1Field.setId("S1Field");
		S2Field.setId("S2Field");
		S3Field.setId("S3Field");
		S4Field.setId("S4Field");
		S5Field.setId("S5Field");
		
		editQuestion.setId("editQuestion");
		deleteQuestion.setId("deleteQuestion");
		successfulOperation.setId("successfulOperation");
		backToUserMenu.setId("backToUserMenu");
	}

	public Scene openEditQuestionMenu(Stage primaryStage, double windowWidth, double windowHeight, User currentUser) {
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
		
		successfulOperation.setText("");
		
		setIds();
		setQuestionsMenu();
		DBOperations.getQuestionInfo(questionsMenu.getValue());
		
		// Disable focus
		questionsMenu.setFocusTraversable(false);
		editQuestion.setFocusTraversable(false);
		deleteQuestion.setFocusTraversable(false);
		backToUserMenu.setFocusTraversable(false);
		S1V.setFocusTraversable(false);
		S2V.setFocusTraversable(false);
		S3V.setFocusTraversable(false);
		S4V.setFocusTraversable(false);
		S5V.setFocusTraversable(false);
		

		Button VBtnList[] = {S1V,S2V,S3V,S4V,S5V};
		for(Button i:VBtnList)
			i.setOnMouseClicked(e ->{
				EditQuestionMenu.reverseButtonValue(i);
			});
		
		// cand selectez o intrebare din camp sa imi apare informatiile
		questionsMenu.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) ->{
			//System.out.println(newValue);
			DBOperations.getQuestionInfo(newValue);
			
		});
		
		deleteQuestion.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
				String val = questionsMenu.getValue();
				DBOperations.deleteQuestion(val);
				setQuestionsMenu();
				successfulOperation.setTextFill(Color.GREEN);
				successfulOperation.setText("Întrebare eliminată!");
			}
		});
		
		editQuestion.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
				if(validateQuestionLength() && validateValidity()) {
				DBOperations.editQuestion(questionsMenu.getValue());
				successfulOperation.setTextFill(Color.GREEN);
				successfulOperation.setText("Întrebare modificată cu succes!");
				}
				if(!validateValidity()) {
					successfulOperation.setTextFill(Color.RED);
					successfulOperation.setText("Cel puțin un răspuns trebuie să fie adevărat");
				}
				if(!validateQuestionLength()) {
					successfulOperation.setTextFill(Color.RED);
					successfulOperation.setText("Lungimea datelor este prea scurtă");
				}
			}
		});
		
		// Back to user menu
		backToUserMenu.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene((new UserMenu()).openUserMenu(primaryStage,windowWidth, windowHeight,currentUser.getEmail()));
			});
		
		a.getStylesheets().add(getClass().getResource("style_EditQuestionMenu.css").toExternalForm());
		root.getChildren().addAll(header,questionsMenu,questionLabel,statementsListLabel,validityListLabel,questionBody);
		root.getChildren().addAll(S1,S2,S3,S4,S5);
		root.getChildren().addAll(S1Field,S2Field,S3Field,S4Field,S5Field);
		root.getChildren().addAll(S1V,S2V,S3V,S4V,S5V);
		root.getChildren().addAll(editQuestion,deleteQuestion,successfulOperation,backToUserMenu);
		
		return a;
		}

	
	public static void reverseButtonValue(Button a) {
		if(a.getText().equals("A"))
			a.setText("F");
		else
			a.setText("A");
	}
	
	
	public static boolean validateQuestionLength() {
		if(questionBody.getText().length() <=3 || questionBody.getText().length() > 150)
			return false;
		if(S1Field.getText().length()<1 || S1Field.getText().length() > 100)
			return false;
		if(S2Field.getText().length()<1 || S2Field.getText().length() > 100)
			return false;
		if(S3Field.getText().length()<1 || S3Field.getText().length() > 100)
			return false;
		if(S4Field.getText().length()<1 || S4Field.getText().length() > 100)
			return false;
		if(S5Field.getText().length()<1 || S5Field.getText().length() > 100)
			return false;
		return true;
	}
	
	public static boolean validateValidity() {
		if(S1V.getText().equals("F") && S2V.getText().equals("F") && S3V.getText().equals("F") && S4V.getText().equals("F") && S5V.getText().equals("F"))
			return false;
		return true;
	}
}
