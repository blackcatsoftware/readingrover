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
import org.jimmutable.cloud.servlets.util.RequestPageData;
import org.jimmutable.cloud.servlets.util.ServletUtil;
import org.jimmutable.core.serialization.reader.HandReader;


@SuppressWarnings("serial")
public class DoLogin extends HttpServlet
{
    private static final Logger LOGGER = LogManager.getLogger(DoLogin.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        RequestPageData page_data = ServletUtil.getPageDataFromPost(request, new RequestPageData());

        if (page_data.isEmpty())
        {
            sendError(response, "Must provide both username and password");
            return;
        }

        HandReader reader = new HandReader(page_data.getOptionalDefaultJSONData(""));
        
        String username = reader.readString("username", null);
        String password = reader.readString("password", null);
        boolean remember_me = reader.readBoolean("remember_me", true);
        
        if (null == username || null == password)
        {
            sendError(response, "Must provide both username and password");
            return;
        }
        
        if (login(username, password, remember_me))
        {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            sendError(response, "Incorrect username or password");
        }
    }
    
    static public boolean login(String username, String password, boolean remember_me)
    {
        if (null == username) return false;
        if (null == password) return false;
        
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(remember_me);
        
        try
        {
            Subject current_user = SecurityUtils.getSubject();
            current_user.login(token);
            
            return true;
        }
        catch (AuthenticationException e)
        {
            LOGGER.trace(e.getMessage());
            
            return false;
        }
    }
    
    static private void sendError(HttpServletResponse response, String message)
    {
        LoginResponseFailure failure = new LoginResponseFailure(message);
        ServletUtil.writeSerializedResponse(response, failure, HttpServletResponse.SC_FORBIDDEN);
    }
}
