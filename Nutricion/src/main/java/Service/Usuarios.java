/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import com.mysql.cj.xdevapi.SessionFactory;
import java.lang.module.Configuration;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.transaction.Transaction;
import javax.websocket.Session;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import modelBD.UsuariosBD;

/**
 *
 * @author LENOVO
 */
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
                        @FormParam("contraseña") String contraseña,
                        @FormParam("rol_usuario") String rol_usuario,
                        @FormParam("estado") int estado){

    if (nombre == null || nombre.trim().isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"nombre es obligatorio.\"}")
                .build();
    }
    if (direccion == null || direccion.trim().isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"direccion es obligatoria.\"}")
                .build();
    }
    if (correo_electronico == null || correo_electronico.trim().isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"correo_electronico es obligatorio.\"}")
                .build();
    }
    if (contraseña == null || contraseña.trim().isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"contraseña es obligatorio.\"}")
                .build();
    }
    if (rol_usuario == null || rol_usuario.trim().isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"correo es obligatorio.\"}")
                .build();
    }
    if (estado == 0) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"activo es obligatorio y debe ser mayor a 0.\"}")
                .build();
    }
}