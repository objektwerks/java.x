package homeschool.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import homeschool.entity.Assignment;
import homeschool.entity.Course;
import homeschool.entity.Grade;
import homeschool.entity.School;
import homeschool.entity.Student;
import homeschool.entity.Teacher;
import homeschool.profiler.Profiler;
import homeschool.repository.AssignmentRepository;
import homeschool.repository.CourseRepository;
import homeschool.repository.GradeRepository;
import homeschool.repository.SchoolRepository;
import homeschool.repository.StudentRepository;
import homeschool.repository.TeacherRepository;

@Service
@ManagedResource(
    objectName = "bean:name=studentService",
    log = true,
    logFile = "student.service.jmx.log",
    currencyTimeLimit = 15,
    persistPolicy = "OnUpdate",
    persistLocation = "~/.homeschool/",
    persistName = "jmxStudentService")
public class StudentService {
    @Autowired
    private Profiler profiler;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    public StudentService() {
    }

    @ManagedAttribute
    public String getProfile() {
        return profiler.toString();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public School saveSchool(School school) {
        assertTransactionIsActive();
        return schoolRepository.save(school);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Teacher saveTeacher(Teacher teacher) {
        assertTransactionIsActive();
        return teacherRepository.save(teacher);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Student saveStudent(Student student) {
        assertTransactionIsActive();
        return studentRepository.save(student);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Grade saveGrade(Grade grade) {
        assertTransactionIsActive();
        return gradeRepository.save(grade);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Course saveCourse(Course course) {
        assertTransactionIsActive();
        return courseRepository.save(course);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Assignment saveAssignment(Assignment assignment) {
        assertTransactionIsActive();
        return assignmentRepository.save(assignment);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Student addGradeToStudent(Student student, Grade grade) {
        assertTransactionIsActive();
        student.addGrade(grade);
        return studentRepository.save(student);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Grade addCourseToGrade(Grade grade, Course course) {
        assertTransactionIsActive();
        grade.addCourse(course);
        return gradeRepository.save(grade);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Course addAssignmentToCourse(Course course, Assignment assignment) {
        assertTransactionIsActive();
        course.addAssignment(assignment);
        return courseRepository.save(course);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Grade> listGradesByStudentId(long studentId) {
        assertTransactionIsActive();
        return studentRepository.listGradesById(studentId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Course> listCoursesByGradeId(long gradeId) {
        assertTransactionIsActive();
        return gradeRepository.listCoursesById(gradeId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Assignment> listAssignmentsByCourseId(long courseId) {
        assertTransactionIsActive();
        return courseRepository.listAssignmentsById(courseId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Teacher findTeacherById(long teacherId) {
        assertTransactionIsActive();
        return teacherRepository.findOne(teacherId);
    }

    private void assertTransactionIsActive() {
        assert(TransactionSynchronizationManager.isActualTransactionActive());
    }
}