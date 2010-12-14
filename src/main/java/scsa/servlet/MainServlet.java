package scsa.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import scsa.client.Client;

public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 6435629367120779511L;
	
	private String username = null;
	private String password = null;
	
	private Client currentClient = null;
	
	private boolean isLoggedIn = false;
	
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
	}

	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException
	{
		handle(request, response);
	}
	
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException
	{
		handle(request, response);
	}
	
	private void handle(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("utf-8");
		
		String target = request.getRequestURI().substring(
				request.getContextPath().length());
		
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		
		if ( target.equals("/login") ) {
			if ( ! this.isLoggedIn )
				handleLogin(request, response);
		} else {
			if ( this.isLoggedIn ) {
				
				if ( target.equals("/doCommand") ) {
					
					String command = request.getParameter("command");
					String params = request.getParameter("params");
					
					if ( (params == null) || "".equals(params) ) 
						params = "";
					else 
						params = " " + params;
					
					String result = "";
					try {
						result = this.currentClient.command(command + params);
					} catch (Exception e) {
						result = e.getMessage();
					}
					
					response.getWriter().write("\n> " + 
							command + " " + params + "\n" +
							result);
					
				} else {
					
					if ( target.equals("/logout") ) {
						this.isLoggedIn = false;
						try {
							this.currentClient.command("quit");
						} catch (Exception e) {
							//
						}
						
						RequestDispatcher dispather = 
							request.getRequestDispatcher("/jsp/login.jsp");
						dispather.forward(request, response);
					} else {
						RequestDispatcher dispather = 
							request.getRequestDispatcher("/jsp/index.jsp");
						dispather.forward(request, response);
					}
					
				}
				
			} else {
				RequestDispatcher dispather = request.getRequestDispatcher("/jsp/login.jsp");
				dispather.forward(request, response);
			}
		}
	}
	
	private void handleLogin(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException
	{
		if ( request.getParameter("username") != null && 
				request.getParameter("password") != null ) {
			// do user login in separate thread
			
			this.username = request.getParameter("username");
			this.password = request.getParameter("password");
			
			System.out.println(this.username + "; " + this.password);
			
			// login results
			String errorMessage = "";
			
			this.currentClient = new Client();
			
			try {
				if ( this.currentClient.connectAndHandshake("localhost", 7070) ) {
					if ( this.currentClient.login(username, password) ) {
						
						this.isLoggedIn = true;
						
					} else {
						errorMessage = "Login failed";
					}
				} else {
					errorMessage = "Connection failed";
				}
			} catch (Exception e) {
				System.out.println("[ERROR] " + e.getMessage());
				errorMessage = e.getMessage();
			}
			
			if ( this.isLoggedIn ) {
				RequestDispatcher dispather = request.getRequestDispatcher("/jsp/index.jsp");
				dispather.forward(request, response);
			} else {
				request.setAttribute("errorMessage", errorMessage);
				
				RequestDispatcher dispather = request.getRequestDispatcher("/jsp/login.jsp");
				dispather.forward(request, response);
			}
			
		} else {
			RequestDispatcher dispather = request.getRequestDispatcher("/jsp/login.jsp");
			dispather.forward(request, response);
		}
	}
}
