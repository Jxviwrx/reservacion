/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import modelBD.UsuariosBD;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

@Path("usuario")
public class Usuarios {
    
    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    
    @POST
    @Path("guardar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardar(UsuariosBD usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El nombre es obligatorio.\"}")
                    .build();
        }
        if (usuario.getDireccion() == null || usuario.getDireccion().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La dirección es obligatoria.\"}")
                    .build();
        }
        if (usuario.getCorreo_electronico() == null || usuario.getCorreo_electronico().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El correo electrónico es obligatorio.\"}")
                    .build();
        }
        if (usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La contraseña es obligatoria.\"}")
                    .build();
        }
        if (usuario.getRol_usuario() == null || usuario.getRol_usuario().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El rol de usuario es obligatorio.\"}")
                    .build();
        }
        
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(usuario);
            tx.commit();
            return Response.status(Response.Status.OK)
                    .entity("{\"mensaje\":\"Usuario guardado correctamente.\"}")
                    .build();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al guardar usuario: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            session.close();
        }
    }

    @PUT
    @Path("actualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizar(UsuariosBD usuario) {
        if (usuario.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El ID del usuario es obligatorio para actualizar.\"}")
                    .build();
        }

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            UsuariosBD usuarioExistente = session.get(UsuariosBD.class, usuario.getId());
            if (usuarioExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Usuario no encontrado.\"}")
                        .build();
            }
            usuarioExistente.setNombre(usuario.getNombre());
            usuarioExistente.setDireccion(usuario.getDireccion());
            usuarioExistente.setCorreo_electronico(usuario.getCorreo_electronico());
            usuarioExistente.setContraseña(usuario.getContraseña());
            usuarioExistente.setRol_usuario(usuario.getRol_usuario());

            session.update(usuarioExistente);
            tx.commit();
            return Response.status(Response.Status.OK)
                    .entity("{\"mensaje\":\"Usuario actualizado correctamente.\"}")
                    .build();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al actualizar usuario: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            session.close();
        }
    }

    @DELETE
    @Path("eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@javax.ws.rs.PathParam("id") Long id) {
            if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El ID del usuario es obligatorio para eliminar.\"}")
                    .build();
        }

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            UsuariosBD usuarioExistente = session.get(UsuariosBD.class, id);
            if (usuarioExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Usuario no encontrado.\"}")
                        .build();
            }

            session.delete(usuarioExistente);
            tx.commit();
            return Response.status(Response.Status.OK)
                    .entity("{\"mensaje\":\"Usuario eliminado correctamente.\"}")
                    .build();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al eliminar usuario: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            session.close();
        }
    }

    @GET
    @Path("obtener/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtener(@javax.ws.rs.PathParam("id") Long id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El ID del usuario es obligatorio para obtener información.\"}")
                    .build();
        }

        Session session = sessionFactory.openSession();
        try {
            UsuariosBD usuario = session.get(UsuariosBD.class, id);
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Usuario no encontrado.\"}")
                        .build();
            }
            return Response.status(Response.Status.OK)
                    .entity(usuario)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al obtener usuario: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            session.close();
        }
    }
}
