package model;

import java.time.LocalDateTime;
import java.util.List;

public class Cartella {

    private Integer idCartella;
    private String  nome;
    private LocalDateTime dataCreazione;
    private Utente proprietarioCartella;
    private List<Sottocartella> listaSottocartelle;

    public Cartella(){
        super();
    }
    
    
    public Cartella(Integer idCartella, String nome, LocalDateTime dataCreazione, Utente proprietarioCartella,
			List<Sottocartella> listaSottocartelle) {
		super();
		this.idCartella = idCartella;
		this.nome = nome;
		this.dataCreazione = dataCreazione;
		this.proprietarioCartella = proprietarioCartella;
		this.listaSottocartelle = listaSottocartelle;
	}


	public Cartella(Integer idCartella, String nome, LocalDateTime dataCreazione, Utente proprietario) {
        this.idCartella = idCartella;
        this.nome = nome;
        this.dataCreazione = dataCreazione;
        this.proprietarioCartella = proprietario;
        this.listaSottocartelle = null;
    }

    public Cartella(String nome, LocalDateTime dataCreazione, Utente proprietario) {
        this.nome = nome;
        this.dataCreazione = dataCreazione;
        this.proprietarioCartella = proprietario;
        this.listaSottocartelle = null;
    }

    public Cartella( String nome, LocalDateTime dataCreazione) {
        this.nome = nome;
        this.dataCreazione = dataCreazione;
        this.listaSottocartelle = null;
    }
    

    @Override
	public String toString() {
		return "Cartella [idCartella=" + idCartella + ", nome=" + nome + ", dataCreazione=" + dataCreazione
				+ ", proprietarioCartella=" + proprietarioCartella + ", listaSottocartelle=" + listaSottocartelle + "]";
	}


	public List<Sottocartella> getListaSottocartelle() {
		return listaSottocartelle;
	}


	public void setListaSottocartelle(List<Sottocartella> listaSottocartelle) {
		this.listaSottocartelle = listaSottocartelle;
	}


	public Integer getIdCartella() {
        return idCartella;
    }

    public void setIdCartella(Integer idCartella) {
        this.idCartella = idCartella;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Utente getProprietarioCartella() {
        return proprietarioCartella;
    }

    public void setProprietarioCartella(Utente proprietarioCartella) {
        this.proprietarioCartella = proprietarioCartella;
    }
}
