package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.CartellaDAO;
import DAO.DocumentoDAO;
import DAO.SottocartellaDAO;
import constans.UtilityConstans;
import model.Cartella;
import model.Sottocartella;
import model.Utente;
import utils.ConnectionHandler;


@WebServlet("/GestioneDocumenti")
public class GestioneDocumentiHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Connection connection = null;
	private String jspPath = UtilityConstans.PRIVATEPATH + "gestione_documenti.jsp";
	
	private CartellaDAO cartellaDao;
	private SottocartellaDAO sottocartellaDao;
	private DocumentoDAO documentoDao;
       

    public GestioneDocumentiHandler() {
        super();
    }

    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
        this.sottocartellaDao = new SottocartellaDAO(connection);
        this.cartellaDao = new CartellaDAO(connection);
        this.documentoDao = new DocumentoDAO(connection);
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
				
		Utente sessionUser = UtilityConstans.getSessionUtente(request);
		// aggiorno la lista delle cartelle di un utente
		sessionUser.setListaCartelle(this.cartellaDao.getFolderByUser(sessionUser));
				
		// Lista delle sottocartelle delle singole cartelle per aggiunta nuovo documento
		// rifaccio le query sulle sottocartelle così da aggiornarle in caso di modifica
		for(Cartella cartella : sessionUser.getListaCartelle()) {
			cartella.setListaSottocartelle(this.sottocartellaDao.getListaSottocartelle(cartella));
		}
		
		
		request.getSession().setAttribute("listaCartelle", sessionUser.getListaCartelle());
		request.getRequestDispatcher(jspPath).forward(request, response);
		
	}
	


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
