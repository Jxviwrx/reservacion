package Service;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        // Registra el filtro CORS
        resources.add(Service.CorsFilter.class);
        // Registra tus recursos (servicios) aquí
        resources.add(Service.Login.class);
        resources.add(Service.Reservaciones.class);
        resources.add(Service.Usuarios.class);
    }
}