package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.Jukebox;
import model.Student;
import model.Administrator;

/**
 * Unit testing for the Admin class.
 * 
 * @author Michelle Monteith and Jamie David
 * @class CSC 335 Spring 18
 */
public class AdminTest {

	public Jukebox jukebox = new Jukebox();
	public Administrator admin = (Administrator) jukebox.getAllUsers().get(4);

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
}