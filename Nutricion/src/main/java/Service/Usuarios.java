/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import java.util.Date;
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

        // Asignamos "1" a activo independientemente del valor recibido
        activo = 1; // Establecer siempre el valor de 'activo' como 1

        // Obtener la fecha actual solo con el año, mes y día
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);   // Establecer la hora a medianoche
        calendar.set(Calendar.MINUTE, 0);        // Establecer los minutos a 0
        calendar.set(Calendar.SECOND, 0);        // Establecer los segundos a 0
        calendar.set(Calendar.MILLISECOND, 0);   // Establecer los milisegundos a 0
        Date fecha_ingreso = calendar.getTime();  // Obtiene la fecha sin la hora

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
            usuario.setActivo(activo == 1);  // Esto siempre será verdadero, ya que 'activo' es 1
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
}
