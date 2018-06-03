package net.jdkr.readingrover.auth;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jimmutable.cloud.CloudExecutionEnvironment;
import org.jimmutable.cloud.servlet_utils.search.OneSearchResultWithTyping;
import org.jimmutable.cloud.servlet_utils.search.StandardSearchRequest;
import org.jimmutable.core.objects.common.ObjectId;
import org.jimmutable.core.utils.Normalizer;

import net.jdkr.readingrover.user.User;
import net.jdkr.readingrover.util.AuthUtil;
import net.jdkr.readingrover.util.StorageUtil;


public class UserRealm extends AuthorizingRealm
{
    static private final Logger LOGGER = LogManager.getLogger(UserRealm.class);

    static private final String REALM_NAME = "rover-users";

    
    public UserRealm()
    {
        super(AuthUtil.createCredentialsMatcher());
    }
    
    public UserRealm(CacheManager cache_manager)
    {
        super(cache_manager, AuthUtil.createCredentialsMatcher());
    }
    
    @Override
    public boolean supports(AuthenticationToken token)
    {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
    {
        if (null == token) return null;
        
        UsernamePasswordToken user_token = (UsernamePasswordToken) token;
        
        String username = Normalizer.lowerCase(user_token.getUsername());
        LOGGER.trace(String.format("Authenticating %s",  username));
        
        String query = String.format("%s:\"%s\"", User.SEARCH_FIELD_USERNAME.getSimpleFieldName().getSimpleName(), username);
        StandardSearchRequest search_request = new StandardSearchRequest(query);
        
        List<OneSearchResultWithTyping> results = CloudExecutionEnvironment.getSimpleCurrent().getSimpleSearch().search(User.INDEX_DEFINITION, search_request, Collections.emptyList());
        if (results.isEmpty())
        {
            LOGGER.trace(String.format("Authenticating %s - Username not found in Search", username));
            return null;
        }
        
        ObjectId id = new ObjectId(results.get(0).readAsAtom(User.SEARCH_FIELD_ID.getSimpleFieldName(), null));
        
        User user = StorageUtil.getComplexCurrentVersion(User.KIND, id, null);
        if (null == user)
        {
            LOGGER.trace(String.format("Authenticating %s - ID: %s - User not found in Storage", username, id));
            return null;
        }
        
        return new SimpleAuthenticationInfo(token.getPrincipal(), user.getSimplePasswordHash(), AuthUtil.decodeSalt(user.getSimplePasswordSalt()), REALM_NAME); 
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
