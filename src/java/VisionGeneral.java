
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 * @author Jorge Silva Borda
 */
public class VisionGeneral extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	PrintWriter out = response.getWriter();
    }

    public JSONObject traerTodo() {
	/*
	String query = "SELECT\n"
		+ "	A.ID AS IDPROYECTO,\n"
		+ "	A.NOMBRE AS NOMPROYECTO,\n"
		+ "	C.NOMBRECLIENTE,\n"
		+ "	FORMAT(A.FECHAINI, 'dd-mm-yyyy') AS INIPROYECTO,\n"
		+ "	-- CALCULAR FECHAFIN\n"
		+ "	B.NOMBRE AS NOMJP,\n"
		+ "	E.NOMBREETAPA,\n"
		+ "	FORMAT(D.FECHAINI, 'dd-mm-yyyy') AS INIETAPA,\n"
		+ "	FORMAT(D.FECHAFIN, 'dd-mm-yyyy') AS FINETAPA,\n"
		+ "	G.NOMBRE AS RECURSO,\n"
		+ "	H.NOMBREROL AS ROL,\n"
		+ "	F.PORCENTAJE\n"
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
	 */
	String query = "SELECT\n"
		+ "	A.ID AS IDPROYECTO,\n"
		+ "	A.NOMBRE AS NOMPROYECTO,\n"
		+ "	C.NOMBRECLIENTE,\n"
		+ "	FORMAT(A.FECHAINI, 'dd-mm-yyyy') AS INIPROYECTO,\n"
		+ "	(SELECT FORMAT(MAX(FECHAFIN), 'dd-mm-yyyy') AS FECHAFIN FROM ETAPAPROYECTO WHERE IDPROYECTO = A.ID) AS FINPROYECTO,\n"
		+ "	B.NOMBRE AS NOMJP,\n"
		+ "	E.NOMBREETAPA,\n"
		+ "	FORMAT(D.FECHAINI, 'dd-mm-yyyy') AS INIETAPA,\n"
		+ "	FORMAT(D.FECHAFIN, 'dd-mm-yyyy') AS FINETAPA,\n"
		+ "	G.NOMBRE AS RECURSO,\n"
		+ "	H.NOMBREROL AS ROL,\n"
		+ "	F.PORCENTAJE\n"
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
	int indProyActual = 0;
	try {
	    while (rs.next()) {
		if(indProyActual == rs.getInt("IDPROYECTO")){
		    
		}else{
		    proyecto = new JSONObject();
		    proyecto.put("", "");
		}
	    }
	} catch (SQLException ex) {
	    System.out.println("No se pudo obtener la informacion general de los proyectos");
	    System.out.println(ex);
	}
	return new JSONObject();
    }
}
