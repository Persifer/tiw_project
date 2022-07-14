package constans;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Utente;

public class UtilityConstans {
	
	public static String PRIVATEPATH = "./WEB-INF/";
	
	public static Utente getSessionUtente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
				
		if(request.getSession().isNew() || request.getSession().getAttribute("user") == null) {
			response.sendRedirect("Login");
		}
		
		return ((Utente) request.getSession().getAttribute("user"));
	}

}
