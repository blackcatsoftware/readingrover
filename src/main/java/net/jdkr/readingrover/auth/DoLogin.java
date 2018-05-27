package net.jdkr.readingrover.auth;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.jimmutable.cloud.servlets.util.ServletUtil;
import org.jimmutable.core.objects.common.ObjectId;


@SuppressWarnings("serial")
public class DoLogin extends HttpServlet
{
    private static final Logger LOGGER = LogManager.getLogger(DoLogin.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        login(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        login(request, response);
    }
    
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        LOGGER.info("Starting login!");
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (null == username || null == password)
        {
            LoginResponseFailure failure = new LoginResponseFailure("Must provide both username and password");
            ServletUtil.writeSerializedResponse(response, failure, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//        token.setRememberMe(true); // TOOD Figure out "remember me" (mostly update front end form)
        
        Subject current_user = SecurityUtils.getSubject();
        
        try
        {
            current_user.login(token);
            
            // TODO Does shiro remember the user? Remember the session?
            // TODO Do we need to forward the user?
            LoginResponseOK ok = new LoginResponseOK(ObjectId.createRandomId()); // TODO This isn't right... Lean into Shiro

            ServletUtil.writeSerializedResponse(response, ok, HttpServletResponse.SC_OK);
            return;
        }
        catch (AuthenticationException e)
        {
            // Failed authentication
            LOGGER.trace(e.getMessage());
        }
        
        LoginResponseFailure failure = new LoginResponseFailure("Invalid login");
        ServletUtil.writeSerializedResponse(response, failure, HttpServletResponse.SC_FORBIDDEN);
        return;
    }
}
