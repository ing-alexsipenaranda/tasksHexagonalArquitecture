package com.hexagonal.tasks.infraestructure.repositories;


import com.hexagonal.tasks.domain.models.Task;
import com.hexagonal.tasks.domain.ports.out.TaskRepositoryPort;
import com.hexagonal.tasks.infraestructure.entities.TaskEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Component
public class JpaTaskRepositoryAdapter implements TaskRepositoryPort {

    private JpaTaskRepository JpaTaskRepository;

    public JpaTaskRepositoryAdapter(com.hexagonal.tasks.infraestructure.repositories.JpaTaskRepository jpaTaskRepository) {
        JpaTaskRepository = jpaTaskRepository;
    }


    @Override
    public Task save(Task task) {
        TaskEntity taskEntity = TaskEntity.fromDomainModel(task);
        TaskEntity savedTaskEntity = JpaTaskRepository.save(taskEntity);
        return savedTaskEntity.toDomainModel();
    }


    @Override
    public Optional<Task> findById(Long id) {
        return JpaTaskRepository.findById(id).map(TaskEntity::toDomainModel);
    }

    @Override
    public List<Task> findAll() {
        return JpaTaskRepository.findAll().stream()
                .map(TaskEntity::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Task> update(Task task) {
        if(JpaTaskRepository.existsById(task.getId())){
            TaskEntity taskEntity = TaskEntity.fromDomainModel(task);
            TaskEntity updateTaskEntity = JpaTaskRepository.save(taskEntity);
            return Optional.of(updateTaskEntity.toDomainModel());
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {

        if(JpaTaskRepository.existsById(id)){
            JpaTaskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
