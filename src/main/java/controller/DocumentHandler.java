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

import DAO.DocumentoDAO;
import constans.UtilityConstans;
import model.Documento;
import model.Utente;
import utils.ConnectionHandler;


@WebServlet("/Documento")
public class DocumentHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Connection connection = null;
	private String jspPath = UtilityConstans.PRIVATEPATH + "documento.jsp";
	
	private DocumentoDAO documentoDao;
	
	
	public DocumentHandler() {
		super();     
	}
	
	public void init() throws ServletException {
	    connection = ConnectionHandler.getConnection(getServletContext());
	    this.documentoDao = new DocumentoDAO(connection);
	} 

   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utente sessionUser =  UtilityConstans.getSessionUtente(request, response);
				
		Integer idDoc = -1;
		
		try {
			// controllo che effettivamente sia un numero oppure no
			idDoc = ( (request.getParameter("idDoc") == null) || (request.getParameter("idDoc").isEmpty()) || 
					(request.getParameter("idDoc").isBlank()) ) ?
							-1 :Integer.parseInt(request.getParameter("idDoc"));
			
		} catch(NumberFormatException error ) {
			error.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserire un id di un documento valido");
			return;
		}
		
		if (idDoc > 0) {
			
			Optional<Documento> tempCheckDoc = this.documentoDao.getDocumentByIdAndUser(idDoc,
					sessionUser.getIdUtente());
			
			if (tempCheckDoc.isPresent()) {
				// il documento appartiene all'utente, quindi posso visualizzarlo
				Documento doc = tempCheckDoc.get();

				// non ci sono errori ed il documento appartiene all'utente selezionato
				request.getSession().setAttribute("documento", doc);
				request.getRequestDispatcher(jspPath).forward(request, response);

			} else {

				// Sto provando ad accedere a qualcosa non dell'utente in sessione
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Non puoi avere accesso a questo elemento!");

			} 
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserire un id di un documento valido");
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
