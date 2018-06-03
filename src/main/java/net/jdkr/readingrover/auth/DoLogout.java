package net.jdkr.readingrover.auth;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


@SuppressWarnings("serial")
public class DoLogout extends HttpServlet
{
    private static final Logger LOGGER = LogManager.getLogger(DoLogin.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Subject current_user = SecurityUtils.getSubject();
        current_user.logout();
        
        LOGGER.trace(String.format("Logged out %s", current_user.getPrincipal()));
        
        // Send back to a public page
        response.sendRedirect("/");
    }
}
