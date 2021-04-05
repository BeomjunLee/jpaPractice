package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private String createdDate; //변경되지 못하게 updatable false(insert 디폴트는 true)
    private String updatedDate;

    @PrePersist
    public void prePersist() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 m분"));
        createdDate = time;
        updatedDate = time;
    }

    @PreUpdate
    public void preUpdate() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 m분"));
        updatedDate = time;
    }
}
