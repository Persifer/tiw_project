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
import model.Sottocartella;
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
		Utente sessionUser = UtilityConstans.getSessionUtente(request);
		
		Integer idDoc = request.getParameter("idDoc") != null ? Integer.decode(request.getParameter("idDoc")) : -1;		
		Optional<Integer> tempCheckId = this.documentoDao.checkIfDocumentIsOfTheUser(idDoc, sessionUser.getIdUtente());
		
		
		if (tempCheckId.isPresent()) {
						
			if (idDoc == tempCheckId.get()) {
				
				Optional<Documento> tempDoc = this.documentoDao.getDocumentById(idDoc);
				
				if (tempDoc.isEmpty()) {
					// errore dovuto ad una modifica dell'id da parte dell'utente che genera un errore
					response.sendError(404); //-> old code
				}
				
				Documento doc = tempDoc.get();
				
				if (doc.getProprietarioDocumento().getIdUtente() == sessionUser.getIdUtente()) {
					// non ci sono errori ed il documento appartiene all'utente selezionato
					request.getSession().setAttribute("documento", doc);
					request.getRequestDispatcher(jspPath).forward(request, response);
					
				} else {
					// errore in cui il documento prelevato non � quello scelto. Eventualit� si spera impossibile
					response.sendError(418); 
				}
				
			} else {
				// non posso accedere a questo documento in quanto non � dell'utente in sessione
				response.sendError(403);
			}
			
		} else {
			
			// Sto provando ad accedere a qualcosa non dell'utente in sessione
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
