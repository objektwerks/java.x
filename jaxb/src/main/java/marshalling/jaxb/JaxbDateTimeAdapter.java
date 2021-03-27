package marshalling.jaxb;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JaxbDateTimeAdapter extends XmlAdapter<String, Date> {
    @Override
    public Date unmarshal(String dateTime) {
	return DatatypeConverter.parseDateTime(dateTime).getTime();
    }

    @Override
    public String marshal(Date dateTime) {
	String value = null;
	if (dateTime != null) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(dateTime);
	    value = DatatypeConverter.printDateTime(calendar);
	}
	return value;
    }
}