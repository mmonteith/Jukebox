package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Song holds an MP3 file and information about it.
 * 
 * @author Michelle Monteith and Jamie David
 * @class CSC 335 Spring 18
 */
public class Song implements Serializable{

	private static final long serialVersionUID = 1L;
	private String title;
	private String artist;
	private int length;
	private String fileName;
	private int timesPlayed;
	private int currentDay;
	private int currentYear;
	private LocalDate dateToday;
	private String time;

	public Song(String title, int length, String artist, String fileName) {
		this.artist = artist;
		this.title = title;
		this.fileName = fileName;
		this.length = length;
		this.timesPlayed = 0;
		this.time = "";
		dateToday = LocalDate.now();
		currentDay = dateToday.getDayOfYear();
		currentYear = dateToday.getYear();
		setTimeString();
	}

	/**
	 * Getter for song length.
	 * @return length
	 */
	public int getLength() {
		return this.length;
	}
	
	/**
	 * Getter for song time (which is length formatted).
	 */
	public String getTime() {
		return this.time;
	}
	
	/**
	 * Stores the length of the Song as a formatted String of form
	 * of "MM:SS".
	 */
	private void setTimeString() {
		int min = (length % 3600) / 60;
		int sec = length % 60;
		time = String.format("%02d:%02d", min, sec);
	}

	/**
	 * getter for timesplayed
	 * @return timesPlayed
	 */
	public int getTimesPlayed() {
		return this.timesPlayed;
	}
	
	/**
	 * Getter for song title.
	 * @return title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Getter for artist.
	 * @return artist
	 */
	public String getArtist() {
		return this.artist;
	}

	/**
	 * Getter for filename.
	 * @return filename
	 */
	public String getFileName() {
		return this.fileName;
	}
	
	/**
	 * Setter for currentDay (testing only)
	 */
	public void setCurDay(int day) {
		currentDay = day;
	}
	
	/**
	 * Setter for currentYear (testing only)
	 */
	public void setCurYear(int yr) {
		currentYear = yr;
	}

	/**
	 * Increases the count for how many times this Song has been played.
	 */
	public void playOnce() {
		this.timesPlayed++;
	}

	/**
	 * Checks if the Song has not exceeded its limit of 3 plays per day.
	 * @return true if the song has been played less than 3 times today
	 * @return false otherwise
	 */
	public boolean playable() {
		adjustDay();
		if (this.timesPlayed < 3) {
			return true;
		}
		return false;
	}

	/**
	 * Adjusts currentDay and currentYear to the day and year today then resets
	 * this Song's play count.
	 */
	private void adjustDay() {
		this.dateToday = LocalDate.now();
		
		if (this.currentDay != dateToday.getDayOfYear() || this.currentYear != dateToday.getYear()) {
			this.timesPlayed = 0;
			this.currentDay = dateToday.getDayOfYear();
			this.currentYear = dateToday.getYear();
		}
	}
	
	/**
	 * Returns the Song as a string.
	 * @return the song in the form: [Title] - [Artist]
	 */
	public String toString() {
		return this.title + " - " + this.artist;
	}
}