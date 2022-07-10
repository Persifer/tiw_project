<%@ page import="java.io.*,java.util.*"%>
<%@page import="model.Cartella"%>
<%@page import="model.Sottocartella"%>
<%@page import="model.Utente"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>

<!DOCTYPE html>

<html> 

    <head> 

        <title>Home</title>

    </head>

    <% 
        Utente user= request.getSession().getAttribute("user") == null ? new Utente() : (Utente) request.getSession().getAttribute("user");

        Boolean isHomePage = request.getSession().getAttribute("isHomePage") == null ?
             true : (Boolean) request.getSession().getAttribute("isHomePage"); 

        Boolean isMovePage = request.getSession().getAttribute("isMovePage") == null ? 
            false : (Boolean) request.getSession().getAttribute("isMovePage");

        String currentSubfolder = request.getSession().getAttribute("currentSubfolder") == null ?
             "" : (String) request.getSession().getAttribute("currentSubfolder"); 


    %>

    <body>
        <h2>Homepage</h2> <br />

        <% if(isHomePage){ %>
        
            <i> Benvenuto <%= user.getUsername() %>  </i>  <br />

            <% if(!user.getListaCartelle().isEmpty() ){ %>

                <ul>

                    <% for(Cartella folder : user.getListaCartelle()) { %>
                    <li><%= folder.getNome() %></li>
                    <li style="list-style-type: none;"> 
                        <ul>
                            <%
                                for(Sottocartella subfolder :  folder.getListaSottocartelle()){     
                            %> 
                                    <li><a href="Sottocartella?idSubFolder=<%= subfolder.getIdSottocartella() %>"><%= subfolder.getNome() %> </a> </li>
                            <% 
                                }        
                            %>
                        </ul>
                    </li>


                    <% } %>

                </ul>

            <% } %>

            <a href="GestioneDocumenti">Gestione documenti </a> <br /><br />
            <a href="Logout">Logout</a>

        <% } %>

        <% if(isMovePage){ %>

            <i> Sposta il documento dentro una nuova sottocartella </i>

            <% if(!user.getListaCartelle().isEmpty() ){ %>

                <ul>

                    <% for(Cartella folder : user.getListaCartelle()) { %>
                    <li><%= folder.getNome() %></li>
                    <li style="list-style-type: none;"> 
                        <ul>
                            <%
                                for(Sottocartella subfolder :  folder.getListaSottocartelle()){     
                            %> 
                                    <% if(subfolder.getNome().equals(currentSubfolder) && !(subfolder.getNome().equals(""))) { %>
                                        <li> <%= subfolder.getNome() %>  <-- Tu sei qui </a> </li>
                                    <% }else{ %>
                                        <li><a href="Execution?idSubFolder=<%= subfolder.getIdSottocartella() %>"><%= subfolder.getNome() %> </a> </li>
                                    <% } %>
                                    
                            <% 
                                }        
                            %>
                        </ul>
                    </li>


                    <% } %>

                </ul>

            <% } %>

            <a href="GestioneDocumenti">Gestione documenti </a> <br /><br />
            <a href="Logout">Logout</a>
        
        <% } %>

    </body>


</hml>