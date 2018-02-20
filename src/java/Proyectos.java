
import java.io.IOException;
import java.io.PrintWriter;
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
	}
    }

    public JSONObject ingresarProyecto(JSONObject json) {
	System.out.println(json);
	return new JSONObject().append("estado", "ok");
    }

}
