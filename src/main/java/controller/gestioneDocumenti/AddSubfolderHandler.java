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

import DAO.CartellaDAO;
import DAO.SottocartellaDAO;
import constans.UtilityConstans;
import model.Sottocartella;
import model.Utente;
import utils.ConnectionHandler;


@WebServlet("/AggiungiSottocartella")
public class AddSubfolderHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private CartellaDAO cartellaDao;
	private SottocartellaDAO sottocartellaDao;
	private Connection connection = null;
	private String jspPath = UtilityConstans.PRIVATEPATH + "gestione_documenti.jsp";
       
    public AddSubfolderHandler() {
        super();
        
    }
    
    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
        this.cartellaDao = new CartellaDAO(connection);
        this.sottocartellaDao = new SottocartellaDAO(connection);
        
    }
    
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	Utente sessionUser = UtilityConstans.getSessionUtente(request, response);
		
    	request.getSession().setAttribute("erroreNewFolder", "");
    	request.getSession().setAttribute("msgNewFolder", "");
		
		request.getSession().setAttribute("erroreNewSubfolder", "");
		request.getSession().setAttribute("msgNewSubfolder", "");

		request.getSession().setAttribute("erroreNewDocument", "");
		request.getSession().setAttribute("msgNewDocument", "");
		
				
		String nomeSottocartella = ((request.getParameter("newSottocartella") == null) || (request.getParameter("newSottocartella").isEmpty()) ||
				(request.getParameter("newSottocartella").isBlank())  ) ?
						null : (String) request.getParameter("newSottocartella") ;
		
		Integer idCartella = -1 ;
		
		try {
			// controllo che effettivamente sia un numero oppure no
			idCartella = ((request.getParameter("cartella") == null) || (request.getParameter("cartella").isEmpty()) ||(request.getParameter("cartella").isBlank())  ) ?
					 -1 :Integer.decode(request.getParameter("cartella")) ;
			
		} catch(NumberFormatException error ) {
			error.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserire un id di una cartella valida");
			return;
		}
		
		if (idCartella > 0) {
			// controllo che la cartella sia dell'utente
			Optional<Integer> tempCheckedIdFolder = this.cartellaDao.checkIfFolderIsOfTheUser(sessionUser.getIdUtente(),
					idCartella);
			
			if (tempCheckedIdFolder.isPresent()) {
				// la cartella selezionata appartiene all'utente in sessione

				if (nomeSottocartella != null) {
					// controllo che il nome della sottocartella non esista già
					Optional<Integer> tempCheckSubfolderNameExistence = this.sottocartellaDao
							.checkSubfolderNameExistence(nomeSottocartella, idCartella);
					
					if (tempCheckSubfolderNameExistence.isEmpty()) {
						// la sottocartella non esiste
						Optional<Sottocartella> newTempFolder = this.sottocartellaDao
								.createNewSubfolder(nomeSottocartella, idCartella);

						if (newTempFolder.isPresent()) {
							request.getSession().setAttribute("msgNewSubfolder", "Sottocartella creata correttamente!");
						} else {
							request.getSession().setAttribute("erroreNewSubfolder",
									"Impossibile creare la sottocartella al momento. Riprovare pi&ugrave; tardi");
						}
						request.getRequestDispatcher("GestioneDocumenti").forward(request, response);

					} else {
						// la sottocartella esiste
						request.getSession().setAttribute("erroreNewSubfolder", "La sottocartella " + nomeSottocartella
								+ " esiste gi&agrave;! Inserire un nome diverso");
						request.getRequestDispatcher("GestioneDocumenti").forward(request, response);
					} 
				} else {
					// nome della sosttocartella vuoto
					request.getSession().setAttribute("erroreNewSubfolder", "Non si può inserire un nome vuoto di cartella");
					request.getRequestDispatcher("GestioneDocumenti").forward(request, response);
				}

			} else {
				// provo ad accedere a qualcosa che non è dell'utente in sessione
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Non puoi avere accesso a quest'elemento!");
			} 
		} else {
			request.getSession().setAttribute("erroreNewSubfolder",
					"Inserire una cartella oppure selezionare una cartella esistente per creare la sottocartella!");
			request.getRequestDispatcher("GestioneDocumenti").forward(request, response);
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
