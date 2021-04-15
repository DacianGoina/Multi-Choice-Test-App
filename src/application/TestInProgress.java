package application;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Duration;

/**
 * 
 * @author Dacian
 * <p> Class for a test in progress (ongoing)
 */
public class TestInProgress {
	// Item (question) number
	private static Label questionNumber = new Label("Item ");
	
	//Fields to show the questions
	private static TextArea questionBody = new TextArea("");
	private static TextField S1 = new TextField("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB");
	private static TextField S2 = new TextField("Varianta 2");
	private static TextField S3 = new TextField("Varianta 3");
	private static TextField S4 = new TextField("Varianta 4");
	private static TextField S5 = new TextField("Varianta 5");
	
	
	// Auxiliary TextFields - for hover
	private static TextField S1aux = new TextField();
	private static TextField S2aux = new TextField();
	private static TextField S3aux = new TextField();
	private static TextField S4aux = new TextField();
	private static TextField S5aux = new TextField();
	
	// Navigation Buttons
	private static Button nextQuestion = new Button("Înainte >");
	private static Button prevQuestion = new Button("< Înapoi");
	private static Button firstQuestion = new Button("<< Prima întrebare");
	private static Button lastQuestion = new Button("Ultima întrebare >>");
	private static Button finishTest = new Button("Finalizare test"); //  doar cand esti la ultima intrebare
	
	// To show remaining time
	private Label timeLabel = new Label("22:22");
	
	
	// Counter for current question (item)
	private static int counter;
	
	
	// Matrices
	private static boolean TruthMatrix[][];
	private static boolean DraftMatrix[][];
	
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
		//timeLabel.setId("timeLabel");
		
		questionBody.setFocusTraversable(false);
		questionBody.setDisable(true);
		questionBody.setWrapText(true);
		
		
		S1aux.setId("S1aux");
		S2aux.setId("S2aux");
		S3aux.setId("S3aux");
		S4aux.setId("S4aux");
		S5aux.setId("S5aux");
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
	
	// Set inner background color (reset to white or set to blue) 
	public static void setFieldsBackgroundColor(boolean DraftMatrix[][]) {
		// Inner color for S1
		if(DraftMatrix[counter][0] == true)
			S1.setStyle("-fx-control-inner-background:#7FDCFF");
		else
			S1.setStyle("-fx-control-inner-background:white");
		
		// Inner color for S2
		if(DraftMatrix[counter][1] == true)
			S2.setStyle("-fx-control-inner-background:#7FDCFF");
		else
			S2.setStyle("-fx-control-inner-background:white");
				
		// Inner color for S3
		if(DraftMatrix[counter][2] == true)
			S3.setStyle("-fx-control-inner-background:#7FDCFF");
		else
			S3.setStyle("-fx-control-inner-background:white");
		
		// Inner color for S4
		if(DraftMatrix[counter][3] == true)
			S4.setStyle("-fx-control-inner-background:#7FDCFF");
		else
			S4.setStyle("-fx-control-inner-background:white");
		
		// Inner color for S5
		if(DraftMatrix[counter][4] == true)
			S5.setStyle("-fx-control-inner-background:#7FDCFF");
		else
			S5.setStyle("-fx-control-inner-background:white");
		
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

	
	// Count time
	public int seconds = 0;
	public int minutes = 0;
	public int hours = 0;
	
	Timeline timeline;
	public void startTimer() {
        timeline = new Timeline(
            new KeyFrame(Duration.seconds(0),
                e ->advanceDuration()),
            new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        
    }
	private void advanceDuration() {
        if (seconds < 59) {
            seconds++;
        } else {
            seconds = 0;
            if (minutes < 59) {
                minutes++;    
            }
        }
        if(minutes < 10) {
        	if(seconds < 10)
        			timeLabel.setText("0" + minutes + ":" + "0" + seconds);
        	else
        		timeLabel.setText("0" + minutes + ":" + seconds);
        }
        else {
        	if(seconds < 10)
        		timeLabel.setText(minutes + ":" + "0" + seconds);
        	else
        		timeLabel.setText(minutes + ":" + seconds);
        }
    }
	
	public Label timeLabelAux = new Label();
	
	public Scene Launch(Stage primaryStage, double windowWidth, double windowHeight, User currentUser, Test f, Question[] A) {
		timeLabel.setVisible(false);
		timeLabelAux.setVisible(false);
		timeLabel.setId("timeLabel");
		timeLabelAux.setId("timeLabelAux");
		
		
		// Show remaining time
		if(f.getTimeLimit() != 0) {
			timeLabel.setVisible(true);
			timeLabelAux.setVisible(true);
			if(f.getTimeLimit() < 10)
				timeLabelAux.setText(" / " + "0" + (f.getTimeLimit() - 1) +":" + "59");
			else
				timeLabelAux.setText(" / " + (f.getTimeLimit() - 1) +":" + "59");
			
			startTimer();
		}
			
		StackPane root = new StackPane();
		Scene a = new Scene(root, windowWidth, windowHeight);
		
		a.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		    	if(DBOperations.checkConnection() == false) {
		    		if(f.getTimeLimit() !=0)
						timeline.stop();
					primaryStage.setScene((new MainMenu()).showMainMenu(primaryStage,windowWidth, windowHeight));
					MainMenu.noConnectionLabel.setText("Nu exista conexiune spre baza de date");
					System.out.println("ai apasat pe root");
				}
				else
					MainMenu.noConnectionLabel.setText("");
		    }
		});
		
