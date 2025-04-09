package nashtech.rookies.jpa;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import nashtech.rookies.jpa.config.CDIBoot;
import nashtech.rookies.jpa.entity.DepartmentEntity;

public class JPAApp {

    private final CDIBoot.TransactionManager transactionManager;
    private final EntityManager              entityManager;

    @Inject
    public JPAApp (CDIBoot.TransactionManager transactionManager, EntityManager entityManager) {
        this.transactionManager = transactionManager;
        this.entityManager = entityManager;
    }


    public void run (String[] args) {
        final DepartmentEntity it = DepartmentEntity
            .builder()
            .name("IT")
            .location("AABC")
            .build();
        transactionManager.execute(em -> {
            em.persist(it);
            return it;
        });
        System.out.println(it);

        var dbIt = entityManager.find(DepartmentEntity.class, it.getId());
        //        var dbIt = transactionManager.query(em -> em.find(DepartmentEntity.class, it.getId()));
        var newIt = transactionManager.execute(em -> {
            //            var u = em.find(DepartmentEntity.class, it.getId());
            //            u.setName("new Name");
            //            em.merge(u);
            //            return u;
            dbIt.setName("Name1");
            return em.merge(dbIt);
        });
        System.out.println(newIt);

    }

    public static void main (String[] args) {
        CDIBoot.with(JPAApp.class).run(args);
    }
}
