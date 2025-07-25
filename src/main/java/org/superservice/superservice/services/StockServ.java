package org.superservice.superservice.services;

import org.superservice.superservice.DAOs.StockDAO;
import org.superservice.superservice.DAOs.StockDAOimpl;
import org.superservice.superservice.entities.Stock;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class StockServ {

    StockDAO dao = new StockDAOimpl();

//    public Boolean actualizarStock(NotaRetiro notaConSalidas) {
//        List<Stock> stocks = new ArrayList<>();
//        for (int i = 0; i < notaConSalidas.getDetallesRetiro().size(); i++) {
//            Stock s = notaConSalidas.getDetallesRetiro().get(i).getRepuesto().getStock();
//            stocks.add(s);
//        }
//        return dao.actualizarStock(stocks);
//    }

    public boolean agregarStock(Stock stockActualizado) {
        if (stockActualizado == null) {
            throw new NullPointerException("El stock que se quiere actualizar es nulo.");
        }
        return dao.agregarStock(stockActualizado);
    }

}
