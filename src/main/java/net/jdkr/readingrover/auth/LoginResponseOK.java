package net.jdkr.readingrover.auth;

import org.jimmutable.core.objects.StandardImmutableObject;
import org.jimmutable.core.serialization.TypeName;
import org.jimmutable.core.serialization.reader.ObjectParseTree;
import org.jimmutable.core.serialization.writer.ObjectWriter;
import org.jimmutable.core.utils.Comparison;


public class LoginResponseOK extends StandardImmutableObject<LoginResponseOK>
{
    /*** Constants ***/
    
	static public final TypeName TYPE_NAME = new TypeName("login_response_ok");

	
    /*** Field Definitions ***/
    
	
    /*** Fields ***/
	
    
	public LoginResponseOK()
	{
		complete();
	}

	public LoginResponseOK(ObjectParseTree tree)
	{
        // Nothing to do
	}
	
	
    /*** Getters ***/
    
	
    /*** StandardObject ***/

    @Override
    public void normalize()
    {
        // Nothing to do
    }

    @Override
    public void validate()
    {
        // Nothing to do
    }

    @Override
    public void freeze()
    {
        // Nothing to do
    }

    @Override
    public boolean equals(Object obj)
    {
        if (! (obj instanceof LoginResponseOK)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return System.identityHashCode(this);
    }

	@Override
	public int compareTo(LoginResponseOK other)
	{
		int ret = Comparison.startCompare();

		return ret;
	}

	@Override
	public TypeName getTypeName()
	{
		return TYPE_NAME;
	}

	@Override
	public void write(ObjectWriter writer)
	{
        // Nothing to do
	}
}
