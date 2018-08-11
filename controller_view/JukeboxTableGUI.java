package controller_view;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

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
import javafx.scene.control.ProgressBar;
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
 * Jukebox GUI that shows the users all songs available and the songs up next in
 * the queue.
 */
public class JukeboxTableGUI extends Application {

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
	private ProgressBar progress;
	private LocalTime localTime;
	private static SongViewer songViewer;
	private static SongQueue songQueue = new SongQueue();

	@Override
	public void start(Stage primaryStage) {
		BorderPane all = new BorderPane();
		users = new LinkedList<Student>();
		all.setStyle("-fx-background-color: white;" +
					"-fx-border-width: 5px;" +
					"-fx-padding: 5px;" +
					"-fx-border-color: blue;");
		message = new Label("Login");
		message.setStyle("-fx-font-family: arial;" +
				"-fx-font-size: 20pt;" + 
				"-fx-font-style: oblique;" +  
				"-fx-font-weight: bolder;" );

		// Create the main jukebox labels.
		Label ss = new Label("     Song Selection");
		ss.setStyle("-fx-font-family: arial;" +
				"-fx-font-size: 20pt;" + 
				"-fx-font-style: oblique;" + 
				"-fx-background-color: white;" + 
				"-fx-font-weight: bolder;" );
		ss.setAlignment(Pos.TOP_LEFT);

		Label sq = new Label("Queue           ");
		sq.setStyle("-fx-font-family: arial;" +
				"-fx-font-size: 20pt;" + 
				"-fx-font-style: oblique;" + 
				"-fx-background-color: white;" + 
				"-fx-font-weight: bolder;" );
		sq.setAlignment(Pos.TOP_RIGHT);

		// Initialize the queue
		songViewer = new SongViewer(jukeBox);
		initializeList();
		
		// Add the progress bar
		progress = new ProgressBar(0);
		progress.setMaxWidth(840);
		all.setCenter(progress);

		// Initialize the play button.
		Button play = new Button("►");
		play.setStyle("-fx-font-size: 10pt; " +
					"-fx-font-family: arial; " +
					"-fx-text-fill: white; -fx-color: #ff6347;");
		play.setOnAction((event) -> {
			Song song = songViewer.getSelectionModel().getSelectedItem();
			if (!isLoggedIn()) {
				message.setText("Login first please");
				return;
			}
			if (jukeBox.canPlay(song)) {
				jukeBox.playSong(song);
				message.setText(currentUser.timeAsString());
				songQueue.enqueue(song);
				songViewer.refresh();
				updateBar();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("Unable to play song.");
				alert.setContentText("Maximum limit reached.");
				alert.showAndWait();
			}
		});

		// Add the labels and song playing elements to the pane.
		HBox labels = new HBox(265, ss, sq);
		HBox selections = new HBox(20, songViewer, play, songQueue);
		VBox juke = new VBox(10, labels, selections);
		labels.setPadding(new Insets(10, 0, 0, 0));
		selections.setPadding(new Insets(5, 20, 10, 20));
		juke.setStyle("-fx-background-color: white;" +
				"-fx-border-width: 2px;" +
				"-fx-border-color: tomato;");
		all.setTop(juke);
		
		// Set up the input fields on a grid pane
		inputFields = new GridPane();
		inputFields.setHgap(5);
		inputFields.setVgap(5);

		// Set the labels.
		message = new Label("Login first");
		Label usernameLabel = new Label("Account Name");
		Label passwordLabel = new Label("Password");

		// Set the textfields.
		usernameTextField = new TextField();
		passwordTextField = new PasswordField();
		inputFields.add(usernameLabel, 1, 1);
		inputFields.add(usernameTextField, 2, 1);
		inputFields.add(passwordLabel, 1, 2);
		inputFields.add(passwordTextField, 2, 2);
		inputFields.setAlignment(Pos.CENTER);

		// Create the login button.
		Button loginButton = new Button("Login");
		loginButton.setStyle("-fx-font-size: 10pt; " +
				"-fx-font-family: arial; " +
				"-fx-text-fill: white; -fx-color: #f198f1;");
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

		// Create the logout button.
		Button logoutButton = new Button("Log out");
		logoutButton.setStyle("-fx-font-size: 10pt; " +
				"-fx-font-family: arial; " +
				"-fx-text-fill: white; -fx-color: #f4aff4;");
		logoutButton.setOnAction(event -> {
			usernameTextField.clear();
			currentUsername = "";
			passwordTextField.clear();
			currentPassword = "";
			currentUser = null;
			message.setText("Login");
		});

		// Organize input fields + buttons.
		inputFields.setAlignment(Pos.CENTER_LEFT);
		loginButton.setAlignment(Pos.CENTER);
		logoutButton.setAlignment(Pos.CENTER_RIGHT);

		// Create an HBox for the login fields and a VBox for the login message
		// and the HBox.
		HBox loginBox = new HBox(inputFields, loginButton, logoutButton);
		VBox userBox = new VBox(10, message, loginBox);
		userBox.setStyle("-fx-background-color: white;" +
				"-fx-border-width: 2px;" +
				"-fx-border-color: violet;");
		loginBox.setAlignment(Pos.CENTER);
		userBox.setAlignment(Pos.CENTER);
		userBox.setPadding(new Insets(20, 20, 20, 20));
		all.setBottom(userBox);

		primaryStage.setOnCloseRequest(event -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Shut Down Option");
			alert.setHeaderText("Save data?");
			alert.setContentText("Press cancel while system testing.");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {
				try {
					FileOutputStream fos = new FileOutputStream("jukebox_data");
					ObjectOutputStream outFile = new ObjectOutputStream(fos);
					outFile.writeObject(jukeBox);
					outFile.close();

					fos = new FileOutputStream("songSelection_data");
					outFile = new ObjectOutputStream(fos);
					outFile.writeObject(jukeBox.getSongSelections());
					outFile.close();

					fos = new FileOutputStream("songQueue_data");
					outFile = new ObjectOutputStream(fos);
					outFile.writeObject(jukeBox.getPlaylist());
					outFile.close();

				} catch (IOException e1) {
					System.out.println("Save failed");
					e1.printStackTrace();
				}
			}
		});

