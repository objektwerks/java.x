package homeschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import homeschool.entity.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
}