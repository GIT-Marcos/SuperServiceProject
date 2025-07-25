package org.superservice.superservice.DAOs;

import java.util.List;
import org.hibernate.Session;
import org.superservice.superservice.entities.Stock;
import org.superservice.superservice.utilities.Util;

/**
 *
 * @author Usuario
 */
public class StockDAOimpl implements StockDAO {

    Session session;

    @Override
    public Boolean actualizarStock(List<Stock> stocks) {

        try {
            session.beginTransaction();
            for (Stock s : stocks) {
                session.merge(s);
            }
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean agregarStock(Stock stockActualizado) {
        session = Util.getHibernateSession();
        try {
            session.beginTransaction();
            session.merge(stockActualizado);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        } finally {
            session.close();
        }
    }

}
