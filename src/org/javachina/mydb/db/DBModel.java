package org.javachina.mydb.db;

import java.io.Serializable;
import java.util.ArrayList;

import org.javachina.mydb.db.dto.Person;
import org.javachina.mydb.db.dto.Student;

public class DBModel implements Serializable{
	private DBModel(){}
	private static DBModel instance = new DBModel();
	
	public static DBModel getInstance(){
		return instance;
	}
	private ArrayList<Person> personTable = new ArrayList<Person>();

	private ArrayList<Student> studentTable = new ArrayList<Student>();

	public ArrayList<Person> getPersonTable() {
		return personTable;
	}

	public ArrayList<Student> getStudentTable() {
		return studentTable;
	}
	
	
}
