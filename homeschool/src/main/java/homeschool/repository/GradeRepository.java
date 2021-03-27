package homeschool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import homeschool.entity.Course;
import homeschool.entity.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    @Query("select g from Grade g left join fetch g.courses where g.id = ?1")
    List<Course> listCoursesById(long gradeId);
}