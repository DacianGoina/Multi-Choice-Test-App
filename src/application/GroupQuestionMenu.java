package application;


import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GroupQuestionMenu {
	private static Label header = new Label("Acțiuni pentru gestiunea întrebărilor");
	
	// Select a question and add it in a group
	private static Label chooseQuestion = new Label("Adăugare în grup - alege întrebare:");
	private static ComboBox<String> questionsMenu = new ComboBox<String>();
	private static Label chooseGroup = new Label("Alege grup:");
	private static ComboBox<String> groupMenu = new ComboBox<String>();
	private static Button addQuestionToGroup = new Button("Adaugă întrebarea selectată în grup");
	
	// Ungrouping - remove a question from group
	private static Label chooseGroupDown = new Label("Elimină din grup - alege grup:");
	private static ComboBox<String> groupMenuDown = new ComboBox<String>();
	private static Label chooseQuestionDown = new Label("Alege întrebare:");
	private static ComboBox<String> questionsMenuDown = new ComboBox<String>();
	private static Button removeQuestionFromGroup = new Button("Elimină întrebarea selectată din grup");
	
	// Creare or delete a group
	private static Label groupsLabel = new Label("Creare și ștergere grupuri");
	private static Label newGroupNameLabel = new Label("Nume grup:");
	private static TextField newGroupName = new TextField();
	private static Button createNewGroup = new Button("Crează grup");
	public static Label createNewGroupSituation = new Label();
	
	private static Label deleteGroupLabel = new Label("Alege grup:");
	private static ComboBox<String> groupMenuToDelete = new ComboBox<String>();
	private static Button deleteGroup = new Button("Ștergere grup");
	public static Label deleteGroupSituation = new Label();
	
	private static Button backToUserMenu  = new Button("Înapoi");
	
	// Horizontal separators
	private static Separator firstSeparator = new Separator(Orientation.HORIZONTAL);
	private static Separator secondSeparator = new Separator(Orientation.HORIZONTAL);

	// Label for add/remove question from group - situation - successful or fail
	public static Label addQuestionInfo = new Label("");
	public static Label removeQuestionInfo = new Label("");
	
	// Set IDs for GUI objects
	public static void setIds() {
		
		header.setId("header");
		chooseQuestion.setId("chooseQuestion");
		questionsMenu.setId("questionsMenu");
		chooseGroup.setId("chooseGroup");
		groupMenu.setId("groupMenu");
		addQuestionToGroup.setId("addQuestionToGroup");
		chooseGroupDown.setId("chooseGroupDown");
		groupMenuDown.setId("groupMenuDown");
		chooseQuestionDown.setId("chooseQuestionDown");
		questionsMenuDown.setId("questionsMenuDown");
		removeQuestionFromGroup.setId("removeQuestionFromGroup");
		newGroupNameLabel.setId("newGroupNameLabel");
		newGroupName.setId("newGroupName");
		createNewGroup.setId("createNewGroup");
		deleteGroupLabel.setId("deleteGroupLabel");
		groupMenuToDelete.setId("groupMenuToDelete");
		deleteGroup.setId("deleteGroup");
		backToUserMenu.setId("backToUserMenu");
		groupsLabel.setId("groupsLabel");
		
		createNewGroupSituation.setId("createNewGroupSituation");
		deleteGroupSituation.setId("deleteGroupSituation");
		
		firstSeparator.setId("firstSeparator");
		secondSeparator.setId("secondSeparator");
		
		addQuestionInfo.setId("addQuestionInfo");
		removeQuestionInfo.setId("removeQuestionInfo");
	}
	
	// Set proprieties for several GUI objects
	public static void setProprieties(ObservableList<String> domains) {
		
		questionsMenu.setItems(DBOperations.getAllQuestions());
		questionsMenu.getSelectionModel().select(0);
		groupMenu.setItems(domains);
		groupMenuDown.setItems(domains);
		groupMenuToDelete.setItems(domains);
		groupMenu.getSelectionModel().select(0);
		groupMenuDown.getSelectionModel().select(0);
		groupMenuToDelete.getSelectionModel().select(0);
		questionsMenuDown.setItems(DBOperations.getQuestionsFromGroup(groupMenuDown.getValue()));
		questionsMenuDown.getSelectionModel().select(0);
		
	}
	
	public Scene OpenEditMenu(Stage primaryStage, double windowWidth, double windowHeight, User currentUser) {
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
		
		setIds();
		setProprieties(DBOperations.getGroups());
		questionsMenu.setFocusTraversable(false);
		groupMenu.setFocusTraversable(false);
		addQuestionToGroup.setFocusTraversable(false);
		groupMenuDown.setFocusTraversable(false);
		questionsMenuDown.setFocusTraversable(false);
		removeQuestionFromGroup.setFocusTraversable(false);
		newGroupName.setFocusTraversable(false);
		createNewGroup.setFocusTraversable(false);
		groupMenuToDelete.setFocusTraversable(false);
		deleteGroup.setFocusTraversable(false);
		backToUserMenu.setFocusTraversable(false);
		
		addQuestionInfo.setText("");
		removeQuestionInfo.setText("");
		deleteGroupSituation.setText("");
		createNewGroupSituation.setText("");
		
		// Actualizeaza lista de intrebari din grup in functie de grupul selectat in ComboBox
		groupMenuDown.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) ->{
			System.out.println(newValue);
			questionsMenuDown.setItems(DBOperations.getQuestionsFromGroup(newValue));
			questionsMenuDown.getSelectionModel().select(0);
		});
		
		
		addQuestionToGroup.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY)
				if(groupMenu.getItems().size() > 0 && questionsMenu.getItems().size() > 0) { // doar daca exista ceva in campurile group, question
					DBOperations.addQuestionToGroup(groupMenu.getValue(), questionsMenu.getValue());
					System.out.println("Grup:" + groupMenu.getValue() + " intrebare: " + questionsMenu.getValue());
					setProprieties(DBOperations.getGroups());
				}
		});
		
		removeQuestionFromGroup.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY) {
				System.out.println("l: " + questionsMenuDown.getValue());
				if(groupMenuDown.getValue().length() > 0 && questionsMenuDown.getValue() != null) {
					DBOperations.removeQuestionFromGroup(groupMenuDown.getValue(), questionsMenuDown.getValue());
					setProprieties(DBOperations.getGroups());
				}
				else {
					GroupQuestionMenu.removeQuestionInfo.setTextFill(Color.RED);
					GroupQuestionMenu.removeQuestionInfo.setText("Întrebarea nu a fost eliminată din grup");
					
					GroupQuestionMenu.addQuestionInfo.setText("");
					GroupQuestionMenu.deleteGroupSituation.setText("");
					GroupQuestionMenu.createNewGroupSituation.setText("");
				}
			}
		});
		
		// Create new group
		createNewGroup.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY) {
				String val = newGroupName.getText();
				if(val.length() > 3 && val.length() <=30 && DBOperations.checkGroupName(val) && DBOperations.checkGroupsNumber()) {
					DBOperations.createNewGroup(val);
					createNewGroupSituation.setTextFill(Color.GREEN);
					createNewGroupSituation.setText("Grup adăugat!");
					setProprieties(DBOperations.getGroups()); 
					
				}
				else if(!DBOperations.checkGroupsNumber()) {
					createNewGroupSituation.setTextFill(Color.RED);
					createNewGroupSituation.setText("S-a atins numărul maxim de 12 grupuri");
				}
				else if(val.length() <=3 || val.length() > 30) {
					createNewGroupSituation.setTextFill(Color.RED);
					createNewGroupSituation.setText("Lungime invalidă pentru numele grupului");
				}
				
				else if(!DBOperations.checkGroupName(val)) {
					createNewGroupSituation.setTextFill(Color.RED);
					createNewGroupSituation.setText("Există deja un grup cu acest nume");
				}
				
				
				deleteGroupSituation.setText("");  // STERGEM RESTUL TEXTELOR DE PE ECRAN
				addQuestionInfo.setText("");
				removeQuestionInfo.setText("");
			}
		});
		
		// Delete a group
		deleteGroup.setOnMouseClicked(e ->{
			if(e.getButton() == MouseButton.PRIMARY) {
				String val = groupMenuToDelete.getValue();
				if(DBOperations.getGroups().size() > 0) {
				DBOperations.deleteGroup(val);
				setProprieties(DBOperations.getGroups());
				deleteGroupSituation.setText("Grup sters");
				deleteGroupSituation.setTextFill(Color.GREEN);
				createNewGroupSituation.setText(""); // STERGEM RESTUL TEXTELOR DE PE ECRAN
				addQuestionInfo.setText("");
				removeQuestionInfo.setText("");
				}
			}
		});
		
		// Back to user menu
		backToUserMenu.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY)
				primaryStage.setScene((new UserMenu()).openUserMenu(primaryStage,windowWidth, windowHeight,currentUser.getEmail()));
		});
		
		
		a.getStylesheets().add(getClass().getResource("style_GroupQuestionMenu.css").toExternalForm());
		root.getChildren().addAll(header);
		root.getChildren().addAll(chooseQuestion,questionsMenu);
		root.getChildren().addAll(chooseGroup,groupMenu,addQuestionToGroup);
		root.getChildren().addAll(chooseGroupDown,groupMenuDown,chooseQuestionDown,questionsMenuDown,removeQuestionFromGroup);
		root.getChildren().addAll(groupsLabel,newGroupNameLabel,newGroupName,createNewGroup);
		root.getChildren().addAll(deleteGroupLabel,groupMenuToDelete,deleteGroup);
		root.getChildren().addAll(backToUserMenu);
		root.getChildren().addAll(createNewGroupSituation,deleteGroupSituation);
		root.getChildren().addAll(firstSeparator,secondSeparator);
		root.getChildren().addAll(addQuestionInfo,removeQuestionInfo);
		
		return a;
	}
	
}
