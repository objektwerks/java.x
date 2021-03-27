package homeschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import homeschool.entity.School;

public interface SchoolRepository extends JpaRepository<School, Long> {
}