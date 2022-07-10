package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.UtenteDAO;
import model.Utente;
import utils.ConnectionHandler;

@WebServlet("/Login")
public class LoginHandler extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

    private Connection connection = null;

    public LoginHandler(){
        super();
    }

    public void init() throws ServletException{
        connection = ConnectionHandler.getConnection(getServletContext());
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	request.getSession().setAttribute("errore","");
    	request.getSession().setAttribute("msg","");

        String username = request.getParameter("username") != null ? request.getParameter("username") : null;
        String pwd = request.getParameter("pwd") != null ? request.getParameter("pwd") : null;

        String error = "";

        if(username == null || username.equals("")){
            error += "Inserisci un username! ";
        }

        if(pwd == null || pwd.equals("")){
            error += "Inserisci una password!";
        }
        
        
        UtenteDAO daoChecker = new UtenteDAO(connection);
        Utente checkedUser = null;
        
        
    	Optional<Utente> tempUser = daoChecker.checkUser(username, pwd);
        if(tempUser.isPresent()) {
        	checkedUser = tempUser.get();
        }
        

        String path = "";
        //System.out.println("Error: " + error);
    
        if(error.equals("")){
        	 if(checkedUser == null){ //controlla esistenza utente       	
             	request.getSession().setAttribute("errore", "Impossibile trovare l'account. Registrarsi!");
             	request.getRequestDispatcher("index.jsp").forward(request, response);   
             }else {
            	 request.getSession().setAttribute("user", checkedUser);
                 response.sendRedirect("Home"); 
             }
        }else {
        	request.getSession().setAttribute("errore", error);
        	request.getRequestDispatcher("index.jsp").forward(request, response);  
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

