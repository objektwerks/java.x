package homeschool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import homeschool.entity.Assignment;
import homeschool.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select c from Course c left join fetch c.assignments where c.id = ?1")
    List<Assignment> listAssignmentsById(long courseId);
}