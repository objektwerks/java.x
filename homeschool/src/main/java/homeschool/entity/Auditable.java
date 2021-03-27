package homeschool.entity;

import java.util.Date;

public interface Auditable {
    void modified(Date modified);

    void modifiedBy(String modifiedBy);
}