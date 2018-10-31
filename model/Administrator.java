package model;

import java.io.Serializable;

/**
 * An Administrator has the same rights as a Student, but is also able
 * to add other Students.
 * 
 * @author Michelle Monteith and Jamie David
 * @class  CSC 335 Spring 18
 */
public class Administrator extends Student implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param id user id
	 * @param j jukebox 
	 */
	public Administrator(String un, String pw, Jukebox j) {
		super(un, pw, j);
	}
	
	/**
	 * Admins can create new user accounts
	 * @param id user id
	 * @param j jukebox
	 * @return 
	 */
	public Student addAccount(String un, String pw) {
		Student s = new Student(un, pw, jukebox);
		jukebox.addStudent(s);
		return s;
	}

	/**
	 * Determines if admin, always returns true since this is an admin
	 * @return true
	 */
	public boolean isAdmin() {
		return true;
	}
}