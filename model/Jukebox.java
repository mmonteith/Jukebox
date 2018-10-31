package model;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import view.SongQueue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * A Jukebox keeps track of a song selection, all of its users, and what day it
 * is. Plays songs as requested by a Student and sends/recieves messages with a
 * Day object to ensure the song is allowed to be played.
 * 
 * @author Michelle Monteith and Jamie David
 * @class CSC 335 Spring 18
 */
public class Jukebox implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Song> songSelection;
	private List<Student> users;
	private Queue<Song> playlist;
	private Student currentUser;
	private static boolean playingASong;

	public Jukebox() {
		this.songSelection = new ArrayList<Song>();
		this.users = new ArrayList<Student>();
		this.playlist = new LinkedList<Song>();
		initializeUserList();
		initializeSongSelection();
	}

	/**
	 * Adds pre-determined users to the List of Students.
	 */
	private void initializeUserList() {
		this.users.add(new Student("Chris", "1", this));
		this.users.add(new Student("Devon", "22", this));
		this.users.add(new Student("River", "333", this));
		this.users.add(new Student("Ryan", "4444", this));
		this.users.add(new Administrator("Merlin", "7777777", this));
	}

	/**
	 * Adds the songs available from the 'songfiles' folder to the List of Song.
	 */
	private void initializeSongSelection() {
		this.songSelection.add(new Song("Pokemon Capture", 5, "Pikachu", "./songfiles/Capture.mp3"));
		this.songSelection
				.add(new Song("Danse Macabre", 34, "Kevin MacLeod", "./songfiles/DanseMacabreViolinHook.mp3"));
		this.songSelection.add(new Song("Determined Tumbao", 20, "FreePlay Music", "./songfiles/DeterminedTumbao.mp3"));
		this.songSelection.add(new Song("Loping Sting", 5, "Kevin MacLeod", "./songfiles/LopingSting.mp3"));
		this.songSelection.add(new Song("Swing Cheese", 15, "FreePlay Music", "./songfiles/SwingCheese.mp3"));
		this.songSelection.add(new Song("The Curtain Rises", 28, "Kevin MacLeod", "./songfiles/TheCurtainRises.mp3"));
		this.songSelection.add(new Song("Untameable Fire", 282, "Pierre Langer", "./songfiles/UntameableFire.mp3"));
	}

	/**
	 * Retrieves a Song from the available song list.
	 * 
	 * @param int songNumber, index of song
	 * @return song at songNumber
	 */
	public Song getSong(int songNumber) {
		return this.songSelection.get(songNumber);
	}

	/**
	 * Getter for songSelection.
	 * @return songSelection
	 */
	public List<Song> getSongSelections() {
		return this.songSelection;
	}

	/**
	 * Setter for songSelection.
	 */
	public void setSongSelections(List<Song> songSelection) {
		this.songSelection = songSelection;
	}

	/**
	 * Getter for playlist.
	 * @return playlist
	 */
	public Queue<Song> getPlaylist() {
		return this.playlist;
	}

	/**
	 * Setter for playlist.
	 */
	public void setPlaylist(Queue<Song> playlist) {
		this.playlist = playlist;
	}

	/**
	 * Getter for currentUser.
	 * @return currentuser
	 */
	public Student getCurrentUser() {
		return this.currentUser;
	}

	/**
	 * Setter for currentUser. (testing only)
	 */
	public void setCurrentUser(Student s) {
		this.currentUser = s;
	}

	/**
	 * Getter for userlist
	 * @return list of all users
	 */
	public List<Student> getAllUsers() {
		return this.users;
	}

	/**
	 * Adds the student to the list of available users.
	 */
	public void addStudent(Student student) {
		this.users.add(student);
	}

	/**
	 * Validates the given username and password. Will loop through the list of
	 * available users and check if there exists user with matching username and
	 * password.
	 * 
	 * @param username, must match a student in the userList
	 * @param password, must match a valid student's password
	 * @return true, if successful login
	 * @return false otherwise
	 */
	public boolean loginUser(String username, String password) {
		for (int i = 0; i < this.users.size(); i++) {
			if (username.equals(this.users.get(i).getUsername()) && password.equals(this.users.get(i).getPassword())) {
				this.currentUser = this.users.get(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * Validates that the currentUser can play the given Song and has not exceeded
	 * their minutes.
	 * 
	 * @param song, song to play
	 * @return true if the song is able to play
	 * @return false otherwise
	 */
	public boolean canPlay(Song song) {
		if (this.currentUser.ableToPlay() && song.playable()) {
			if (this.currentUser.getSecondsAvailable() >= song.getLength())
				return true;
		}
		return false;
	}

	/**
	 * Adds a song to the queue and plays the queue. Also adjusts the current user's
	 * available minutes.
	 * 
	 * @param song, song to play
	 */
	public void playSong(Song song) {
		this.currentUser.playSong();
		this.currentUser.adjustTime(song);
		this.playlist.add(song);
		for (Song temp : songSelection) {
			if (temp.getTitle().equals(song.getTitle())) {
				temp.playOnce();
			}
		}
		if (!playingASong) {
			this.playPlaylist();
		}
	}

	/**
	 * Plays songs from the queue in FIFO order if a song is not currently playing.
	 */
	public void playPlaylist() {
		if (!playingASong) {
			playFile(this.playlist.peek().getFileName());
			playingASong = true;
		}
		this.playlist.poll();
	}

	/**
	 * Plays an mp3 file.
	 * @param path, string path of the song to be played.
	 */
	private void playFile(String path) {
		// Need a File and URI object so the path works on all OSs
		File file = new File(path);
		URI uri = file.toURI();

		// Play one mp3 and and have code run when the song ends
		Media media = new Media(uri.toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setOnEndOfMedia(new EndOfSongHandler());
		mediaPlayer.play();
		mediaPlayer.getOnEndOfMedia();
	}

	/**
	 * This class handles what happens after an mp3 file finishes playing. It will
	 * continue to play songs from the playlist until all songs have been played.
	 */
	private class EndOfSongHandler implements Runnable {
		@Override
		public void run() {

			// This Runnable apparently does not get called all the time.
			// However, I have the same code in my Jukebox and it works.
			// This question "setOnEndOfMedia does not work" is unanswered on
			// the web.
			playingASong = false;
			if (!playlist.isEmpty()) {
				playPlaylist();
			}
			SongQueue.dequeue();
		}
	}
}