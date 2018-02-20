
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jorge Silva Borda
 */
@WebServlet(urlPatterns = {"/Roles"})
public class Roles extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();

	JSONObject entrada = new JSONObject(request.getParameter("datos"));

	switch (entrada.getString("tipo")) {
	    case "cargarRoles":
		out.print(cargarRoles());
		break;
	    case "getRolId":
		out.print(getRolId(entrada.getString("idRol")));
		break;
	    case "insertarRol":
		out.print(insertarRol(entrada));
		break;
	    case "grabarRol":
		out.print(grabarRol(entrada));
		break;
	    case "eliminarRol":
		out.print(eliminarRol(entrada));
		break;
	}
    }

    public JSONObject cargarRoles() {
	String query = "SELECT * FROM ROL";
	JSONObject salida = new JSONObject();
	Conexion c = new Conexion();
	ResultSet rs = c.ejecutarQuery(query);
	String tab = "";
	int cont = 1;
	try {
	    while (rs.next()) {
		tab += "<tr>";
		tab += "<td>" + cont + "</td>";
		tab += "<td>" + rs.getObject("ID") + "</td>";
		tab += "<td>" + rs.getObject("NOMBREROL") + "</td>";
		tab += "<td style='width: 120px;' class='text-center'>"
			+ "<div class=\"btn-group\">\n"
			+ "  <button onclick='edicionRol(\"" + rs.getObject("ID") + "\");' type=\"button\" class=\"btn btn-primary btn-xs\">Editar</button>\n"
			+ "  <button onclick='eliminarRol(\"" + rs.getObject("ID") + "\");' type=\"button\" class=\"btn btn-danger btn-xs\">Eliminar</button>\n"
			+ "</div>"
			+ "</td>";
		tab += "</tr>";
		cont++;
	    }

	    salida.put("estado", "ok");
	    salida.put("tabla", tab);
	    c.cerrar();
	    return salida;
	} catch (SQLException | JSONException ex) {
	    System.out.println("No se puede recorrer el ResultSet");
	    System.out.println(ex);
	    salida.put("estado", "error");
	    salida.put("error", ex);
	}
	return salida;
    }

    public JSONObject getRolId(String idRol) {
	String query = "SELECT * FROM ROL WHERE ID = " + idRol;
	Conexion con = new Conexion();
	ResultSet rs = con.ejecutarQuery(query);
	JSONObject rol;
	JSONObject salida = new JSONObject();
	try {
	    while (rs.next()) {
		rol = new JSONObject();
		rol.put("ID", rs.getObject("ID"));
		rol.put("NOMBRE", rs.getObject("NOMBREROL"));
		salida.put("rol", rol);
	    }
	    salida.put("estado", "ok");
	} catch (SQLException | JSONException ex) {
	    System.out.println("No se puede obtener el rol por ID");
	    System.out.println(ex);
	    salida.put("estado", "error");
	    salida.put("error", ex);
	}
	con.cerrar();
	return salida;
    }

    public JSONObject insertarRol(JSONObject json) {
	JSONObject rol = json.getJSONObject("rol");
	Conexion con = new Conexion();
	String query = "INSERT INTO ROL(NOMBREROL) VALUES("
		+ "'" + rol.getString("nombre") + "'"
		+ ")";
	con.ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	con.cerrar();
	return salida;
    }

    public JSONObject grabarRol(JSONObject json) {
	JSONObject rol = json.getJSONObject("rol");
	Conexion con = new Conexion();
	String query = "UPDATE ROL SET "
		+ "NOMBREROL = '" + rol.getString("nombre") + "' "
		+ "WHERE ID = " + rol.getString("id")
		+ "";
	con.ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	con.cerrar();
	return salida;
    }

    public JSONObject eliminarRol(JSONObject json) {
	String query = "DELETE FROM ROL WHERE ID = " + json.getString("id");
	Conexion c = new Conexion();
	c.ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	return salida;
    }
}