		Scene scene = new Scene(all, 850, 650);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Updates the progress of the song playing in the ProgressBar.
	 */
	private void updateBar() {
		
		// Always start at 0
		progress.setProgress(0);
	
		// Get the song length.
		localTime = LocalTime.now();
		int time = 0;
		LocalTime endTime = localTime.plusSeconds(jukeBox.getSongtime());
	}

	/**
	 * Prompts user if they would like to load a saved data, if not it will
	 * create a new Jukebox
	 */
	@SuppressWarnings("unchecked")
	private void initializeList() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Start up Option");
		alert.setHeaderText("Read saved data?");
		alert.setContentText("Press cancel while system testing.");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			try {
				FileInputStream fis = new FileInputStream("jukebox_data");
				ObjectInputStream input = new ObjectInputStream(fis);
				jukeBox = (Jukebox) input.readObject();
				input.close();
				fis.close();

				fis = new FileInputStream("songSelection_data");
				input = new ObjectInputStream(fis);
				jukeBox.setSongSelections((List<Song>) input.readObject());
				input.close();
				fis.close();

				fis = new FileInputStream("songQueue_data");
				input = new ObjectInputStream(fis);
				jukeBox.setPlaylist((Queue<Song>) input.readObject());
				input.close();
				fis.close();

				// Load the saved queue
				songQueue.setList(jukeBox.getPlaylist());
				jukeBox.playPlaylist();

				// Load the saved status of the song selection
				songViewer = new SongViewer(jukeBox);
				songViewer.refresh();
			} catch (Exception i) {
				jukeBox = new Jukebox();
				songViewer = new SongViewer(jukeBox);
				System.out.println("Error loading file.");
			}
		} else {
			jukeBox = new Jukebox();
			songViewer = new SongViewer(jukeBox);
		}
	}

	/**
	 * Determines if a user is logged in by checking the value of the Strings
	 * currentUsername and currentPassword. Considered not logged in if the
	 * values are empty.
	 * 
	 * @return true, if logged in
	 * @return false, otherwise
	 */
	private boolean isLoggedIn() {
		return !(currentUsername.equals("") && currentPassword.equals(""));
	}

	/**
	 * Determines if an administrator is currently logged in.
	 * 
	 * @return true if logged in
	 * @return false otherwise
	 */
	private boolean adminLoggedIn() {
		return currentUser.isAdmin();
	}

	/**
	 * Note: This code snippet is a modified version of the Custom Login Dialog
	 * example found at: http://code.makery.ch/blog/javafx-dialogs-official/.
	 * Modifications by Rick Mercer.
	 * 
	 * Rick is providing this to use "as is" for your Jukebox project and long
	 * as you in the above attribution.
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

		// Convert the result to a username-password-pair when the
		// Add user button is clicked.
		// This is lambda instead of an instance of a new event handler: shorter
		// code.
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