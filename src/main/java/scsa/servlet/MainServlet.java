package scsa.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 6435629367120779511L;
	
	private String username = null;
	private String password = null;
	
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
			// call login handler
			handleLogin(request, response);
		} else {
			
			// check if user is logged in
			// if yes - go to index, else - go to login
			
			if ( this.isLoggedIn ) {
				RequestDispatcher dispather = request.getRequestDispatcher("/jsp/index.jsp");
				dispather.forward(request, response);
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
			this.isLoggedIn = true;
			
			if ( this.isLoggedIn ) {
				RequestDispatcher dispather = request.getRequestDispatcher("/jsp/index.jsp");
				dispather.forward(request, response);
			} else {
				RequestDispatcher dispather = request.getRequestDispatcher("/jsp/login.jsp");
				dispather.forward(request, response);
			}
			
		} else {
			RequestDispatcher dispather = request.getRequestDispatcher("/jsp/login.jsp");
			dispather.forward(request, response);
		}
	}
}
