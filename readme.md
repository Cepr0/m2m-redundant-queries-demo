Demo of redundant select queries while updating the entity with ManyToMany relation.

### Entities:

```java
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
	
   // skipped
}
```

```java
@Entity
@Table(name = "students")
public class Student extends BaseEntity {

    private String name;
    
    public Student(String name) {
        this.name = name;
    }
    
    // skipped
}
```

### Updating 

```java
public void onReady(ApplicationReadyEvent e) {

    Student s1 = appService.createStudent("s1");
    Student s2 = appService.createStudent("s2");
    Student s3 = appService.createStudent("s3");
    Student s4 = appService.createStudent("s4");
    
    Group g1 = appService.createGroup("g1", asList(s1.getId(), s2.getId()));
    
    appService.updateGroup(g1.getId(), "g1_", asList(s3.getId(), s4.getId()));
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
                return groupRepo.save(group);
            } else {
                return group;
            }
        });
}

private <T extends BaseEntity, ID extends Serializable> List<T> toRefList(Collection<ID> ids, JpaRepository<T, ID> repo) {
    return ids.stream()
        .map(repo::getOne) // take reference to the entity
        .collect(Collectors.toList());
}
```

### Query log

```java
select group0_.id as id1_0_0_, group0_.name as name2_0_0_ from groups group0_ where group0_.id=5;
-- two redundant selects:
select student0_.id as id1_2_0_, student0_.name as name2_2_0_ from students student0_ where student0_.id=3;
select student0_.id as id1_2_0_, student0_.name as name2_2_0_ from students student0_ where student0_.id=4;
--
update groups set name='g1_' where id=5;
delete from groups_students where group_id=5;
insert into groups_students (group_id, students_id) values (5, 3);
insert into groups_students (group_id, students_id) values (5, 4);
```