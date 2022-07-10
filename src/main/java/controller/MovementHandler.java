package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

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
import model.Documento;
import model.Utente;
import utils.ConnectionHandler;


@WebServlet("/Movement")
public class MovementHandler extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private String jspPath = UtilityConstans.PRIVATEPATH + "home.jsp";
	private DocumentoDAO documentoDao;
	private CartellaDAO cartellaDao;
	private SottocartellaDAO sottocartellaDao;
       

    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
        this.documentoDao = new DocumentoDAO(connection);
        this.cartellaDao = new CartellaDAO(connection);
        this.sottocartellaDao = new SottocartellaDAO(connection);
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Integer idDocChange = request.getParameter("idDocMove") != null ? Integer.decode(request.getParameter("idDocMove")) : -1;
		
		Utente sessionUser = UtilityConstans.getSessionUtente(request);
		
		Optional<Integer> tempCheckId = this.documentoDao.checkIfDocumentIsOfTheUser(idDocChange, sessionUser.getIdUtente());
		
		if (tempCheckId.isPresent()) {
			
			
			if (idDocChange == tempCheckId.get()) {
				// Qui il documento richiesto appartiene all'utente
				Optional<Documento> tempDoc = this.documentoDao.getDocumentById(idDocChange);
				sessionUser.setListaCartelle(cartellaDao.getFolderByUser(sessionUser));
				
				if (tempDoc.isEmpty()) {

					response.sendError(404);
				} 				
				//qui aggiorno le cartelle ?
				for (Cartella cartella : sessionUser.getListaCartelle()) {
					cartella.setListaSottocartelle(sottocartellaDao.getListaSottocartelle(cartella));
				}
				
				request.getSession().setAttribute("user", sessionUser);
				request.getSession().setAttribute("idDocToMove", tempDoc.get().getIdDocumento());
				request.getSession().setAttribute("currentSubfolder", tempDoc.get().getSottocartella().getNome());
				request.getSession().setAttribute("isHomePage", false);
				request.getSession().setAttribute("isMovePage", true);
				
				request.getRequestDispatcher(jspPath).forward(request, response);
				
			} else {
				// provo a spostare un documento non dell'utente in sessione
				System.out.println("idDocChange | provo a spostare un documento non dell'utente in sessione");
				response.sendError(403);
			}
			
		} else {
			
			// provo a spostare un documento non dell'utente in sessione
			System.out.println("tempCheckId | provo a spostare un documento non dell'utente in sessione");
			response.sendError(403);
		}
		
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