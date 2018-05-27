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
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.jimmutable.cloud.CloudExecutionEnvironment;
import org.jimmutable.cloud.servlet_utils.search.OneSearchResultWithTyping;
import org.jimmutable.cloud.servlet_utils.search.StandardSearchRequest;
import org.jimmutable.core.objects.common.ObjectId;
import org.jimmutable.core.utils.Normalizer;

import net.jdkr.readingrover.user.User;
import net.jdkr.readingrover.util.StorageUtil;


public class UserRealm extends AuthorizingRealm
{
    static private final Logger LOGGER = LogManager.getLogger(UserRealm.class);

    static private final String REALM_NAME = "rover-users";

    
    static private CredentialsMatcher createCredentialsMatcher()
    {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName(Sha512Hash.ALGORITHM_NAME);
        matcher.setHashIterations(1024); // TODO Match to User creation / storage
        matcher.setStoredCredentialsHexEncoded(false);
        return matcher;
    }
    
    
    public UserRealm()
    {
        super(createCredentialsMatcher());
    }
    
    public UserRealm(CacheManager cache_manager)
    {
        super(cache_manager, createCredentialsMatcher());
    }
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
    {
        UsernamePasswordToken user_token = (UsernamePasswordToken) token;
        
        String username = Normalizer.lowerCase(user_token.getUsername());
        
        String query = String.format("%s:\"%s\"", User.SEARCH_FIELD_USERNAME.getSimpleFieldName().getSimpleName(), username);
        StandardSearchRequest search_request = new StandardSearchRequest(query);
        
        List<OneSearchResultWithTyping> results = CloudExecutionEnvironment.getSimpleCurrent().getSimpleSearch().search(User.INDEX_DEFINITION, search_request, Collections.emptyList());
        if (results.isEmpty()) return null;
        
        ObjectId id = new ObjectId(results.get(0).readAsAtom(User.SEARCH_FIELD_ID.getSimpleFieldName(), null));
        
        User user = StorageUtil.getComplexCurrentVersion(User.KIND, id, null);
        if (null == user) return null;
        
        ByteSource salt = new SimpleByteSource(Base64.decode(user.getSimplePasswordSalt()));
        return new SimpleAuthenticationInfo(token.getPrincipal(), user.getSimplePasswordHash(), salt, REALM_NAME);
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
