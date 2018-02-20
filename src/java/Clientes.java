
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
public class Clientes extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();

	JSONObject entrada = new JSONObject(request.getParameter("datos"));

	switch (entrada.getString("tipo")) {
	    case "cargarClientes":
		out.print(cargarClientes());
		break;
	    case "getClienteId":
		out.print(getClienteId(entrada.getString("idCliente")));
		break;
	    case "insertarCliente":
		out.print(insertarCliente(entrada));
		break;
	    case "grabarCliente":
		out.print(grabarCliente(entrada));
		break;
	    case "eliminarCliente":
		out.print(eliminarCliente(entrada));
		break;
	}
    }

    public JSONObject cargarClientes() {
	String query = "SELECT * FROM CLIENTE";
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
		tab += "<td>" + rs.getObject("NOMBRECLIENTE") + "</td>";
		tab += "<td style='width: 120px;' class='text-center'>"
			+ "<div class=\"btn-group\">\n"
			+ "  <button onclick='edicionCliente(\"" + rs.getObject("ID") + "\");' type=\"button\" class=\"btn btn-primary btn-xs\">Editar</button>\n"
			+ "  <button onclick='eliminarCliente(\"" + rs.getObject("ID") + "\");' type=\"button\" class=\"btn btn-danger btn-xs\">Eliminar</button>\n"
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

    public JSONObject getClienteId(String idCliente) {
	String query = "SELECT * FROM CLIENTE WHERE ID = " + idCliente;
	Conexion con = new Conexion();
	ResultSet rs = con.ejecutarQuery(query);
	JSONObject cliente;
	JSONObject salida = new JSONObject();
	try {
	    while (rs.next()) {
		cliente = new JSONObject();
		cliente.put("ID", rs.getObject("ID"));
		cliente.put("NOMBRE", rs.getObject("NOMBRECLIENTE"));
		salida.put("cliente", cliente);
	    }
	    salida.put("estado", "ok");
	} catch (SQLException | JSONException ex) {
	    System.out.println("No se puede obtener el cliente por ID");
	    System.out.println(ex);
	    salida.put("estado", "error");
	    salida.put("error", ex);
	}
	con.cerrar();
	return salida;
    }

    public JSONObject insertarCliente(JSONObject json) {
	JSONObject cliente = json.getJSONObject("cliente");
	Conexion con = new Conexion();
	String query = "INSERT INTO CLIENTE(NOMBRECLIENTE) VALUES("
		+ "'" + cliente.getString("nombre") + "'"
		+ ")";
	con.ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	con.cerrar();
	return salida;
    }

    public JSONObject grabarCliente(JSONObject json) {

	JSONObject cliente = json.getJSONObject("cliente");
	Conexion con = new Conexion();
	String query = "UPDATE CLIENTE SET "
		+ "NOMBRECLIENTE = '" + cliente.getString("nombre") + "' "
		+ "WHERE ID = " + cliente.getString("id")
		+ "";
	con.ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	con.cerrar();
	return salida;
    }

    public JSONObject eliminarCliente(JSONObject json) {

	String query = "DELETE FROM CLIENTE WHERE ID = " + json.getString("id");
	Conexion c = new Conexion();
	c.ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	return salida;
    }
}
