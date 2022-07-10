package model;

import java.util.List;

public class Utente {

    private Integer idUtente;
    private String username;
    private String password;
    private List<Cartella> listaCartelle;
    private List<Documento> listaDocumenti;

    public Utente(){
        super();
    }
    
    public Utente(Integer idUtente, String username, String password, List<Cartella> listaCartelle,
			List<Documento> listaDocumenti) {
		super();
		this.idUtente = idUtente;
		this.username = username;
		this.password = password;
		this.listaCartelle = listaCartelle;
		this.listaDocumenti = listaDocumenti;
	}
    
    
    public Utente(Integer idUtente, String username, String password) {
        this.idUtente = idUtente;
        this.username = username;
        this.password = password;
        this.listaCartelle = null;
		this.listaDocumenti = null;
    }
    
	public Utente(String username, String password) {
        this.username = username;
        this.password = password;
        this.listaCartelle = null;
		this.listaDocumenti = null;
    }

    @Override
	public String toString() {
		return "Utente [idUtente=" + idUtente + ", username=" + username + ", password=" + password + ", listaCartelle="
				+ listaCartelle + ", listaDocumenti=" + listaDocumenti + "]";
	}

	public List<Cartella> getListaCartelle() {
		return listaCartelle;
	}

	public void setListaCartelle(List<Cartella> listaCartelle) {
		this.listaCartelle = listaCartelle;
	}

	public List<Documento> getListaDocumenti() {
		return listaDocumenti;
	}

	public void setListaDocumenti(List<Documento> listaDocumenti) {
		this.listaDocumenti = listaDocumenti;
	}

	public Integer getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
