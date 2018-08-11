package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.Test;

import controller_view.SongQueue;
import javafx.embed.swing.JFXPanel;
import model.Jukebox;
import model.Student;
import model.Administrator;
import model.Song;

/**
 * Unit testing for all model classes.
 * 
 * @author Michelle Monteith and Jamie David
 * @class CSC 335 Spring 18
 */
public class SongTest {

	public Jukebox jukebox = new Jukebox();
	public Administrator admin = (Administrator) jukebox.getAllUsers().get(4);
	public Student chris = jukebox.getAllUsers().get(0);
	public Song song = jukebox.getSong(0);

	@Test
	public void testGetters() {
		jukebox.setCurrentUser(chris);
		assertEquals(jukebox.getAllUsers().size(), 5);
		assertEquals(jukebox.getCurrentUser().getUsername(), "Chris");
		assertEquals(jukebox.getSong(0).getTitle(), song.getTitle());

		assertEquals(song.getLength(), 5);
		assertEquals(song.getArtist(), "Pikachu");
		assertEquals(song.getFileName(), "./songfiles/Capture.mp3");
		assertEquals(song.getTitle(), "Pokemon Capture");
		assertEquals(song.getTimesPlayed(), 0);
		assertEquals(song.toString(), "Pokemon Capture - Pikachu");

		assertEquals(chris.getSecondsAvailable(), 90000);
	}

	// ---------------- ADMIN + STUDENT TESTING ----------------
	@Test
	public void testIsAdmin() {
		Student s = new Student("s", "1", jukebox);
		Student a = new Administrator("a", "1", jukebox);

		assertTrue(a.isAdmin());
		assertFalse(s.isAdmin());
	}

	@Test
	public void testAdminAdd() {

		int size = jukebox.getAllUsers().size();
		admin.addAccount("Hi", "test");
		assertEquals(jukebox.getAllUsers().size(), size + 1);
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

	// ---------------- JUKEBOX TESTING ----------------
	@Test
	public void testLogin() {

		assertTrue(jukebox.loginUser("Chris", "1"));
		assertFalse(jukebox.loginUser("Chris", "badpw"));
		assertFalse(jukebox.loginUser("badusr", "bad"));
	}

	@Test
	public void testGetSongSelections() {
		String expected = "[Pokemon Capture - Pikachu, Danse Macabre - Kevin MacLeod, Determined Tumbao - FreePlay Music, Loping Sting - Kevin MacLeod, Swing Cheese - FreePlay Music, The Curtain Rises - Kevin MacLeod, Untameable Fire - Pierre Langer]";
		List<Song> list = jukebox.getSongSelections();
		assertEquals(expected, list.toString());
	}

	@Test
	public void testSetSongSelections() {
		List<Song> songSelection = new ArrayList<Song>();
		songSelection.add(new Song("Pokemon Capture", 5, "Pikachu", "./songfiles/Capture.mp3"));
		jukebox.setSongSelections(songSelection);

		String expected = "[Pokemon Capture - Pikachu]";
		List<Song> list = jukebox.getSongSelections();

		assertEquals(expected, list.toString());
	}

	@Test
	public void testGetPlaylist() {
		Queue<Song> playlist = jukebox.getPlaylist();

		assertTrue(playlist.isEmpty());
	}

	@Test
	public void testSetPlaylist() {
		assertTrue(jukebox.getPlaylist().isEmpty());

		Queue<Song> playlist = new LinkedList<Song>();
		playlist.add(new Song("Pokemon Capture", 5, "Pikachu", "./songfiles/Capture.mp3"));
		jukebox.setPlaylist(playlist);

		assertFalse(jukebox.getPlaylist().isEmpty());
	}

	@Test
	public void testCanPlay() {

		jukebox.setCurrentUser(chris);
		assertTrue(jukebox.canPlay(song));

		chris.playSong();
		chris.playSong();
		chris.playSong();

		assertFalse(jukebox.canPlay(song));

		song.playOnce();
		song.playOnce();
		song.playOnce();

		assertFalse(jukebox.canPlay(song));
	}

	@Test
	public void testCanPlay2() {

		jukebox.setCurrentUser(chris);
		assertTrue(jukebox.canPlay(song));

		chris.setSecs(0);

		assertFalse(jukebox.canPlay(song));

		chris.setSecs(song.getLength());
		assertTrue(jukebox.canPlay(song));
	}

	@Test
	public void testCanPlay3() {

		jukebox.setCurrentUser(chris);
		song.playOnce();
		song.playOnce();
		song.playOnce();

		assertFalse(jukebox.canPlay(song));
	}

	@Test
	public void testPlay() throws InterruptedException {
		@SuppressWarnings("unused")
		final JFXPanel fxPanel = new JFXPanel();
		jukebox.setCurrentUser(chris);
		SongQueue queue = new SongQueue();
		queue.enqueue(song);
		jukebox.playSong(song);
		queue.enqueue(song);
		jukebox.playSong(song);
		Thread.sleep(6000);
	}

	// ---------------- SONG TESTING ----------------
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