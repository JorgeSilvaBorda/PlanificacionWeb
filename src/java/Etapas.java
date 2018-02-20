
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

public class Etapas extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();

	JSONObject entrada = new JSONObject(request.getParameter("datos"));
	switch (entrada.getString("tipo")) {
	    case "cargarEtapas":
		out.print(cargarEtapas());
		break;
	    case "getEtapaId":
		out.print(getEtapaId(entrada.getString("idEtapa")));
		break;
	    case "insertarEtapa":
		out.print(insertarEtapa(entrada));
		break;
	    case "grabarEtapa":
		out.print(grabarEtapa(entrada));
		break;
	    case "eliminarEtapa":
		out.print(eliminarEtapa(entrada));
		break;
	}
    }

    public JSONObject cargarEtapas() {
	String query = "SELECT * FROM ETAPA";
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
		tab += "<td>" + rs.getObject("NOMBREETAPA") + "</td>";
		tab += "<td style='width: 120px;' class='text-center'>"
			+ "<div class=\"btn-group\">\n"
			+ "  <button onclick='edicionEtapa(\"" + rs.getObject("ID") + "\");' type=\"button\" class=\"btn btn-primary btn-xs\">Editar</button>\n"
			+ "  <button onclick='eliminarEtapa(\"" + rs.getObject("ID") + "\");' type=\"button\" class=\"btn btn-danger btn-xs\">Eliminar</button>\n"
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

    public JSONObject getEtapaId(String idEtapa) {
	String query = "SELECT * FROM ETAPA WHERE ID = " + idEtapa;
	Conexion con = new Conexion();
	ResultSet rs = con.ejecutarQuery(query);
	JSONObject etapa;
	JSONObject salida = new JSONObject();
	try {
	    while (rs.next()) {
		etapa = new JSONObject();
		etapa.put("ID", rs.getObject("ID"));
		etapa.put("NOMBRE", rs.getObject("NOMBREETAPA"));
		salida.put("etapa", etapa);
	    }
	    salida.put("estado", "ok");
	} catch (SQLException | JSONException ex) {
	    System.out.println("No se puede obtener la etapa por ID");
	    System.out.println(ex);
	    salida.put("estado", "error");
	    salida.put("error", ex);
	}
	con.cerrar();
	return salida;
    }

    public JSONObject insertarEtapa(JSONObject json) {
	JSONObject etapa = json.getJSONObject("etapa");
	Conexion con = new Conexion();
	String query = "INSERT INTO ETAPA(NOMBREETAPA) VALUES("
		+ "'" + etapa.getString("nombre") + "'"
		+ ")";
	con.ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	con.cerrar();
	return salida;
    }

    public JSONObject grabarEtapa(JSONObject json) {

	JSONObject etapa = json.getJSONObject("etapa");
	Conexion con = new Conexion();
	String query = "UPDATE ETAPA SET "
		+ "NOMBREETAPA = '" + etapa.getString("nombre") + "' "
		+ "WHERE ID = " + etapa.getString("id")
		+ "";
	con.ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	con.cerrar();
	return salida;
    }

    public JSONObject eliminarEtapa(JSONObject json) {

	String query = "DELETE FROM ETAPA WHERE ID = " + json.getString("id");
	Conexion c = new Conexion();
	c.ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	return salida;
    }

}
