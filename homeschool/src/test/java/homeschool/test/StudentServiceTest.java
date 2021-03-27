package homeschool.test;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.bind.JAXBException;

import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import homeschool.entity.Assignment;
import homeschool.entity.Course;
import homeschool.entity.Grade;
import homeschool.entity.School;
import homeschool.entity.Student;
import homeschool.entity.Teacher;
import homeschool.profiler.Profiler;
import homeschool.service.StudentService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@TransactionConfiguration(defaultRollback = false)
public class StudentServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceTest.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private Validator validator;

    @Autowired
    private Profiler profiler;

    private School school;
    private Teacher teacher;
    private Student student;
    private Grade grade;
    private Course course;
    private Assignment assignment;

    @Test
    public void service() throws JAXBException {
        newSchool();
        assertNotNull("school is null", school);
        assertTrue("school id not set", school.getId() > -1);
        validate(school);
        newTeacher();
        assertNotNull("teacher is null", teacher);
        assertTrue("teacher id not set", teacher.getId() > -1);
        validate(teacher);
        newStudent();
        assertNotNull("student is null", student);
        assertTrue("student id not set", student.getId() > -1);
        validate(student);
        newGrade();
        assertNotNull("grade is null", grade);
        assertTrue("grade id not set", grade.getId() > -1);
        validate(grade);
        newCourse();
        assertNotNull("course is null", course);
        assertTrue("course id not set", course.getId() > -1);
        validate(course);
        newAssignment();
        assertNotNull("assignment is null", assignment);
        assertTrue("assignment id not set", assignment.getId() > -1);
        validate(assignment);
        addGrade();
        addCourse();
        addAssignment();
        assertTrue("grade not attached to student", studentService.listGradesByStudentId(student.getId()).size() == 1);
        assertTrue("course not attached to grade", studentService.listCoursesByGradeId(grade.getId()).size() == 1);
        assertTrue("assignment not attached to course", studentService.listAssignmentsByCourseId(course.getId()).size() == 1);
        Teacher teacherFound = studentService.findTeacherById(teacher.getId());
        assertEquals("Teacher fineOne failed", teacher, teacherFound);
        String studentXml = student.marshal();
        assertTrue("marshalled student xml is null and/or empty", (studentXml != null && studentXml.length() > 0));
        assertTrue("unmarshalled student is invalid", validate(student.unmarshal(studentXml)).size() == 0);
        logger.info(printHibernateStatistics());
        logger.info(profiler.toString());
        logger.info(studentXml);
    }

    @Test
    public void validation() {
        assertTrue(validate(new School("")).size() > 0);
        assertTrue(validate(new Teacher("")).size() > 0);
        assertTrue(validate(new Student("")).size() > 0);
        assertTrue(validate(new Grade(0, new Date(), new Date(), school)).size() > 0);
        assertTrue(validate(new Course("")).size() > 0);
        assertTrue(validate(new Assignment(teacher, Assignment.Type.Standard, "")).size() > 0);
    }

    private void newSchool() {
        school = new School("Slippery Rock");
        school = studentService.saveSchool(school);
    }

    private void newTeacher() {
        teacher = new Teacher("Albert Einstein");
        teacher = studentService.saveTeacher(teacher);
    }

    private void newStudent() {
        student = new Student("Homer Simpson");
        student = studentService.saveStudent(student);
    }

    private void newGrade() {
        Date start = new Date();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DATE, 180);
        grade = new Grade(1, start, end.getTime(), school);
        grade = studentService.saveGrade(grade);
    }

    private void addGrade() {
        student = studentService.addGradeToStudent(student, grade);
    }

    private void newCourse() {
        course = new Course("Astro Physics");
        course = studentService.saveCourse(course);
    }

    private void addCourse() {
        grade = studentService.addCourseToGrade(grade, course);
    }

    private void newAssignment() {
        assignment = new Assignment(teacher, Assignment.Type.Standard, "Explain the big bang theory.");
        assignment = studentService.saveAssignment(assignment);
    }

    private void addAssignment() {
        course = studentService.addAssignmentToCourse(course, assignment);
    }

    private <T> Set<ConstraintViolation<T>> validate(T type) {
        return validator.validate(type);
    }

    private String printHibernateStatistics() {
        EntityManagerFactoryInfo factoryInfo = (EntityManagerFactoryInfo)entityManagerFactory;
        EntityManagerFactory nativeFactory = factoryInfo.getNativeEntityManagerFactory();
        EntityManagerFactoryImpl hibernate = (EntityManagerFactoryImpl)nativeFactory;
        return hibernate.getSessionFactory().getStatistics().toString();
    }
}