
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jorge Silva Borda
 */
public class Otros extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();

	JSONObject entrada = new JSONObject(request.getParameter("datos"));

	switch (entrada.getString("tipo")) {
	    case "getNomClienteId":
		out.print(getNomClienteId(entrada));
		break;
	    case "getNomJefeProyectoId":
		out.print(getNomJefeProyectoId(entrada));
		break;
	    case "cargarCombo":
		out.print(cargarCombo(entrada));
		break;
	}
    }

    public JSONObject getNomClienteId(JSONObject json) {
	String query = "SELECT * FROM CLIENTE WHERE ID = " + json.getString("idCliente");
	Conexion c = new Conexion();
	ResultSet rs = c.ejecutarQuery(query);
	JSONObject salida = new JSONObject();
	try {
	    while (rs.next()) {
		salida.append("nombre", rs.getString("NOMBRECLIENTE"));
	    }
	    salida.put("estado", "ok");
	    System.out.println("Salida de getNomClienteId:");
	    System.out.println(salida);
	    return salida;
	} catch (SQLException | JSONException ex) {
	    System.out.println("No se puede obtener el nombre del cliente por ID");
	    System.out.println(ex);
	    salida.append("estado", "error");
	    salida.append("error", ex);
	    return salida;
	}
    }

    public JSONObject getNomJefeProyectoId(JSONObject json) {
	System.out.println(json);
	String query = "SELECT * FROM RECURSO WHERE ID = " + json.getString("idJefeProyecto");
	Conexion c = new Conexion();
	ResultSet rs = c.ejecutarQuery(query);
	JSONObject salida = new JSONObject();
	try {
	    while (rs.next()) {
		salida.append("nombre", rs.getString("NOMBRE"));
	    }
	    salida.put("estado", "ok");
	    return salida;
	} catch (SQLException | JSONException ex) {
	    System.out.println("No se puede obtener el nombre del jefe de proyecto por ID");
	    System.out.println(ex);
	    salida.put("estado", "error");
	    salida.put("error", ex);
	    return salida;
	}
    }

    public JSONObject cargarCombo(JSONObject json) {
	JSONObject salida = new JSONObject();
	String combo = "";
	switch (json.getString("cual")) {
	    case "cliente":
		combo = Util.armarCombo("Seleccione", "0", "NOMBRECLIENTE", "ID", "SELECT * FROM CLIENTE");
		break;
	    case "jefeproyecto":
		combo = Util.armarCombo("Seleccione", "0", "NOMBRE", "ID", "SELECT * FROM RECURSO");
		break;
	}
	salida.put("estado", "ok");
	salida.put("combo", combo);
	return salida;
    }
}
