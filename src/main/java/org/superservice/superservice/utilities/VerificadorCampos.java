package org.superservice.superservice.utilities;

/**
 * Clase para verificar que lo inresado por el usuario en los campos sea
 * correcto.
 *
 * @author Usuario
 */
public class VerificadorCampos {

    // ==== Métodos Privados Auxiliares ====    
    private static void verificarVacio(String paraVerificar) {
        if (paraVerificar == null || paraVerificar.isBlank()) {
            throw new IllegalArgumentException("El campo no puede estar vacío.");
        }
    }

    private static void espaciosEnBlanco(String input) {
        if (input.contains(" ")) {
            throw new IllegalArgumentException("No puede haber espacios en blanco.");
        }
    }

    private static void verificaLargo(String paraVerificar, Integer largoMin, Integer largoMax) {
        int largoInput = paraVerificar.length();
        if (largoMin != null && largoMax != null) {
            if (largoInput < largoMin || largoInput > largoMax) {
                throw new IllegalArgumentException("El campo debe tener entre " + largoMin + " "
                        + "y " + largoMax + " caracteres.");
            }
        } else if (largoMin != null) {
            if (largoInput < largoMin) {
                throw new IllegalArgumentException("El campo no puede tener menos de: " + largoMin + " caracteres.");
            }
        } else if (largoMax != null) {
            if (largoInput > largoMax) {
                throw new IllegalArgumentException("El campo no puede tener más de: " + largoMax + " caracteres.");
            }
        }
    }

    private static void soloHayNumeros(String input) {
        char[] chars = input.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("El texto solo puede tener números.");
            }
        }
    }

    private static void soloHayLetras(String input) {
        char[] chars = input.toCharArray();
        for (char c : chars) {
            if (!Character.isAlphabetic(c)) {
                throw new IllegalArgumentException("El texto solo puede tener letras.");
            }
        }
    }

    // ==== Validaciones Públicas ====
    /**
     * Usado para todo campo que no encaje en los específicos de esta clase.
     *
     * @param tipoCaracteres NULL: cualquiera. TRUE: solo letras y símbolos.
     * FALSE: solo números sin puntos ni coma.
     */
    public static void inputTextoGenerico(String input, Integer largoMin, Integer largoMax, boolean mandatorio,
            boolean espaciosEnBlanco, Boolean tipoCaracteres) {
        if (mandatorio) {
            verificarVacio(input);
        }
        if (!input.isEmpty() && !mandatorio) {
            if (largoMin != null || largoMax != null) {
                verificaLargo(input, largoMin, largoMax);
            }
        }
        if (!espaciosEnBlanco) {
            espaciosEnBlanco(input);
        }
        if (tipoCaracteres != null) {
            if (tipoCaracteres) {
                soloHayLetras(input);
            } else {
                soloHayNumeros(input);
            }
        }
    }

    public static void patente(String input, boolean esObligatorio) {
        if (input == null) {
            throw new NullPointerException("Error: La patente es nula");
        }
        if (esObligatorio) {
            verificarVacio(input);
        }
        verificaLargo(input, 6, 9);
        espaciosEnBlanco(input);
        if (!input.matches("^[a-zA-Z0-9]*$")) {
            throw new IllegalArgumentException("Error: la patente está en formato incorrecto.");
        }
    }

    public static void codFacturaCodBarra(String input, boolean esObligatorio) {
        if (esObligatorio) {
            verificarVacio(input);
        }
        if (input != null) {
            verificaLargo(input, null, 23);
        }
    }

    public static void dinero(String input, boolean esObligatorio) {
        if (esObligatorio) {
            verificarVacio(input);
        }
        if (input != null) {
            verificaLargo(input, null, 15);
            espaciosEnBlanco(input);
        }
    }

    public static void cantidadStock(String input, boolean esObligatorio) {
        if (esObligatorio) {
            verificarVacio(input);
        }
        if (input != null) {
            verificaLargo(input, null, 12);
            espaciosEnBlanco(input);
        }
    }

    public static void ultimos4(String input, boolean esObligatorio) {
        if (esObligatorio) {
            verificarVacio(input);
        }
        if (input != null) {
            verificaLargo(input, null, 4);
            soloHayNumeros(input);
        }
    }

    public static void referenciaTarjeta(String input, boolean esObligatorio) {
        if (esObligatorio) {
            verificarVacio(input);
        }
        if (input != null) {
            verificaLargo(input, null, 20);
            soloHayNumeros(input);
        }
    }

    public static void nroTransaccion(String input, boolean esObligatorio) {
        if (esObligatorio) {
            verificarVacio(input);
        }
        if (input != null) {
            verificaLargo(input, null, 16);
            soloHayNumeros(input);
        }
    }

    public static void validarPassword(char[] password) {
        if (password == null) {
            throw new NullPointerException("La contraseña es nula.");
        }

        if (password.length == 0) {
            throw new IllegalArgumentException("La contraseña no puede quedar vacía.");
        }

        if (password.length < 6 || password.length > 20) {
            throw new IllegalArgumentException("La contraseña debe tener entre 6 y 20 caracteres.");
        }

        for (char c : password) {
            if (Character.isWhitespace(c)) {
                throw new IllegalArgumentException("La contraseña no puede contener espacios en blanco.");
            }
        }
    }
}
