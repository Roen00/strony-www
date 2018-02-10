package kolos.repositories;

import kolos.models.TaskModel;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<TaskModel, Long> {

//    TaskModel findAllRequest();

}
