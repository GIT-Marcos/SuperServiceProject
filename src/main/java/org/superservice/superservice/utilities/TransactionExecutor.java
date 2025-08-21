package org.superservice.superservice.utilities;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Para evitar algunos problemas de concurrencia y además poder tener un sistema thread-safe
 * resolví usar este formato para las llamadas a bd.
 * La apertura y cerrado de la Session y la ejecución de la Transaction se repite mucho en operaciones
 * simples como la carga y la modificación de entidades.
 * Esta clase intenta reducir esa repetición de código encapsulando la lógica y argumentándole como función
 * la operación necesaria.
 */
public class TransactionExecutor {

    public static <T> T executeInTransaction(TransactionFunction<T> function) {
        Transaction tx = null;
        try (Session session = Util.getHibernateSessionThreadSafe()) {
            tx = session.beginTransaction();
            T result = function.apply(session);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            Util.cerrarHibernateSessionThreadSafe();
        }
    }

}
