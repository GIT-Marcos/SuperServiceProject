package org.superservice.superservice.services;

import org.superservice.superservice.DAOs.UsuarioDAO;
import org.superservice.superservice.DAOs.UsuarioDAOimpl;
import org.superservice.superservice.entities.Usuario;

import java.util.List;

/**
 *
 * @author Usuario
 */
public class UsuarioServ {

    private final UsuarioDAO dao = new UsuarioDAOimpl();

    public List<Usuario> todosUsuarios() {
        return dao.todosUsuarios();
    }

    public Usuario buscarUsuario(Long id){
        return dao.buscarUsuario(id);
    }
    
    public Usuario cargarUsuario(Usuario usuario){
        return dao.cargarUsuario(usuario);
    }
    
}
