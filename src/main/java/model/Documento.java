package model;

import java.sql.Date;
import java.time.LocalDateTime;

public class Documento {

    private Integer idDocumento;
    private String nome;
    private String sommario;
    private LocalDateTime dataCreazione;
    
    private Utente proprietarioDocumento;
    private Sottocartella sottocartella;
    
    public Documento() {
    	super();
    }

    public Documento(Integer idDocumento, String nome, String sommario, LocalDateTime dataCreazione,
                     Utente proprietarioDocumento, Sottocartella sottocartella) {
        this.idDocumento = idDocumento;
        this.nome = nome;
        this.sommario = sommario;
        this.dataCreazione = dataCreazione;
        this.proprietarioDocumento = proprietarioDocumento;
        this.sottocartella = sottocartella;
    }

    public Documento(String nome, String sommario, LocalDateTime dataCreazione, Utente proprietarioDocumento, Sottocartella sottocartella) {
        this.nome = nome;
        this.sommario = sommario;
        this.dataCreazione = dataCreazione;
        this.proprietarioDocumento = proprietarioDocumento;
        this.sottocartella = sottocartella;
    }
    
    

    @Override
	public String toString() {
		return "Documento [idDocumento=" + idDocumento + ", nome=" + nome + ", sommario=" + sommario
				+ ", dataCreazione=" + dataCreazione + ", proprietarioDocumento=" + proprietarioDocumento
				+ ", sottocartella=" + sottocartella + "]";
	}

	public Integer getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSommario() {
        return sommario;
    }

    public void setSommario(String sommario) {
        this.sommario = sommario;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Utente getProprietarioDocumento() {
        return proprietarioDocumento;
    }

    public void setProprietarioDocumento(Utente proprietarioDocumento) {
        this.proprietarioDocumento = proprietarioDocumento;
    }

    public Sottocartella getSottocartella() {
        return sottocartella;
    }

    public void setSottocartella(Sottocartella sottocartella) {
        this.sottocartella = sottocartella;
    }
}
