package net.jdkr.readingrover.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.jdkr.readingrover.App;

public class ViewController extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9034757451680916127L;
	private static final Logger logger = LogManager.getLogger(ViewController.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{

		String requestUri = request.getRequestURI() == null ? "" : request.getRequestURI();

		if (App.VIEW_MAPPINGS.containsKey(request.getRequestURI()))
		{
			try
			{
				request.getRequestDispatcher(App.VIEW_MAPPINGS.get(requestUri).getForwardUri()).forward(request, response);
			} catch (ServletException | IOException e)
			{
				logger.error(e);
			}
		} else
		{
			logger.error(String.format("No view for requestUri:%s in App.VIEW_MAPPINGS:%s", requestUri, App.VIEW_MAPPINGS.toString()));
		}

	}

}
