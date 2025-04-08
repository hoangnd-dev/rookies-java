package nashtech.rookies.jpa.repository.boot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nashtech.rookies.jpa.entity.RoleEntity;
import nashtech.rookies.jpa.repository.RoleRepository;

@Repository
public interface RoleRepositoryBoot extends RoleRepository, JpaRepository<RoleEntity, Long> {

    @Query("SELECT b from RoleEntity b where lower(b.code) like lower(:code) order by b.id")
    List<RoleEntity> getRoleByQuery (String name);
    // Projection
    // JOIN
    // GraphQUERY
    //
}
