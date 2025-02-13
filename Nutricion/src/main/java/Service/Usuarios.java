/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;


import com.google.gson.Gson;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import modelBD.UsuariosBD;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

@Path("Usuarios")
public class Usuarios {
    private static SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @POST
    @Path("guardar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardar(@FormParam("nombre") String nombre,
                            @FormParam("direccion") String direccion,
                            @FormParam("correo_electronico") String correo_electronico,
                            @FormParam("contrasena") String contrasena,
                            @FormParam("rol_usuario") String rol_usuario,
                            @FormParam("activo") int activo) {
        // Validación de parámetros
        if (nombre == null || nombre.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El nombre es obligatorio.\"}")
                    .build();
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La dirección es obligatoria.\"}")
                    .build();
        }
        if (correo_electronico == null || correo_electronico.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El correo electrónico es obligatorio.\"}")
                    .build();
        }
        if (contrasena == null || contrasena.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La contraseña es obligatoria.\"}")
                    .build();
        }
        if (rol_usuario == null || rol_usuario.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El rol de usuario es obligatorio.\"}")
                    .build();
        }

        activo = 1; // Establecer siempre el valor de 'activo' como 1

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);   
        calendar.set(Calendar.MINUTE, 0);        
        calendar.set(Calendar.SECOND, 0);        
        calendar.set(Calendar.MILLISECOND, 0);   
        Date fecha_ingreso = calendar.getTime();  

        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Crear un nuevo objeto UsuarioBD
            UsuariosBD usuario = new UsuariosBD();
            usuario.setNombre(nombre);
            usuario.setDireccion(direccion);
            usuario.setCorreo_electronico(correo_electronico);
            usuario.setContrasena(contrasena);
            usuario.setRol_usuario(rol_usuario);
            usuario.setActivo(activo == 1);  
            usuario.setFecha_ingreso(fecha_ingreso);  // Asignar la fecha de ingreso sin la hora

            // Guardar el usuario en la base de datos
            session.save(usuario);
            transaction.commit();

            return Response.status(Response.Status.OK)
                    .entity("{\"mensaje\":\"Usuario guardado correctamente.\"}")
                    .build();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al guardar usuario: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            session.close();
        }
    }

    @GET
    @Path("eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@QueryParam("id") Long id) {
        Session session = null;
        Transaction transaction = null;

        if (id == null || id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El campo 'id' es obligatorio y debe ser mayor a cero.\"}")
                    .build();
        }

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            UsuariosBD usuario = session.get(UsuariosBD.class, id);

            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Usuario no encontrado con id: " + id + "\"}")
                        .build();
            }

            usuario.setActivo(false);
            session.update(usuario);
            transaction.commit();

            return Response.ok("{\"message\":\"Usuario desactivado exitosamente\"}")
                    .build();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al desactivar al usuario: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    @POST
   @Path("editar")
   @Produces(MediaType.APPLICATION_JSON)
   public Response editar(@FormParam("id") long id,
           @FormParam("nombre") String nombre,
           @FormParam("direccion") String direccion,
           @FormParam("correo_electronico") String correo_electronico,
           @FormParam("contrasena") String contrasena,
           @FormParam("rol_usuario") String rol_usuario) {

      Session session = null;
      Transaction transaction = null;

      if (id <= 0) {
         return Response.status(Response.Status.BAD_REQUEST)
                 .entity("{\"error\":\"El campo 'id' es obligatorio y debe ser mayor a cero.\"}")
                 .build();
      }

      try {

         session = sessionFactory.openSession();
         transaction = session.beginTransaction();

         UsuariosBD usuario = session.get(UsuariosBD.class, id);
         if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Usuario no encontrado.\"}")
                    .build();
         }

         if (nombre != null && !nombre.trim().isEmpty()) {
            usuario.setNombre(nombre);
         }
         
         if (direccion != null && !direccion.trim().isEmpty()) {
            usuario.setDireccion(direccion);
         }
         
         if (correo_electronico != null && !correo_electronico.trim().isEmpty()) {
            usuario.setCorreo_electronico(correo_electronico);
         }
         
         if (contrasena != null && !contrasena.trim().isEmpty()) {
            usuario.setContrasena(contrasena);
         }
         
         if (rol_usuario != null && !rol_usuario.trim().isEmpty()) {
            usuario.setRol_usuario(rol_usuario);
         }
         

         session.update(usuario);
         transaction.commit();

         return Response.ok("{\"message\":\"Usuario actualizado exitosamente\"}")
                 .build();
      } catch (Exception e) {
         if (transaction != null) {
            transaction.rollback();
         }
         e.printStackTrace();
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"error\":\"Error al actualizar el Usuario: " + e.getMessage() + "\"}")
                 .build();
      } finally {
         if (session != null) {
            session.close();
         }
      }
   }
   
    @GET
    @Path("mostrar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response mostrar() {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            // Crear una consulta HQL para obtener todos los usuarios
            String hql = "FROM UsuariosBD";
            List<UsuariosBD> usuarios = session.createQuery(hql, UsuariosBD.class).getResultList();

            // Convertir la lista de usuarios a JSON
            Gson gson = new Gson();
            String json = gson.toJson(usuarios);

            return Response.ok(json).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al obtener los usuarios: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    } 
   
   @GET
    @Path("activar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activar(@QueryParam("id") Long id) {
        Session session = null;
        Transaction transaction = null;

        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El campo 'id' es obligatorio..\"}")
                    .build();
        }

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            UsuariosBD usuario = session.get(UsuariosBD.class, id);

            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Usuario no encontrado con id: " + id + "\"}")
                        .build();
            }

            usuario.setActivo(true);
            session.update(usuario);
            transaction.commit();

            return Response.ok("{\"message\":\"Usuario reactivado exitosamente\"}")
                    .build();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al desactivar al usuario: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
