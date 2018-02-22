
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Jorge Silva Borda
 */
public class VisionGeneral extends HttpServlet {

    public final String SEP = System.getProperty("line.separator");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");

	PrintWriter out = response.getWriter();
	JSONObject entrada = new JSONObject(request.getParameter("datos"));
	switch (entrada.getString("tipo")) {
	    case "traerTodos":
		out.print(traerTodo());
		break;
	}
    }

    public JSONObject traerTodo() {

	String query = "SELECT\n"
		+ "	A.ID AS COLID,\n"
		+ "	A.NOMBRE AS COLNOMBRE,\n"
		+ "	C.NOMBRECLIENTE COLCLIENTE,\n"
		+ "	FORMAT(A.FECHAINI, 'dd-mm-yyyy') AS COLINIPROYECTO,\n"
		+ "	(SELECT FORMAT(MAX(FECHAFIN), 'dd-mm-yyyy') AS FECHAFIN FROM ETAPAPROYECTO WHERE IDPROYECTO = A.ID) AS COLFINPROYECTO,\n"
		+ "	B.NOMBRE AS COLJP,\n"
		+ "	E.NOMBREETAPA AS COLETAPA,\n"
		+ "	FORMAT(D.FECHAINI, 'dd-mm-yyyy') AS COLFECHAINI,\n"
		+ "	FORMAT(D.FECHAFIN, 'dd-mm-yyyy') AS COLFECHAFIN,\n"
		+ "	G.NOMBRE AS COLRECURSO,\n"
		+ "	H.NOMBREROL AS COLROL,\n"
		+ "	F.PORCENTAJE AS COLPORCENTAJE\n"
		+ "FROM\n"
		+ "	PROYECTO A INNER JOIN RECURSO B\n"
		+ "	ON A.IDJP = B.ID INNER JOIN CLIENTE C\n"
		+ "	ON A.IDCLIENTE = C.ID INNER JOIN ETAPAPROYECTO D\n"
		+ "	ON A.ID = D.IDPROYECTO INNER JOIN ETAPA E\n"
		+ "	ON D.IDETAPA = E.ID INNER JOIN ETAPAPROYECTOPERSONAL F\n"
		+ "	ON D.ID = F.IDETAPAPROYECTO INNER JOIN RECURSO G\n"
		+ "	ON F.IDRECURSO = G.ID INNER JOIN ROL H\n"
		+ "	ON H.ID = F.IDROL\n"
		+ "WHERE\n"
		+ "	A.ACTIVO = 1\n"
		+ "ORDER BY\n"
		+ "	A.NOMBRE ASC,"
		+ "	E.ID ASC,\n"
		+ "	D.FECHAINI ASC";
	ResultSet rs = new Conexion().ejecutarQuery(query);
	JSONObject proyecto;
	JSONObject recurso;
	JSONObject etapa;
	JSONArray proyectos = new JSONArray();
	JSONArray etapas = new JSONArray();
	String indProyActual = "";
	String nomEtapaActual = "";
	String nomRecursoActual = "";
	try {
	    proyectos = new JSONArray();
	    while (rs.next()) {
		if (!indProyActual.equals(rs.getString("COLID"))) {
		    //Si es distinto, se crea un nuevo proyecto
		    indProyActual = rs.getString("COLID");
		    proyecto = new JSONObject();
		    proyecto.put("COLID", rs.getString("COLID"));
		    proyecto.put("COLNOMBRE", rs.getString("COLNOMBRE"));
		    proyecto.put("COLCLIENTE", rs.getString("COLCLIENTE"));
		    proyecto.put("COLJP", rs.getString("COLJP"));
		    proyecto.put("COLINIPROYECTO", rs.getString("COLINIPROYECTO"));
		    proyecto.put("COLFINPROYECTO", rs.getString("COLFINPROYECTO"));
		    //if (!proyecto.getJSONArray("etapas").getJSONObject(proyecto.getJSONArray("etapas").length() - 1).getString("COLETAPA").equals(rs.getString("COLETAPA"))) {
		    nomEtapaActual = rs.getString("COLETAPA");
		    etapa = new JSONObject();
		    etapa.put("COLETAPA", rs.getString("COLETAPA"));
		    etapa.put("COLFECHAINI", rs.getString("COLFECHAINI"));
		    etapa.put("COLFECHAFIN", rs.getString("COLFECHAFIN"));

		    nomRecursoActual = rs.getString("COLRECURSO");
		    recurso = new JSONObject();
		    recurso.put("COLRECURSO", rs.getString("COLRECURSO"));
		    recurso.put("COLPORCENTAJE", rs.getString("COLPORCENTAJE"));
		    recurso.put("COLROL", rs.getString("COLROL"));
		    etapa.append("recursos", recurso);
		    proyecto.append("etapas", etapa);
		    //}
		    proyectos.put(proyecto);
		} else {
		    //Si es igual, no se vuelven a meter los datos del proyecto. Se toma el proyecto ultimo en la lista para agregar etapas
		    proyecto = proyectos.getJSONObject(proyectos.length() - 1); //se le agregan etapas
		    indProyActual = rs.getString("COLID");
		    if (!proyecto.getJSONArray("etapas").getJSONObject(proyecto.getJSONArray("etapas").length() - 1).getString("COLETAPA").equals(rs.getString("COLETAPA"))) {
			nomEtapaActual = rs.getString("COLETAPA");
			etapa = new JSONObject();
			etapa.put("COLETAPA", rs.getString("COLETAPA"));
			etapa.put("COLFECHAINI", rs.getString("COLFECHAINI"));
			etapa.put("COLFECHAFIN", rs.getString("COLFECHAFIN"));
			nomRecursoActual = rs.getString("COLRECURSO");
			recurso = new JSONObject();
			recurso.put("COLRECURSO", rs.getString("COLRECURSO"));
			recurso.put("COLPORCENTAJE", rs.getString("COLPORCENTAJE"));
			recurso.put("COLROL", rs.getString("COLROL"));
			etapa.append("recursos", recurso);
			//proyecto.append("etapas", etapa);
			proyectos.getJSONObject(proyectos.length() - 1).append("etapas", etapa);
		    }else{
			//Si la etapa es la misma, se le meten mas recursos
			etapa = proyecto.getJSONArray("etapas").getJSONObject(proyecto.getJSONArray("etapas").length() - 1);
			nomRecursoActual = rs.getString("COLRECURSO");
			recurso = new JSONObject();
			recurso.put("COLRECURSO", rs.getString("COLRECURSO"));
			recurso.put("COLPORCENTAJE", rs.getString("COLPORCENTAJE"));
			recurso.put("COLROL", rs.getString("COLROL"));
			etapa.append("recursos", recurso);
		    }
		}
		//proyectos.put(proyecto);
	    }
	    //System.out.println(proyectos.toString().replace("[{", "[{" + SEP).replace("\",", "\"," + SEP));
	    JSONObject salida = new JSONObject();

	    System.out.println(proyectos);
	    salida.put("estado", "ok");
	    salida.put("data", proyectos);
	    return salida;
	} catch (SQLException ex) {
	    System.out.println("No se pudo obtener la informacion general de los proyectos");
	    System.out.println(ex);
	}
	return new JSONObject();
    }
}
