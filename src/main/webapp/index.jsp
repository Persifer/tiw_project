<%@ page import="java.io.*,java.util.* "%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>

<!DOCTYPE html>

<html>

    <head>
        <meta charset="ISO-8859-1">
        <title> Login </title>
    </head>

    <body>

        <%
        
            String errore = request.getSession().getAttribute("errore") == null ? "" : (String) request.getSession().getAttribute("errore"); 
            String msg = request.getSession().getAttribute("msg") == null ? "" : (String) request.getSession().getAttribute("msg");
        
        %>

        <h2> Login </h2>
        <form action="Login" method="POST">
            Username <br />
            <input type="text" name="username"> <br /><br />

            Password <br />
            <input type="password" name="pwd"> <br /><br />

            <input type="submit" value="Login"><br />

            <p style="color:red" > <%= errore %> </p>
            <p> <%= msg %> </p>

        </form>

        <br /><br /><br />

        Non hai un account? <a href="Registration"> Registrati </a>

    </body>

</html>