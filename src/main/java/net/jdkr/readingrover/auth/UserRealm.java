package net.jdkr.readingrover.auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.jimmutable.core.objects.Builder;
import org.jimmutable.core.objects.common.ObjectId;
import org.jimmutable.core.utils.Normalizer;

import net.jdkr.readingrover.user.User;


public class UserRealm extends AuthorizingRealm
{
    static private final Logger LOGGER = LogManager.getLogger(UserRealm.class);

    static private final String REALM_NAME = "rover-users";

    
    static private User createJeff()
    {
        Builder builder = new Builder(User.TYPE_NAME);
        
        builder.set(User.FIELD_ID, ObjectId.createRandomId());
        
        builder.set(User.FIELD_USERNAME, "jdezso");
        builder.set(User.FIELD_EMAIL_ADDRESS, "jdezso@gmail.com");
        
        // TODO Create util for salting/hashing passwords
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        ByteSource salt = rng.nextBytes();
        
        String hashed_password = new Sha512Hash("password", salt, 1024).toBase64();
        
        builder.set(User.FIELD_PASSWORD_HASH, hashed_password);
        builder.set(User.FIELD_PASSWORD_SALT, salt.toBase64());
        
        return builder.create(null);
    }
    
    static private User createKristen()
    {
        Builder builder = new Builder(User.TYPE_NAME);
        
        builder.set(User.FIELD_ID, ObjectId.createRandomId());
        
        builder.set(User.FIELD_USERNAME, "christy1865");
        builder.set(User.FIELD_EMAIL_ADDRESS, "k.r.richter@gmail.com");
        
        // TODO Create util for salting/hashing passwords
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        ByteSource salt = rng.nextBytes();
        
        String hashed_password = new Sha512Hash("password", salt, 1024).toBase64();
        
        builder.set(User.FIELD_PASSWORD_HASH, hashed_password);
        builder.set(User.FIELD_PASSWORD_SALT, salt.toBase64());
        
        return builder.create(null);
    }
    
    static private User createHaley()
    {
        Builder builder = new Builder(User.TYPE_NAME);
        
        builder.set(User.FIELD_ID, ObjectId.createRandomId());
        
        builder.set(User.FIELD_USERNAME, "haleybug");
        
        // TODO Create util for salting/hashing passwords
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        ByteSource salt = rng.nextBytes();
        
        String hashed_password = new Sha512Hash("password", salt, 1024).toBase64();
        
        builder.set(User.FIELD_PASSWORD_HASH, hashed_password);
        builder.set(User.FIELD_PASSWORD_SALT, salt.toBase64());
        
        return builder.create(null);
    }
    
    // TODO Use Storage instead of RAM!
    static private final Map<String, User> USERS;
    static
    {
        Map<String, User> users = new HashMap<>();
        
        User jeff = createJeff();
        users.put(jeff.getSimpleUsername(), jeff);
        
        User kristen = createKristen();
        users.put(kristen.getSimpleUsername(), kristen);
        
        User haley = createHaley();
        users.put(haley.getSimpleUsername(), haley);
        
        USERS = Collections.unmodifiableMap(users);
    }
    
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
        
        User user = USERS.get(username);
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
