package controller_view;

import model.Jukebox;
import model.Song;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.Serializable;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * This class creates a table for each song in the jukebox.
 * 
 * @author Michelle Monteith and Jamie David
 * @class  CSC 335 Spring 18
 */
public class SongViewer extends TableView<Song> implements Serializable {

	private static final long serialVersionUID = 1L;
	Jukebox jukebox;

	@SuppressWarnings("unchecked")
	public SongViewer(Jukebox jukebox) {
		
		this.jukebox = jukebox;
		this.setStyle("-fx-font-size: 11pt;" +
				"-fx-font-family: arial; " +
				"-fx-background-color: white;" +
				"-fx-control-inner-background: white;" +
				"-fx-table-header-border-color: transparent;" +
				"-fx-table-cell-border-color: transparent;");
		
		// Create all the columns.
		TableColumn<Song, Integer> playsColumn = new TableColumn<>("Plays");
		TableColumn<Song, String> titleColumn = new TableColumn<>("Title");
		TableColumn<Song, String> artistColumn = new TableColumn<>("Artist");
		TableColumn<Song, String> timeColumn = new TableColumn<>("Time");

		// Get all the instance variables.
		playsColumn.setCellValueFactory(new PropertyValueFactory<Song, Integer>("timesPlayed"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));
		artistColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("time"));
		
		// Add the columns to the TableView
		this.getColumns().addAll(playsColumn, titleColumn, artistColumn, timeColumn);
	
		// Set up widths
		playsColumn.setPrefWidth(60);
		titleColumn.setPrefWidth(150);
		artistColumn.setPrefWidth(150);
		timeColumn.setPrefWidth(100);
		this.setMaxWidth(460);
		this.setMaxHeight(600);
		
		// Set up the model
		setObserverListToViewCurrentSongSelection();	
	}

	/**
	 * Creates an ObservableList to wrap the collection and populates the list 
	 * to show in the TableView.
	 */
	private void setObserverListToViewCurrentSongSelection() {
		List<Song> songSelect = jukebox.getSongSelections();
		ObservableList<Song> songs = FXCollections.observableArrayList();

		// Populate the observable list so all show in the TableView in the GUI
		for (int i = 0; i < songSelect.size(); i++) {
			songs.add(songSelect.get(i));
		}
		this.setItems(songs);
		this.getSelectionModel().select(0);
	}
}
