package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import model.Utente;

public class UtenteDAO {
	
    private final Connection connection;

    public UtenteDAO(Connection connection) {
        this.connection = connection;
    }

    
    public Optional<Utente> checkUser(String username, String pwd){
    	
    	String query = "SELECT * FROM Utente WHERE username = ? AND password = ?";
    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		statement.setString(1, username);
    		statement.setString(2, pwd);
    		
    		try(ResultSet result = statement.executeQuery()){
    			if(result.next()) {
    				return Optional.of(new Utente(result.getInt("idUtente"), result.getString("username"), result.getString("password")));
    			}else {
    				return Optional.empty();
    			}
    		}
    		
    	}catch(SQLException error) {
    		error.printStackTrace();
    		return Optional.empty();
    	}
    }
    
    public Optional<Utente> getByUsername(String username) {
    	
    	String query = "SELECT * FROM Utente WHERE username = ? ";
    	
    	
    	try (PreparedStatement statement = connection.prepareStatement(query)) {
    		statement.setString(1, username);
    		try (ResultSet resultSet = statement.executeQuery()) {
    			if (resultSet.next()) {
    				return Optional.of(new Utente(resultSet.getInt("idUtente"), resultSet.getString("username"), resultSet.getString("password")));
    			} else {
    				return Optional.empty();
    			}
    		}
    	} catch (SQLException e) {
			e.printStackTrace();
			return Optional.empty();
		}
    	
    }
    
    public Optional<Utente> getById(Integer idUtente) {
    	
    	String query = "SELECT * FROM Utente WHERE idUtente = ? ";
    	
    	
    	try (PreparedStatement statement = connection.prepareStatement(query)) {
    		statement.setInt(1, idUtente);
    		try (ResultSet resultSet = statement.executeQuery()) {
    			if (resultSet.next()) {
    				return Optional.of(new Utente(resultSet.getInt("idUtente"), resultSet.getString("username"), resultSet.getString("password")));
    			} else {
    				return Optional.empty();
    			}
    		}
    	} catch (SQLException e) {
			e.printStackTrace();
			return Optional.empty();
		}
    	
    }
    
    public Optional<Utente> createNewUser(String username, String pwd){
    	
    	String query = "INSERT INTO Utente(username, password) VALUES(?,?)";
    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		statement.setString(1, username);
    		statement.setString(2, pwd);
    		
    		int result = statement.executeUpdate();
    	
			if(result == 1) {
				// utente creato
				return Optional.of(new Utente(username, pwd));
			}else {
				return Optional.empty();
			}
    		
    	}catch(SQLException error) {
    		
    		error.printStackTrace();
    		return Optional.empty();
    		
    	}
    	
    }
    
    

}
