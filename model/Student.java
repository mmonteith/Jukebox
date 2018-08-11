package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Student interacts with the Jukebox and requests Songs to be played. They
 * are only allowed 1500 minutes of song time.
 * 
 * @author Michelle Monteith and Jamie David
 * @class CSC 335 Spring 18
 */
public class Student implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private int songsPlayed;

	private int secondsAvailable;
	private int currentDay;
	private int currentYear;
	private LocalDate dateToday;
	private String time;
	protected Jukebox jukebox;
	
	/**
	 * Creates a new Student
	 * @param un username
	 * @param pw password
	 * @param j jukebox
	 */
	public Student(String un, String pw, Jukebox j) {
		username = un;
		password = pw;
		songsPlayed = 0;
		secondsAvailable = 90000;
		
		jukebox = j;
		
		dateToday = LocalDate.now();
		currentDay = dateToday.getDayOfYear();
		currentYear = dateToday.getYear();
		setTimeString();
	}

	/**
	 * Stores the available time for the Student as a formatted String if form
	 * of "HH:MM:SS".
	 */
	private void setTimeString() {
		int hour = secondsAvailable / 3600;
		int min = (secondsAvailable % 3600) / 60;
		int sec = secondsAvailable % 60;
		time = String.format("%02d:%02d:%02d", hour, min, sec);
	}

	/**
	 * Getter for username.
	 * @return username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Getter for password.
	 * @return password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Getter for secondsAvailable.
	 * @return secondsAvailable
	 */
	public int getSecondsAvailable() {
		return this.secondsAvailable;
	}

	/**
	 * A student is not an admin, always return false.
	 * @return false
	 */
	public boolean isAdmin() {
		return false;
	}
	
	/**
	 * Checks if the Student has not exceeded its limit of 3 songs per day.
	 * @return true if limit not reached
	 * @return false otherwise
	 */
	public boolean ableToPlay() {
		adjustDay();
		if (this.songsPlayed < 3) {
			return true;
		}
		return false;
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
	 * Setter for secondsAvailable (testing only)
	 */
	public void setSecs(int s) {
		secondsAvailable = s;
	}
	
	/**
	 * Adjusts currentDay and currentYear to the day and year today then resets
	 * this Student's song played count.
	 */
	private void adjustDay() {
		this.dateToday = LocalDate.now();
		if (this.currentDay != dateToday.getDayOfYear() || this.currentYear != dateToday.getYear()) {
			this.songsPlayed = 0;
			this.currentDay = dateToday.getDayOfYear();
			this.currentYear = dateToday.getYear();
		}
	}

	/**
	 * Increases the songs played count for this Student.
	 */
	public void playSong() {
		this.songsPlayed++;
	}

	/**
	 * Decreases the Student's available minutes by the length of the song.
	 * @param song, song to use to decrease the minutes available for the user
	 */
	public void adjustTime(Song song) {
		this.secondsAvailable -= song.getLength();
		setTimeString();
	}

	/**
	 * Returns a message displaying the count of songs played and available
	 * minutes.
	 * @return time as the string in the format [SongsPlayed] selected [Time]
	 */
	public String timeAsString() {
		return (this.songsPlayed + " selected, " + this.time);
	}
}