		counter = 0;
		
		
		// Event pentru cand se modifica valoarea din timeLabel
		timeLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               if(minutes == f.getTimeLimit()) {
            	  timeline.stop();
   				primaryStage.setScene((new TestResult()).openTestResult(primaryStage, windowWidth, windowHeight, currentUser, f, A, TruthMatrix, DraftMatrix));
   				DBOperations.IncrementTestsNumber(currentUser);
               }
               
            }
        }); 
		
		TruthMatrix = TestInProgress.getTruthMatrix(A);
		//TestInProgress.printMatrix(TruthMatrix);
		DraftMatrix = TestInProgress.getDraftMatrix(A);
		//printMatrix(DrafMatrix);
		
		for(Question i:A)
			System.out.println(i);
		
		TestInProgress.SetIds();
		TestInProgress.setProprieties();
		TestInProgress.setFieldsBackgroundColor(DraftMatrix);
		
		
		int lastQuestionIndex = f.getNumberOfQuestions() - 1;
		
		TestInProgress.setButtonsStates(lastQuestionIndex);
		TestInProgress.setFieldsValues(A);
		
		firstQuestion.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
				
				counter = 0;
				TestInProgress.setFieldsBackgroundColor(DraftMatrix);
				TestInProgress.setFieldsValues(A);
				TestInProgress.setButtonsStates(lastQuestionIndex);
			}
		});
		
		prevQuestion.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
			
			counter--;
			TestInProgress.setFieldsValues(A);
			TestInProgress.setButtonsStates(lastQuestionIndex);
			TestInProgress.setFieldsBackgroundColor(DraftMatrix);
			}
		});
		
		nextQuestion.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
			
			counter++;
			TestInProgress.setFieldsValues(A);
			TestInProgress.setButtonsStates(lastQuestionIndex);
			TestInProgress.setFieldsBackgroundColor(DraftMatrix);
			}
		});
		
		lastQuestion.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
		
			counter = lastQuestionIndex;
			TestInProgress.setFieldsValues(A);
			TestInProgress.setButtonsStates(lastQuestionIndex);
			TestInProgress.setFieldsBackgroundColor(DraftMatrix);
			}
		});
		
		
		
		// Finish Test Button
		finishTest.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY) {
				if(f.getTimeLimit() !=0)
					timeline.stop();
				primaryStage.setScene((new TestResult()).openTestResult(primaryStage, windowWidth, windowHeight, currentUser, f, A, TruthMatrix, DraftMatrix));
				DBOperations.IncrementTestsNumber(currentUser);
				
				}
		});
		
		
		// Event pe campurile cu variante de raspuns
		S1aux.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
				if(DraftMatrix[counter][0] == false) {
					DraftMatrix[counter][0] = true;
					S1.setStyle("-fx-control-inner-background:#7FDCFF");
				}
				else {
					DraftMatrix[counter][0] = false;
					S1.setStyle("-fx-control-inner-background:white");
				}
			System.out.println("am intrat pe S1");
			}
		});
		
		S2aux.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
				if(DraftMatrix[counter][1] == false) {
					DraftMatrix[counter][1] = true;
					S2.setStyle("-fx-control-inner-background:#7FDCFF");
				}
				else {
					DraftMatrix[counter][1] = false;
					S2.setStyle("-fx-control-inner-background:white");
				}
			System.out.println("am intrat pe S2");
			}
		});
		
		S3aux.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
				if(DraftMatrix[counter][2] == false) {
					DraftMatrix[counter][2] = true;
					S3.setStyle("-fx-control-inner-background:#7FDCFF");
				}
				else {
					DraftMatrix[counter][2] = false;
					S3.setStyle("-fx-control-inner-background:white");
				}
			System.out.println("am intrat pe S3");
			}
		});
		
		S4aux.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
				if(DraftMatrix[counter][3] == false) {
					DraftMatrix[counter][3] = true;
					S4.setStyle("-fx-control-inner-background:#7FDCFF");
				}
				else {
					DraftMatrix[counter][3] = false;
					S4.setStyle("-fx-control-inner-background:white");
				}
			System.out.println("am intrat pe S4");
			}
		});
		
		S5aux.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
				if(DraftMatrix[counter][4] == false) {
					DraftMatrix[counter][4] = true;
					S5.setStyle("-fx-control-inner-background:#7FDCFF");
				}
				else {
					DraftMatrix[counter][4] = false;
					S5.setStyle("-fx-control-inner-background:white");
				}
			System.out.println("am intrat pe S5");
			}
		});
		
		a.getStylesheets().add(getClass().getResource("style_TestInProgress.css").toExternalForm());
		root.getChildren().addAll(questionNumber,questionBody);
		root.getChildren().addAll(S1,S2,S3,S4,S5);
		root.getChildren().addAll(nextQuestion,prevQuestion,firstQuestion,lastQuestion,finishTest);
		root.getChildren().addAll(timeLabel,timeLabelAux);
		root.getChildren().addAll(S1aux,S2aux,S3aux,S4aux,S5aux);
		
		return a;
	}
	
	public static boolean[][] getTruthMatrix(Question[] A){
		// In matricea astea se pun valorile de adevar ale intrebarilor 
		// TruthMatrix[i][j] = valoare de adevar a variantei de raspuns j, intrebarea i 
		// A - tabloul cu intrebari  
		int numberOfQuestions = A.length; 
		boolean TruthMatrix[][] = new boolean[numberOfQuestions][5];
		for(int i = 0 ; i < numberOfQuestions ; i++) {
			TruthMatrix[i][0] = A[i].isS1V();
			TruthMatrix[i][1] = A[i].isS2V(); 
			TruthMatrix[i][2] = A[i].isS3V(); 
			TruthMatrix[i][3] = A[i].isS4V(); 
			TruthMatrix[i][4] = A[i].isS5V(); 
		}
		return TruthMatrix;
	}
	
	public static boolean[][] getDraftMatrix(Question[] A) {
		// O matrice plina cu false initial, se va completa pe masura ce se raspunde la intrebari
		// la final se va compara cu TruthMatrix pentru a calcula puntacjul
		int numberOfQuestions = A.length;
		boolean M[][] = new boolean[numberOfQuestions][5];
		for(int i = 0; i < numberOfQuestions; i++)
			for(int j = 0; j < 5;j++)
				M[i][j] = false;
		return M;
	}
	
	public static void printMatrix(boolean[][] M) {
		for(int i=0;i<M.length;i++) {
			for(int j=0;j<5;j++)
				System.out.print(M[i][j] + " ");
			System.out.println();
		}
	}
	
	
}
