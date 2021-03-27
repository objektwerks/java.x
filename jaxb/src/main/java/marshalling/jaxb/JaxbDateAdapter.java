package marshalling.jaxb;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JaxbDateAdapter extends XmlAdapter<String, Date> {
    @Override
    public Date unmarshal(String date) {
        Calendar calendar = toCalendar(DatatypeConverter.parseDate(date).getTime());
        return calendar.getTime();
    }

    @Override
    public String marshal(Date date) {
        String value = null;
        if (date != null) {
            value = DatatypeConverter.printDate(toCalendar(date));
        }
        return value;
    }

    private Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}