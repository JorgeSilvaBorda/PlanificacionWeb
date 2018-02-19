
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    Connection conn;
    public void conectar(){
	//String rutaArchivo = "C:/Users/jsilvab/Documents/NetBeansProjects/Planificacion/base.mdb";
	String rutaArchivo = "C:/tomcat7/base.mdb";
	try{
	    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	    String baseDatos = "jdbc:ucanaccess://" + rutaArchivo;
	    conn = DriverManager.getConnection(baseDatos, "", "");
	}catch (ClassNotFoundException | SQLException ex) {
	    System.out.println("No se puede abrir la conexion al archivo mdb.");
	    System.out.println(ex);
	}
    }
    
    public void cerrar(){
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println("Error al cerrar la conexion.");
	    System.out.println(ex);
	}
	conn = null;
    }
    
    public ResultSet ejecutarQuery(String query){
	try {
	    conectar();
	    Statement st = conn.createStatement();
	    return st.executeQuery(query);
	} catch (SQLException ex) {
	    System.out.println("Error al ejecutar la query.");
	    System.out.println(ex);
	}
	return null;
    }
    
    public void ejecutarUpdate(String query){
	try {
	    conectar();
	    Statement st = conn.createStatement();
	    st.executeUpdate(query);
	} catch (SQLException ex) {
	    System.out.println("Error al ejecutar la query.");
	    System.out.println(ex);
	}
    }
}
