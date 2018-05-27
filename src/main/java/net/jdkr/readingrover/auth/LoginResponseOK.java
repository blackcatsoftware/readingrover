package net.jdkr.readingrover.auth;

import java.util.Objects;

import org.jimmutable.core.objects.StandardImmutableObject;
import org.jimmutable.core.objects.common.ObjectId;
import org.jimmutable.core.serialization.FieldDefinition;
import org.jimmutable.core.serialization.TypeName;
import org.jimmutable.core.serialization.reader.ObjectParseTree;
import org.jimmutable.core.serialization.writer.ObjectWriter;
import org.jimmutable.core.utils.Comparison;
import org.jimmutable.core.utils.Validator;


public class LoginResponseOK extends StandardImmutableObject<LoginResponseOK>
{
    /*** Constants ***/
    
	static public final TypeName TYPE_NAME = new TypeName("login_response_ok");

	
    /*** Field Definitions ***/
    
	static public final FieldDefinition.Stringable<ObjectId> FIELD_AUTH_TOKEN_ID = new FieldDefinition.Stringable<ObjectId>("auth_token_id", null, ObjectId.CONVERTER);
	
	
    /*** Fields ***/
	
    private ObjectId auth_token_id;
    
    
	public LoginResponseOK(ObjectId auth_token_id)
	{
		this.auth_token_id = auth_token_id;
		complete();
	}

	public LoginResponseOK(ObjectParseTree tree)
	{
		auth_token_id = tree.getStringable(FIELD_AUTH_TOKEN_ID);
	}
	
	
    /*** Getters ***/
    
	public ObjectId getSimpleAuthTokenId() { return auth_token_id; }
	
	
    /*** StandardObject ***/

    @Override
    public void normalize()
    {
        // Nothing to do
    }

    @Override
    public void validate()
    {
        Validator.notNull(auth_token_id);
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

        LoginResponseOK other = (LoginResponseOK) obj;

        if (! Objects.equals(auth_token_id, other.auth_token_id))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(auth_token_id);
    }

	@Override
	public int compareTo(LoginResponseOK other)
	{
		int ret = Comparison.startCompare();

		ret = Comparison.continueCompare(ret, auth_token_id, other.auth_token_id);

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
		writer.writeStringable(FIELD_AUTH_TOKEN_ID, auth_token_id);
	}
}
