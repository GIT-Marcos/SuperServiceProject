package org.superservice.superservice.utilities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.hibernate.Session;

 
public class Util {
    private static final String UNIDAD_PERSISTENCIA = "persistence";
    private static final EntityManagerFactory emf;

    static {
        EntityManagerFactory tempEmf = null;
        try {
            // Cargar las propiedades desde un archivo externo
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("config/db.properties")) {
                props.load(fis);
            }

            // Convertir a Map y pasar al EntityManagerFactory
            Map<String, Object> overrideProps = new HashMap<>((Map) props);

            tempEmf = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA, overrideProps);
            System.out.println("EntityManagerFactory creado exitosamente con propiedades externas.");
        } catch (IOException ex) {
            System.err.println("No se pudo cargar db.properties: " + ex.getMessage());
        } catch (Throwable ex) {
            System.err.println("Error al crear EntityManagerFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }

        emf = tempEmf;
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static Session getHibernateSession() {
        return getEntityManager().unwrap(Session.class);
    }

    public static void shutdown() {
        if (emf != null) {
            emf.close();
        }
    }
}
