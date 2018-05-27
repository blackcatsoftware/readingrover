package net.jdkr.readingrover;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jimmutable.cloud.CloudExecutionEnvironment;
import org.jimmutable.core.serialization.Format;

import net.jdkr.readingrover.user.User;


public class DevStartupData
{
    static private final Logger LOGGER = LogManager.getLogger(DevStartupData.class);

    static final private String JEFF = String.format("%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s"
                    , "{"
                    , "  \"type_hint\" : \"user\","
                    , "  \"id\" : \"61ce-60c9-d20e-968a\","
                    , "  \"username\" : \"jdezso\","
                    , "  \"email_address\" : \"jdezso@gmail.com\","
                    , "  \"password_hash\" : \"L1ABf5HnwZ/C/fmvJM/11kqY0Tk/FoRGaazSrvfs3Ha/1n3mnoKt6Bx+OgELbTEhzshrMaSPWiHwXuuHmjYi/A==\","
                    , "  \"password_salt\" : \"mzm0cZufUKInaMORdy0/hw==\""
                    , "}"
               );    
    
    static final private String KRISTEN = String.format("%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s"
                    , "{"
                    , "  \"type_hint\" : \"user\","
                    , "  \"id\" : \"5a5e-7bea-71b8-22b7\","
                    , "  \"username\" : \"christy1865\","
                    , "  \"email_address\" : \"k.r.richter@gmail.com\","
                    , "  \"password_hash\" : \"9T1moW0/KXUMDSQP3dA0dJL/r3wFrrzYzLOJoYHM1byZXuZNvzGIBBPJ2FXTbVfBMjcvjFMH64kXBrlA4QT0YQ==\","
                    , "  \"password_salt\" : \"+XKX72RKMvGowBgPb1YmYg==\""
                    , "}"
               );    
    
    static private void upsertUser(User user)
    {
        CloudExecutionEnvironment.getSimpleCurrent().getSimpleStorage().upsert(user, Format.JSON);
        CloudExecutionEnvironment.getSimpleCurrent().getSimpleSearch().upsertDocument(user);
        
        LOGGER.info(String.format("Added User %s (%s) to Storage and Search", user.getSimpleUsername(), user.getSimpleObjectId()));
    }
    
    static public void upsertAdminUsers()
    {
        upsertUser((User) User.deserialize(JEFF));
        upsertUser((User) User.deserialize(KRISTEN));
    }
    
    static public void upsertAllStartupData()
    {
        upsertAdminUsers();
    }
    
    
    private DevStartupData() {}
}
