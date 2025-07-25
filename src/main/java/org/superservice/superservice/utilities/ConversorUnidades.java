package org.superservice.superservice.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Usuario
 */
public class ConversorUnidades {

    private static void esNegativo(Number number) {
        if (number instanceof Integer && (Integer) number < 0) {
            lanzaExcepcion();
        } else if (number instanceof Double && (Double) number < 0) {
            lanzaExcepcion();
        } else if (number instanceof Long && (Long) number < 0) {
            lanzaExcepcion();
        } else if (number instanceof BigDecimal && ((BigDecimal) number).compareTo(BigDecimal.ZERO) == -1) {
            lanzaExcepcion();
        }
    }

    private static void lanzaExcepcion() {
        throw new IllegalArgumentException("El N° ingresado no puede ser nagativo.");
    }

    /**
     *
     * @param input
     * @return
     */
    public static Double double2Decimales(String input) {
        Double d;
        if (input == null || input.isBlank()) {
            return null;
        }
        try {
            d = Double.valueOf(input);
            esNegativo(d);
            d = Math.round(d * 100.0) / 100.0;
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("El campo está en formato incorrecto.");
        }
        return d;
    }

    /**
     *
     * @param input
     * @return
     */
    public static BigDecimal bdParaDinero(String input) {
        BigDecimal bd;
        if (input == null || input.isBlank()) {
            return null;
        }
        try {
            bd = BigDecimal.valueOf(Double.parseDouble(input)).setScale(2, RoundingMode.HALF_UP);
            esNegativo(bd);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("El campo está en formato incorrecto.");
        }
        return bd;
    }

    /**
     *
     * @param input
     * @return
     */
    public static Long longParaCodVenta(String input) {
        Long l;
        if (input == null || input.isBlank()) {
            return null;
        }
        try {
            l = Long.valueOf(input);
            esNegativo(l);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("El campo está en formato incorrecto.");
        }
        return l;
    }

}
