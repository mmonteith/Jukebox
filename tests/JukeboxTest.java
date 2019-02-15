package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.Test;

import view.SongQueue;
import javafx.embed.swing.JFXPanel;
import model.Jukebox;
import model.Student;
import model.Song;

/**
 * Unit testing for the Jukebox class.
 * 
 * @author Michelle Monteith and Jamie David
 * @class CSC 335 Spring 18
 */
public class JukeboxTest {

	public Jukebox jukebox = new Jukebox();
	public Student chris = jukebox.getAllUsers().get(0);
	public Song song = jukebox.getSong(0);

	@Test
	public void testGetters() {
		jukebox.setCurrentUser(chris);
		assertEquals(jukebox.getAllUsers().size(), 5);
		assertEquals(jukebox.getCurrentUser().getUsername(), "Chris");
		assertEquals(jukebox.getSong(0).getTitle(), song.getTitle());
	}

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
}