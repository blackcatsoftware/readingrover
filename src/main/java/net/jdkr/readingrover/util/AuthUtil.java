package net.jdkr.readingrover.util;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

public class AuthUtil
{
    static private final int HASH_ITERATIONS = 1024;
    static private final String HASH_ALGORITHM = Sha512Hash.ALGORITHM_NAME;
    
    static private final RandomNumberGenerator RNG = new SecureRandomNumberGenerator();
    
    
    static public CredentialsMatcher createCredentialsMatcher()
    {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName(HASH_ALGORITHM);
        matcher.setHashIterations(HASH_ITERATIONS);
        matcher.setStoredCredentialsHexEncoded(false); // We use Base 64
        return matcher;
    }
    
    
    /*** Salt Utils ***/
    
    /**
     * Create a new random salt
     */
    static public ByteSource newSalt()
    {
        return RNG.nextBytes();
    }
    
    static public String encodeSalt(ByteSource salt)
    {
        if (null == salt) return null;
        return salt.toBase64();
    }
    
    static public ByteSource decodeSalt(String salt)
    {
        if (null == salt) return null;
        return new SimpleByteSource(Base64.decode(salt));
    }
    
    
    /*** Password Utils ***/
    
    /**
     * Convert raw password to encoded String
     */
    static public String encodePassword(byte[] password)
    {
        if (null == password) return null;
        return Base64.encodeToString(password);
    }
    
    static public byte[] decodePassword(String password)
    {
        if (null == password) return null;
        return Base64.decode(password);
    }
    
    static public byte[] hashPassword(String password, ByteSource salt)
    {
        return new Sha512Hash(password, salt, HASH_ITERATIONS).getBytes();
    }
    
    static public byte[] hashPassword(String password, String salt)
    {
        return hashPassword(password, decodeSalt(salt));
    }
    
    static public String hashPasswordAndEncode(String password, ByteSource salt)
    {
        return encodePassword(hashPassword(password, salt));
    }
    
    static public String hashPasswordAndEncode(String password, String salt)
    {
        return encodePassword(hashPassword(password, salt));
    }
    
    
    private AuthUtil() {}
}
