package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.Jukebox;
import model.Student;
import model.Song;

/**
 * Unit testing for the Student class.
 * 
 * @author Michelle Monteith and Jamie David
 * @class CSC 335 Spring 18
 */
public class StudentTest {

	public Jukebox jukebox = new Jukebox();
	public Student chris = jukebox.getAllUsers().get(0);
	public Song song = jukebox.getSong(0);

	@Test
	public void testGetters() {
		jukebox.setCurrentUser(chris);
		assertEquals(chris.getSecondsAvailable(), 90000);
	}

	@Test
	public void testRequestSong() {

		assertTrue(chris.ableToPlay());
		assertEquals(chris.timeAsString(), "0 selected, 25:00:00");

		chris.playSong();
		chris.playSong();
		chris.playSong();

		assertEquals(chris.timeAsString(), "3 selected, 25:00:00");
		assertFalse(chris.ableToPlay());
	}

	@Test
	public void testTime() {

		int sec = chris.getSecondsAvailable();
		int adjust = sec - song.getLength();

		chris.adjustTime(song);
		assertEquals(chris.getSecondsAvailable(), adjust);
	}

	@Test
	public void testUserTime() {
		chris.playSong();
		chris.playSong();
		chris.playSong();
		assertFalse(chris.ableToPlay());

		chris.setCurDay(1);

		assertTrue(chris.ableToPlay());
	}

	@Test
	public void testUserYear() {
		chris.playSong();
		chris.playSong();
		chris.playSong();
		assertFalse(chris.ableToPlay());

		chris.setCurYear(2001);

		assertTrue(chris.ableToPlay());
	}
}