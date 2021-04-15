package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 * 
 * @author Dacian
 * <p> Class for StartTestMenu page
 */

public class StartTestMenu {
	// First panel - for questions from all categories
	// Main Headers
	private Label panelOneHeader = new Label("Pornește test");
	
	private Label proprieties = new Label("Proprietăți:");
	
	//---------------------
	// Time limit information
	private Label timeLimit = new Label("Limită de timp:");
	private static RadioButton noTimeLimit = new RadioButton("fără limită de timp");
	private static RadioButton withTimeLimit = new RadioButton("cu limită de timp - minute: ");
	private static Spinner<Integer> numberOfMinutes = new Spinner<>(5,180,15);
	private ToggleGroup timeLimitGroup = new ToggleGroup();
	
	// -------------------------------------------
	//Number of questions
	private Label numberOfQuestionsLabel = new Label("Număr de întrebări:");
	private static Spinner<Integer> numberOfQuestions = new Spinner<Integer>(3,180,10);
	
	//--------------------------
	// Questions' areas(domains)
	private Label questionsDomains = new Label("Domenii întrebări:");
	private RadioButton allDomains = new RadioButton("toate domeniile");
	private RadioButton specifiedDomains = new RadioButton("alege domenii");
	private ToggleGroup domainsGroup = new ToggleGroup();
	
	//---------------------------
	// Evaluation type
	private Label evaluationType = new Label("Mod acordare punctaj:");
	private static RadioButton totalMode = new RadioButton("total");
	private static RadioButton mixtMode = new RadioButton("parțial");
	private ToggleGroup evaluationGroup = new ToggleGroup();
	
	//-----------------
	// Back to user menu button
	private Button backToUserMenu = new Button("Înapoi");
	// Next page (panel) button
	private Button goToNextPanel = new Button("Continuă >");
	
	private Label tooFewQuestions = new Label("");
	
	// Tooltip for total and mixt mode - compute grade
	public Tooltip tipTotal = new Tooltip("Se acordă punctaj pe întrebare doar dacă se selectează toate variantele corecte");
	public Tooltip tipMixt = new Tooltip("Se acordă punctaj parțial în funcție de numărul de variante corecte selectate."
			+ "\n" + "Dacă se selectează o variantă greșită nu se acordă punctaj pe întrebare");
	
	
	// First Panel
	public Scene openPanelOne(Stage primaryStage, double windowWidth, double windowHeight, User currentUser) {
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
			input = new FileInputStream("./start_test_icon.png");
			Image image = new Image(input);
		    ImageView imageView = new ImageView(image);
		    imageView.setStyle("-fx-translate-x:280px; -fx-translate-y:-180px");
		    root.getChildren().add(imageView);
		} catch (FileNotFoundException e1) {
			System.out.println("Nu am gasit imaginea!");
			e1.printStackTrace();
		}
		
		totalMode.setTooltip(tipTotal);
		mixtMode.setTooltip(tipMixt);
		
		
		panelOneHeader.setId("panelOneHeader");
		proprieties.setId("proprieties");
		timeLimit.setId("timeLimit");
		noTimeLimit.setId("noTimeLimit");
		withTimeLimit.setId("withTimeLimit");
		numberOfMinutes.setId("numberOfMinutes");
		numberOfQuestionsLabel.setId("numberOfQuestionsLabel");
		numberOfQuestions.setId("numberOfQuestions");
		questionsDomains.setId("questionsDomains");
		allDomains.setId("allDomains");
		specifiedDomains.setId("specifiedDomains");
		evaluationType.setId("evaluationType");
		totalMode.setId("totalMode");
		mixtMode.setId("mixtMode");
		backToUserMenu.setId("backToUserMenu");
		goToNextPanel.setId("goToNextPanel");
		tooFewQuestions.setId("tooFewQuestions");
		
		// Toggle Group for time limit
		noTimeLimit.setToggleGroup(timeLimitGroup);
		withTimeLimit.setToggleGroup(timeLimitGroup);
		timeLimitGroup.selectToggle(noTimeLimit);
		
		// Toggle Group for domains
		allDomains.setToggleGroup(domainsGroup);
		specifiedDomains.setToggleGroup(domainsGroup);
		domainsGroup.selectToggle(allDomains);
		
