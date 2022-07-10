package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Cartella;
import model.Sottocartella;

public class SottocartellaDAO {
	
	private final Connection connection;

	private CartellaDAO folderDao;
	
    public SottocartellaDAO(Connection connection) {
        this.connection = connection;
        this.folderDao = new CartellaDAO(connection);
        
    }
    
    // prendi tutte le sottocartelle appartenenti ad una cartella
    public List<Sottocartella> getListaSottocartelle(Cartella cartella) throws IllegalStateException{
    	
		String query = "SELECT idSottocartella, s.nome, s.data_creazione"
					+ "	FROM Sottocartella s JOIN Cartella c ON (s.idCartella = c.idCartella)"
					+ " WHERE c.idCartella = ?;";
		
		List<Sottocartella> subfolderList;
		
		try(PreparedStatement statement = connection.prepareStatement(query)){
			statement.setInt(1, cartella.getIdCartella());
			
			try(ResultSet result = statement.executeQuery()){
				
				subfolderList = new ArrayList<>();
				
				while(result.next()) {
					subfolderList.add(new Sottocartella(
							result.getInt("idSottocartella"), result.getString("nome"),
							result.getTimestamp("data_creazione").toLocalDateTime(),
							cartella
							));
				}
									
				return subfolderList;
				
			}					
		}catch( SQLException error) {
			error.printStackTrace();
			return new ArrayList<>();
		}
		
	}
    
    // prendi i dati di una sottocartella tramite id
    public Optional<Sottocartella> getById(Integer idSottocartella){
    	
    	String query = "SELECT * FROM Sottocartella WHERE idSottocartella = ?;";
	
		try(PreparedStatement statement = connection.prepareStatement(query)){
			statement.setInt(1, idSottocartella);
			
			try(ResultSet result = statement.executeQuery()){
				if(result.next()) {			
					Cartella folder= (this.folderDao.getById(result.getInt("idCartella"))).isPresent() ? (this.folderDao.getById(result.getInt("idCartella"))).get() : null;
					return Optional.of(new Sottocartella(
							result.getInt("idSottocartella"), result.getString("nome"),
    						result.getTimestamp("data_creazione").toLocalDateTime(), folder
							));
				}else {
					return Optional.empty();
				}
			
			}					
		}catch( SQLException error) {
			error.printStackTrace();
			return Optional.empty();
		}
    }
    
    // controlla se una sottocartella è di un utente e restituisce solamente l'id della sottocartella
    public Optional<Integer> checkIfSubfolderIsOfTheUser(Integer idSottocartella, Integer idUtente){
    	String query = "SELECT s.idSottocartella"
	    			+ "	FROM Sottocartella s JOIN Cartella c ON (s.idCartella = c.idCartella) JOIN Utente u ON (c.idProprietario = u.idUtente)"
	    			+ " WHERE s.idSottocartella = ? AND u.idUtente = ?";
    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		statement.setInt(1, idSottocartella);
    		statement.setInt(2, idUtente);
    		
    		try(ResultSet result = statement.executeQuery()){
    			if(result.next()) {
    				return Optional.of(result.getInt("idSottocartella"));
    			}else {
    				return Optional.empty();
    			}
    		}
    		
    	}catch(SQLException error) {
    		
    		error.printStackTrace();
    		return Optional.empty();
    		
    	}
    	
    	
    }
    
    // crea una nuova sottocartella
    public Optional<Sottocartella> createNewSubfolder(String nomeSottocartella, Integer idCartella){
    	
    	String query = "INSERT INTO Sottocartella(nome, data_creazione, idCartella) VALUES(?, ?, ?);";
    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		Timestamp creationTime = Timestamp.from(Instant.now());
    		
    		statement.setString(1, nomeSottocartella);
    		statement.setTimestamp(2, creationTime);
    		statement.setInt(3, idCartella);
    		
    		int result = statement.executeUpdate();
    	
			if(result == 1) {
				Optional<Cartella> tempCartella = this.folderDao.getById(idCartella);
				
				// sottocartella creata creato
				return Optional.of(new Sottocartella(nomeSottocartella, creationTime.toLocalDateTime(), 
						tempCartella.isEmpty() ? null : tempCartella.get()));
			}else {
				return Optional.empty();
			}
    		
    	}catch(SQLException error) {
    		
    		error.printStackTrace();
    		return Optional.empty();
    		
    	}
    	
    }
    
    // prende i dati di una sottocartella associata ad un utente
    public Optional<Sottocartella> getByIdAndUtente(Integer idSottocartella, Integer idUtente){
    	String query = "SELECT s.idSottocartella, s.nome, s.data_creazione, s.idCartella"
	    			+ "	FROM Sottocartella s JOIN Cartella c ON (s.idCartella = c.idCartella) JOIN Utente u ON (c.idProprietario = u.idUtente)"
	    			+ " WHERE s.idSottocartella = ? AND u.idUtente = ?";
    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		statement.setInt(1, idSottocartella);
    		statement.setInt(2, idUtente);
    		
    		try(ResultSet result = statement.executeQuery()){
				if(result.next()) {			
					Cartella folder= (this.folderDao.getById(result.getInt("idCartella"))).isPresent() ? (this.folderDao.getById(result.getInt("idCartella"))).get() : null;
					return Optional.of(new Sottocartella(
							result.getInt("idSottocartella"), result.getString("nome"),
    						result.getTimestamp("data_creazione").toLocalDateTime(), folder
							));
				}else {
					return Optional.empty();
				}
			
			}		
    		
    	}catch(SQLException error) {
    		
    		error.printStackTrace();
    		return Optional.empty();
    		
    	}
    	
    	
    }
    
    // controlla che una cartella non esista già all'interno di una sottocartella
    public Optional<Integer> checkSubfolderNameExistence(String nomeSottocartella, Integer idCartella){
		String query = "SELECT idSottocartella "
					+ " FROM Sottocartella s JOIN Cartella c ON (s.idCartella = c.idCartella)"
					+ " WHERE s.nome = ? AND c.idCartella = ?;";
		
		try(PreparedStatement statement = connection.prepareStatement(query)){
			statement.setString(1, nomeSottocartella);
			statement.setInt(2, idCartella);
			
			System.out.println(statement);
			
			try(ResultSet result = statement.executeQuery()){
				if(result.next()) {
					return Optional.of(result.getInt("idSottocartella"));
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
