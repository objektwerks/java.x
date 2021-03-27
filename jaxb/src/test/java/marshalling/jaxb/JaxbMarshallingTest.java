package marshalling.jaxb;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class JaxbMarshallingTest {
    private static final String packageName = "marshalling.jaxb";

    private String pingXml;

    @Before
    public void setUp() throws IOException {
        this.pingXml = IOUtils.toString(this.getClass().getResourceAsStream("/ping.xml"));
    }

    @Test
    public void adaptDate() {
        JaxbDateAdapter jaxbDateAdapter = new JaxbDateAdapter();
        Calendar calendar = Calendar.getInstance();
        String dateAsString = DatatypeConverter.printDate(calendar);
        Date date = jaxbDateAdapter.unmarshal(dateAsString);
        assertNotNull(date);
        dateAsString = jaxbDateAdapter.marshal(date);
        assertNotNull(dateAsString);
        assertEquals(date, jaxbDateAdapter.unmarshal(dateAsString));
    }

    @Test
    public void adaptTime() {
        JaxbTimeAdapter jaxbTimeAdapter = new JaxbTimeAdapter();
        Calendar calendar = Calendar.getInstance();
        String timeAsString = DatatypeConverter.printDate(calendar);
        Date time = jaxbTimeAdapter.unmarshal(timeAsString);
        assertNotNull(time);
        timeAsString = jaxbTimeAdapter.marshal(time);
        assertNotNull(timeAsString);
        assertEquals(time, jaxbTimeAdapter.unmarshal(timeAsString));
    }

    @Test
    public void adaptDateTime() {
        JaxbDateTimeAdapter jaxbDateTimeAdapter = new JaxbDateTimeAdapter();
        Calendar calendar = Calendar.getInstance();
        String dateTimeAsString = DatatypeConverter.printDate(calendar);
        Date dateTime = jaxbDateTimeAdapter.unmarshal(dateTimeAsString);
        assertNotNull(dateTime);
        dateTimeAsString = jaxbDateTimeAdapter.marshal(dateTime);
        assertNotNull(dateTimeAsString);
        assertEquals(dateTime, jaxbDateTimeAdapter.unmarshal(dateTimeAsString));
    }

    @Test
    public void unmarshal() throws JAXBException {
        JaxbUnmarshaller jaxbUnmarshaller = new JaxbUnmarshaller(packageName);
        Ping ping = jaxbUnmarshaller.unmarshal(pingXml);
        assertNotNull(ping);
    }

    @Test
    public void marshal() throws JAXBException {
        JaxbMarshaller jaxbMarshaller = new JaxbMarshaller(packageName);
        Ping ping = new Ping("ping");
        String xml = jaxbMarshaller.marshal(ping);
        assertTrue(xml.indexOf("Ping>") > 0);
    }
}