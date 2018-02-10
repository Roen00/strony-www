package kolos.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(unique = true)
    private String taskName;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    private int rate;

    @NotNull
    private int fileName;

}
