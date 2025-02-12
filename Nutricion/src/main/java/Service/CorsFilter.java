package Service;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider // Marca esta clase como un proveedor de JAX-RS
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        // Permite solicitudes desde http://localhost:5173 (tu frontend React)
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:5173");

        // Permite los métodos HTTP especificados
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        // Permite los encabezados especificados
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // Permite el envío de credenciales (si es necesario)
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");

        // Permite exponer encabezados personalizados en la respuesta
        responseContext.getHeaders().add("Access-Control-Expose-Headers", "Authorization");
    }
}