		// Evaluation type group
		totalMode.setToggleGroup(evaluationGroup);
		mixtMode.setToggleGroup(evaluationGroup);
		evaluationGroup.selectToggle(totalMode);
		
		//Spinner Value Factory
		numberOfMinutes.setEditable(true);
		numberOfQuestions.setEditable(true);
		
		// Disable Focus Traversable
		backToUserMenu.setFocusTraversable(false);
		goToNextPanel.setFocusTraversable(false);
		noTimeLimit.setFocusTraversable(false);
		withTimeLimit.setFocusTraversable(false);
		numberOfMinutes.setFocusTraversable(false);
		numberOfQuestions.setFocusTraversable(false);
		allDomains.setFocusTraversable(false);
		specifiedDomains.setFocusTraversable(false);
		totalMode.setFocusTraversable(false);
		mixtMode.setFocusTraversable(false);
		
		
		// Disable and undisable numberOfMinutes Spinner
		numberOfMinutes.setDisable(true);
		
		noTimeLimit.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY)
				numberOfMinutes.setDisable(true);
		});
		
		withTimeLimit.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY)
				numberOfMinutes.setDisable(false);
		});
		
		goToNextPanel.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY) 
				if(StartTestMenu.validateNumberOfQuestions(numberOfQuestions.getValue().toString()) && 
						StartTestMenu.validateTimeLimit(numberOfMinutes.getValue().toString()) ) {
					System.out.println("nr intrebari: " + numberOfQuestions.getValue() + " minute " + numberOfMinutes.getValue());
					if(allDomains.isSelected()) {
						if(DBOperations.getNumberOfQuestions(StartTestMenu.getPanelOneProprieties().getNumberOfQuestions())) {
							Question A[] = DBOperations.getQuestions(StartTestMenu.getPanelOneProprieties());
							primaryStage.setScene((new TestInProgress()).Launch(primaryStage,windowWidth, windowHeight,currentUser,StartTestMenu.getPanelOneProprieties(),A));
						}
						else {
							tooFewQuestions.setText("Nu există sufiente întrebări - micșorează numărul");
							tooFewQuestions.setTextFill(Color.RED);
						}
					}
					
					else {
						// specifiedDomains is selected
						primaryStage.setScene(new StartTestMenu().openPanelTwo(primaryStage, windowWidth, windowHeight, currentUser, 
								StartTestMenu.getPanelOneProprieties()));
					}
				}
				else
					System.out.println("Nu ai introdus integer in Spinners");
		});
		
		backToUserMenu.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene((new UserMenu()).openUserMenu(primaryStage,windowWidth, windowHeight,currentUser.getEmail()));
		});
		
		a.getStylesheets().add(getClass().getResource("style_StartTestMenuPanelOne.css").toExternalForm());
		root.getChildren().addAll(panelOneHeader,proprieties,tooFewQuestions);
		root.getChildren().addAll(timeLimit,noTimeLimit,withTimeLimit,numberOfMinutes);
		root.getChildren().addAll(numberOfQuestionsLabel,numberOfQuestions);
		root.getChildren().addAll(questionsDomains,allDomains,specifiedDomains);
		root.getChildren().addAll(evaluationType,totalMode,mixtMode);
		root.getChildren().addAll(backToUserMenu,goToNextPanel);
		
		
		return a;
	}
	
	
	private Label panelTwoHeader = new Label("Selecteza cel puțin un domeniu, maxim 3");
	private Button startTestPanelTwo = new Button("Începe testul");
	private Label panelTwoState = new Label("");
	private Button backToPanelOne = new Button("Înapoi");
	
	public int seconds = 0;
	public int minutes = 0;
	public int hours = 0;

	
	// Second Panel - when specifiedDomains is selected
	public Scene openPanelTwo(Stage primaryStage, double windowWidth, double windowHeight, User currentUser, Test f) {
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
		
		Group BtnsGroup = new Group();
		
		panelTwoHeader.setId("panelTwoHeader");
		panelTwoState.setId("panelTwoState");
		startTestPanelTwo.setId("startTestPanelTwo");
		backToPanelOne.setId("backToPanelOne");
		BtnsGroup.setId("BtnsGroup");
		
		startTestPanelTwo.setFocusTraversable(false);
		backToPanelOne.setFocusTraversable(false);
		
		List<String> L = DBOperations.getGroupsAsString();
		List<RadioButton> Btns = new LinkedList<>();
		int nr = 0;
		
		for(String i:L) {
			Btns.add(new RadioButton(i));
			root.getChildren().add(Btns.get(nr));
			nr++;
		}
		
		nr = -210;
		for(RadioButton i:Btns) {
			BtnsGroup.getChildren().add(i);
			i.setStyle("-fx-font-size:20px; -fx-translate-y:" + nr +"px");
			i.setFocusTraversable(false);
			nr = nr + 30;
		}
		
		startTestPanelTwo.setOnMouseClicked(e->{
			int selected = 0;
			for(RadioButton i:Btns) {
				if(i.isSelected())
					selected++;
			}
			if(selected < 1 || selected > 3) {
				panelTwoState.setTextFill(Color.RED);
				panelTwoState.setText("Trebuie selectat cel puțin un domeniu și maxim 3");
			}
			else {
				List<String> domains = new LinkedList<>();
				for(RadioButton i:Btns)
					if(i.isSelected())
						domains.add(i.getText());
				Question A[] = DBOperations.getQuestionsFromGroups(domains, f.getNumberOfQuestions());
				if(A == null) {
					// daca A are valoarea null atunci nu sunt suficiente intrebari in grupurile selectat
					panelTwoState.setTextFill(Color.RED);
					panelTwoState.setText("Domeniile selectate nu conțin suficiente întrebări");
				}
				else
					primaryStage.setScene((new TestInProgress()).Launch(primaryStage,windowWidth, windowHeight,currentUser,f,A));
			}
					
		});
		
		backToPanelOne.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene((new StartTestMenu()).openPanelOne(primaryStage,windowWidth, windowHeight,currentUser));
		});
		
		
		root.getChildren().addAll(panelTwoHeader,startTestPanelTwo,panelTwoState,backToPanelOne);
		root.getChildren().add(BtnsGroup);
		a.getStylesheets().add(getClass().getResource("style_StartTestMenuPanelTwo.css").toExternalForm());
		return a;
	}
	
	public static String whiteIndentation(String a) {
		String rez = a;
		for(int i = 0 ;i < 30 - a.length();i++)
			a = a + " ";
		return rez;
	}
	

	// Proprietati despre panel One: timpul, mod punctaj etc, returneaza un obiect de tip Test
	// Proprieties from Panel One: time limit, number of questions etc 
	public static Test getPanelOneProprieties() {
		int time;
		int questionsNumber;
		String evalType = "";
		
		// get time limit
		if(StartTestMenu.noTimeLimit.isSelected())
			time = 0;
		else {
			time = StartTestMenu.numberOfMinutes.getValue();
		}
		
		// get number of questions
		questionsNumber = StartTestMenu.numberOfQuestions.getValue();
		
		// get evaluation type
		if(StartTestMenu.totalMode.isSelected())
			evalType = "T";
		if(StartTestMenu.mixtMode.isSelected())
			evalType="M";
		
		return (new Test(time,questionsNumber,evalType));
	}
	
	
	// Validate number of questions - value from Integer Spinner
	public static boolean validateNumberOfQuestions(String S) {
		// se trimit argument ca numberOfQuestions.getValue().toString();
		
		for(int i = 0;i<S.length();i++)
			if(S.charAt(i) != '0' && S.charAt(i) != '1' && S.charAt(i) != '2' && S.charAt(i) != '3' && S.charAt(i) != '4' &&
					S.charAt(i) != '5' && S.charAt(i) != '6' && S.charAt(i) != '7' && S.charAt(i) != '8' && S.charAt(i) != '9')
				return false;
		
		int a = Integer.parseInt(S);
		
		if(a >=3 && a <= 180)
			return true;
		
		return false;
	}

	// Validate number of minutes - value from Integer Spinner
	public static boolean validateTimeLimit(String S) {
		
		for(int i = 0;i<S.length();i++)
			if(S.charAt(i) != '0' && S.charAt(i) != '1' && S.charAt(i) != '2' && S.charAt(i) != '3' && S.charAt(i) != '4' &&
					S.charAt(i) != '5' && S.charAt(i) != '6' && S.charAt(i) != '7' && S.charAt(i) != '8' && S.charAt(i) != '9')
				return false;
		
		int a = Integer.parseInt(S);
		
		if(a >=5 && a <= 180)
			return true;
		
		return false;
	}
	
}
