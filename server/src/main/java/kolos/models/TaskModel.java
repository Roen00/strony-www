package kolos.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class TaskModel {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    private String fileName;

    @NotNull
    private Long userId;

    @NotNull
    private boolean isUploaded;

}
