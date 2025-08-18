package org.superservice.superservice.services;

import org.hibernate.HibernateException;
import org.mindrot.jbcrypt.BCrypt;
import org.superservice.superservice.DAOs.UsuarioDAO;
import org.superservice.superservice.DAOs.UsuarioDAOimpl;
import org.superservice.superservice.entities.Usuario;
import org.superservice.superservice.excepciones.DuplicateUserException;
import java.util.List;

/**
 * @author Usuario
 */
public class UsuarioServ {

    private final UsuarioDAO dao = new UsuarioDAOimpl();

    public List<Usuario> todosUsuarios() {
        return dao.todosUsuarios();
    }

    public Usuario buscarUsuario(Long id) {
        return dao.buscarUsuario(id);
    }

    public void cargarUsuario(Usuario usuario) throws DuplicateUserException {
        if (usuario == null) {
            throw new NullPointerException("usuario a cargar nulo.");
        }
        String hashed = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
        usuario.setPassword(hashed);
        try {
            dao.cargarUsuario(usuario);
        } catch (DuplicateUserException e) {
            throw e;
        }
    }

    public Usuario loguear(String nombre, String inputPass) {
        if (nombre == null || inputPass == null) {
            throw new NullPointerException("nombre o contraseña nula al loguear.");
        }
        Usuario usuario = dao.buscarPorNombre(nombre);
        if (usuario == null) {
            throw new HibernateException("No se encontró usuario con nombre " + nombre);
        }
        if (!BCrypt.checkpw(inputPass, usuario.getPassword())) {
            throw new HibernateException("Contraseña incorrecta para el usuario: " + nombre);
        }
        return usuario;
    }
}
