package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.Jukebox;
import model.Song;

/**
 * Unit testing for the Song class.
 * 
 * @author Michelle Monteith and Jamie David
 * @class CSC 335 Spring 18
 */
public class SongTest {

	public Jukebox jukebox = new Jukebox();
	public Song song = jukebox.getSong(0);
	
	@Test
	public void testGetters() {
		assertEquals(song.getLength(), 5);
		assertEquals(song.getArtist(), "Pikachu");
		assertEquals(song.getFileName(), "./songfiles/Capture.mp3");
		assertEquals(song.getTitle(), "Pokemon Capture");
		assertEquals(song.getTimesPlayed(), 0);
		assertEquals(song.toString(), "Pokemon Capture - Pikachu");
	}
	
	@Test
	public void testPlays() {
		assertTrue(song.playable());
		song.playOnce();
		assertTrue(song.playable());

		song.playOnce();
		song.playOnce();

		assertFalse(song.playable());
	}

	@Test
	public void testDay() {
		song.playOnce();
		song.playOnce();
		song.playOnce();
		assertFalse(song.playable());

		song.setCurDay(1);

		assertTrue(song.playable());
	}

	@Test
	public void testYear() {
		song.playOnce();
		song.playOnce();
		song.playOnce();
		assertFalse(song.playable());

		song.setCurYear(2001);

		assertTrue(song.playable());
	}

	@Test
	public void testGetSongTime() {
		assertEquals("00:05", song.getTime());
	}
}