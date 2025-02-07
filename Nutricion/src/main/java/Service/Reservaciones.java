/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.logging.Logger;
import modelBD.ReservacionesBD;

@Path("Reservaciones")
public class Reservaciones {
    
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static final Logger LOGGER = Logger.getLogger(Reservaciones.class.getName());

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            LOGGER.severe("Error al inicializar Hibernate: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    @POST
    @Path("guardar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardar(@FormParam("nombre_paciente") String nombre_paciente,
                            @FormParam("direccion_paciente") String direccion_paciente,
                            @FormParam("telefono_paciente") String telefono_paciente,
                            @FormParam("correo_electronico") String correo_electronico,
                            @FormParam("fecha") String fecha,
                            @FormParam("hora") String hora,
                            @FormParam("motivo_consulta") String motivo_consulta) {

        if (nombre_paciente == null || nombre_paciente.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El nombre del paciente es obligatorio.\"}").build();
        }
        if (direccion_paciente == null || direccion_paciente.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La dirección del paciente es obligatoria.\"}").build();
        }
        if (telefono_paciente == null || telefono_paciente.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El teléfono del paciente es obligatorio.\"}").build();
        }
        if (correo_electronico == null || correo_electronico.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El correo electrónico es obligatorio.\"}").build();
        }
        if (fecha == null || fecha.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La fecha de la reservación es obligatoria.\"}").build();
        }
        if (hora == null || hora.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La hora de la reservación es obligatoria.\"}").build();
        }
        if (motivo_consulta == null || motivo_consulta.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El motivo de consulta es obligatorio.\"}").build();
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            ReservacionesBD reservacion = new ReservacionesBD();
            reservacion.setNombre_paciente(nombre_paciente);
            reservacion.setDireccion_paciente(direccion_paciente);
            reservacion.setTelefono_paciente(telefono_paciente);
            reservacion.setCorreo_electronico(correo_electronico);
            reservacion.setFecha(fecha);
            reservacion.setHora(hora);
            reservacion.setMotivo_consulta(motivo_consulta);

            session.save(reservacion);
            transaction.commit();

            return Response.ok("{\"mensaje\":\"Reservación guardada correctamente.\"}").build();
        } catch (Exception e) {
            LOGGER.severe("Error al guardar la reservación: " + e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al guardar la reservación.\"}").build();
        }
    }
    @DELETE
    @Path("eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@PathParam("id") Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            ReservacionesBD reservacion = session.get(ReservacionesBD.class, id);
            
            if (reservacion == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Reservación no encontrada.\"}").build();
            }
            
            session.delete(reservacion);
            transaction.commit();
            return Response.ok("{\"mensaje\":\"Reservación eliminada correctamente.\"}").build();
        } catch (Exception e) {
            LOGGER.severe("Error al eliminar la reservación: " + e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al eliminar la reservación.\"}").build();
        }
    }
    @PUT
    @Path("editar/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
        public Response editar(@PathParam("id") int id,
                       @FormParam("nombre_paciente") String nombre_paciente,
                       @FormParam("direccion_paciente") String direccion_paciente,
                       @FormParam("telefono_paciente") String telefono_paciente,
                       @FormParam("correo_electronico") String correo_electronico,
                       @FormParam("fecha") String fecha,
                       @FormParam("hora") String hora,
                       @FormParam("motivo_consulta") String motivo_consulta) {

    try (Session session = sessionFactory.openSession()) {
        Transaction transaction = session.beginTransaction();

        ReservacionesBD reservacion = session.get(ReservacionesBD.class, id);
        if (reservacion == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Reservación no encontrada.\"}").build();
        }

        reservacion.setNombre_paciente(nombre_paciente);
        reservacion.setDireccion_paciente(direccion_paciente);
        reservacion.setTelefono_paciente(telefono_paciente);
        reservacion.setCorreo_electronico(correo_electronico);
        reservacion.setFecha(fecha);
        reservacion.setHora(hora);
        reservacion.setMotivo_consulta(motivo_consulta);

        session.update(reservacion);
        transaction.commit();

        return Response.ok("{\"mensaje\":\"Reservación actualizada correctamente.\"}").build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\":\"Error al actualizar la reservación.\"}").build();
        }
    }
}
