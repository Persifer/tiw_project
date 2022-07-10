package model;

import java.time.LocalDateTime;
import java.util.List;

public class Sottocartella {

    private Integer idSottocartella;
    private String  nome;
    private LocalDateTime dataCreazione;
    private Cartella cartella;
    private List<Documento> listaDocumenti;

    public Sottocartella(){
        super();
    }
    
    

    public Sottocartella(Integer idSottocartella, String nome, LocalDateTime dataCreazione, Cartella cartella,
			List<Documento> listaDocumenti) {
		super();
		this.idSottocartella = idSottocartella;
		this.nome = nome;
		this.dataCreazione = dataCreazione;
		this.cartella = cartella;
		this.listaDocumenti = listaDocumenti;
	}


	public Sottocartella(Integer idSottocartella, String nome, LocalDateTime dataCreazione, Cartella cartella) {
        this.idSottocartella = idSottocartella;
        this.nome = nome;
        this.dataCreazione = dataCreazione;
        this.cartella = cartella;
        this.listaDocumenti = null;
    }

    public Sottocartella(String nome, LocalDateTime dataCreazione, Cartella cartella) {
        this.nome = nome;
        this.dataCreazione = dataCreazione;
        this.cartella = cartella;
        this.listaDocumenti = null;
    }
    
    public Sottocartella(Integer idSottocartella, String nome, LocalDateTime dataCreazione) {
		super();
		this.idSottocartella = idSottocartella;
		this.nome = nome;
		this.dataCreazione = dataCreazione;
		this.cartella = null;
		this.listaDocumenti = null;
	}
    
    
    
    

    @Override
	public String toString() {
		return "Sottocartella [idSottocartella=" + idSottocartella + ", nome=" + nome + ", dataCreazione="
				+ dataCreazione + ", cartella=" + cartella + ", listaDocumenti=" + listaDocumenti
				+ ", getListaDocumenti()=" + getListaDocumenti() + ", getIdSottocartella()=" + getIdSottocartella()
				+ ", getNome()=" + getNome() + ", getDataCreazione()=" + getDataCreazione() + ", getCartella()="
				+ getCartella() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}



	public List<Documento> getListaDocumenti() {
		return listaDocumenti;
	}



	public void setListaDocumenti(List<Documento> listaDocumenti) {
		this.listaDocumenti = listaDocumenti;
	}



	public Integer getIdSottocartella() {
        return idSottocartella;
    }

    public void setIdSottocartella(Integer idSottocartella) {
        this.idSottocartella = idSottocartella;
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

    public Cartella getCartella() {
        return cartella;
    }

    public void setCartella(Cartella cartella) {
        this.cartella = cartella;
    }
}
