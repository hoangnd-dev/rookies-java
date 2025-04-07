package nashtech.rookies.jpa.repository.boot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nashtech.rookies.jpa.entity.DepartmentEntity;
import nashtech.rookies.jpa.repository.DepartmentRepository;

@Repository
public interface DepartmentRepositoryBoot extends DepartmentRepository, JpaRepository<DepartmentEntity, String> {

}
