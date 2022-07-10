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
import model.Documento;
import model.Sottocartella;
import model.Utente;

public class DocumentoDAO {
	
	private final Connection connection;

	private UtenteDAO userDAO;
	private SottocartellaDAO subfolderDAO;
	
    public DocumentoDAO(Connection connection) {
        this.connection = connection;
        this.userDAO = new UtenteDAO(connection);
        this.subfolderDAO = new SottocartellaDAO(connection);
        
    }
    
       	
    	
	public List<Documento> getListaDocumentiByUserAndSubfolder(Sottocartella subfolder, Integer idUtente){
    	
    	String query = "SELECT d.idDocumento, d.nome, d.data_creazione, d.sommario, d.idUtenteProprietario, d.idSottocartella\r\n"
	    			 + "FROM Documento d JOIN Sottocartella s ON (d.idSottocartella = s.idSottocartella) \r\n"
	    			 + "WHERE s.idSottocartella = ?;";
    	
    	List<Documento> docs = null;
    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		statement.setInt(1,subfolder.getIdSottocartella());
    		
    		docs = new ArrayList<>();
    		
    		try(ResultSet result = statement.executeQuery()){
    			
    			Optional<Utente> tempUser = this.userDAO.getById(idUtente);
    			while(result.next()) {
    				
    				
    				docs.add(new Documento(
    						result.getInt("idDocumento"),
    						result.getString("nome"), result.getString("sommario"),
    						result.getTimestamp("data_creazione").toLocalDateTime(),
    						tempUser.isPresent()?tempUser.get() : null, 
    						subfolder
    						));
    			}
    			
    			return docs;
    		}
    		
    	}catch(SQLException error) {
    		error.printStackTrace();
    		return List.of();
    	}
    }
	
	public Optional<Documento> getDocumentById(Integer idDoc){
	    	
	    	String query = "SELECT * FROM Documento WHERE idDocumento = ?;";
	    		    	
	    	try(PreparedStatement statement = connection.prepareStatement(query)){
	    		
	    		statement.setInt(1,idDoc);
	    		

	    		try(ResultSet result = statement.executeQuery()){
	    		
	    			
	    			if(result.next()) {
	    				
	    				Optional<Utente> tempUser = this.userDAO.getById(result.getInt("idUtenteProprietario"));
		    			Optional<Sottocartella> tempSubfolder = this.subfolderDAO.getById(result.getInt("idSottocartella"));
	    				
	    				return Optional.of(new Documento(
	    						result.getInt("idDocumento"),
	    						result.getString("nome"), result.getString("sommario"),
	    						result.getTimestamp("data_creazione").toLocalDateTime(),
	    						tempUser.isPresent()?tempUser.get() : null, 
	    						tempSubfolder.isPresent()?tempSubfolder.get() : null
	    						));
	    			}
	    			
	    		}
	    		
	    	}catch(SQLException error) {
	    		error.printStackTrace();
	    		return Optional.empty();
	    	}
	    	
			return Optional.empty();
	}
	
	public Optional<Documento> getDocumentByIdAndUser(Integer idDoc, Integer idUtente){
    	
    	String query = "SELECT * FROM Documento WHERE idDocumento = ? AND idUtenteProprietario = ?;";
    		    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		statement.setInt(1,idDoc);
    		statement.setInt(2,idUtente);

    		try(ResultSet result = statement.executeQuery()){
    		
    			
    			if(result.next()) {
    				
    				Optional<Utente> tempUser = this.userDAO.getById(result.getInt("idUtenteProprietario"));
	    			Optional<Sottocartella> tempSubfolder = this.subfolderDAO.getById(result.getInt("idSottocartella"));
    				
    				return Optional.of(new Documento(
    						result.getInt("idDocumento"),
    						result.getString("nome"), result.getString("sommario"),
    						result.getTimestamp("data_creazione").toLocalDateTime(),
    						tempUser.isPresent()?tempUser.get() : null, 
    						tempSubfolder.isPresent()?tempSubfolder.get() : null
    						));
    			}
    			
    		}
    		
    	}catch(SQLException error) {
    		error.printStackTrace();
    		return Optional.empty();
    	}
    	
		return Optional.empty();
    }
	
	public Optional<Documento> createNewDocument(String nomeDocumento, String sommario, Utente user, Integer idSottocartella){
    	
    	String query = "INSERT INTO Documento(nome, data_creazione, sommario, idUtenteProprietario, idSottocartella) \r\n"
    			+ "	VALUES(?, ?, ? , ? ,? );";
    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		Timestamp creationTime = Timestamp.from(Instant.now());
    		
    		statement.setString(1, nomeDocumento);
    		statement.setTimestamp(2, creationTime);
    		statement.setString(3, sommario);
    		statement.setInt(4, user.getIdUtente());
    		statement.setInt(5, idSottocartella);
    		
    		int result = statement.executeUpdate();
    	
			if(result == 1) {				
				// sottocartella creata creato
				Optional<Sottocartella> tempSubfolder = this.subfolderDAO.getById(idSottocartella);
				
				return Optional.of(new Documento(
						nomeDocumento, sommario,
						creationTime.toLocalDateTime(),
						user, 
						tempSubfolder.isPresent()?tempSubfolder.get() : null
						));
			}else {
				return Optional.empty();
			}
    		
    	}catch(SQLException error) {
    		
    		error.printStackTrace();
    		return Optional.empty();
    		
    	}
    	
    }
	
	public Optional<Documento> moveDocument(Integer idDocumento, Integer idSottocartella){
    	
    	String query = "UPDATE Documento SET idSottocartella = ? WHERE idDocumento = ?; ";
    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		
     		
    		statement.setInt(1, idSottocartella);
    		statement.setInt(2, idDocumento);
    		    		
    		int result = statement.executeUpdate();
    	
			if(result == 1) {				
				// sottocartella creata creato
							
				return Optional.of(new Documento());
			}else {
				return Optional.empty();
			}
    		
    	}catch(SQLException error) {
    		
    		error.printStackTrace();
    		return Optional.empty();
    		
    	}
    	
    }
	
	public Optional<Integer> checkIfDocumentIsOfTheUser(Integer idDocumento, Integer idUtente){
		
		String query = "SELECT idDocumento FROM Documento WHERE idUtenteProprietario = ? AND idDocumento = ?;";
		
		try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		statement.setInt(1, idUtente);
    		statement.setInt(2, idDocumento);
    		
    		try(ResultSet result = statement.executeQuery()){
    			if(result.next()) {
    				return Optional.of(result.getInt("idDocumento"));
    			}else {
    				return Optional.empty();
    			}
    		}
    		
    	}catch(SQLException error) {
    		
    		error.printStackTrace();
    		return Optional.empty();
    		
    	}
		
		
	}
	
	public Optional<Integer> checkDocumentNameExistence(String nomeDocumento, Integer idUtente){
		
		String query = "SELECT idDocumento"
					+ "	FROM Documento d JOIN Sottocartella s ON (d.idSottocartella = s.idSottocartella)"
					+ " WHERE s.idSottocartella = ? AND d.nome = ?;";
		
		try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		statement.setInt(1, idUtente);
    		statement.setString(2, nomeDocumento);
    		
    		try(ResultSet result = statement.executeQuery()){
    			if(result.next()) {
    				return Optional.of(result.getInt("idDocumento"));
    			}else {
    				return Optional.empty();
    			}
    		}
    		
    	}catch(SQLException error) {
    		
    		error.printStackTrace();
    		return Optional.empty();
    		
    	}
		
		
	}
	
	public Optional<Integer> docNameAlredyInSubfolder(Integer idDocumentoDaSpostare, Integer idSottocartellaDiDestinazione){
		
		String query = "SELECT idDocumento, d.nome"
					+ "	FROM Documento d "
					+ " WHERE d.idDocumento = ? " // id documento da spostare 
					+ "		AND d.nome not in("
					+ "							SELECT dd.nome"
					+ "								FROM Documento dd JOIN Sottocartella sf ON (dd.idSottocartella = sf.idSottocartella)"
					+ "                                WHERE sf.idSottocartella = ?" // id sottocartella di destinazione
					+ "						 );";
		
		try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		statement.setInt(1, idDocumentoDaSpostare);
    		statement.setInt(2, idSottocartellaDiDestinazione);
    		
    		try(ResultSet result = statement.executeQuery()){
    			if(result.next()) {
    				// quando provo a spostare un documento in una sotto cartella diversa che non ha
    				// un documento con nome uguale a quello che sto provando a spostare mi da il documento che sto spostando
    				// questo significa che non esiste un documento con lo stesso nome in quella sottocartella
    				return Optional.of(result.getInt("idDocumento"));
    			}else {
    				// quando provo a spostare un documento in una sotto cartella diversa che ha un documento con 
    				// nome uguale a quello che sto provando a spostare mi da vuoto
    				// questo significa che esiste un documento con lo stesso nome in quella sottocartella
    				return Optional.empty();
    			}
    		}
    		
    	}catch(SQLException error) {
    		
    		error.printStackTrace();
    		return Optional.empty();
    		
    	}
		
		
	}
	
}


