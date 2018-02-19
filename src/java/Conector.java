
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

public class Conector extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();

	JSONObject entrada = new JSONObject(request.getParameter("datos"));

	switch (entrada.getString("tipo")) {
	    //RECURSOS-------------------------------------------------------------
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
		//ROLES-------------------------------------------------------------
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
		//ETAPAS-------------------------------------------------------------
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
		//CLIENTES-------------------------------------------------------------
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

    //<editor-fold defaultstate="collapsed" desc="ROLES">
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

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="RECURSOS">
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
	//System.out.println(json);
	JSONObject recurso = json.getJSONObject("recurso");
	Conexion con = new Conexion();
	String query = "INSERT INTO RECURSO(RUT, NOMBRE, COSTO, COSTOUSO) VALUES("
		+ "'" + recurso.getString("rut") + "', "
		+ "'" + recurso.getString("nombre") + "', "
		+ "'" + recurso.getString("costo") + "', "
		+ "'" + recurso.getString("costouso") + "'"
		+ ")";
	//System.out.println(query);
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
	//System.out.println(query);
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

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ETAPAS">
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

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="CLIENTES">
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

    //</editor-fold>
}
