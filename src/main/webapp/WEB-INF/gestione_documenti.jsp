<%@ page import="java.io.*,java.util.*"%>
<%@page import="model.Cartella"%>
<%@page import="model.Sottocartella"%>
<%@page import="model.Documento"%>
<%@page import="model.Utente"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>

<!DOCTYPE html>

<html>

    <head>
        <title> Gestione documenti </title>
    </head>

    <%

        List<Cartella> listaCartelle = ((List<Cartella>) request.getSession().getAttribute("listaCartelle")).isEmpty() ? 
            new ArrayList<>() : (List<Cartella>) request.getSession().getAttribute("listaCartelle");

        String erroreNewFolder = request.getSession().getAttribute("erroreNewFolder") == null ?
             "" : (String) request.getSession().getAttribute("erroreNewFolder"); 
        String msgNewFolder = request.getSession().getAttribute("msgNewFolder") == null ? 
            "" : (String) request.getSession().getAttribute("msgNewFolder");

        String erroreNewSubfolder = request.getSession().getAttribute("erroreNewSubfolder") == null ? 
            "" : (String) request.getSession().getAttribute("erroreNewSubfolder"); 
        String msgNewSubfolder = request.getSession().getAttribute("msgNewSubfolder") == null ? 
            "" : (String) request.getSession().getAttribute("msgNewSubfolder");

        String erroreNewDocument = request.getSession().getAttribute("erroreNewDocument") == null ? 
            "" : (String) request.getSession().getAttribute("erroreNewDocument"); 
        String msgNewDocument = request.getSession().getAttribute("msgNewDocument") == null ? 
            "" : (String) request.getSession().getAttribute("msgNewDocument");

    %>

    <body>
        <h2> Seleziona cosa vuoi fare </h2>

        <form action="AggiungiCartella" method="POST">
            Aggiungi nuova cartella &nbsp; &nbsp;
            <input type="text" name="newCartella"> &nbsp; &nbsp;
            <input type="submit" value="Aggiungi cartella">
        </form>

        <p style="color:red" > <%= erroreNewFolder %> </p>
        <p> <%= msgNewFolder %> </p>

        <% if(!listaCartelle.isEmpty()){ %>

            <br /><br />
            <hr>
            <br /><br />


            <form action="AggiungiSottocartella" method="POST">
                Aggiungi nuova sottocartella &nbsp; &nbsp;
                <select name="cartella" id="cartella">

                    <% for(Cartella cartella : listaCartelle) { %>
                        <option value="<%= cartella.getIdCartella() %>"> <%=cartella.getNome() %></option>
                    <% } %>

                </select> &nbsp; &nbsp;

                <input type="text" name="newSottocartella"> &nbsp; &nbsp;
                <input type="submit" value="Aggiungi sottocartella">

            </form>

            <p style="color:red" > <%= erroreNewSubfolder %> </p>
            <p> <%= msgNewSubfolder %> </p>
            

            <br /><br />
            <hr>
            <br /><br />

            <form action="AggiungiDocumento" method="POST">
                Aggiungi un nuovo documento &nbsp; &nbsp;
                <select name="sottocartella" id="sottocartella">

                    <% for(Cartella cartella : listaCartelle) { %>
                        <% for(Sottocartella subfolder : cartella.getListaSottocartelle()){ %>
                            <option value="<%= subfolder.getIdSottocartella() %>"> <%=cartella.getNome() %> | <%=subfolder.getNome() %></option>
                        <% } %>
                    <% } %>

                </select> <br /><br />

                Nome del documento<br />
                <input type="text" name="newDocumento"> <br /><br />
                Sommario <br />
                <textarea id="sommario" name="sommario" rows="5" cols="50"> </textarea> <br /><br />

                <input type="submit" value="Aggiungi documento">

            </form>

            <p style="color:red" > <%= erroreNewDocument %> </p>
            <p> <%= msgNewDocument %> </p>

           

        <% } %>


        <br /><br />
        <a href="Home">Indietro</a>

    </body>

</html>