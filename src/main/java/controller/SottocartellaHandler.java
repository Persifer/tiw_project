package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.DocumentoDAO;
import DAO.SottocartellaDAO;
import constans.UtilityConstans;
import model.Sottocartella;
import model.Utente;
import utils.ConnectionHandler;


@WebServlet("/Sottocartella")
public class SottocartellaHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Connection connection = null;
	private String jspPath = UtilityConstans.PRIVATEPATH + "sottocartella.jsp";
	
	private SottocartellaDAO sottocartellaDao;
	private DocumentoDAO documentoDao;
       
    
    public SottocartellaHandler() {
        super();
        
    }
    
    public void init() throws ServletException {

        connection = ConnectionHandler.getConnection(getServletContext());
        this.sottocartellaDao = new SottocartellaDAO(connection);
        this.documentoDao = new DocumentoDAO(connection);
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Utente sessionUser =  UtilityConstans.getSessionUtente(request, response);

		request.getSession().setAttribute("erroreMovement", "");
		
		Integer idChosenSubFolder = -1;
		
		try {
			idChosenSubFolder = ( (request.getParameter("idSubFolder") == null) || (request.getParameter("idSubFolder").isEmpty()) || 
					(request.getParameter("idSubFolder").isBlank()) ) ?
							-1 :Integer.parseInt(request.getParameter("idSubFolder")) ;
		} catch(NumberFormatException error ) {
			error.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserire un id di sottocartella valido");
			return;
		}
		
		
				
		if (idChosenSubFolder > 0) {
			
			Optional<Sottocartella> tempCheckIdSubfolder = this.sottocartellaDao.getByIdAndUtente(idChosenSubFolder,
					sessionUser.getIdUtente());
			if (tempCheckIdSubfolder.isPresent()) {

				// la sottocartella appartiene all'utente

				Sottocartella subfolder = tempCheckIdSubfolder.get();

				// La sottocartella esiste veramente e quindi posso procedere nella sua visualizzazione
				subfolder.setListaDocumenti(
						this.documentoDao.getListaDocumentiByUserAndSubfolder(subfolder, sessionUser.getIdUtente()));

				request.getSession().setAttribute("sottocartella", subfolder);
				request.getRequestDispatcher(jspPath).forward(request, response);

			} else {
				// sto provando ad avere accesso a qualcosa che non è dell'utente in sessione 
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Non puoi avere accesso a questo elemento!");
			} 
		} else {
			//sottocartella con id negativo
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserire un id di sottocartella valido");
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
