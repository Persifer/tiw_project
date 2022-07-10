package constans;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import model.Utente;

public class UtilityConstans {
	
	public static String PRIVATEPATH = "./WEB-INF/";
	
	public static Utente getSessionUtente(HttpServletRequest request) throws ServletException, IOException{
		return ((Utente) request.getSession().getAttribute("user"));
	}

}
