
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
public class Asignaciones extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();

	JSONObject entrada = new JSONObject(request.getParameter("datos"));

	switch (entrada.getString("tipo")) {
	    case "cargarProyectos":
		out.print(cargarProyectos());
		break;
	    case "cargarEtapas":
		out.print(cargarEtapas(entrada.getString("idProyecto")));
		break;
	    case "verRecursos":
		out.print(verRecursos(entrada));
		break;
	    case "cargarRecursos":
		out.print(cargarRecursos());
		break;
	    case "cargarRoles":
		out.print(cargarRoles());
		break;
	    case "quitarRecurso":
		out.print(quitarRecurso(entrada.getString("idEtapaProyectoPersonal")));
		break;
	    case "agregarRecurso":
		out.print(agregarRecurso(entrada));
		break;
	}
    }

    public JSONObject cargarProyectos() {
	String query = "SELECT ID AS IDPROYECTO, NOMBRE AS NOMPROYECTO FROM PROYECTO WHERE ACTIVO = 1 ORDER BY FECHAINI ASC";
	String combo = Util.armarCombo("SELECCIONE", "0", "NOMPROYECTO", "IDPROYECTO", query);
	JSONObject salida = new JSONObject();
	salida.put("estado", "ok");
	salida.put("combo", combo);
	return salida;
    }

    public JSONObject cargarEtapas(String idProyecto) {
	String query = "SELECT \n"
		+ "	A.ID AS IDETAPAPROYECTO,\n"
		+ "	B.ID AS IDETAPA,\n"
		+ "	A.IDPROYECTO AS IDPROYECTO,\n"
		+ "	B.NOMBREETAPA,\n"
		+ "	FORMAT(A.FECHAINI, 'dd-mm-yyyy') AS FECHAINI,\n"
		+ "	FORMAT(A.FECHAFIN, 'dd-mm-yyyy') AS FECHAFIN\n"
		+ "FROM\n"
		+ "	ETAPAPROYECTO A INNER JOIN ETAPA B\n"
		+ "	ON A.IDETAPA = B.ID\n"
		+ "WHERE \n"
		+ "	IDPROYECTO = " + idProyecto;
	ResultSet rs = new Conexion().ejecutarQuery(query);
	String tabla = "";
	JSONObject salida = new JSONObject();
	int cont = 0;
	try {
	    while (rs.next()) {
		tabla += "<tr>";
		tabla += "<td><input type='hidden' value='" + rs.getString("IDETAPAPROYECTO") + "' />" + rs.getString("NOMBREETAPA") + "</td>";
		tabla += "<td>" + rs.getString("FECHAINI") + "</td>";
		tabla += "<td>" + rs.getString("FECHAFIN") + "</td>";
		tabla += "<td><button onclick='verRecursos(\"" + rs.getString("IDPROYECTO") + "\", \"" + rs.getString("IDETAPA") + "\", \"" + rs.getString("NOMBREETAPA") + "\", \"" + rs.getString("IDETAPAPROYECTO") + "\")' 'type='button' class='btn btn-warning btn-xs'>Recursos</button></td>";
		tabla += "<tr></tr>";
		cont++;
	    }

	    salida.put("estado", "ok");
	    salida.put("registros", cont);
	    salida.put("tabla", tabla);
	} catch (SQLException | JSONException ex) {
	    System.out.println("No se puede cargar las etapas del proyecto seleccionado.");
	    System.out.println(ex);
	    salida.put("estado", "error");
	    salida.put("error", ex);
	}
	return salida;
    }

    public JSONObject verRecursos(JSONObject json) {
	String query = "SELECT\n"
		+ "	F.ID AS IDPROYECTO,\n"
		+ "	F.NOMBRE AS NOMPROYECTO,\n"
		+ "	E.ID AS IDETAPA,\n"
		+ "	E.NOMBREETAPA AS NOMETAPA,\n"
		+ "	FORMAT(B.FECHAINI, 'dd-mm-yyyy') AS FECHAINI,\n"
		+ "	FORMAT(B.FECHAFIN, 'dd-mm-yyyy') AS FECHAFIN,\n"
		+ "	C.ID AS IDRECURSO,\n"
		+ "	C.NOMBRE AS NOMRECURSO,\n"
		+ "	D.ID AS IDROL,\n"
		+ "	D.NOMBREROL AS NOMROL,\n"
		+ "	A.PORCENTAJE,\n"
		+ "	A.ID AS IDETAPAPROYECTOPERSONAL,\n"
		+ "	B.ID AS IDETAPAPROYECTO\n"
		+ "FROM\n"
		+ "	ETAPAPROYECTOPERSONAL A INNER JOIN ETAPAPROYECTO B \n"
		+ "	ON A.IDETAPAPROYECTO = B.ID INNER JOIN RECURSO C \n"
		+ "	ON A.IDRECURSO = C.ID INNER JOIN ROL D \n"
		+ "	ON A.IDROL = D.ID INNER JOIN ETAPA E\n"
		+ "	ON B.IDETAPA = E.ID INNER JOIN PROYECTO F\n"
		+ "	ON B.IDPROYECTO = F.ID\n"
		+ "WHERE\n"
		+ "	B.IDPROYECTO = " + json.getString("idProyecto") + "\n"  //1
		+ "	AND B.IDETAPA = " + json.getString("idEtapa"); //5
	ResultSet rs = new Conexion().ejecutarQuery(query);

	int cont = 0;
	JSONObject salida = new JSONObject();

	String tabla = "";
	try {
	    while (rs.next()) {
		tabla += "<tr>";
		tabla += "<td>" + rs.getString("NOMRECURSO") + "</td>";
		tabla += "<td>" + rs.getString("NOMROL") + "</td>";
		tabla += "<td>" + rs.getString("PORCENTAJE") + "%</td>";
		tabla += "<td style='width:80px;'><button type='button' class='btn btn-danger btn-xs' onclick='quitarRecurso(\"" + rs.getString("IDETAPAPROYECTOPERSONAL") + "\");'>Quitar</button></td>";
		cont++;
	    }
	    salida.put("estado", "ok");

	    if (cont == 0) {
		tabla = "<tr><td colspan='4' class='text-center'>La etapa seleccionada no posee recursos asignados</td></tr>";
	    }
	    salida.put("tabla", tabla);

	    return salida;
	} catch (SQLException | JSONException ex) {
	    salida.put("estado", "error");
	    salida.put("error", ex);
	    System.out.println("No se puede obtener los recursos asignados a la etapa seleccionada");
	    System.out.println(ex);
	}
	return salida;
    }

    public JSONObject cargarRecursos() {
	String query = "SELECT ID AS IDRECURSO, NOMBRE AS NOMRECURSO FROM RECURSO";
	JSONObject json = new JSONObject();
	json.put("estado", "ok");
	json.put("combo", Util.armarCombo("Seleccione", "0", "NOMRECURSO", "IDRECURSO", query));
	return json;
    }

    public JSONObject cargarRoles() {
	String query = "SELECT ID AS IDROL, NOMBREROL AS NOMROL FROM ROL";
	JSONObject json = new JSONObject();
	json.put("estado", "ok");
	json.put("combo", Util.armarCombo("Seleccione", "0", "NOMROL", "IDROL", query));
	return json;
    }

    public JSONObject quitarRecurso(String idEtapaProyectoPersonal) {
	String query = "DELETE FROM ETAPAPROYECTOPERSONAL WHERE ID = " + idEtapaProyectoPersonal;
	new Conexion().ejecutarUpdate(query);
	JSONObject json = new JSONObject();
	json.put("estado", "ok");
	return json;
    }

    public JSONObject agregarRecurso(JSONObject json) {
	JSONObject salida = new JSONObject();
	String query = "INSERT INTO ETAPAPROYECTOPERSONAL\n"
		+ "(PORCENTAJE, IDETAPAPROYECTO, IDRECURSO, IDROL)\n"
		+ "VALUES("
		+ json.getString("porcentaje") + ", "
		+ json.getString("idEtapaProyecto") + ", "
		+ json.getString("idRecurso") + ", "
		+ json.getString("idRol") + ")";
	new Conexion().ejecutarUpdate(query);
	salida.put("estado", "ok");
	return salida;
    }

}