// JUST IN CASE CODE 
//===============================================================================================================================================================	
	
	
	// AL MOMENTO NON DA UTILIZZARE, MANTENUTA NEL CASO SI RILEVASSE UTILE
    /*public List<Documento> getByUserAndFolder(Utente user, Sottocartella subfolder){
    	
    	String query = "SELECT d.nome, d.data_creazione, d.sommario, d.idUtenteProprietario, d.idSottocartella"
	    			+  "FROM Documento d JOIN Sottocartella s ON (d.idSottocartella = s.idSottocartella)"
	    			+  "    JOIN Cartella c ON (s.idCartella = c.idCartella"
	    			+  "    JOIN Utente u ON (c.idProprietario = u.idUtente)"
	    			+  "WHERE s.idSottocartella = ? AND c.idCartella = ? AND u.idUtente = ?; ";
    	
    	List<Documento> docs = null;
    	
    	try(PreparedStatement statement = connection.prepareStatement(query)){
    		
    		statement.setInt(1,subfolder.getIdSottocartella());
    		statement.setInt(2,subfolder.getCartella().getIdCartella());
    		statement.setInt(3,user.getIdUtente());
    		
    		docs = new ArrayList<>();
    		
    		try(ResultSet result = statement.executeQuery()){
    			while(result.next()) {
    				docs.add(new Documento(
    						result.getString("nome"), result.getString("sommario"),
    						result.getTimestamp("data_creazione").toLocalDateTime(),
    						user, subfolder
    						));
    			}
    			
    			return docs;
    		}
    		
    	}catch(SQLException error) {
    		error.printStackTrace();
    		return List.of();
    	}*/

//===============================================================================================================================================================	


