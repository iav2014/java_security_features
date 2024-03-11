package com.XSS;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.apache.commons.text.StringEscapeUtils;
/*
Cross-Site Scripting (XSS)
En el contexto de una aplicación web Java, un ejemplo de XSS se daría cuando la aplicación toma entradas del usuario
y las incrusta directamente en la página web sin sanitizar o escapar adecuadamente.
 */
public class XSSVulnerableServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        // Tomar la entrada del usuario directamente de la petición
        String userInput = request.getParameter("userInput");
        // Incrustar la entrada del usuario directamente en la respuesta HTML
        out.println("<html><body>");
        out.println("<h1>Hola, " + userInput + "</h1>");
        out.println("</body></html>");
    }
// solucion
    protected void doGet2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        // Tomar la entrada del usuario directamente de la petición
        String userInput = request.getParameter("userInput");
        // Escapar la entrada del usuario para prevenir XSS
        String safeUserInput = StringEscapeUtils.escapeHtml4(userInput); // satinizar
        out.println("<html><body>");
        out.println("<h1>Hola, " + safeUserInput + "</h1>");
        out.println("</body></html>");
    }
}


