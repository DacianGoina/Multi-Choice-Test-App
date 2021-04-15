package application;

import java.text.DecimalFormat;

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
 * <p> Class for test's result
 */
public class TestResult {
	private Label header = new Label("Test finalizat!");
	private Label result = new Label();
	
	private Button backToUserMenu = new Button("Înapoi la meniul principal");
	private Button viewTest = new Button("Vizualizare test");
	
	public Scene openTestResult(Stage primaryStage, double windowWidth, double windowHeight, User currentUser, Test f, Question[] A, boolean TM[][], boolean DM[][]) {
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
		
		header.setId("header");
		result.setId("result");
		backToUserMenu.setId("backToUserMenu");
		viewTest.setId("viewTest");
		
		backToUserMenu.setFocusTraversable(false);
		viewTest.setFocusTraversable(false);
		
		double rez = 0;
		String evalType;
		
		if(f.getEvaluationType().equals("T")) {
			rez = TestResult.computeGradeTotal(TM, DM);
			evalType = "total";
		}
		else {
			rez = TestResult.computeGradeMixt(TM, DM);
			evalType = "partial";
		}
		
		DBOperations.updateTotalScore(currentUser, rez);
		
		String trunc;
		if(rez == (int) rez)
			trunc  = String.valueOf((int)rez) + " / " + f.getNumberOfQuestions();
		else
			trunc = new DecimalFormat("#.##").format(rez) + " / " + f.getNumberOfQuestions();
		
		System.out.println(f.getEvaluationType());
		result.setText("Punctaj obținut: " + trunc + "\n" + "\n" + "Informații test " + "\n"+
		"Număr de întrebari: " + f.getNumberOfQuestions() + "\n" + "Mod acordare punctaj: " + evalType);
		
		backToUserMenu.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene((new UserMenu()).openUserMenu(primaryStage,windowWidth, windowHeight,currentUser.getEmail()));

		});
		
		viewTest.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY) {
				primaryStage.setScene((new TestView()).View(primaryStage, windowWidth, windowHeight, currentUser, f, A, TM, DM));
			}
				
		});
		
		a.getStylesheets().add(getClass().getResource("style_TestResult.css").toExternalForm());
		root.getChildren().addAll(header,result,backToUserMenu,viewTest);
		return a;
	}
	
	// Compute grade - for TOTAL evaluation type
	public static double computeGradeTotal(boolean TM[][], boolean DM[][]) {
		double S = 0 ;
		for(int i = 0; i < TM.length; i++) {
			int ok = 1;
			for(int j = 0; j < 5 && ok == 1; j++)
				if(TM[i][j] != DM[i][j])
					ok = 0;
			if(ok == 1)
				S++;
		}
		return S;
	}
	
	// Compute grade - for MIXT evaluation type
	public static double computeGradeMixt(boolean TM[][], boolean DM[][]) {
		// se primeste punctaj partial - adica se primeste o parte din punctaj
		// chiar daca nu s-au bifat toate raspunsurile corecte dar s-au bifat unele
		// daca s-a bifat cel putin unul gresit se pune 0
		double S = 0;
		double nTruth; // numarul de raspunsuri corecte de pe linie
		double nSelectedTruth; // numarul de raspunsuri corecte selectate pe linie
		
		for(int i = 0; i < TM.length; i++) {
			nTruth = 0;
			nSelectedTruth = 0;
			
			for(int j = 0; j<5;j++) // nTruth
				if(TM[i][j])
					nTruth++;
			int ok = 1;
			
			for(int j = 0;j<5 && ok == 1;j++) { //nSelectedTruth
				if(TM[i][j] == true && DM[i][j] == true) 
					nSelectedTruth++;
				if(TM[i][j] == false && DM[i][j] == true)
					ok = 0;
				}
			
			if(ok == 1) // daca nu a pus niciuna gresita
				S = S + (nSelectedTruth/nTruth);
			
		}
		
		return S;
	}
}
