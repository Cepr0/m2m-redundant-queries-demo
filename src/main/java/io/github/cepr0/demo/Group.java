package io.github.cepr0.demo;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group extends BaseEntity {

	private String name;

	@ManyToMany
	private List<Student> students;

	public Group(String name, List<Student> students) {
		this.name = name;
		this.students = students;
	}

	public Group() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Student> getStudents() {
		return this.students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}
}
