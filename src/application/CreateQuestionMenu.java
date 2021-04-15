package application;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 * 
 * @author Dacian
 * <p> Class for CreateQuestionMenu page
 */
public class CreateQuestionMenu {
	// Page header
	private Label header = new Label("Întrebare nouă:");
	
	// Main labels
	private Label questionLabel = new Label("Corp întrebare:");
	private Label statementsListLabel = new Label("Variante de răspuns");
	private Label validityListLabel = new Label("Validitate");
	
	private static TextField questionBody = new TextField();
	
	// Variants labels
	private Label S1 = new Label("V1:");
	private Label S2 = new Label("V2:");
	private Label S3 = new Label("V3:");
	private Label S4 = new Label("V4:");
	private Label S5 = new Label("V5:");
	
	// Fields labels
	private static TextField S1Field = new TextField();
	private static TextField S2Field = new TextField();
	private static TextField S3Field = new TextField();
	private static TextField S4Field = new TextField();
	private static TextField S5Field = new TextField();
	
	// Validity buttons
	private static Button S1V = new Button("A");
	private static Button S2V = new Button("A");
	private static Button S3V = new Button("A");
	private static Button S4V = new Button("A");
	private static Button S5V = new Button("A");
	
	// Create new question button
	private Button createQuestion = new Button("Crează întrebare");
	
	// Create Question situation - fail or success
	private Label successfulOperation = new Label("");
	
	// Additional information
	private Label footerNotes = new Label("Notă: V<i> se referă la varianta de răspuns numărul <i>");
	
	// Back To User Menu button
	private Button backToUserMenu = new Button("Înapoi");
	
	// Clear all fields button
	private Button clearFields = new Button("Golește toate câmpurile");
	
	public Scene openCreateQuestionMenu(Stage primaryStage, double windowWidth, double windowHeight, User currentUser) {
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
		
		header.setId("header");
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
		
		createQuestion.setId("createQuestion");
		successfulOperation.setId("successfulOperation");
		footerNotes.setId("footerNotes");
		backToUserMenu.setId("backToUserMenu");
		clearFields.setId("clearFields");

		
		// Disable focus Traversable
		createQuestion.setFocusTraversable(false);
		backToUserMenu.setFocusTraversable(false);
		clearFields.setFocusTraversable(false);
		S1V.setFocusTraversable(false);
		S2V.setFocusTraversable(false);
		S3V.setFocusTraversable(false);
		S4V.setFocusTraversable(false);
		S5V.setFocusTraversable(false);

		
		
		// Validity buttons alternate values
		Button VBtnList[] = {S1V,S2V,S3V,S4V,S5V};
		for(Button i:VBtnList)
			i.setOnMouseClicked(e ->{
				CreateQuestionMenu.reverseButtonValue(i);
			});
		
		
		// Clear all fields and reset validity buttons values
		clearFields.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY) {
				questionBody.setText("");
				S1Field.setText("");
				S2Field.setText("");
				S3Field.setText("");
				S4Field.setText("");
				S5Field.setText("");
				for(Button i:VBtnList) {
					i.setText("A");
				}
				successfulOperation.setText("");
					
			}
		});
		
		// Creare new question
		createQuestion.setOnMouseClicked(e ->{
			if(validateQuestionLength() && validateValidity()) { // campuri valide - metoda de verificare
			Question v = new Question("id",questionBody.getText(),S1Field.getText(),CreateQuestionMenu.validityTextToBool(S1V.getText()),
					S2Field.getText(),CreateQuestionMenu.validityTextToBool(S2V.getText()),
					S3Field.getText(),CreateQuestionMenu.validityTextToBool(S3V.getText()),
					S4Field.getText(),CreateQuestionMenu.validityTextToBool(S4V.getText()),
					S5Field.getText(),CreateQuestionMenu.validityTextToBool(S5V.getText()));
			DBOperations.createQuestion(v);
			successfulOperation.setTextFill(Color.GREEN);
			successfulOperation.setText("Întrebare adăugată");
			questionBody.setText("");
			}
			else if(!validateQuestionLength()) {
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Lungimea datelor este prea scurtă");
			}
			else if(!validateValidity()) {
				successfulOperation.setTextFill(Color.RED);
				successfulOperation.setText("Cel puțin o variantă de răspuns trebuie să fie adevărată");
			}
		});
		
		// Back to user menu
		backToUserMenu.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY)
			primaryStage.setScene((new UserMenu()).openUserMenu(primaryStage,windowWidth, windowHeight,currentUser.getEmail()));
		});
		
		a.getStylesheets().add(getClass().getResource("style_CreateQuestionMenu.css").toExternalForm());
		root.getChildren().addAll(header,questionLabel,statementsListLabel,validityListLabel,questionBody);
		root.getChildren().addAll(S1,S2,S3,S4,S5);
		root.getChildren().addAll(S1Field,S2Field,S3Field,S4Field,S5Field);
		root.getChildren().addAll(S1V,S2V,S3V,S4V,S5V);
		root.getChildren().addAll(createQuestion,successfulOperation,footerNotes,backToUserMenu,clearFields);
		
		return a;
	}
	
	// Reverse buttons values - if is A turn to F, else turn to A
	public static void reverseButtonValue(Button a) {
		if(a.getText().equals("A"))
			a.setText("F");
		else
			a.setText("A");
	}
	
	// Convert buttons values from String to boolean
	public static boolean validityTextToBool(String V){
		if(V.equals("A"))
			return true;
		return false;
	}
	
	// Validate text from fields
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
	
	// Vadilate buttons values
	public static boolean validateValidity() {
		if(S1V.getText().equals("F") && S2V.getText().equals("F") && S3V.getText().equals("F") && S4V.getText().equals("F") && S5V.getText().equals("F"))
			return false;
		return true;
	}
	
}
