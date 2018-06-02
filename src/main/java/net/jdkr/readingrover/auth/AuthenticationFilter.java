package net.jdkr.readingrover.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class AuthenticationFilter implements Filter
{
    static private Logger logger = null; 
    
    synchronized static private Logger getLogger()
    {
        if (null == logger)
        {
            synchronized (AuthenticationFilter.class)
            {
                logger = LogManager.getLogger(AuthenticationFilter.class);
            }
        }
        
        return logger;
    }
    
    static public final String LOGIN_URL = "/public/login";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        // Nothing to do
        getLogger().trace("init<>");
    }
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
    {
        try
        {
            if (! ((req instanceof HttpServletRequest) && (resp instanceof HttpServletResponse)))
            {
                getLogger().fatal(String.format("Not an http servlet!"));
                return;
            }
            
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;

            Subject user = SecurityUtils.getSubject();
            getLogger().trace(String.format("URI: %s - Current User: %s - Remembered?: %s - Authenticated?: %s", request.getRequestURI(), user.getPrincipal(), user.isRemembered() ? "Yes" : "No", user.isAuthenticated() ? "Yes" : "Not"));
            
            if (request.getRequestURI().startsWith("/public/"))
            {
                getLogger().trace(String.format("URI: %s - Allowed / Public", request.getRequestURI()));
                chain.doFilter(request, response);
                return;
            }
            
            // Don't allow users to know that we use jsp files
            if (null == request.getAttribute("javax.servlet.forward.request_uri") && request.getRequestURI().endsWith(".jsp"))
            {
                getLogger().warn(String.format("URI: %s - Current User: %s - Not Allowed / JSP", request.getRequestURI(), user.getPrincipal()));
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            if (user.isAuthenticated())
            {
                // Everything's cool. Let the request continue.
                chain.doFilter(request, response);
            }
            else
            {
                // Send them to the login page
                // Which will forward them to the original URL if successful
                redirectToLogin(request, response);
            }
        }
        catch (Exception e)
        {
            logger.error(e);
        }
    }
    
    static private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        StringBuffer request_url = request.getRequestURL();
        if (request.getQueryString() != null)
        {
            request_url.append("?").append(request.getQueryString());
        }
        
        StringBuilder login_url = new StringBuilder()
                                  .append(request.getContextPath())
                                  .append(LOGIN_URL + "?fwd=")
                                  .append(request_url.toString());
        
        response.sendRedirect(login_url.toString());
    }
    
    @Override
    public void destroy()
    {
        // Nothing to do
        logger.trace("destroy<>");
    }
}
