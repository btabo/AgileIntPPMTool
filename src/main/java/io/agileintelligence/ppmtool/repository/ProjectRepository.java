package io.agileintelligence.ppmtool.repository;

import io.agileintelligence.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByProjectIdentifier(String projectIdentifier);

    List<Project> findByProjectLeader(String projectLeader);

}
