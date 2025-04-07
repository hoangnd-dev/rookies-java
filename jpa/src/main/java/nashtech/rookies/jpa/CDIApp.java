package nashtech.rookies.jpa;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import nashtech.rookies.jpa.config.CDIBoot;
import nashtech.rookies.jpa.entity.DepartmentEntity;
import nashtech.rookies.jpa.service.UserService;

public class CDIApp {

    private final EntityManager entityManager;
    private final UserService   userService;

    @Inject
    public CDIApp (EntityManager entityManager,
                   UserService userService) {
        this.entityManager = entityManager;
        this.userService = userService;

    }


    public void run (String[] arga) {
        DepartmentEntity it = DepartmentEntity
            .builder()
            .name("IT")
            .build();

        it = userService.save(it);

    }

    public static void main (String[] args) {
        CDIBoot.with(CDIApp.class).run(args);
    }
}
