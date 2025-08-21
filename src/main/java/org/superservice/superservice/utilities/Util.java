package org.superservice.superservice.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.superservice.superservice.entities.*;


public class Util {
    private static final String PROPERTIES_PATH = "config/db.properties";
    private static final SessionFactory sessionFactory;
    private static final ThreadLocal<Session> threadLocalSession = new ThreadLocal<>();

    static {
        Properties properties = cargarProperties();
        sessionFactory = inicializarSessionFactory(properties);
    }

    private static Properties cargarProperties() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_PATH)) {
            props.load(fis);
            System.out.println("✅ Propiedades cargadas desde " + PROPERTIES_PATH);
        } catch (IOException ex) {
            System.err.println("⚠️ No se pudo cargar " + PROPERTIES_PATH + ": " + ex.getMessage());
        }
        return props;
    }

    private static SessionFactory inicializarSessionFactory(Properties props) {
        try {
            Configuration configuration = new Configuration().configure(); // hibernate.cfg.xml
            configuration.addProperties(props);
            //agregar entidades
            configuration.addAnnotatedClass(Repuesto.class);
            configuration.addAnnotatedClass(Stock.class);
            configuration.addAnnotatedClass(AuditoriaVenta.class);
            configuration.addAnnotatedClass(DetalleRetiro.class);
            configuration.addAnnotatedClass(Pago.class);
            configuration.addAnnotatedClass(Usuario.class);
            configuration.addAnnotatedClass(VentaRepuesto.class);
            configuration.addAnnotatedClass(NotaRetiro.class);

            SessionFactory factory = configuration.buildSessionFactory();
            System.out.println("✅ SessionFactory creado exitosamente.");
            return factory;
        } catch (Throwable ex) {
            System.err.println("❌ Error al crear SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Devuelve una nueva Hibernate Session desde SessionFactory.
     * ¡NO compartir entre hilos!
     * Funciona con la config de hibernarte.cfg.xml.
     */
    public static Session getHibernateSession() {
        return sessionFactory.openSession();
    }

    /**
     * Más robusto para usar varios hilos que getHibernateSession().
     * Luego de usar cerrar en un finally con cerrarHibernateSessionThreadSafe().
     */
    public static Session getHibernateSessionThreadSafe(){
        Session session = threadLocalSession.get();
        if (session == null || !session.isOpen()) {
            session = sessionFactory.openSession();
            threadLocalSession.set(session);
        }
        return session;
    }

    public static void cerrarHibernateSessionThreadSafe() {
        Session session = threadLocalSession.get();
        threadLocalSession.remove();
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    /**
     * Cierra todas las fábricas al apagar la aplicación.
     */
    //todo: agregar esto al cerrar app
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
