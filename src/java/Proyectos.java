
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
	    case "traerEtapas":
		out.print(traerEtapas(Integer.toString(entrada.getInt("idProyecto"))));
		break;
	    case "eliminarEtapaProyecto":
		out.print(eliminarEtapaProyecto(entrada.getString("idEtapaProyecto")));
		break;
	    case "agregarEtapaProyecto":
		out.print(agregarEtapaProyecto(entrada.getJSONObject("etapa")));
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
		tabla += "<td style='width: 160px;' class='text-center'>"
			+ "<div class=\"btn-group\">\n"
			+ "  <button onclick='edicionProyecto(\"" + rs.getObject("ID") + "\");' type=\"button\" class=\"btn btn-primary btn-xs\">Editar</button>\n"
			+ "  <button onclick='etapasProyecto(this);' type=\"button\" class=\"btn btn-success btn-xs\">Etapas</button>\n"
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

    public JSONObject traerEtapas(String idProyecto) {
	String query = "SELECT\n"
		+ "	A.ID AS IDPROYECTO,\n"
		+ "	A.NOMBRE,\n"
		+ "	B.ID AS IDETAPAPROYECTO,\n"
		+ "	B.FECHAINI,\n"
		+ "	B.FECHAFIN,\n"
		+ "	B.IDETAPA,\n"
		+ "	B.NUMETAPA,\n"
		+ "	C.NOMBREETAPA\n"
		+ "FROM	\n"
		+ "	PROYECTO A INNER JOIN ETAPAPROYECTO B\n"
		+ "	ON A.ID = B.IDPROYECTO INNER JOIN ETAPA C\n"
		+ "	ON B.IDETAPA = C.ID\n"
		+ "WHERE\n"
		+ "	B.IDPROYECTO = " + idProyecto + "\n"
		+ "ORDER BY\n"
		+ "	B.FECHAINI ASC";
	ResultSet rs = new Conexion().ejecutarQuery(query);
	String tabla = "<table class=\"table table-hover table-condensed table-striped small\">";
	tabla += "<thead>";
	tabla += "<tr>\n"
		+ "<th>#</th>"
		+ "<th>ETAPA</th>\n"
		+ "<th>FECHA INICIO</th>\n"
		+ "<th>FECHA FIN</th>\n"
		+ "<th>ACCIONES</th>\n"
		+ "</tr>";
	JSONObject salida = new JSONObject();
	salida.put("combo", genComboEtapas(idProyecto));
	int cont = 0;
	try {
	    while (rs.next()) {
		String fechaIni, fechaFin;
		fechaIni = rs.getString("FECHAINI");
		fechaFin = rs.getString("FECHAFIN");
		fechaIni = fechaIni.substring(0, 10);
		fechaFin = fechaFin.substring(0, 10);
		
		fechaIni = fechaIni.split("-")[2] + "-" + fechaIni.split("-")[1] + "-" + fechaIni.split("-")[0];
		fechaFin = fechaFin.split("-")[2] + "-" + fechaFin.split("-")[1] + "-" + fechaFin.split("-")[0];
		tabla += "<tr>";
		tabla += "<td><input type='hidden' value='" + rs.getString("IDETAPAPROYECTO") + "' />" + Integer.toString(cont + 1) + "</td>";
		tabla += "<td>" + rs.getString("NOMBREETAPA") + "</td>";
		tabla += "<td>" + fechaIni + "</td>";
		tabla += "<td>" + fechaFin + "</td>";
		/*
		tabla += "<td>" + rs.getString("FECHAINI").split("-")[0] + "-" + rs.getString("FECHAINI").split("-")[1] + "-" + rs.getString("FECHAINI").split("-")[2] + "</td>";
		tabla += "<td>" + rs.getString("FECHAFIN").split("-")[0] + "-" + rs.getString("FECHAFIN").split("-")[1] + "-" + rs.getString("FECHAFIN").split("-")[2] + "</td>";
		 */
		tabla += "<td style='width: 100px;' class='text-center'>"
			+ "<div class=\"btn-group\">\n"
			+ "  <button onclick='eliminarEtapaProyecto(\"" + rs.getObject("IDETAPAPROYECTO") + "\", \"" + rs.getObject("IDPROYECTO") + "\");' type=\"button\" class=\"btn btn-danger btn-xs\">Eliminar</button>\n"
			+ "</div>"
			+ "</td>";
		cont++;
	    }
	    salida.put("registros", cont);
	    salida.put("tabla", tabla);
	    salida.put("estado", "ok");
	} catch (SQLException | JSONException ex) {
	    salida.put("estado", "error");
	    salida.put("error", ex);
	    System.out.println("No se puede obtener las etapas del proyecto.");
	    System.out.println(ex);
	}
	return salida;
    }

    private String genComboEtapas(String idProyecto) {
	String query = "SELECT\n"
		+ "	*\n"
		+ "FROM\n"
		+ "	ETAPA\n"
		+ "WHERE\n"
		+ "	ID NOT IN(\n"
		+ "	SELECT\n"
		+ "		B.IDETAPA\n"
		+ "	FROM	\n"
		+ "		PROYECTO A INNER JOIN ETAPAPROYECTO B\n"
		+ "		ON A.ID = B.IDPROYECTO INNER JOIN ETAPA C\n"
		+ "		ON B.IDETAPA = C.ID\n"
		+ "	WHERE\n"
		+ "		B.IDPROYECTO = " + idProyecto
		+ "	)";
	ResultSet rs = new Conexion().ejecutarQuery(query);
	String contenido = "<div class='row'>\n"
		+ "        <input id='idProyectoEdicion' type='hidden' value='" + idProyecto + "'>\n"
		+ "        <div class='col-sm-8'>\n"
		+ "            <div class='form-group-sm'>\n"
		+ "                <select id='etapas' class='form-control'>##contenido-select##</select>\n"
		+ "            </div>\n"
		+ "        </div>\n"
		+ "        <div class='col-sm-4'>\n"
		+ "            <div class='form-group-sm'>\n"
		+ "                <button type='button' class='btn btn-success btn-sm' onclick='agregarEtapaProyecto()'>Agregar</button>\n"
		+ "            </div>\n"
		+ "        </div>\n"
		+ "    </div>"
		+ "<div class='row'>\n"
		+ "    <div class='col-sm-4'>\n"
		+ "        <div class='form-group-sm'>\n"
		+ "            <label for='fechaIniEtapa'>Fecha inicio</label>\n"
		+ "            <input id='fechaIniEtapa' type='date' class='form-control'/>\n"
		+ "        </div>\n"
		+ "        \n"
		+ "    </div>\n"
		+ "    <div class='col-sm-4'>\n"
		+ "        <div class='form-group-sm'>\n"
		+ "            <label for='fechaFinEtapa'>Fecha fin</label>\n"
		+ "            <input id='fechaFinEtapa' type='date' class='form-control'/>\n"
		+ "        </div>\n"
		+ "    </div>\n"
		+ "    <div class='col-sm-4'></div>\n"
		+ "</div>";

	String combo = "<option value='0'>Seleccione...</option>";
	int cont = 0;
	try {
	    while (rs.next()) {
		combo += "<option value='" + rs.getString("ID") + "' >" + rs.getString("NOMBREETAPA") + "</option>";
		cont++;
	    }
	    if (cont == 0) {
		contenido = contenido.replace("select id='etapas'", "select disabled='disabled' id='etapas'");
		combo = combo.replace("option value='0'>Seleccione...", "option value='0'>No hay etapas disponibles");
		contenido = contenido.replace("button type='button' class='btn btn-success btn-sm' ", "button type='button' class='btn btn-success btn-xs' disabled='disabled' ");
		contenido = contenido.replace("id='fechaFinEtapa' type='date'", "id='fechaFin' disabled='disabled' type='date'");
		contenido = contenido.replace("id='fechaIniEtapa' type='date'", "id='fechaIni' disabled='disabled' type='date'");
	    }
	    contenido = contenido.replace("##contenido-select##", combo);
	    return contenido;
	} catch (SQLException ex) {
	    System.out.println("No se puede obtener el listado de etapas");
	    System.out.println(ex);
	}
	return "";
    }

    public JSONObject eliminarEtapaProyecto(String idEtapaProyecto) {
	String query1 = "DELETE FROM ETAPAPROYECTOPERSONAL WHERE ID = " + idEtapaProyecto;
	String query2 = "DELETE FROM ETAPAPROYECTO WHERE ID = " + idEtapaProyecto;
	new Conexion().ejecutarUpdate(query1);
	new Conexion().ejecutarUpdate(query2);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	return salida;
    }
    
    public JSONObject agregarEtapaProyecto(JSONObject etapa){
	java.sql.Date dtIni = java.sql.Date.valueOf(etapa.getString("fechaIni"));
	java.sql.Date dtFin = java.sql.Date.valueOf(etapa.getString("fechaFin"));
	String query = "INSERT INTO ETAPAPROYECTO(FECHAINI, FECHAFIN, IDPROYECTO, IDETAPA, NUMETAPA)VALUES("
		+ "#" + dtIni + "#,"
		+ "#" + dtFin + "#,"
		+ etapa.getInt("idProyecto") + ","
		+ etapa.getInt("idEtapa") + ","
		+ "(SELECT MAX(NUMETAPA) FROM ETAPAPROYECTO WHERE IDPROYECTO = " + etapa.getInt("idProyecto") + "))";
	
	new Conexion().ejecutarUpdate(query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	return salida;
    }
}
