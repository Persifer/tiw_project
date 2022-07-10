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
import model.Documento;
import model.Utente;
import utils.ConnectionHandler;

@WebServlet("/Execution")
public class ExecutionHandler extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private String jspPath = UtilityConstans.PRIVATEPATH + "home.jsp";
	
	private Connection connection = null;
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
		
		Utente sessionUser = UtilityConstans.getSessionUtente(request);
		request.getSession().setAttribute("erroreMovement", "");
		
		//id sottocartella in cui spostare il documento
		Integer idSubFolder = request.getParameter("idSubFolder") != null ? Integer.decode(request.getParameter("idSubFolder")) : -1;
		Optional<Integer> tempCheckIdSubfolder = this.sottocartellaDao.checkIfSubfolderIsOfTheUser(idSubFolder, sessionUser.getIdUtente());
		
		// Il documento è già controllato in quanto lo prelevo dalla sessione. Il controllo dell'id è stato fatto in MovementHandler
		// id del documento che voglio spostare
		Integer idDocToMove = request.getSession().getAttribute("idDocToMove") != null ? (Integer) request.getSession().getAttribute("idDocToMove"): -1;
				
		
		if (tempCheckIdSubfolder.isPresent()) {
			// la sottocartella appartiene all'utente e posso spostare il file
			
			// Controllo che il documento selezionato non abbia un nome di un documento già presente
			Optional<Integer> docNameAlredyInSubfolder = this.documentoDao.docNameAlredyInSubfolder(idDocToMove, idSubFolder);
			
			if (docNameAlredyInSubfolder.isPresent()) {
				// non esiste un file che ha stesso nome nella stessa sottocartella. procedo
				Optional<Documento> tempDoc = this.documentoDao.moveDocument(idDocToMove, idSubFolder);
				if (tempDoc.isPresent()) {
					request.getSession().setAttribute("erroreMovement", "");
					request.getRequestDispatcher("Home").forward(request, response);
				} else {
					// errore interno al server
					response.sendError(404);
				} 
				
			} else {
				// esiste un documento che ha lo stesso nome di quello che sto provando a spostare nella stessa sottocartella
				request.getSession().setAttribute("erroreMovement", "Il documento che stai provando a spostare esiste gi&agrave; nella cartella di"
						+ " destinazione. Cambiare nome o cartella di destinazione");
				request.getRequestDispatcher(UtilityConstans.PRIVATEPATH + "sottocartella.jsp").forward(request, response);
			}

				
			
		} else {
			// tempCheckIdSubfolder non è presente, accesso a sottocartella non dell'utente in sessione
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
