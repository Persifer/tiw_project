package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Cartella;
import model.Utente;

public class CartellaDAO {
	private final Connection connection;

	private UtenteDAO userDao;
	
    public CartellaDAO(Connection connection) {
        this.connection = connection;
        this.userDao = new UtenteDAO(connection);
        
    }
    
    // prende la lista delle cartelle di un utente
    public List<Cartella> getFolderByUser(Utente user){
    	String query = "SELECT idCartella, nome, data_creazione, idProprietario"
	    			 + " FROM Cartella c JOIN Utente u ON (c.idProprietario = u.idUtente)"
	    			 + " WHERE u.idUtente = ?;";
    	
    	List<Cartella> cartelleUtente = null;
    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		statement.setInt(1, user.getIdUtente());
    		
 
    		try(ResultSet result = statement.executeQuery()){
    			cartelleUtente = new ArrayList<>();
    			while(result.next()) {
    				
    				cartelleUtente.add(new Cartella(
    						result.getInt("idCartella"), result.getString("nome"),
    						result.getTimestamp("data_creazione").toLocalDateTime(), user));
    			}
    			
    			
    			return cartelleUtente;
    		}
    		
    	}catch(SQLException error) {
    		error.printStackTrace();
    		return new ArrayList<>();
    	}
    	
    	
    	
	}
    
    // controlla se la cartella selezionata è dell'utente, se si restituisce un id
    public Optional<Integer> checkIfFolderIsOfTheUser(Integer idUtente, Integer idCartella){
    	String query = "SELECT idCartella FROM Cartella WHERE idProprietario = ? AND idCartella = ?;";
    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		statement.setInt(1, idUtente);
    		statement.setInt(2, idCartella);
    		
    		try(ResultSet result = statement.executeQuery()){
    			if(result.next()) {
    				return Optional.of(result.getInt("idCartella"));
    			}else {
    				return Optional.empty();
    			}
    		}
    		
    	}catch(SQLException error) {
    		
    		error.printStackTrace();
    		return Optional.empty();
    		
    	}
    	
    	
    }
    
    // prende i dati di una cartella tramite il suo id
    public Optional<Cartella> getById(Integer idCartella){
		String query = "SELECT * FROM Cartella WHERE idCartella = ? ";
		
		try(PreparedStatement statement = connection.prepareStatement(query)){
			statement.setInt(1, idCartella);
			
			try(ResultSet result = statement.executeQuery()){
				if(result.next()) {
					Optional<Utente> tempUser = this.userDao.getById(result.getInt("idProprietario"));
					return Optional.of(new Cartella(
    						result.getInt("idCartella"), result.getString("nome"),
    						result.getTimestamp("data_creazione").toLocalDateTime(), 
    						(tempUser.isPresent() ? tempUser.get() : null )));
				}else {
					return Optional.empty();
				}
			}
		}catch( SQLException error) {
			error.printStackTrace();
			return Optional.empty();
		}
	}
    
    // crea una nuova cartella
    public Optional<Cartella> createNewFolder(String nomeCartella, Utente user){
    	
    	String query = "INSERT INTO Cartella(nome, data_creazione, idProprietario) VALUES(?, ?, ?);";
    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		Timestamp creationTime = Timestamp.from(Instant.now());
    		
    		statement.setString(1, nomeCartella);
    		statement.setTimestamp(2, creationTime);
    		statement.setInt(3, user.getIdUtente());
    		
    		int result = statement.executeUpdate();
    	
			if(result == 1) {
				// cartella creata
				return Optional.of(new Cartella(nomeCartella, creationTime.toLocalDateTime(), user));
			}else {
				return Optional.empty();
			}
    		
    	}catch(SQLException error) {
    		
    		error.printStackTrace();
    		return Optional.empty();
    		
    	}
    	
    }
    
    // controlla che non esista un'altra cartella con lo stesso nome per un determinato utente
    public Optional<Integer> checkFolderNameForUser(String nomeCartella, Integer idUtente){
		String query = "SELECT idCartella FROM Cartella WHERE nome = ? AND idProprietario =?;";
		
		try(PreparedStatement statement = connection.prepareStatement(query)){
			statement.setString(1, nomeCartella);
			statement.setInt(2, idUtente);
			
			try(ResultSet result = statement.executeQuery()){
				if(result.next()) {
					return Optional.of(result.getInt("idCartella"));
				}else {
					return Optional.empty();
				}
			}
		}catch( SQLException error) {
			error.printStackTrace();
			return Optional.empty();
		}
	} 

}
