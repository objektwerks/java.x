package homeschool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import homeschool.entity.Grade;
import homeschool.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("select s from Student s left join fetch s.grades where s.id = ?1")
    List<Grade> listGradesById(long studentId);
}