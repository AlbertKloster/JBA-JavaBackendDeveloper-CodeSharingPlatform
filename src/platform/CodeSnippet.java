package platform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class CodeSnippet {

    @Id
    @GenericGenerator(name = "uuid", strategy = "platform.UuidGenerator")
    @GeneratedValue(generator = "uuid")
    @JsonIgnore
    private String uuid;
    private LocalDateTime date;
    private String code;
    private long time;
    private int views;
    @JsonIgnore
    private LocalDateTime exp;
    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
