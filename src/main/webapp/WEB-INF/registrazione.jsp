<%@ page import="java.io.*,java.util.* "%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>

<!DOCTYPE html>

<html>

    <head>
        <title> Registrazione </title>
    </head>

    <%
        
        String errore = request.getSession().getAttribute("errore") == null ? "" : (String) request.getSession().getAttribute("errore"); 

    %>  

    <body>
        <h2> Registrazione </h2>
        <form action="Registration" method="POST">
            Username <br />
            <input type="text" name="username"> <br /><br />

            Password <br />
            <input type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
            title="Deve contenere almeno un numero, un carattere minuscolo e maiuscolo  e deve essere lunga almeno 8 caratteri" 
            name="pwd"> <br /><br />

            <input type="submit" value="Registrati">  <br />

            <p style="color:red"> <%= errore %> </p>

        </form>

        <br /><br /><br />

    </body>

</html>