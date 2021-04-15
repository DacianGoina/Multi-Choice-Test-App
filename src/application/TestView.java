package application;

import java.text.DecimalFormat;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * 
 * @author Dacian
 * <p> Class for Test View page - review test's result
 */

public class TestView {
	// Question (item) number
	private static Label questionNumber = new Label("Item ");
		
	//Fields for question's body and variants
	private static TextArea questionBody = new TextArea("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
	private static TextField S1 = new TextField("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB");
	private static TextField S2 = new TextField("Varianta 2");
	private static TextField S3 = new TextField("Varianta 3");
	private static TextField S4 = new TextField("Varianta 4");
	private static TextField S5 = new TextField("Varianta 5");
	
	
	private static Button nextQuestion = new Button("Înainte >");
	private static Button prevQuestion = new Button("< Înapoi");
	private static Button firstQuestion = new Button("<< Prima întrebare");
	private static Button lastQuestion = new Button("Ultima întrebare >>");
	private static Button finishTest = new Button("Finalizare verificare"); //  doar cand esti la ultima intrebare
	
	// Counter for current question
	private static int counter;
	
	public Label currentPoints = new Label("aaa");
	
	// Set IDs for GUI objects
	public static void SetIds() {
		questionNumber.setId("questionNumber");
		questionBody.setId("questionBody");
		
		S1.setId("S1");
		S2.setId("S2");
		S3.setId("S3");
		S4.setId("S4");
		S5.setId("S5");
		
		nextQuestion.setId("nextQuestion");
		prevQuestion.setId("prevQuestion");
		firstQuestion.setId("firstQuestion");
		lastQuestion.setId("lastQuestion");
		finishTest.setId("finishTest");
		
		questionBody.setFocusTraversable(false);
		questionBody.setDisable(true);
		questionBody.setWrapText(true);
		
	}
	
	// Set proprieties for GUI objects
	public static void setProprieties() {
		S1.setFocusTraversable(false);
		S1.setDisable(true);
		
		S2.setFocusTraversable(false);
		S2.setDisable(true);
		
		S3.setFocusTraversable(false);
		S3.setDisable(true);
		
		S4.setFocusTraversable(false);
		S4.setDisable(true);
		
		S5.setFocusTraversable(false);
		S5.setDisable(true);
		
		
		nextQuestion.setFocusTraversable(false);
		prevQuestion.setFocusTraversable(false);
		firstQuestion.setFocusTraversable(false);
		lastQuestion.setFocusTraversable(false);
		finishTest.setFocusTraversable(false);
	}
	public static void setFieldsBackgroundColor(boolean TruthMatrix[][],boolean DraftMatrix[][]) {
		
		// Inner color for S1
		if(TruthMatrix[counter][0] == true && DraftMatrix[counter][0] == true)
			S1.setStyle("-fx-control-inner-background:#7FDCFF");
		if(DraftMatrix[counter][0] == false)
			S1.setStyle("-fx-control-inner-background:white");
		if(TruthMatrix[counter][0] == false && DraftMatrix[counter][0] == true)
			S1.setStyle("-fx-control-inner-background:#ff4d4d");
		
		// Inner color for S2
		if(TruthMatrix[counter][1] == true && DraftMatrix[counter][1] == true)
			S2.setStyle("-fx-control-inner-background:#7FDCFF");
		if(DraftMatrix[counter][1] == false)
			S2.setStyle("-fx-control-inner-background:white");
		if(TruthMatrix[counter][1] == false && DraftMatrix[counter][1] == true)
			S2.setStyle("-fx-control-inner-background:#ff4d4d");
		
		// Inner color for S3
		if(TruthMatrix[counter][2] == true && DraftMatrix[counter][2] == true)
			S3.setStyle("-fx-control-inner-background:#7FDCFF");
		if(DraftMatrix[counter][2] == false)
			S3.setStyle("-fx-control-inner-background:white");
		if(TruthMatrix[counter][2] == false && DraftMatrix[counter][2] == true)
			S3.setStyle("-fx-control-inner-background:#ff4d4d");
		
		// Inner color for S4
		if(TruthMatrix[counter][3] == true && DraftMatrix[counter][3] == true)
			S4.setStyle("-fx-control-inner-background:#7FDCFF");
		if(DraftMatrix[counter][3] == false)
			S4.setStyle("-fx-control-inner-background:white");
		if(TruthMatrix[counter][3] == false && DraftMatrix[counter][3] == true)
			S4.setStyle("-fx-control-inner-background:#ff4d4d");
		
		// Inner color for S5
		if(TruthMatrix[counter][4] == true && DraftMatrix[counter][4] == true)
			S5.setStyle("-fx-control-inner-background:#7FDCFF");
		if(DraftMatrix[counter][4] == false)
			S5.setStyle("-fx-control-inner-background:white");
		if(TruthMatrix[counter][4] == false && DraftMatrix[counter][4] == true)
			S5.setStyle("-fx-control-inner-background:#ff4d4d");
		
		
	}
	public static void setFieldsValues(Question[] A) {
		
		questionNumber.setText("Item " + (counter + 1));
		questionBody.setText(A[counter].getQuestionBody());
		S1.setText(A[counter].getS1());
		S2.setText(A[counter].getS2());
		S3.setText(A[counter].getS3());
		S4.setText(A[counter].getS4());
		S5.setText(A[counter].getS5());
	}
	public static void setButtonsStates(int lastQuestionIndex) {
		// Daca sunt la prima intrebare nu are sens merg la prima
		if(counter == 0)
			firstQuestion.setDisable(true);
		else
			firstQuestion.setDisable(false);
		
		// Daca sunt la prima intrebare nu pot merge in spate
		if(counter == 0)
			prevQuestion.setDisable(true);
		else
			prevQuestion.setDisable(false);
		
		// Daca sunt la ultima intrebare nu pot merge in fata
		if(counter == lastQuestionIndex) {
			nextQuestion.setDisable(true);
		}
		else
			nextQuestion.setDisable(false);
		
		// Daca sunt la ultima intrebare nu are rost merg la ultima folosind butonul
		if(counter == lastQuestionIndex)
			lastQuestion.setDisable(true);
		else
			lastQuestion.setDisable(false);
		
		// Doar daca sunt la ultima intrebare pot finaliza testul
		if(counter == lastQuestionIndex)
			finishTest.setVisible(true);
		else
			finishTest.setVisible(false);
		
	}

	public Scene View(Stage primaryStage, double windowWidth, double windowHeight, User currentUser, Test f, Question[] A, boolean TM[][], boolean DM[][]) {
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
		
		currentPoints.setId("currentPoints");
		
		counter = 0;
		
		for(Question i:A)
			System.out.println(i);
		
		SetIds();
		setProprieties();
		setFieldsBackgroundColor(TM,DM);
		
		setCurrentPoints(currentPoints,f,A,TM,DM,counter);
		
		int lastQuestionIndex = f.getNumberOfQuestions() - 1;
		setButtonsStates(lastQuestionIndex);
		setFieldsValues(A);
		
		firstQuestion.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
				counter = 0;
				setFieldsBackgroundColor(TM,DM);
				setFieldsValues(A);
				setButtonsStates(lastQuestionIndex);
				setCurrentPoints(currentPoints,f,A,TM,DM,counter);
			}
		});
		
		prevQuestion.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
			counter--;
			setFieldsValues(A);
			setButtonsStates(lastQuestionIndex);
			setFieldsBackgroundColor(TM,DM);
			setCurrentPoints(currentPoints,f,A,TM,DM,counter);
			}
		});
		
		nextQuestion.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
			counter++;
			setFieldsValues(A);
			setButtonsStates(lastQuestionIndex);
			setFieldsBackgroundColor(TM,DM);
			setCurrentPoints(currentPoints,f,A,TM,DM,counter);
			}
		});
		
		lastQuestion.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
			counter = lastQuestionIndex;
			setFieldsValues(A);
			setButtonsStates(lastQuestionIndex);
			setFieldsBackgroundColor(TM,DM);
			setCurrentPoints(currentPoints,f,A,TM,DM,counter);
			}
		});
		
		
		// Back to User Menu
		finishTest.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY) {
				primaryStage.setScene((new UserMenu()).openUserMenu(primaryStage,windowWidth, windowHeight,currentUser.getEmail()));
			}
		});
		
		a.getStylesheets().add(getClass().getResource("style_TestInProgress.css").toExternalForm());
		root.getChildren().addAll(questionNumber,questionBody);
		root.getChildren().addAll(S1,S2,S3,S4,S5);
		root.getChildren().addAll(nextQuestion,prevQuestion,firstQuestion,lastQuestion,finishTest,currentPoints);
		return a;
	}
	
	// Seteaza valoarea pentru Label currentPoints, se va folosi pentru a afisa punctajul obtinut pentru fiecare intrebare
	public static void setCurrentPoints(Label a, Test f, Question A[], boolean TM[][], boolean DM[][], int questionIndex) {
		
		if(f.getEvaluationType().equals("T")) {
			// acordare punctaj total
			int ok = 1;
			for(int j = 0; j < 5 && ok == 1;j++)
				if( TM[questionIndex][j] != DM[questionIndex][j] )
					ok = 0;
			if(ok == 1)
				a.setText("1/1");
			else
				a.setText("0/1");
		}
		
		else {
			// acordare punctaj partial
			int ok  = 1;
			double nTruth = 0; // numarul de raspunsuri corecte de pe linie
			double nSelectedTruth = 0; // numarul de raspunsuri corecte selectate de pe linie
			
			// variante true pe linie
			for(int j = 0;j<5;j++)
				if(TM[questionIndex][j])
					nTruth++;
			
			// calculare punctaj partial
			for(int j = 0;j<5 && ok == 1;j++) { //nSelectedTruth
				if(TM[questionIndex][j] == true && DM[questionIndex][j] == true) //{
					nSelectedTruth++;
				if(TM[questionIndex][j] == false && DM[questionIndex][j] == true)
					ok = 0;
				}
			
			double S;
			if(ok == 1) // daca nu a pus niciuna gresita
				S = nSelectedTruth/nTruth;
			else
				S = 0;
			
			// trunchiere rezultat cu zecimale - pentru a evita cazurile in genul 0.6666666667
			String trunc;
			if(S == (int) S)
				trunc  = String.valueOf((int)S) + " / " + "1";
			else
				trunc = new DecimalFormat("#.##").format(S) + " / " + "1";
			
			a.setText(trunc);
			
		}
		
	}
		
}
