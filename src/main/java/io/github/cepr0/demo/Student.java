package io.github.cepr0.demo;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "students")
public class Student extends BaseEntity {

	private String name;

	public Student(String name) {
		this.name = name;
	}

	public Student() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
