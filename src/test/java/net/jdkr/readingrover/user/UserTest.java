package net.jdkr.readingrover.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jimmutable.core.objects.Builder;
import org.jimmutable.core.objects.StandardObject;
import org.jimmutable.core.objects.common.ObjectId;
import org.jimmutable.core.serialization.Format;
import org.junit.Test;

import net.jdkr.readingrover.StubTest;

public class UserTest extends StubTest
{
    static private final ObjectId ID = new ObjectId(123);
    
    static private final String USERNAME = "username";
    static private final String EMAIL_ADDRESS= "email@address.com";
    
    static private final String PASSWORD_HASH = "password#hash";
    static private final String PASSWORD_SALT = "password:salt";
    
    
    static private Builder createAndPopulateUserBuilder()
    {
        Builder builder = new Builder(User.TYPE_NAME);
        
        builder.set(User.FIELD_ID, ID);
        
        builder.set(User.FIELD_USERNAME, USERNAME);
        builder.set(User.FIELD_EMAIL_ADDRESS, EMAIL_ADDRESS);

        builder.set(User.FIELD_PASSWORD_HASH, PASSWORD_HASH);
        builder.set(User.FIELD_PASSWORD_SALT, PASSWORD_SALT);
        
        return builder;
    }
    
    
    @Test
    public void testBuilder()
    {
        Builder builder = new Builder(User.TYPE_NAME);
        
        try
        {
            builder.create(null);
            fail();
        }
        catch (Exception e)
        {
            // expect this, required fields not set
        }
        
        builder = new Builder(User.TYPE_NAME);
        
        builder.set(User.FIELD_ID, ID);
        builder.set(User.FIELD_USERNAME, USERNAME);
        builder.set(User.FIELD_PASSWORD_HASH, PASSWORD_HASH);
        builder.set(User.FIELD_PASSWORD_SALT, PASSWORD_SALT);
        
        User required_only = builder.create(null);
        
        assertNotNull(required_only);
        assertEquals(required_only.getSimpleKind(), User.KIND);
        assertFalse(required_only.hasEmailAddress());
        
        builder = new Builder(required_only);
        
        builder.set(User.FIELD_EMAIL_ADDRESS, EMAIL_ADDRESS);
        
        User getter_test = builder.create(null);
        assertNotNull(getter_test);
        
        assertEquals(getter_test.getSimpleObjectId(), ID);
        assertEquals(getter_test.getSimpleUsername(), USERNAME);
        assertEquals(getter_test.getOptionalEmailAddress(null), EMAIL_ADDRESS);
        assertEquals(getter_test.getSimplePasswordHash(), PASSWORD_HASH);
        assertEquals(getter_test.getSimplePasswordSalt(), PASSWORD_SALT);
        
        System.out.println(getter_test.toJavaCode(Format.JSON_PRETTY_PRINT, "obj"));
    }
    
    @Test
    public void testSerialization()
    {
        String obj_string = String.format("%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s"
                        , "{"
                        , "  \"type_hint\" : \"user\","
                        , "  \"id\" : \"0000-0000-0000-007b\","
                        , "  \"username\" : \"username\","
                        , "  \"email_address\" : \"email@address.com\","
                        , "  \"password_hash\" : \"password#hash\","
                        , "  \"password_salt\" : \"password:salt\""
                        , "}"
                   );

        User user = (User) StandardObject.deserialize(obj_string);
        
        assertEquals(user.getSimpleObjectId(), ID);
        assertEquals(user.getSimpleUsername(), USERNAME);
        assertEquals(user.getOptionalEmailAddress(null), EMAIL_ADDRESS);
        assertEquals(user.getSimplePasswordHash(), PASSWORD_HASH);
        assertEquals(user.getSimplePasswordSalt(), PASSWORD_SALT);
            
        assertEquals(obj_string, user.serialize(Format.JSON_PRETTY_PRINT));
    }
    
    @Test
    public void testComparisonAndEquals()
    {
        Builder builder = createAndPopulateUserBuilder();
        User user = builder.create(null);
        
        builder.set(User.FIELD_USERNAME, "aaa");
        User user_less = builder.create(null);
        
        builder.set(User.FIELD_USERNAME, "zzz");
        User user_more = builder.create(null);
        
        assertEquals(0, user.compareTo(user));
        assertTrue(1 <= user.compareTo(user_less));
        assertTrue(-1 >= user.compareTo(user_more));
        assertTrue(-1 >= user_less.compareTo(user_more));
    }
    
    @Test(expected = Exception.class)
    public void testRequiredFieldId()
    {
        Builder builder = createAndPopulateUserBuilder();
        builder.unset(User.FIELD_ID);
        builder.create(null);
    }
    
    @Test(expected = Exception.class)
    public void testRequiredFieldUsername()
    {
        Builder builder = createAndPopulateUserBuilder();
        builder.unset(User.FIELD_USERNAME);
        builder.create(null);
    }
    
    @Test(expected = Exception.class)
    public void testRequiredFieldPasswordHash()
    {
        Builder builder = createAndPopulateUserBuilder();
        builder.unset(User.FIELD_PASSWORD_HASH);
        builder.create(null);
    }
    
    @Test(expected = Exception.class)
    public void testRequiredFieldPasswordSalt()
    {
        Builder builder = createAndPopulateUserBuilder();
        builder.unset(User.FIELD_PASSWORD_SALT);
        builder.create(null);
    }
}
