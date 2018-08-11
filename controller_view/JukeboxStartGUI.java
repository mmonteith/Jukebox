package controller_view;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Jukebox;
import model.Song;
import model.Student;

/**
 * This program is a functional spike to determine the interactions are 
 * actually working. It is an event-driven program with a graphical user
 * interface to affirm the functionality all Iteration 1 tasks have been 
 * completed and are working correctly. This program will be used to 
 * test your code for the first 100 points of the JukeBox project.
 */
public class JukeboxStartGUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private Student currentUser;
	private Jukebox jukeBox = new Jukebox();
	private Label message;
	private GridPane inputFields;
	private TextField usernameTextField;
	private PasswordField passwordTextField;
	private String currentUsername = "";
	private String currentPassword = "";
	private List<Student> users;

	@Override
	public void start(Stage primaryStage) {
		BorderPane all = new BorderPane();
		users = new LinkedList<Student>();
		
		// Set the initial label
		message = new Label("Login");

		// Initialize the two buttons that play the songs
		Button song1 = new Button("Select song 1");
		Button song2 = new Button("Select song 2");

		// Button 1 (song1) should play "Capture"
		song1.setOnAction((event) -> {
			Song song = jukeBox.getSong(0);
			if (!isLoggedIn()) {
				message.setText("Login first please");
				return;
			}

			if (jukeBox.canPlay(song)) {
				jukeBox.playSong(song);
				message.setText(currentUser.timeAsString());
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("Unable to play song.");
				alert.setContentText("Maximum limit reached.");
				alert.showAndWait();
			}
		});

		// Button 2 (song2) should play "Loping String"
		song2.setOnAction((event) -> {
			Song song = jukeBox.getSong(1);
			if (!isLoggedIn()) {
				message.setText("Login first please");
				return;
			}

			if (jukeBox.canPlay(song)) {
				jukeBox.playSong(song);
				message.setText(currentUser.timeAsString());
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("Unable to play song.");
				alert.setContentText("Maximum limit reached.");
				alert.showAndWait();
			}
		});

		// HBox to hold the music buttons
		HBox hbox = new HBox(50, song1, song2);
		hbox.setAlignment(Pos.CENTER);
		all.setTop(hbox);

		// Set up the input fields on a grid pane
		inputFields = new GridPane();
		inputFields.setHgap(5);
		inputFields.setVgap(5);
		Label usernameLabel = new Label("Account Name");
		Label passwordLabel = new Label("Password");
		usernameTextField = new TextField();
		passwordTextField = new PasswordField();
		inputFields.add(usernameLabel, 1, 1);
		inputFields.add(usernameTextField, 2, 1);
		inputFields.add(passwordLabel, 1, 2);
		inputFields.add(passwordTextField, 2, 2);
		inputFields.setAlignment(Pos.CENTER);

		Button loginButton = new Button("Login");
		loginButton.setOnAction(event -> {
			currentUsername = usernameTextField.getText();
			currentPassword = passwordTextField.getText();
			if (!jukeBox.loginUser(currentUsername, currentPassword)) {
				message.setText("Invalid Credentials.");
				usernameTextField.clear();
				currentUsername = "";
				passwordTextField.clear();
				currentPassword = "";
			} else {
				currentUser = jukeBox.getCurrentUser();
				message.setText(currentUser.timeAsString());

				if (adminLoggedIn()) {
					addNewUser();
				}
			}
		});

		// Organize all the login components on a vertical box
		VBox loginBox = new VBox(20, inputFields, loginButton, message);
		loginBox.setAlignment(Pos.CENTER);
		all.setCenter(loginBox);

		// Place the logout button at the bottom of the pane
		Button logoutButton = new Button("Log out");
		logoutButton.setOnAction(event -> {
			usernameTextField.clear();
			currentUsername = "";
			passwordTextField.clear();
			currentPassword = "";
			currentUser = null;
			message.setText("Login");
		});
		HBox logoutBox = new HBox(logoutButton);
		logoutBox.setAlignment(Pos.CENTER);
		all.setBottom(logoutBox);

		Scene scene = new Scene(all, 400, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Determines if a user is logged in by checking the value of the Strings
	 * currentUsername and currentPassword. Considered not logged in if the values
	 * are empty.
	 */
	private boolean isLoggedIn() {
		return !(currentUsername.equals("") && currentPassword.equals(""));
	}

	/**
	 * Determines if an administrator is currently logged in.
	 * @return true if logged in
	 */
	private boolean adminLoggedIn() {
		return currentUser.isAdmin();
	}

	/**
	 * Note: This code snippet is a modified version of the Custom Login Dialog
	 * example found at: http://code.makery.ch/blog/javafx-dialogs-official/.
	 * Modifications by Rick Mercer.
	 * 
	 * Rick is providing this to use "as is" for your Jukebox project and long as
	 * you in the above attribution.
	 */
	private void addNewUser() {
		
		// Create a custom dialog with two input fields
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Adding new user");
		dialog.setHeaderText("Enter the new user ID and password");

		// Set the button types
		ButtonType loginButtonType = new ButtonType("Add new user", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the Account Name and password labels and fields
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Account Name");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Account Name:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		dialog.getDialogPane().setContent(grid);

		// Convert the result to a username-password-pair when the Add user button is
		// clicked.
		// This is lambda instead of an instance of a new event handler: shorter code.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(username.getText(), password.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();
		result.ifPresent(usernamePassword -> {
			System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
			
			Student s = new Student(usernamePassword.getKey(), usernamePassword.getValue(), jukeBox);
			users.add(s);
			jukeBox.addStudent(s);
		});
	}
}