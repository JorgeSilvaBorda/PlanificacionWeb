
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
public class Recursos extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();

	JSONObject entrada = new JSONObject(request.getParameter("datos"));

	switch (entrada.getString("tipo")) {
	    case "cargarRecursos":
		out.print(cargarRecursos());
		break;
	    case "getRecursoId":
		out.print(getRecursoId(entrada.getString("idRecurso")));
		break;
	    case "insertarRecurso":
		out.print(insertarRecurso(entrada));
		break;
	    case "grabarRecurso":
		out.print(grabarRecurso(entrada));
		break;
	    case "eliminarRecurso":
		out.print(eliminarRecurso(entrada));
		break;
	}
    }

    public JSONObject cargarRecursos() {
	String query = "SELECT * FROM RECURSO";
	JSONObject salida = new JSONObject();
	Conexion c = new Conexion();
	ResultSet rs = c.ejecutarQuery(query);
	String tab = "";
	int cont = 1;
	try {
	    while (rs.next()) {
		String rut = rs.getString("RUT");
		if (rut == null) {
		    rut = "";
		}
		tab += "<tr>";
		tab += "<td>" + cont + "</td>";
		tab += "<td>" + rs.getObject("ID") + "</td>";
		tab += "<td>" + rut + "</td>";
		tab += "<td>" + rs.getObject("NOMBRE") + "</td>";
		tab += "<td style='width: 80px;'>$ " + rs.getObject("COSTO") + "</td>";
		tab += "<td style='width: 80px;'>$ " + rs.getObject("COSTOUSO") + "</td>";
		tab += "<td style='width: 120px;' class='text-center'>"
			+ "<div class=\"btn-group\">\n"
			+ "  <button onclick='edicionRecurso(\"" + rs.getObject("ID") + "\");' type=\"button\" class=\"btn btn-primary btn-xs\">Editar</button>\n"
			+ "  <button onclick='eliminarRecurso(\"" + rs.getObject("ID") + "\");' type=\"button\" class=\"btn btn-danger btn-xs\">Eliminar</button>\n"
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

    public JSONObject getRecursoId(String idRecurso) {
	String query = "SELECT * FROM RECURSO WHERE ID = " + idRecurso;
	Conexion con = new Conexion();
	ResultSet rs = con.ejecutarQuery(query);
	JSONObject recurso;
	JSONObject salida = new JSONObject();
	try {
	    while (rs.next()) {
		recurso = new JSONObject();
		recurso.put("ID", rs.getObject("ID"));
		recurso.put("NOMBRE", rs.getObject("NOMBRE"));
		recurso.put("RUT", rs.getObject("RUT"));
		recurso.put("COSTO", rs.getObject("COSTO"));
		recurso.put("COSTOUSO", rs.getObject("COSTOUSO"));
		salida.put("recurso", recurso);
	    }
	    salida.put("estado", "ok");
	} catch (SQLException | JSONException ex) {
	    System.out.println("No se puede obtener el recurso por ID");
	    System.out.println(ex);
	    salida.put("estado", "error");
	    salida.put("error", ex);
	}
	con.cerrar();
	return salida;
    }

    public JSONObject insertarRecurso(JSONObject json) {
	JSONObject recurso = json.getJSONObject("recurso");
	Conexion con = new Conexion();
	String query = "INSERT INTO RECURSO(RUT, NOMBRE, COSTO, COSTOUSO) VALUES("
		+ "'" + recurso.getString("rut") + "', "
		+ "'" + recurso.getString("nombre") + "', "
		+ "'" + recurso.getString("costo") + "', "
		+ "'" + recurso.getString("costouso") + "'"
		+ ")";
	con.ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	con.cerrar();
	return salida;
    }

    public JSONObject grabarRecurso(JSONObject json) {

	JSONObject recurso = json.getJSONObject("recurso");
	Conexion con = new Conexion();
	String query = "UPDATE RECURSO SET "
		+ "RUT = '" + recurso.getString("rut") + "', "
		+ "NOMBRE = '" + recurso.getString("nombre") + "', "
		+ "COSTO = '" + recurso.getString("costo") + "', "
		+ "COSTOUSO = '" + recurso.getString("costouso") + "'"
		+ "WHERE ID = " + recurso.getString("id")
		+ "";
	con.ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	con.cerrar();
	return salida;
    }

    public JSONObject eliminarRecurso(JSONObject json) {

	String query = "DELETE FROM RECURSO WHERE ID = " + json.getString("id");
	Conexion c = new Conexion();
	c.ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	return salida;
    }

}
