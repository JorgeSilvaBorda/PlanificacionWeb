
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
	PrintWriter out = response.getWriter();
	JSONObject entrada = new JSONObject(request.getParameter("datos"));
	switch (entrada.getString("tipo")) {
	    case "traerTodos":
		traerTodo();
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
		+ "	D.FECHAINI ASC";
	ResultSet rs = new Conexion().ejecutarQuery(query);
	JSONObject proyecto;
	JSONObject recurso;
	JSONObject etapa;
	JSONArray proyectos = new JSONArray();
	String indProyActual = "";
	String nomEtapaActual = "";
	String nomRecursoActual = "";
	try {
	    proyectos = new JSONArray();
	    while (rs.next()) {
		if(!indProyActual.equals(rs.getString("COLID"))){
		    proyecto = new JSONObject();
		    proyecto.put("COLID", rs.getString("COLID"));
		    proyecto.put("COLNOMBRE", rs.getString("COLNOMBRE"));
		    proyecto.put("COLCLIENTE", rs.getString("COLCLIENTE"));
		    proyecto.put("COLJP", rs.getString("COLJP"));
		    proyecto.put("COLINIPROYECTO", rs.getString("COLINIPROYECTO"));
		    proyecto.put("COLFINPROYECTO", rs.getString("COLFINPROYECTO"));
		    proyectos.put(proyecto);
		}else{
		    //Si es igual, no se vuelven a meter los datos del proyecto. Se toma el proyecto ultimo en la lista para agregar etapas
		    proyecto = proyectos.getJSONObject(proyectos.length() - 1);
		}
	    }
	    System.out.println(proyectos.toString().replace("[{", "[{" + SEP).replace("\",", "\"," + SEP));
	} catch (SQLException ex) {
	    System.out.println("No se pudo obtener la informacion general de los proyectos");
	    System.out.println(ex);
	}
	return new JSONObject();
    }
}
