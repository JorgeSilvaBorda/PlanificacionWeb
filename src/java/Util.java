
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jorge Silva Borda
 */
public class Util {
    public static String armarCombo(String primerItemText, String primerItemValue, String campoTexto, String campoValue, String query){
	Conexion con = new Conexion();
	ResultSet rs = con.ejecutarQuery(query);
	String salida = "";
	try{
	    if(!primerItemValue.equals(campoValue) && ! primerItemText.equals(campoTexto)){
		salida += "<option value='" + primerItemValue + "'>" + primerItemText + "</option>";
	    }
	    while(rs.next()){
		salida += "<option value='" + rs.getString(campoValue) + "'>" + rs.getString(campoTexto) + "</option>";
	    }
	    return salida;
	}catch (SQLException ex) {
	    System.out.println("No se puede armar el combo:");
	    System.out.println(ex);
	}
	return "";
    }
}
