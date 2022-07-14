package controller;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.CartellaDAO;
import DAO.SottocartellaDAO;
import constans.UtilityConstans;
import model.Cartella;
import model.Utente;
import utils.ConnectionHandler;

@WebServlet("/Home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Connection connection = null;
	private String jspPath = UtilityConstans.PRIVATEPATH + "home.jsp";
	
	private CartellaDAO cartellaDao;
	private SottocartellaDAO sottocartellaDao;

    public void init() throws ServletException {

        connection = ConnectionHandler.getConnection(getServletContext());
        this.cartellaDao = new CartellaDAO(connection);
        this.sottocartellaDao = new SottocartellaDAO(connection);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	
    	try {
    		// codice per lista delle cartelle
    		Utente sessionUser =  UtilityConstans.getSessionUtente(request, response);
			    		
    		sessionUser.setListaCartelle(cartellaDao.getFolderByUser(sessionUser));
    		
    		for(Cartella cartella : sessionUser.getListaCartelle()) {
    			cartella.setListaSottocartelle(sottocartellaDao.getListaSottocartelle(cartella));
    		}
			
    		request.getSession().setAttribute("user", sessionUser);
    		request.getSession().setAttribute("isHomePage", true);
    		request.getSession().setAttribute("isMovePage", false);
			request.getRequestDispatcher(jspPath).forward(request, response);
			
    	}catch(IllegalStateException error) {
			error.printStackTrace();
			response.sendError(HttpServletResponse.SC_CONFLICT, "Errore sconosciuto del server, riprovare più tardi");
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_CONFLICT, "Errore sconosciuto del server, riprovare più tardi");
		}
    	
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }
    
    public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
