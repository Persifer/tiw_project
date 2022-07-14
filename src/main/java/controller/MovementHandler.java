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


@WebServlet("/Movement")
public class MovementHandler extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private String jspPath = UtilityConstans.PRIVATEPATH + "home.jsp";
	private DocumentoDAO documentoDao;

       

    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
        this.documentoDao = new DocumentoDAO(connection);
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Utente sessionUser =  UtilityConstans.getSessionUtente(request, response);
				
		Integer idDocChange = -1;
		
		try {
			// controllo che effettivamente sia un numero oppure no
			idDocChange = ( (request.getParameter("idDocMove") == null) || (request.getParameter("idDocMove").isEmpty()) || 
					(request.getParameter("idDocMove").isBlank()) ) ?
							-1 :Integer.decode(request.getParameter("idDocMove")) ;
			
		} catch(NumberFormatException error ) {
			error.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserire un id di un documento valido");
			return;
		}
		
		if (idDocChange > 0) {
			
			Optional<Documento> tempCheckDoc = this.documentoDao.getDocumentByIdAndUser(idDocChange,
					sessionUser.getIdUtente());
			
			if (tempCheckDoc.isPresent()) {
				// se tempCheckDoc è presente, significa che l'id del documento inserito appartiene all'utente 
				// e quindi posso svolgere operazioni su di lui senza problemi

				request.getSession().setAttribute("user", sessionUser);
				request.getSession().setAttribute("idDocToMove", tempCheckDoc.get().getIdDocumento());
				request.getSession().setAttribute("currentSubfolder", tempCheckDoc.get().getSottocartella().getIdSottocartella());
				request.getSession().setAttribute("isHomePage", false);
				request.getSession().setAttribute("isMovePage", true);

				request.getRequestDispatcher(jspPath).forward(request, response);

			} else {

				// provo a spostare un documento non dell'utente in sessione
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Non puoi spostare a questo elemento!");
			} 
		} else {
			// idDocChange non è un id valido
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
