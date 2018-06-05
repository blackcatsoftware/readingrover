package net.jdkr.readingrover;

import org.jimmutable.cloud.JimmutableCloudTypeNameRegister;
import org.jimmutable.cloud.elasticsearch.SearchSync;
import org.jimmutable.core.serialization.JimmutableTypeNameRegister;
import org.jimmutable.core.serialization.reader.ObjectParseTree;

import net.jdkr.readingrover.auth.LoginResponseFailure;
import net.jdkr.readingrover.user.Avatar;
import net.jdkr.readingrover.user.User;



public class TypeNameRegister
{
	public static void registerAllTypes()
	{
	    JimmutableTypeNameRegister.registerAllTypes();
	    JimmutableCloudTypeNameRegister.registerAllTypes();
	    
	    ObjectParseTree.registerTypeName(User.class);
        ObjectParseTree.registerTypeName(Avatar.class);
	    
        ObjectParseTree.registerTypeName(LoginResponseFailure.class);
	}
	
    static public void registerAllIndexableKinds()
    {
        SearchSync.registerIndexableKind(User.class);
        SearchSync.registerIndexableKind(Avatar.class);
    }	
}
