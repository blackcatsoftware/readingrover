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
import org.jimmutable.core.exceptions.ValidationException;
import org.jimmutable.core.objects.Builder;
import org.jimmutable.core.objects.StandardImmutableObject;
import org.jimmutable.core.objects.common.Day;
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
    
    static public final FieldDefinition.Stringable<ObjectId> FIELD_ID = new FieldDefinition.Stringable<>("id", null, ObjectId.CONVERTER);
    
    static public final FieldDefinition.String FIELD_USERNAME = new FieldDefinition.String("username",null);
    static public final FieldDefinition.String FIELD_EMAIL_ADDRESS = new FieldDefinition.String("email_address",null);
    
    static public final FieldDefinition.String FIELD_FIRST_NAME = new FieldDefinition.String("first_name",null);
    static public final FieldDefinition.String FIELD_LAST_INITIAL = new FieldDefinition.String("last_initial",null);
    
    static public final FieldDefinition.Stringable<Day> FIELD_BIRTHDAY = new FieldDefinition.Stringable<>("birthday", null, Day.CONVERTER);
    
    static public final FieldDefinition.Stringable<ObjectId> FIELD_AVATAR_ID = new FieldDefinition.Stringable<>("avatar_id", null, ObjectId.CONVERTER);
    
    static public final FieldDefinition.String FIELD_PASSWORD_HASH = new FieldDefinition.String("password_hash",null);
    static public final FieldDefinition.String FIELD_PASSWORD_SALT = new FieldDefinition.String("password_salt",null);
    
    
    /*** Index Definitions ***/
    
    static public final IndexDefinition INDEX_DEFINITION = new IndexDefinition(CloudExecutionEnvironment.getSimpleCurrent().getSimpleApplicationId(), new IndexId("user"), new IndexVersion("v1"));

    static public final SearchIndexFieldDefinition SEARCH_FIELD_ID = new SearchIndexFieldDefinition(FIELD_ID.getSimpleFieldName(), SearchIndexFieldType.ATOM);
    
    static public final SearchIndexFieldDefinition SEARCH_FIELD_USERNAME = new SearchIndexFieldDefinition(FIELD_USERNAME.getSimpleFieldName(), SearchIndexFieldType.TEXT);
    static public final SearchIndexFieldDefinition SEARCH_FIELD_EMAIL_ADDRESS = new SearchIndexFieldDefinition(FIELD_EMAIL_ADDRESS.getSimpleFieldName(), SearchIndexFieldType.TEXT);
    
    static public final SearchIndexFieldDefinition SEARCH_FIELD_FIRST_NAME = new SearchIndexFieldDefinition(FIELD_FIRST_NAME.getSimpleFieldName(), SearchIndexFieldType.TEXT);
    static public final SearchIndexFieldDefinition SEARCH_FIELD_LAST_INITIAL = new SearchIndexFieldDefinition(FIELD_LAST_INITIAL.getSimpleFieldName(), SearchIndexFieldType.TEXT);
    
    static public final SearchIndexFieldDefinition SEARCH_FIELD_BIRTHDAY = new SearchIndexFieldDefinition(FIELD_BIRTHDAY.getSimpleFieldName(), SearchIndexFieldType.DAY);
    
    static public final SearchIndexFieldDefinition SEARCH_FIELD_AVATAR_ID = new SearchIndexFieldDefinition(FIELD_AVATAR_ID.getSimpleFieldName(), SearchIndexFieldType.ATOM);
    
    // Password fields not searchable... duh
    
    
    static public final SearchIndexDefinition INDEX_MAPPING;
    
    static
    {
        Builder builder = new Builder(SearchIndexDefinition.TYPE_NAME);
        
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_ID);
        
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_USERNAME);
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_EMAIL_ADDRESS);
        
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_FIRST_NAME);
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_LAST_INITIAL);
        
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_BIRTHDAY);
        
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_AVATAR_ID);
        
        builder.set(SearchIndexDefinition.FIELD_INDEX_DEFINITION, INDEX_DEFINITION);
        
        INDEX_MAPPING = builder.create(null);
    }
    
    
    
    /*** Fields ***/
    
    private ObjectId id; // Required
    
    private String username; // Required, lowercase
    private String email_address; // Optional
    
    private String first_name; // Required
    private String last_initial; // Required, len = 1
    
    private Day birthday; // Required
    
    // TODO Figure out how to store avatars
    private ObjectId avatar_id; // Required
    
    private String password_hash; // Required
    private String password_salt; // Required
    
    
    public User(ObjectParseTree tree)
    {
        id = tree.getStringable(FIELD_ID);
        
        username = tree.getString(FIELD_USERNAME);
        email_address = tree.getString(FIELD_EMAIL_ADDRESS);

        first_name = tree.getString(FIELD_FIRST_NAME);
        last_initial = tree.getString(FIELD_LAST_INITIAL);

        birthday = tree.getStringable(FIELD_BIRTHDAY);
        
        avatar_id = tree.getStringable(FIELD_AVATAR_ID);

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
    
    public String getSimpleFirstName() { return first_name; }
    public String getSimpleLastInitial() { return last_initial; }
    
    public Day getSimpleBirthday() { return birthday; }
    
    public ObjectId getSimpleAvatarId() { return avatar_id; }
    
    public String getSimplePasswordHash() { return password_hash; }
    public String getSimplePasswordSalt() { return password_salt; }
    
    
    /*** StandardObject ***/

    @Override
    public void normalize()
    {
        username = Normalizer.lowerCase(Normalizer.trim(username));
        email_address = Normalizer.trim(email_address);
        
        first_name = Normalizer.trim(first_name);
        last_initial = Normalizer.trim(last_initial);
        
        if (null != last_initial)
        {
            last_initial = last_initial.substring(0, 1);
        }
    }

    @Override
    public void validate()
    {
        Validator.notNull(id, "Object ID");
        Validator.notNull(username, "Username");
        
        Validator.notNull(first_name, "First Name");
        Validator.notNull(last_initial, "Last Initial");
        if (1 != last_initial.length()) throw new ValidationException("Last Initial must be exactly one letter");
        
        Validator.notNull(birthday, "Birthday");
        Validator.notNull(avatar_id, "Avatar ID");
        
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
        
        if (! Objects.equals(first_name, other.first_name)) return false;
        if (! Objects.equals(last_initial, other.last_initial)) return false;
        
        if (! Objects.equals(birthday, other.birthday)) return false;
        
        if (! Objects.equals(avatar_id, other.avatar_id)) return false;
        
        if (! Objects.equals(password_hash, other.password_hash)) return false;
        if (! Objects.equals(password_salt, other.password_salt)) return false;
        
        return true;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, username, email_address, first_name, last_initial, birthday, avatar_id, password_hash, password_salt);
    }

    @Override
    public int compareTo(User other)
    {
        int ret = Comparison.startCompare();
        
        ret = Comparison.continueCompare(ret, username, other.username);
        
        ret = Comparison.continueCompare(ret, first_name, other.first_name);
        ret = Comparison.continueCompare(ret, last_initial, other.last_initial);
        
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
        
        writer.writeString(FIELD_FIRST_NAME, first_name);
        writer.writeString(FIELD_LAST_INITIAL, last_initial);
        
        writer.writeStringable(FIELD_BIRTHDAY, birthday);
        
        writer.writeStringable(FIELD_AVATAR_ID, avatar_id);
        
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
        
        writer.writeText(SEARCH_FIELD_FIRST_NAME, first_name);
        writer.writeText(SEARCH_FIELD_LAST_INITIAL, last_initial);
        
        writer.writeDay(SEARCH_FIELD_BIRTHDAY, birthday);
        
        writer.writeAtom(SEARCH_FIELD_AVATAR_ID, avatar_id.getSimpleValue());
    }
}
