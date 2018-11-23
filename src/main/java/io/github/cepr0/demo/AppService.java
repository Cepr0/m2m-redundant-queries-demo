package io.github.cepr0.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class AppService {

	private final GroupRepo groupRepo;
	private final StudentRepo studentRepo;

	public AppService(GroupRepo groupRepo, StudentRepo studentRepo) {
		this.groupRepo = groupRepo;
		this.studentRepo = studentRepo;
	}

	@NonNull
	public Student createStudent(String name) {
		return studentRepo.save(new Student(name));
	}

	@NonNull
	public Group createGroup(String name, List<Long> studentIds) {
		List<Student> students = toRefList(studentIds, studentRepo);
		Group group = new Group(name, students);
		log.info("[i] Inserting...");
		return groupRepo.save(group);
	}

	public Optional<Group> updateGroup(Long id, String name, Collection<Long> studentIds) {
		return groupRepo.findById(id)
				.map(group -> {

					boolean isChanged = false;

					if (name != null) {
						group.setName(name);
						isChanged = true;
					}

					if (studentIds != null && !studentIds.isEmpty()) {
						List<Student> students = toRefList(studentIds, studentRepo);
						group.setStudents(students);
						isChanged = true;
					}

					if (isChanged) {
						log.info("[i] Updating...");
						return groupRepo.save(group);
					} else {
						return group;
					}
				});
	}

	private <T extends BaseEntity, ID extends Serializable> List<T> toRefList(Collection<ID> ids, JpaRepository<T, ID> repo) {
		log.info("[i] Taking reference list...");
		return ids.stream()
				.map(repo::getOne)
				.collect(Collectors.toList());
	}
}
