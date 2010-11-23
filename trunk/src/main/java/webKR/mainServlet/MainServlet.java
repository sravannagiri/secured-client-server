package webKR.mainServlet;

/**
 * @author Sergii Melnychuk
 *
 */

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private String servletContextPath = null;

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		
		this.servletContextPath = 
			servletConfig.getServletContext().getRealPath("/") + File.separator;
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
		
		System.out.println("[WEB-KR] target: " + target + "\n");
		
		// process request handling
		
		response.getWriter().write( "Hello from web-kr" );
	}
}
