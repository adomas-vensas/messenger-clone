package persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.SynchronizationType;

/**
 * CDI Resources class to produce and dispose EntityManager instances in a Jakarta EE application.
 */
@ApplicationScoped
public class Resources {

    @PersistenceUnit
    private EntityManagerFactory emf;

    /**
     * Produces a request-scoped EntityManager.
     * This EntityManager is synchronized with the current JTA transaction.
     *
     * @return a JTA-synchronized EntityManager
     */
    @Produces
    @Default
    @RequestScoped
    private EntityManager createJTAEntityManager() {
        return emf.createEntityManager(SynchronizationType.SYNCHRONIZED);
    }

    /**
     * Disposes the EntityManager at the end of the request.
     *
     * @param em the EntityManager to be disposed
     */
    private void closeDefaultEntityManager(@Disposes @Default EntityManager em) {
        em.close();
    }
}

