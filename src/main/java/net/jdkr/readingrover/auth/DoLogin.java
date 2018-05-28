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
import org.jimmutable.core.objects.common.ObjectId;
import org.jimmutable.core.serialization.reader.HandReader;


@SuppressWarnings("serial")
public class DoLogin extends HttpServlet
{
    private static final Logger LOGGER = LogManager.getLogger(DoLogin.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        login(request, response);
    }
    
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        RequestPageData page_data = ServletUtil.getPageDataFromPost(request, new RequestPageData());

        if (page_data.isEmpty())
        {
            sendError(response, "Must provide both username and password");
            return;
        }

        HandReader reader = new HandReader(page_data.getOptionalDefaultJSONData(""));
        
        String username = reader.readString("data/username", null);
        String password = reader.readString("data/password", null);
        boolean remember_me = reader.readBoolean("data/remember_me", true);
        
        if (null == username || null == password)
        {
            sendError(response, "Must provide both username and password");
            return;
        }
        
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(remember_me);
        
        Subject current_user = SecurityUtils.getSubject();
        
        try
        {
            current_user.login(token);
            
            LoginResponseOK ok = new LoginResponseOK(ObjectId.createRandomId()); // TODO This isn't right... Lean into Shiro
            ServletUtil.writeSerializedResponse(response, ok, HttpServletResponse.SC_OK);
            return;
        }
        catch (AuthenticationException e)
        {
            // Failed authentication
            LOGGER.trace(e.getMessage());
        }
        
        sendError(response, "Incorrect username or password");
    }
    
    static private void sendError(HttpServletResponse response, String message)
    {
        LoginResponseFailure failure = new LoginResponseFailure(message);
        ServletUtil.writeSerializedResponse(response, failure, HttpServletResponse.SC_FORBIDDEN);
    }
}
