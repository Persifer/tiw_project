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
import constans.UtilityConstans;
import model.Cartella;
import model.Utente;
import utils.ConnectionHandler;


@WebServlet("/AggiungiCartella")
public class AddFolderHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private CartellaDAO cartellaDao;
	private Connection connection = null;
	private String jspPath = UtilityConstans.PRIVATEPATH + "gestione_documenti.jsp";
	
    public AddFolderHandler() {
        super();
        
    }

    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
        this.cartellaDao = new CartellaDAO(connection);
        
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Utente sessionUser = UtilityConstans.getSessionUtente(request);
		
		request.getSession().setAttribute("msgNewFolder", "");
		request.getSession().setAttribute("erroreNewFolder", "");
		
		request.getSession().setAttribute("erroreNewSubfolder", "");
		request.getSession().setAttribute("msgNewSubfolder", "");

		request.getSession().setAttribute("erroreNewDocument", "");
		request.getSession().setAttribute("msgNewDocument", "");
		
		String nomeCartella = request.getParameter("newCartella") != null ? (String) request.getParameter("newCartella") : null;
		// controllo che il nome della cartella non esista per quell'utente
		Optional<Integer> checkFolderNameExistence = this.cartellaDao.checkFolderNameForUser(nomeCartella, sessionUser.getIdUtente());
			
        
		if (checkFolderNameExistence.isEmpty()) {
			//Il nome della cartella non esiste
			// creo la cartella
			Optional<Cartella> newTempFolder = this.cartellaDao.createNewFolder(nomeCartella, sessionUser);
			if (newTempFolder.isPresent()) {
				request.getSession().setAttribute("msgNewFolder", "Cartella creata correttamente!");
			} else {
				request.getSession().setAttribute("erroreNewFolder",
						"Impossibile creare la cartella al momento. Riprovare pi&ugrave; tardi");
			}
			request.getRequestDispatcher("GestioneDocumenti").forward(request, response);
			
		} else {
			// il nome della cartella esiste
			request.getSession().setAttribute("erroreNewFolder",
					"La cartella " + nomeCartella + " esiste gi&agrave;! Inserire un nome diverso");
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
