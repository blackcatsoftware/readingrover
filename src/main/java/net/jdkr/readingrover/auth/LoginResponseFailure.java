package net.jdkr.readingrover.auth;

import java.util.Objects;

import org.jimmutable.core.objects.StandardImmutableObject;
import org.jimmutable.core.serialization.FieldDefinition;
import org.jimmutable.core.serialization.TypeName;
import org.jimmutable.core.serialization.reader.ObjectParseTree;
import org.jimmutable.core.serialization.writer.ObjectWriter;
import org.jimmutable.core.utils.Comparison;
import org.jimmutable.core.utils.Validator;

public class LoginResponseFailure extends StandardImmutableObject<LoginResponseFailure>
{
    /*** Constants ***/
    
	static public final TypeName TYPE_NAME = new TypeName("login_response_failure");
	
	
    /*** Field Definitions ***/
    
	static public final FieldDefinition.String FIELD_MESSAGE = new FieldDefinition.String("message", null);

	
    /*** Fields ***/
    
	private String message;

	
	
	public LoginResponseFailure(String message)
	{
		this.message = message;
		complete();
	}

	public LoginResponseFailure(ObjectParseTree tree)
	{
		message = tree.getString(FIELD_MESSAGE);
	}

	
    /*** Getters ***/
    
	public String getSimpleMessage() { return message; }

	
    /*** StandardObject ***/

    @Override
    public void normalize()
    {
        // Nothing to do
    }

    @Override
    public void validate()
    {
        Validator.notNull(message);
    }

    @Override
    public void freeze()
    {
        // Nothing to do
    }

    @Override
    public boolean equals(Object obj)
    {
        if (! (obj instanceof LoginResponseFailure)) return false;

        LoginResponseFailure other = (LoginResponseFailure) obj;

        if (! Objects.equals(message, other.message))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(message);
    }
	
	@Override
	public int compareTo(LoginResponseFailure other)
	{
		int ret = Comparison.startCompare();

		ret = Comparison.continueCompare(ret, message, other.message);

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
		writer.writeString(FIELD_MESSAGE, message);
	}
}
