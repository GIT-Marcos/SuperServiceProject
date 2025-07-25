package org.superservice.superservice.DAOs;

import org.superservice.superservice.entities.Stock;

import java.util.List;

/**
 *
 * @author Usuario
 */
public interface StockDAO {

    Boolean actualizarStock(List<Stock> stocks);

    boolean agregarStock(Stock stockActualizado);
    
}
