<%@ page import="java.io.*,java.util.*"%>
<%@page import="model.Cartella"%>
<%@page import="model.Sottocartella"%>
<%@page import="model.Documento"%>
<%@page import="model.Utente"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>

<!DOCTYPE html>

<html>

    <% 
        Sottocartella sottocartella = request.getSession().getAttribute("sottocartella") == null ? 
                new Sottocartella() : (Sottocartella) request.getSession().getAttribute("sottocartella");  
        
        String erroreMovement = request.getSession().getAttribute("erroreMovement") == null ? 
                "" : (String) request.getSession().getAttribute("erroreMovement"); 

    %>

    <head> 

        <title> Documenti sottocartelle </title>

    </head>

    <body>
         <h2> <%= sottocartella.getCartella().getNome() %> > <%= sottocartella.getNome() %> > ... </h2>
        
        <ul>
            <% for(Documento doc : sottocartella.getListaDocumenti() ){ %>
            <li> <%= doc.getNome() %>  > 
                <a href="Documento?idDoc=<%=doc.getIdDocumento()%>"> accedi</a> | 
                <a href="Movement?idDocMove=<%=doc.getIdDocumento() %>"> sposta</a> </li>
            
            <% } %>
        </ul>

        <p style="color:red" > <%= erroreMovement %> </p>

        <a href="Home">Indietro</a>

    </body>


</hml>