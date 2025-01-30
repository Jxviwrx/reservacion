/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;


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


@Path("usuario")
public class Usuarios {
    
    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    
    @POST
    @Path("guardar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardar(@FormParam("nombre") String nombre,
                        @FormParam("direccion") String direccion,
                        @FormParam("correo_electronico") String correo_electronico,
                        @FormParam("contraseña") String contraseña,
                        @FormParam("rol_usuario") String rol_usuario) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"nombre_usuario es obligatorio.\"}")
                .build();
        }
        if (direccion == null || direccion.trim().isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"nombre_usuario es obligatorio.\"}")
                .build();
        }
        if (correo_electronico == null || correo_electronico.trim().isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"nombre_usuario es obligatorio.\"}")
                .build();
        }
        if (contraseña == null || contraseña.trim().isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"nombre_usuario es obligatorio.\"}")
                .build();
        }
        if (rol_usuario == null || rol_usuario.trim().isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"nombre_usuario es obligatorio.\"}")
                .build();
        }
 
        
        // Guardar usuario en la base de datos
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            UsuariosBD Usuarios = new UsuariosBD();
            session.save(Usuarios);
            tx.commit();
            return Response.status(Response.Status.OK)
                    .entity("{\"mensaje\":\"Usuario guardado correctamente.\"}")
                    .build();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al guardar usuario.\"}")
                    .build();
        } finally {
            session.close();
        }
    }
}
