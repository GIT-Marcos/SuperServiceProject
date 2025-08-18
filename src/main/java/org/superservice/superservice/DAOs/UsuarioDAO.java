package org.superservice.superservice.DAOs;


import org.superservice.superservice.entities.Usuario;
import org.superservice.superservice.excepciones.DuplicateUserException;

import java.util.List;

/**
 *
 * @author Usuario
 */
public interface UsuarioDAO {
    
    //LECTURA
    
    List<Usuario> todosUsuarios();
    
    Usuario buscarUsuario(Long id);

    Usuario buscarPorNombre(String nombre);

    //ESCRITURA
    
    Usuario cargarUsuario(Usuario usuario) throws DuplicateUserException;
    
    Usuario modificarUsuario(Usuario usuario);
    
    Boolean eliminarUsuario(Usuario usuario);
}
