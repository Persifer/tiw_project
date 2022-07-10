<%@ page import="java.io.*,java.util.*"%>
<%@page import="model.Cartella"%>
<%@page import="model.Sottocartella"%>
<%@page import="model.Documento"%>
<%@page import="model.Utente"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>

<html>

    <% 
        Documento documento = request.getSession().getAttribute("documento") == null ? new Documento() : (Documento) request.getSession().getAttribute("documento");
    
    %>

    <head> 

        <title> <%= documento.getNome() %> </title>

    </head>

    <body>
        <h2>  
            <%= documento.getSottocartella().getCartella().getNome() %> > 
            <%= documento.getSottocartella().getNome() %> > 
            <%= documento.getNome() %> 
        </h2> <br />
        
        <div>
            <h3> titolo: <%= documento.getNome() %> </h3> 
                <i> Creato da <%= documento.getProprietarioDocumento().getUsername() %> il <%= documento.getDataCreazione().toLocalDate() %> </i>
        </div>

        <div>
            <h3> Sommario </h3><br />
            <%= documento.getSommario() %>
        </div>
        <br /><br /><br />
        <a href="Sottocartella?idSubFolder=<%= documento.getSottocartella().getIdSottocartella() %>">Indietro</a>

    </body>


</hml>