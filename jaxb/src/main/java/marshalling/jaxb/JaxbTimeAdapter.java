package marshalling.jaxb;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JaxbTimeAdapter extends XmlAdapter<String, Date> {
    @Override
    public Date unmarshal(String time) {
        Calendar calendar = toCalendar(DatatypeConverter.parseTime(time).getTime());
        return calendar.getTime();
    }

    @Override
    public String marshal(Date time) {
        String value = null;
        if (time != null) {
            value = DatatypeConverter.printTime(toCalendar(time));
        }
        return value;
    }

    private Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, 0);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 0);
        return calendar;
    }
}