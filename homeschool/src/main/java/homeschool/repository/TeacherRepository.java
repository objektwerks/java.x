package homeschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import homeschool.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}