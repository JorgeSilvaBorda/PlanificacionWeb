
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
public class Proyectos extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();

	JSONObject entrada = new JSONObject(request.getParameter("datos"));
	switch (entrada.getString("tipo")) {
	    case "ingresarProyecto":
		out.print(ingresarProyecto(entrada));
		break;
	    case "cargarProyectos":
		out.print(cargarProyectos());
		break;
	    case "getProyectoId":
		out.print(getProyectoId(entrada));
		break;
	    case "guardarProyecto":
		out.print(guardarProyecto(entrada));
		break;
	    case "eliminarProyecto":
		out.print(eliminarProyecto(entrada.getString("idProyecto")));
		break;
	}
    }

    public JSONObject ingresarProyecto(JSONObject json) {
	JSONObject proyecto = json.getJSONObject("proyecto");
	java.sql.Date dt = java.sql.Date.valueOf(proyecto.getString("fechaIni"));
	String query = "INSERT INTO PROYECTO(NOMBRE, FECHAINI, IDCLIENTE, IDJP)VALUES("
		+ "'" + proyecto.getString("nombre") + "',"
		+ "#" + dt + "#,"
		+ proyecto.getString("idCliente") + ","
		+ proyecto.getString("idJefeProyecto") + ")";
	new Conexion().ejecutarUpdate(query);
	return new JSONObject().put("estado", "ok");
    }

    public JSONObject cargarProyectos() {
	int cont = 1;
	String query = "SELECT\n"
		+ "	A.ID,\n"
		+ "	A.NOMBRE,\n"
		+ "	FORMAT(A.FECHAINI, 'dd-mm-yyyy') FECHAINI,\n"
		+ "	A.IDCLIENTE,\n"
		+ "	A.IDJP,\n"
		+ "	B.NOMBRECLIENTE,\n"
		+ "	C.NOMBRE NOMJP\n"
		+ "FROM	\n"
		+ "	PROYECTO A INNER JOIN CLIENTE B\n"
		+ "	ON A.IDCLIENTE = B.ID INNER JOIN RECURSO C\n"
		+ "	ON A.IDJP = C.ID\n"
		+ "WHERE\n"
		+ "	ACTIVO = 1\n"
		+ "ORDER BY\n"
		+ "	A.FECHAINI ASC";
	ResultSet rs = new Conexion().ejecutarQuery(query);
	JSONObject salida = new JSONObject();
	String tabla = "";
	try {
	    while (rs.next()) {
		tabla += "<tr>";
		tabla += "<td>" + cont + "</td>";
		tabla += "<td>" + rs.getObject("ID") + "</td>";
		tabla += "<td>" + rs.getObject("NOMBRE") + "</td>";
		tabla += "<td>" + rs.getObject("NOMBRECLIENTE") + "</td>";
		tabla += "<td>" + rs.getObject("FECHAINI") + "</td>";
		tabla += "<td>" + rs.getObject("NOMJP") + "</td>";
		tabla += "<td style='width: 120px;' class='text-center'>"
			+ "<div class=\"btn-group\">\n"
			+ "  <button onclick='edicionProyecto(\"" + rs.getObject("ID") + "\");' type=\"button\" class=\"btn btn-primary btn-xs\">Editar</button>\n"
			+ "  <button onclick='eliminarProyecto(\"" + rs.getObject("ID") + "\");' type=\"button\" class=\"btn btn-danger btn-xs\">Eliminar</button>\n"
			+ "</div>"
			+ "</td>";
		cont++;
	    }
	    salida.put("estado", "ok");
	    salida.put("tabla", tabla);
	} catch (SQLException ex) {
	    salida.put("estado", "error");
	    salida.put("error", ex);
	    System.out.println("No se pudo leer los proyectos");
	    System.out.println(ex);
	}
	return salida;
    }

    public JSONObject getProyectoId(JSONObject json) {
	String query = "SELECT * FROM PROYECTO WHERE ID = " + json.getString("idProyecto") + " AND ACTIVO = 1";
	JSONObject salida = new JSONObject();
	JSONObject proyecto = new JSONObject();
	ResultSet rs = new Conexion().ejecutarQuery(query);
	try {
	    while (rs.next()) {
		proyecto.put("nombre", rs.getString("NOMBRE"));
		proyecto.put("idCliente", rs.getString("IDCLIENTE"));
		proyecto.put("fechaIni", rs.getString("FECHAINI"));
		proyecto.put("idJefeProyecto", rs.getString("IDJP"));
		salida.put("proyecto", proyecto);
	    }
	    salida.put("estado", "ok");
	} catch (SQLException | JSONException ex) {
	    salida.put("estado", "error");
	    salida.put("error", ex);
	    System.out.println("No se puede obtener el proyecto por ID");
	    System.out.println(ex);
	}
	return salida;
    }

    public JSONObject guardarProyecto(JSONObject json) {
	JSONObject proyecto = json.getJSONObject("proyecto");
	java.sql.Date dt = java.sql.Date.valueOf(proyecto.getString("fechaIni"));
	String query = "UPDATE PROYECTO SET "
		+ "NOMBRE = '" + proyecto.getString("nombre") + "',"
		+ "FECHAINI = #" + dt + "#,"
		+ "IDCLIENTE = " + proyecto.getString("idCliente") + ","
		+ "IDJP = " + proyecto.getString("idJefeProyecto") + " "
		+ "WHERE ID = " + proyecto.getString("idProyecto");
	new Conexion().ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	return salida;
    }

    public JSONObject eliminarProyecto(String id) {
	String query = "UPDATE PROYECTO SET ACTIVO = 0 WHERE ID = " + id;
	new Conexion().ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	return salida;
    }
}
