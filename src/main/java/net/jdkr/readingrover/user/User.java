package net.jdkr.readingrover.user;

import java.util.Objects;

import org.jimmutable.cloud.CloudExecutionEnvironment;
import org.jimmutable.cloud.elasticsearch.IndexDefinition;
import org.jimmutable.cloud.elasticsearch.IndexId;
import org.jimmutable.cloud.elasticsearch.IndexVersion;
import org.jimmutable.cloud.elasticsearch.Indexable;
import org.jimmutable.cloud.elasticsearch.SearchDocumentId;
import org.jimmutable.cloud.elasticsearch.SearchDocumentWriter;
import org.jimmutable.cloud.elasticsearch.SearchIndexDefinition;
import org.jimmutable.cloud.elasticsearch.SearchIndexFieldDefinition;
import org.jimmutable.cloud.elasticsearch.SearchIndexFieldType;
import org.jimmutable.cloud.storage.Storable;
import org.jimmutable.core.objects.Builder;
import org.jimmutable.core.objects.StandardImmutableObject;
import org.jimmutable.core.objects.common.Kind;
import org.jimmutable.core.objects.common.ObjectId;
import org.jimmutable.core.serialization.FieldDefinition;
import org.jimmutable.core.serialization.TypeName;
import org.jimmutable.core.serialization.reader.ObjectParseTree;
import org.jimmutable.core.serialization.writer.ObjectWriter;
import org.jimmutable.core.utils.Comparison;
import org.jimmutable.core.utils.Normalizer;
import org.jimmutable.core.utils.Optional;
import org.jimmutable.core.utils.Validator;


public class User extends StandardImmutableObject<User> implements Storable, Indexable
{
    /*** Constants ***/
    
    static public final TypeName TYPE_NAME = new TypeName("user");
    static public final Kind KIND = new Kind("user");
    
    
    /*** Field Definitions ***/
    
    static public final FieldDefinition.Stringable<ObjectId> FIELD_ID = new FieldDefinition.Stringable<ObjectId>("id", null, ObjectId.CONVERTER);
    
    static public final FieldDefinition.String FIELD_USERNAME = new FieldDefinition.String("username",null);
    static public final FieldDefinition.String FIELD_EMAIL_ADDRESS = new FieldDefinition.String("email_address",null);
    
    static public final FieldDefinition.String FIELD_PASSWORD_HASH = new FieldDefinition.String("password_hash",null);
    static public final FieldDefinition.String FIELD_PASSWORD_SALT = new FieldDefinition.String("password_salt",null);
    
    
    /*** Index Definitions ***/
    
    static public final IndexDefinition INDEX_DEFINITION = new IndexDefinition(CloudExecutionEnvironment.getSimpleCurrent().getSimpleApplicationId(), new IndexId("user"), new IndexVersion("v1"));

    static public final SearchIndexFieldDefinition SEARCH_FIELD_ID = new SearchIndexFieldDefinition(FIELD_ID.getSimpleFieldName(), SearchIndexFieldType.ATOM);
    
    static public final SearchIndexFieldDefinition SEARCH_FIELD_USERNAME = new SearchIndexFieldDefinition(FIELD_USERNAME.getSimpleFieldName(), SearchIndexFieldType.TEXT);
    static public final SearchIndexFieldDefinition SEARCH_FIELD_EMAIL_ADDRESS = new SearchIndexFieldDefinition(FIELD_EMAIL_ADDRESS.getSimpleFieldName(), SearchIndexFieldType.TEXT);
    
    // Password fields not searchable... duh
    
    
    static public final SearchIndexDefinition INDEX_MAPPING;
    
    static
    {
        Builder builder = new Builder(SearchIndexDefinition.TYPE_NAME);
        
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_ID);
        
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_USERNAME);
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_EMAIL_ADDRESS);
        
        builder.set(SearchIndexDefinition.FIELD_INDEX_DEFINITION, INDEX_DEFINITION);
        
        INDEX_MAPPING = builder.create(null);
    }
    
    
    
    /*** Fields ***/
    
    private ObjectId id; // Required
    
    private String username; // Required, lowercase
    private String email_address; // Optional
    
    private String password_hash; // Required
    private String password_salt; // Required
    
    
    public User(ObjectParseTree tree)
    {
        id = tree.getStringable(FIELD_ID);
        
        username = tree.getString(FIELD_USERNAME);
        email_address = tree.getString(FIELD_EMAIL_ADDRESS);

        password_hash = tree.getString(FIELD_PASSWORD_HASH);
        password_salt = tree.getString(FIELD_PASSWORD_SALT);
    }

    
    /*** Getters ***/
    
    @Override
    public ObjectId getSimpleObjectId() { return id; }
    
    public String getSimpleUsername() { return username; }
    
    public String getOptionalEmailAddress(String default_value)
    {
        return Optional.getOptional(email_address, FIELD_EMAIL_ADDRESS.getSimpleUnsetValue(), default_value);
    }
    
    public boolean hasEmailAddress()
    {
        return null != getOptionalEmailAddress(null);
    }
    
    public String getSimplePasswordHash() { return password_hash; }
    public String getSimplePasswordSalt() { return password_salt; }
    
    
    /*** StandardObject ***/

    @Override
    public void normalize()
    {
        username = Normalizer.lowerCase(username);
    }

    @Override
    public void validate()
    {
        Validator.notNull(id, "Object Id");
        Validator.notNull(username, "Username");
        
        Validator.notNull(password_hash, "Password Hash");
        Validator.notNull(password_salt, "Password Salt");
    }

    @Override
    public void freeze()
    {
        // Nothing to do
    }

    @Override
    public boolean equals(Object obj)
    {
        if (! (obj instanceof User)) return false;

        User other = (User) obj;

        if (! Objects.equals(id, other.id)) return false;
        
        if (! Objects.equals(username, other.username)) return false;
        if (! Objects.equals(email_address, other.email_address)) return false;
        
        if (! Objects.equals(password_hash, other.password_hash)) return false;
        if (! Objects.equals(password_salt, other.password_salt)) return false;
        
        return true;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, username, email_address, password_hash, password_salt);
    }

    @Override
    public int compareTo(User other)
    {
        int ret = Comparison.startCompare();
        
        ret = Comparison.continueCompare(ret, username, other.username);
        ret = Comparison.continueCompare(ret, email_address, other.email_address);
        
        ret = Comparison.continueCompare(ret, id, other.id);
        
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
        writer.writeStringable(FIELD_ID, id);
        
        writer.writeString(FIELD_USERNAME, username);
        writer.writeString(FIELD_EMAIL_ADDRESS, email_address);
        
        writer.writeString(FIELD_PASSWORD_HASH, password_hash);
        writer.writeString(FIELD_PASSWORD_SALT, password_salt);
    }
    
    
    /*** Storable ***/

    @Override
    public Kind getSimpleKind()
    {
        return KIND;
    }


    /*** Indexable ***/

    @Override
    public IndexDefinition getSimpleSearchIndexDefinition()
    {
        return INDEX_DEFINITION;
    }

    @Override
    public SearchDocumentId getSimpleSearchDocumentId()
    {
        return new SearchDocumentId(id.getSimpleValue());
    }

    @Override
    public void writeSearchDocument(SearchDocumentWriter writer)
    {
        writer.writeAtom(SEARCH_FIELD_ID, id.getSimpleValue());

        writer.writeText(SEARCH_FIELD_USERNAME, username);
        if (hasEmailAddress())
        {
            writer.writeText(SEARCH_FIELD_EMAIL_ADDRESS, email_address);
        }
    }
}
