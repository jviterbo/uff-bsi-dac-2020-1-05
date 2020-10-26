/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import model.Entrada;

/**
 *
 * @author viter
 */
@WebServlet(name = "ConsultaTodosServlet", urlPatterns = {"/ConsultaTodosServlet"})
public class ConsultaTodosServlet extends HttpServlet {

    WebTarget wt;
    String corpo, saida;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        Entrada e = null;
        List<Entrada> lista = null;
        int status;
                
        try (PrintWriter out = response.getWriter()) {

            Client client = ClientBuilder.newClient();
            URI uri;
            try {
                uri = new URI("http://localhost:8080/AgendaREST/resources/agenda/todos");
                this.wt = client.target(uri);
                wt.request().accept("application/xml");
                Invocation call = wt.request().buildGet();
                Response resposta = call.invoke();
                status = resposta.getStatus();
                lista = resposta.readEntity(new GenericType<List<Entrada>>(){});
            } catch (URISyntaxException ex) {
                Logger.getLogger(ConsultaTodosServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Lista todas</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Entradas armazenadas na agenda</h1>");
            out.println("<ul>");
            String base = "http://localhost:8080/ClienteAgenda/ConsultaIdServlet";
            Iterator<Entrada> entradasAsIterator = lista.iterator();
                        while (entradasAsIterator.hasNext()) {
                Entrada ent = entradasAsIterator.next();
                String link = base+"?id="+ent.getId();
                out.println("<li><a href=\"" + link + "\">" + ent.getSobrenome() + ", " + ent.getNome() + "</a></li>");
            }
            out.println("</ul>");
            out.println(" <p><a href=\"http://localhost:8080/ClienteAgenda\">Página inicial</a></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
