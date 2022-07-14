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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DAO.UtenteDAO;
import constans.UtilityConstans;
import model.Utente;
import utils.ConnectionHandler;


@WebServlet("/Registration")
public class RegistrationHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String PASSWORD_PATTERN = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}";

    private Pattern pattern;

	
	private Connection connection = null;
	private String jspPath = UtilityConstans.PRIVATEPATH + "registrazione.jsp";
       
    public RegistrationHandler() {
        super();
        
    }
    
    private boolean isValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    public void init() throws ServletException{
    	connection = ConnectionHandler.getConnection(getServletContext());
    	this.pattern = Pattern.compile(PASSWORD_PATTERN);
       
    }
    
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute("errore", "");
		request.getRequestDispatcher(jspPath).forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = ( (request.getParameter("username") == null) || (request.getParameter("username").isEmpty()) || 
				(request.getParameter("username").isBlank()) ) 
        			? null : (String)request.getParameter("username");
        
        String pwd = ( (request.getParameter("pwd") == null) || (request.getParameter("pwd").isEmpty()) || 
				(request.getParameter("pwd").isBlank()) ) 
        			? null : (String)request.getParameter("pwd");
		
		String error = "";
		String path = "";
		
		UtenteDAO userDao = new UtenteDAO(connection);
		Utente newUser = null;
		
		
		if(username.equals("")) {
			error+="Inserisci un username!\n";
		}
		
		if(pwd.equals("")) {
			error += "Inserisci una password!\n";
		}
		
		if(!isValid(pwd)) {
			error += "Inserire una password che contenga almeno un numero, un carattere minuscolo e maiuscolo e che sia lunga almeno 8 caratteri\n";
		}
		
		// faccio questo controllo per evitare, in caso di credenziali mancanti, il controllo nel database sull'esitenza dell'utente.
		// se sono già presenti degli errori, allora non procedo al controllo dell'utente. Altrimenti si procede
		if (error.equals("")) {
			Optional<Utente> tempUser = userDao.getByUsername(username);
			if (tempUser.isPresent()) {
				error += "Username già selezionato, inserirne un'altro!\n";
			}else {
				tempUser = userDao.createNewUser(username, pwd);
				if(tempUser.isEmpty()) {
					error += "Siamo spiacenti, al momento siamo impossibilitati a creare un nuovo utente. Riprovi pi&ugrave; tardi\n";
				}else {
					newUser = tempUser.get();
				}
			}
		}
		
		if(error.equals("")) {
			// non sono presenti errori, allora vado un passo avanti
			
			if(newUser != null) {
				// nessun errore sull'utente, procedo 
				request.getSession().setAttribute("msg", "Il suo utente è stato creato correttamente. Eseguire il login");
				request.getRequestDispatcher("index.jsp").forward(request, response);
				
			}else {
				// errore sconosciuto. Controllo effettuato per pura completezza e per evitare qualsiasi errore
				request.getSession().setAttribute("errore", "Errore sconosciuto, riprovare più tardi");
             	request.getRequestDispatcher(jspPath).forward(request, response);  
			}
		}else { // sono presenti errori
			request.getSession().setAttribute("errore", error);
         	request.getRequestDispatcher(jspPath).forward(request, response);  

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
