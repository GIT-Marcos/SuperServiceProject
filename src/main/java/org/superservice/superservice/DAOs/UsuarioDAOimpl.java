package org.superservice.superservice.DAOs;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.superservice.superservice.entities.Usuario;
import org.superservice.superservice.excepciones.DuplicateUserException;
import org.superservice.superservice.utilities.Util;


/**
 * @author Usuario
 */
public class UsuarioDAOimpl implements UsuarioDAO {

    private Session session;

    @Override
    public List<Usuario> todosUsuarios() {
        session = Util.getHibernateSession();
        List<Usuario> usuarios = session.createQuery("SELECT DISTINCT u FROM Usuario u",
                Usuario.class).list();
        session.close();
        return usuarios;
    }

    @Override
    public Usuario buscarUsuario(Long id) {
        session = Util.getHibernateSession();
        Usuario usuario = session.find(Usuario.class, id);
        session.close();
        return usuario;
    }

    @Override
    public Usuario buscarPorNombre(String nombre) {
        session = Util.getHibernateSession();
        Usuario usuario = session.createQuery("SELECT DISTINCT u FROM Usuario u " +
                                "WHERE u.nombre = :nombre",
                        Usuario.class).setParameter("nombre", nombre)
                .getSingleResultOrNull();
        session.close();
        return usuario;
    }

    @Override
    public Usuario cargarUsuario(Usuario usuario) throws DuplicateUserException {
        session = Util.getHibernateSession();
        try {
            session.beginTransaction();
            session.persist(usuario);
            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            session.getTransaction().rollback();
            if (e.getCause() instanceof PSQLException) {
                throw new DuplicateUserException("El nombre de usuario: " + usuario.getNombre() +
                        " ya existe en bd.", e);
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return usuario;
    }

    @Override
    public Usuario modificarUsuario(Usuario usuario) {
        session = Util.getHibernateSession();
        try {
            session.beginTransaction();
            session.merge(usuario);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return usuario;
    }

    @Override
    public Boolean eliminarUsuario(Usuario usuario) {
        session = Util.getHibernateSession();
        try {
            session.beginTransaction();
            session.remove(usuario);
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
