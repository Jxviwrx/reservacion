/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import modelBD.UsuariosBD;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


@Path("Login")
@Produces(MediaType.APPLICATION_JSON)
public class Login {
private static SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @POST
    @Path("usuario")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response autenticar(@FormParam("nombre") String nombre,
                               @FormParam("contraseña") String contraseña) {
        Session session = null;

        if (nombre == null || nombre.trim().isEmpty() ||
            contraseña == null || contraseña.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"Nombre de usuario y contraseña son obligatorios.\"}")
                           .build();
        }

        try {
            session = sessionFactory.openSession();
            
            String hql = "FROM UsuariosBD u WHERE u.nombre = :nombre AND u.contraseña = :contraseña";
            UsuariosBD Usuarios = (UsuariosBD) session.createQuery(hql)
                                                     .setParameter("nombre", nombre)
                                                     .setParameter("contraseña", contraseña)
                                                     .uniqueResult();

            if (Usuarios == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .entity("{\"error\":\"Usuario o contraseña incorrectos.\"}")
                               .build();
            }

            return Response.ok("{\"message\":\"Login exitoso\", \"id_usuario\": " + Usuarios.getId() + "}")
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"Error al procesar la solicitud de login.\"}")
                           .build();
        } finally {
            if (session != null) session.close();
        }
    }
}


