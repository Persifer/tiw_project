package controller.gestioneDocumenti;

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
import DAO.SottocartellaDAO;
import constans.UtilityConstans;
import model.Documento;
import model.Utente;
import utils.ConnectionHandler;


@WebServlet("/AggiungiDocumento")
public class AddDocumentHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DocumentoDAO documentoDao;
	private SottocartellaDAO sottocartellaDao;
	private Connection connection = null;
	private String jspPath = UtilityConstans.PRIVATEPATH + "gestione_documenti.jsp";
       

    public AddDocumentHandler() {
        super();
       
    }
    
    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
        this.documentoDao = new DocumentoDAO(connection);
        this.sottocartellaDao = new SottocartellaDAO(connection);
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	Utente sessionUser = UtilityConstans.getSessionUtente(request);
		
    	request.getSession().setAttribute("msgNewFolder", "");
		request.getSession().setAttribute("erroreNewFolder", "");
		
		request.getSession().setAttribute("erroreNewSubfolder", "");
		request.getSession().setAttribute("msgNewSubfolder", "");

		request.getSession().setAttribute("erroreNewDocument", "");
		request.getSession().setAttribute("msgNewDocument", "");
		
		String nomeDocumento = request.getParameter("newDocumento") != null ? request.getParameter("newDocumento") : "";
		String sommarioDocumento = request.getParameter("sommario") != null ? request.getParameter("sommario") : "";
		
		Integer idSottocartella = request.getParameter("sottocartella") != null ? Integer.decode(request.getParameter("sottocartella")) : -1;
		// controlla che la sottocartella sia dell'utente
		Optional<Integer> tempCheckIdSubfolder = this.sottocartellaDao.checkIfSubfolderIsOfTheUser(idSottocartella, sessionUser.getIdUtente());
		
		Optional<Documento> newTempFolder = null;
        
		if (tempCheckIdSubfolder.isPresent()) {	
			// la sottocartella appartiene all'utente
			
			// controllo che non esista un documento che ha già quel nome. Necessario perhcé potrei non aver mai aperto la sottocartella
			// durante la sessione di utilizzo e quindi potrei non avere i suoi dati salvati
			Optional<Integer> checkDocumentExistence = this.documentoDao.checkDocumentNameExistence(nomeDocumento, idSottocartella);
			
			if (checkDocumentExistence.isEmpty()) {
				// il documento non esiste, posso crearlo
				newTempFolder = this.documentoDao.createNewDocument(nomeDocumento, sommarioDocumento, sessionUser,
						idSottocartella);
				
				if (newTempFolder.isPresent()) {
					request.getSession().setAttribute("msgNewDocument", "Documento creato correttamente!");
				} else {
					request.getSession().setAttribute("erroreNewDocument",
							"Impossibile creare la sottocartella al momento. Riprovare pi&ugrave; tardi");
				} 
			} else {
				// il documento esiste, non devo crearlo
				request.getSession().setAttribute("erroreNewDocument",
						"Il nome del documento già esiste, inserirne un'altro");
			}
			
			request.getRequestDispatcher("GestioneDocumenti").forward(request, response);
				

			
		} else {
			// Qui si può arrivare per due motivazioni
			// 1) idSottocartella è uguale a -1 e quindi a priori non trova niente
			// 2) idSottocartella non è dell'utente
			response.sendError(403);
		}
		
	}
    
    public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
