package bank.server;

import bank.Bank;
import bank.commands.Command;
import bank.local.LocalBank;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.Enumeration;

@SuppressWarnings("serial")
@WebServlet(
    urlPatterns={"/bank/*"},
    initParams={
        @WebInitParam(name="title", value="Annotation based Bank")
    }
)
public class Servlet_Server extends HttpServlet {
  private static final long serialVersionUID = -718514557424640898L;
  private String title;
  private Bank bank;

  public void init() {
    title = getServletConfig().getInitParameter("title");
    bank = new bank.local.LocalBank();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
    System.out.println(request.getContentType());
    ObjectInputStream in = new ObjectInputStream(request.getInputStream());
    try{
      Command command = (Command) in.readObject();
      System.out.println("Recived command: " + command.getClass().getName());
      out.writeObject(command.execute(bank));
      System.out.println("Executued: " + command.getClass().getName());
    }catch (ClassNotFoundException | StreamCorruptedException e){
      System.out.println("Received Trash from client");
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doGet(req, resp);
  }

}