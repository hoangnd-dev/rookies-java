package nashtech.rookies.jpa.service.impl;

import org.springframework.stereotype.Service;

import nashtech.rookies.jpa.entity.RoleEntity;
import nashtech.rookies.jpa.repository.RoleRepository;
import nashtech.rookies.jpa.service.RoleService;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleEntity, String> implements RoleService {

    public RoleServiceImpl (RoleRepository repository) {
        super(repository);
    }
}
