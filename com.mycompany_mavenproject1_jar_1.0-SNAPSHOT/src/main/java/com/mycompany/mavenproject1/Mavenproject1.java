

package com.mycompany.mavenproject1;

import entidades.proveedores;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import vistas.ventanaProveedores;
//import vistas.ventanaProducto;

/**
 *
 * @author LENOVO
 */
public class Mavenproject1 {
    
  private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        ventanaProveedores ventana = new ventanaProveedores();
        ventana.setVisible(true);
    }
    
    public static void guardarProveedores() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        
       proveedores proveedores = new proveedores();
       proveedores.setNombre_proveedor("enano");
       proveedores.setNombre_contacto("cristian");
       proveedores.setDireccion("lerdo");
       proveedores.setCodigo_postal("17934");
       proveedores.setPais("Francia");
       proveedores.setTipo_pago("contado");
       proveedores.setTelefono("37515841");
       proveedores.setEmail("enano@gmail.com");
       
       session.save(proveedores);
      
    session.getTransaction().commit();
    session.close();
    }
}
