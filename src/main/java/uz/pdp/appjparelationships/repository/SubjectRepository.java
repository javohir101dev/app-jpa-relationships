package uz.pdp.appjparelationships.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.appjparelationships.entity.Subject;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    boolean existsByName(String name);

    @Query(value = "select s.id, s.name from subject s\n" +
            "                  join student_subjects ss on s.id = ss.subjects_id\n" +
            "                  join groups g on s.name = g.name\n" +
            "                  join faculty f on f.id = g.faculty_id\n" +
            "                  join university u on u.id = f.university_id\n" +
            "where u.id=:universityId", nativeQuery = true)
    List<Subject> findAllByUniversityId(Integer universityId);

    @Query(value = "select s.id, s.name from subject s\n" +
            "                  join student_subjects ss on s.id = ss.subjects_id\n" +
            "                  join groups g on s.name = g.name\n" +
            "                  join faculty f on f.id = g.faculty_id\n" +
            "where f.id=:facultyId", nativeQuery = true)
    List<Subject> findAllByFacultyId(Integer facultyId);

    @Query(value = "select s.id, s.name from subject s\n" +
            "                  join student_subjects ss on s.id = ss.subjects_id\n" +
            "                  join groups g on s.name = g.name\n" +
            "where g.id=:groupId", nativeQuery = true)
    List<Subject> findAllByGroupId(Integer groupId);

}
