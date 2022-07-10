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
		
    	Utente sessionUser = UtilityConstans.getSessionUtente(request);
		
    	request.getSession().setAttribute("erroreNewFolder", "");
    	request.getSession().setAttribute("msgNewFolder", "");
		
		request.getSession().setAttribute("erroreNewSubfolder", "");
		request.getSession().setAttribute("msgNewSubfolder", "");

		request.getSession().setAttribute("erroreNewDocument", "");
		request.getSession().setAttribute("msgNewDocument", "");
		
		String nomeSottocartella = request.getParameter("newSottocartella") != null ? request.getParameter("newSottocartella") : "";
		
		Integer idCartella = request.getParameter("cartella") != null ? Integer.decode(request.getParameter("cartella")) : -1;
        Optional<Integer> tempCheckedIdFolder = this.cartellaDao.checkIfFolderIsOfTheUser(sessionUser.getIdUtente(), idCartella);
		
        if (tempCheckedIdFolder.isPresent()) {
        	// è presente un'id 
        	System.out.println("tempCheckedIdFolder.get() ->" + tempCheckedIdFolder.get());
        	System.out.println("idCartella ->" + idCartella);
        	
			if (idCartella == tempCheckedIdFolder.get()) {
				// la cartella selezionata appartiene all'utente in sessione
				Optional<Integer> tempCheckSubfolderNameExistence = this.sottocartellaDao.checkSubfolderNameExistence(nomeSottocartella, idCartella);
				
				if (tempCheckSubfolderNameExistence.isEmpty()) {
					// la sottocartella non esiste
					Optional<Sottocartella> newTempFolder = this.sottocartellaDao.createNewSubfolder(nomeSottocartella,
							idCartella);
					
					if (newTempFolder.isPresent()) {
						request.getSession().setAttribute("msgNewSubfolder", "Sottocartella creata correttamente!");
					} else {
						request.getSession().setAttribute("erroreNewSubfolder",
								"Impossibile creare la sottocartella al momento. Riprovare pi&ugrave; tardi");
					}
					request.getRequestDispatcher("GestioneDocumenti").forward(request, response);
					
				} else {
					// la sottocartella esiste
					request.getSession().setAttribute("erroreNewSubfolder",
							"La sottocartella " + nomeSottocartella + " esiste gi&agrave;! Inserire un nome diverso");
					request.getRequestDispatcher("GestioneDocumenti").forward(request, response);
				}
				
			} else {
				// la cartella non appartiene all'utente in sessione
				System.out.println("la cartella non appartiene all'utente in sessione");
				response.sendError(403);
			}
		} else {
			// provo ad accedere a qualcosa che non è dell'utente in sessione
			System.out.println("provo ad accedere a qualcosa che non è dell'utente in sessione");
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
