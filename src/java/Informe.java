
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jorge Silva Borda
 */
public class Informe extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();

	JSONObject entrada = new JSONObject(request.getParameter("datos"));
	switch (entrada.getString("tipo")) {
	    case "getCargaTodos":
		out.print(getCargaTodos());
		break;
	}
    }

    public JSONObject getCargaTodos() {
	String query = "SELECT\n"
		+ "	FORMAT(A.FECHA, 'yyyy-mm-dd') AS FECHA,\n"
		+ "	A.IDRECURSO,\n"
		+ "	A.NOMBRE,\n"
		+ "	SUM(CASE WHEN B.PORCENTAJE IS NULL THEN 0 ELSE B.PORCENTAJE END) AS PORCENTAJE\n"
		+ "FROM\n"
		+ "\n"
		+ "(SELECT DISTINCT FECHA, RECURSO.ID AS IDRECURSO, NOMBRE FROM(SELECT FECHAINI AS FECHA FROM ETAPAPROYECTO UNION ALL SELECT FECHAFIN AS FECHA FROM ETAPAPROYECTO), RECURSO) A \n"
		+ "\n"
		+ "LEFT JOIN \n"
		+ "\n"
		+ "(SELECT\n"
		+ "	A.FECHAINI AS FECHA, \n"
		+ "	C.ID AS IDRECURSO,\n"
		+ "	C.NOMBRE,\n"
		+ "	B.PORCENTAJE\n"
		+ "FROM\n"
		+ "	ETAPAPROYECTO A INNER JOIN ETAPAPROYECTOPERSONAL B\n"
		+ "	ON A.ID = B.IDETAPAPROYECTO INNER JOIN RECURSO C\n"
		+ "	ON B.IDRECURSO = C.ID INNER JOIN PROYECTO D \n"
		+ "	ON A.IDPROYECTO = D.ID\n"
		+ "WHERE\n"
		+ "	D.ACTIVO = 1\n"
		+ "UNION ALL\n"
		+ "SELECT\n"
		+ "	A.FECHAFIN AS FECHA, \n"
		+ "	C.ID AS IDRECURSO,\n"
		+ "	C.NOMBRE,\n"
		+ "	B.PORCENTAJE\n"
		+ "FROM\n"
		+ "	ETAPAPROYECTO A INNER JOIN ETAPAPROYECTOPERSONAL B\n"
		+ "	ON A.ID = B.IDETAPAPROYECTO INNER JOIN RECURSO C\n"
		+ "	ON B.IDRECURSO = C.ID INNER JOIN PROYECTO D \n"
		+ "	ON A.IDPROYECTO = D.ID\n"
		+ "WHERE\n"
		+ "	D.ACTIVO = 1\n"
		+ "ORDER BY\n"
		+ "	1 ASC,\n"
		+ "	3 ASC) B\n"
		+ "ON A.FECHA = B.FECHA\n"
		+ "AND A.IDRECURSO = B.IDRECURSO\n"
		+ "GROUP BY\n"
		+ "	A.FECHA,\n"
		+ "	A.IDRECURSO,\n"
		+ "	A.NOMBRE\n"
		+ "ORDER BY A.FECHA ASC, A.NOMBRE ASC";

	ResultSet rs = new Conexion().ejecutarQuery(query);
	JSONObject salida = new JSONObject();
	JSONArray valores = new JSONArray();
	JSONObject valor = new JSONObject();
	JSONObject val = new JSONObject();
	String fechaActual = "";
	try {
	    while (rs.next()) {
		if (!rs.getString("FECHA").equals(fechaActual)) {
		    fechaActual = rs.getString("FECHA");
		    valor = new JSONObject();
		    valor.put("date", rs.getString("FECHA"));
		    valor.put(rs.getString("NOMBRE"), rs.getString("PORCENTAJE"));
		    valores.put(valor);
		} else {
		    valores.getJSONObject(valores.length() - 1).append(rs.getString("NOMBRE"), rs.getString("PORCENTAJE"));
		}
	    }
	    val.put("valores", valores);
	    salida.put("nombres", getNombres());
	    salida.put("valores", valores);
	    salida.put("estado", "ok");
	    System.out.println(valores);
	} catch (SQLException | JSONException ex) {
	    System.out.println("No se puede obtener la carga general de trabajo");
	    System.out.println(ex);
	    salida.put("estado", "error");
	    salida.put("error", ex);
	}
	return salida;
    }

    private JSONArray getNombres() {
	String query = "SELECT\n"
		+ "	DISTINCT NOMBRE\n"
		+ "FROM\n"
		+ "(\n"
		+ "SELECT\n"
		+ "	FORMAT(A.FECHA, 'yyyy-mm-dd') AS FECHA,\n"
		+ "	A.IDRECURSO,\n"
		+ "	A.NOMBRE,\n"
		+ "	CDbl(SUM(CASE WHEN B.PORCENTAJE IS NULL THEN 0 ELSE B.PORCENTAJE END))/100 AS PORCENTAJE\n"
		+ "FROM\n"
		+ "\n"
		+ "(SELECT DISTINCT FECHA, RECURSO.ID AS IDRECURSO, NOMBRE FROM(SELECT FECHAINI AS FECHA FROM ETAPAPROYECTO UNION ALL SELECT FECHAFIN AS FECHA FROM ETAPAPROYECTO), RECURSO) A \n"
		+ "\n"
		+ "LEFT JOIN \n"
		+ "\n"
		+ "(SELECT\n"
		+ "	A.FECHAINI AS FECHA, \n"
		+ "	C.ID AS IDRECURSO,\n"
		+ "	C.NOMBRE,\n"
		+ "	B.PORCENTAJE\n"
		+ "FROM\n"
		+ "	ETAPAPROYECTO A INNER JOIN ETAPAPROYECTOPERSONAL B\n"
		+ "	ON A.ID = B.IDETAPAPROYECTO INNER JOIN RECURSO C\n"
		+ "	ON B.IDRECURSO = C.ID INNER JOIN PROYECTO D \n"
		+ "	ON A.IDPROYECTO = D.ID\n"
		+ "WHERE\n"
		+ "	D.ACTIVO = 1\n"
		+ "UNION ALL\n"
		+ "SELECT\n"
		+ "	A.FECHAFIN AS FECHA, \n"
		+ "	C.ID AS IDRECURSO,\n"
		+ "	C.NOMBRE,\n"
		+ "	B.PORCENTAJE\n"
		+ "FROM\n"
		+ "	ETAPAPROYECTO A INNER JOIN ETAPAPROYECTOPERSONAL B\n"
		+ "	ON A.ID = B.IDETAPAPROYECTO INNER JOIN RECURSO C\n"
		+ "	ON B.IDRECURSO = C.ID INNER JOIN PROYECTO D \n"
		+ "	ON A.IDPROYECTO = D.ID\n"
		+ "WHERE\n"
		+ "	D.ACTIVO = 1\n"
		+ "ORDER BY\n"
		+ "	1 ASC,\n"
		+ "	3 ASC) B\n"
		+ "ON A.FECHA = B.FECHA\n"
		+ "AND A.IDRECURSO = B.IDRECURSO\n"
		+ "GROUP BY\n"
		+ "	A.FECHA,\n"
		+ "	A.IDRECURSO,\n"
		+ "	A.NOMBRE\n"
		+ "ORDER BY A.FECHA ASC, A.NOMBRE ASC)";
	JSONArray nombres = new JSONArray();
	ResultSet rs = new Conexion().ejecutarQuery(query);
	JSONObject nombre;
	try{
	    while (rs.next()) {
		nombre = new JSONObject();
		nombre.put("nombre", rs.getString("NOMBRE"));
		nombres.put(nombre);
	    }
	    return nombres;
	}catch (SQLException ex) {
	    System.out.println("No se puede obtener el listado de nombres de la base de datos");
	    System.out.println(ex);
	}
	return new JSONArray();
    }
}
