package homeschool.entity;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
    public static final String packageName = "homeschool.entity";
    private static final QName qname = new QName("http://homeschool/entity/student", "student");

    public ObjectFactory() {
    }

    @XmlElementDecl(namespace = "http://homeschool/entity/student", name = "student")
    public JAXBElement<Student> createStudent(Student student) {
        return new JAXBElement<>(qname, Student.class, null, student);
    }
}