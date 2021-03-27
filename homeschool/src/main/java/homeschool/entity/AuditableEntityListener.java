package homeschool.entity;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AuditableEntityListener {
    @PreUpdate
    @PrePersist
    public void onSave(Auditable auditable) {
        auditable.modified(new Date());
        auditable.modifiedBy("super user"); // Required ThreadLocal via Spring Security?
    }
}