package controller_view;
import model.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * This class creates a ListView specializing in displaying Songs
 * that are currently in queue to play on the Jukebox.
 * 
 * @author Michelle Monteith and Jamie David
 * @class  CSC 335 Spring 18
 */
public class SongQueue extends ListView<String> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ObservableList<String> observable;
	List<String> songList;
	
	public SongQueue() {
		songList = new ArrayList<String>();
		observable = FXCollections.observableList(songList);
		this.setItems(observable);
		this.setMaxWidth(500);
		this.setMaxHeight(600);
	}
	
	/**
	 * Adds the song to the listView
	 * @param s Song to add
	 */
	public void enqueue(Song s) {
		observable.add(s.toString());
	}
	
	/**
	 * Clears the current list.
	 */
	public void clear() {
		observable.clear();
	}
	
	/**
	 * Populate the list with the items in the given queue.
	 * @param queue song queue.
	 */
	public void setList(Queue<Song> queue) {
		clear();
		for(Song song : queue) {
			observable.add(song.toString());
		}
	}
	
	/**
	 * Removes the song from the top of the queue after playing.
	 */
	public static void dequeue() {
		if(!observable.isEmpty()) {
			observable.remove(0);
		}
	}
}
