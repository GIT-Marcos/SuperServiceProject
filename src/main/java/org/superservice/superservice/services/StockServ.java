package org.superservice.superservice.services;

import org.superservice.superservice.DAOs.StockDAO;
import org.superservice.superservice.DAOs.StockDAOimpl;
import org.superservice.superservice.entities.DetalleRetiro;
import org.superservice.superservice.entities.NotaRetiro;
import org.superservice.superservice.entities.Stock;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class StockServ {

    StockDAO dao = new StockDAOimpl();

    public Boolean actualizarStock(NotaRetiro notaConSalidas) {
        List<Stock> stocks = new ArrayList<>();
        for (DetalleRetiro detalle : notaConSalidas.getDetallesRetiro()) {
            stocks.add(detalle.getRepuesto().getStock());
        }
        return dao.actualizarStock(stocks);
    }

    public boolean agregarStock(Stock stockActualizado) {
        if (stockActualizado == null) {
            throw new NullPointerException("El stock que se quiere actualizar es nulo.");
        }
        return dao.agregarStock(stockActualizado);
    }

}
