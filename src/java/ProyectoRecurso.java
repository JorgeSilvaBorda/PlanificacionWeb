
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
public class ProyectoRecurso extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	PrintWriter out = response.getWriter();
	JSONObject entrada = new JSONObject(request.getParameter("datos"));
	switch (entrada.getString("tipo")) {
	    case "cargarGrid":
		out.print(cargarGrid());
		break;
	}
    }

    private JSONObject cargarGrid() {
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
	JSONObject row = new JSONObject();
	String tabla = "";
	try {
	    while (rs.next()) {
		
	    }
	    salida.put("estado", "ok");
	    salida.put("tabla", tabla);
	} catch (SQLException ex) {
	    salida.put("estado", "error");
	    salida.put("error", ex);
	    System.out.println("No se pudo leer los proyectos");
	    System.out.println(ex);
	}
	return new JSONObject();
    }
}
