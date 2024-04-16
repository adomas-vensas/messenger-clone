package org.example.messengerclone;

import java.io.*;

import entities.Message;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.MessageService;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;
    private MessageService messageService;
    private EntityManagerFactory emf;


    @Override
    public void init() throws ServletException {
        message = "Hello World!";
        emf = Persistence.createEntityManagerFactory("yourPersistenceUnitName");
        messageService = new MessageService(emf.createEntityManager());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Use the service to save a message, for example
        Message newMessage = new Message();
        newMessage.setText("Sample message content");
        messageService.saveMessage(newMessage);

        // Output to the client
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("<p>Message saved: " + newMessage.getText() + "</p>");
        out.println("</body></html>");
    }

    @Override
    public void destroy() {
        emf.close();  // Clean up
    }
}