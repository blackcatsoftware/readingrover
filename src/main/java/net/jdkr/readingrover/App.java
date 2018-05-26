package net.jdkr.readingrover;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jimmutable.cloud.ApplicationId;
import org.jimmutable.cloud.CloudExecutionEnvironment;
import org.jimmutable.cloud.DefaultJetty2Log4j2Bridge;
import org.jimmutable.cloud.EnvironmentType;
import org.jimmutable.cloud.utils.AppAdminUtil;

import net.jdkr.readingrover.util.Log4jOneUtil;
import net.jdkr.readingrover.util.ViewController;


public class App
{
    static private final Logger logger = LogManager.getLogger(App.class);
    static private final int MAX_FORM_SIZE = 1024 * 1024 * 1024; // ~1074 MB
    
    static public final Map<String, ServletSpecForwardUri> VIEW_MAPPINGS;
    static public final Set<String> PAGES_AVAILABLE_TO_ALL_AUTHENTICATED_USERS;
    
    static public final Map<String, Class<? extends HttpServlet>> ACTION_MAPPINGS;
    static public final ApplicationId APP_ID = new ApplicationId("reading-rover");
    
    static
    {
        VIEW_MAPPINGS = new HashMap<>();
        VIEW_MAPPINGS.put("/", new ServletSpecForwardUri("", "/index/index.jsp"));
        VIEW_MAPPINGS.put("/public/login.html", new ServletSpecForwardUri("/public/login.html", "/public/login.jsp"));
        VIEW_MAPPINGS.put("/index/index.html", new ServletSpecForwardUri("/index/index.html", "/index/index.jsp"));
    }
    
    static
    {
        // Add any URLs here that are only available to users once authenticated but are
        // not RWS staff
        PAGES_AVAILABLE_TO_ALL_AUTHENTICATED_USERS = new HashSet<>();
        PAGES_AVAILABLE_TO_ALL_AUTHENTICATED_USERS.add("/index");
        PAGES_AVAILABLE_TO_ALL_AUTHENTICATED_USERS.add("/dealer-program/manage-advertising");
        PAGES_AVAILABLE_TO_ALL_AUTHENTICATED_USERS.add("/dealer-program/select-business-to-manage");
        PAGES_AVAILABLE_TO_ALL_AUTHENTICATED_USERS.add("/dealer-program/no-business-to-manage");
        
        PAGES_AVAILABLE_TO_ALL_AUTHENTICATED_USERS.add("/avatars/do-get");
        PAGES_AVAILABLE_TO_ALL_AUTHENTICATED_USERS.add("/favicon.ico");
        PAGES_AVAILABLE_TO_ALL_AUTHENTICATED_USERS.add("/attachments/do-get");
        
    }
    
    static
    {
        ACTION_MAPPINGS = new HashMap<>();
        
//        ACTION_MAPPINGS.put("/public/do-login", DoLogin.class);
//        ACTION_MAPPINGS.put("/index/index/do-redirect", DoIndexRedirect.class);
    }
    
    static public class ServletSpecForwardUri
    {
        private String pathSpec;
        private String forwardUri;
        
        public ServletSpecForwardUri(String pathSpec, String forwardUri)
        {
            this.pathSpec = pathSpec;
            this.forwardUri = forwardUri;
        }
        
        public String getPathSpec()
        {
            return pathSpec;
        }
        
        public String getForwardUri()
        {
            return forwardUri;
        }
        
        @Override
        public int hashCode()
        {
            return Objects.hash(pathSpec, forwardUri);
        }
        
        @Override
        public String toString()
        {
            return "ServletSpecForwardUri [pathSpec=" + pathSpec + ", forwardUri=" + forwardUri + "]";
        }
    }
    
    static public void main(String[] args) throws Exception
    {
        org.eclipse.jetty.util.log.Log.setLog(new DefaultJetty2Log4j2Bridge("jetty"));
        
        EnvironmentType type = CloudExecutionEnvironment.getEnvironmentTypeFromSystemProperty(null);
        if (type == null)
        {
            logger.info("No environment type was passed in with flag: -DJIMMUTABLE_ENV_TYPE, defaulting to DEV environment. See environment type options in the class EnvironmentType");
            CloudExecutionEnvironment.startup(APP_ID, EnvironmentType.DEV);
        }
        else
        {
            logger.info("Starting with environment type: " + type);
            CloudExecutionEnvironment.startup(APP_ID, type);
        }
        
        // note:this is required beforehand
        TypeNameRegister.registerAllTypes();
        TypeNameRegister.registerAllIndexableKinds();
        
        boolean startup_allowed = AppAdminUtil.indicesProperlyConfigured();
        if (! startup_allowed)
        {
            logger.fatal("Exiting now...");
            System.exit(1);
        }
        
        Log4jOneUtil.setupListeners();
        
        // Creating the server on port 8080
        Server server = new Server(8080);
        
        // Suppress server version in header
        for (Connector connector : server.getConnectors())
        {
            for (ConnectionFactory factory : connector.getConnectionFactories())
            {
                if (factory instanceof HttpConnectionFactory)
                {
                    ((HttpConnectionFactory) factory).getHttpConfiguration().setSendServerVersion(false);
                }
            }
        }
        
        // Creating the WebAppContext for the created content
        WebAppContext context = new WebAppContext();
        
        String html_src = System.getProperty("jimmutable.html.src");
        if ("webapp".equalsIgnoreCase(html_src))
        {
            context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
            
            URL webapp_location = App.class.getResource("/src/main/webapp/");
            if (webapp_location == null)
            {
                throw new IllegalStateException("Unable to determine webapp URL location");
            }
            
            URI webapp_uri = null;
            try
            {
                webapp_uri = URI.create(webapp_location.toURI().toASCIIString());
            }
            catch (URISyntaxException e)
            {
                logger.error(e);
            }
            logger.info(String.format("Webapp URI: %s", webapp_uri));
            
            context.setBaseResource(Resource.newResource(webapp_uri));
        }
        else
        {
            context.setResourceBase("src/main/webapp");
        }
        
        context.setContextPath("/");
        
        // Add servlets to context
        logger.trace(String.format("ACTION_MAPPINGS: %s", ACTION_MAPPINGS));
        ACTION_MAPPINGS.forEach((k, v) ->
        {
            context.addServlet(v, k);
        });
        
        logger.trace(String.format("VIEW_MAPPINGS: %s", VIEW_MAPPINGS));
        VIEW_MAPPINGS.forEach((k, v) ->
        {
            context.addServlet(ViewController.class, v.getPathSpec());
        });
        
        // TODO Create /common/404.html
//        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
//        errorHandler.addErrorPage(404, "/common/404.html");
//        context.setErrorHandler(errorHandler);
        
        // Add filters to context
        // TODO Implement authentication
//        context.addFilter(AuthenticationFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        
        org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList.setServerDefault(server);
        
        // For some reason this is needed for JSPs to work
        classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration", "org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");
        classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");
        
        // Including the JSTL jars for the webapp.
        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/[^/]*jstl.*\\.jar$");
        
        // Allows live-updating of CSS/HTML/JS files on Windows
        context.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        
        context.setMaxFormContentSize(MAX_FORM_SIZE);
        
        // Setting the handler and starting the Server
        server.setHandler(context);
        
        // Set max form size - ie. all Apps on server must work within it.
        server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", MAX_FORM_SIZE);
        server.start();
        server.join();
    }
}
