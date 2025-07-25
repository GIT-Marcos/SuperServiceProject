package org.superservice.superservice.DAOs;


import org.superservice.superservice.entities.Usuario;

import java.util.List;

/**
 *
 * @author Usuario
 */
public interface UsuarioDAO {
    
    //LECTURA
    
    List<Usuario> todosUsuarios();
    
    Usuario buscarUsuario(Long id);
    
    
    //ESCRITURA
    
    Usuario cargarUsuario(Usuario usuario);
    
    Usuario modificarUsuario(Usuario usuario);
    
    Boolean eliminarUsuario(Usuario usuario);
}
