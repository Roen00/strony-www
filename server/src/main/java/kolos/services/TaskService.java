package kolos.services;

import kolos.models.TaskModel;
import kolos.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskModel save(TaskModel requestModel) {
        return taskRepository.save(requestModel);
    }

    public Iterable<TaskModel> findAllRequest(){
        return taskRepository.findAll();
    }

    public TaskModel findOne(Long id){
        return taskRepository.findOne(id);
    }

}
