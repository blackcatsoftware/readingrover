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
import org.jimmutable.core.utils.Validator;


public class Avatar extends StandardImmutableObject<Avatar> implements Storable, Indexable
{
    /*** Constants ***/
    
    static public final TypeName TYPE_NAME = new TypeName("avatar");
    static public final Kind KIND = new Kind("avatar");
    
    
    /*** Field Definitions ***/
    
    static public final FieldDefinition.Stringable<ObjectId> FIELD_ID = new FieldDefinition.Stringable<>("id", null, ObjectId.CONVERTER);
    
    static public final FieldDefinition.String FIELD_NAME = new FieldDefinition.String("name",null);
    static public final FieldDefinition.Stringable<ObjectId> FIELD_IMAGE_ID = new FieldDefinition.Stringable<>("image_id", null, ObjectId.CONVERTER);
    
    
    /*** Index Definitions ***/
    
    static public final IndexDefinition INDEX_DEFINITION = new IndexDefinition(CloudExecutionEnvironment.getSimpleCurrent().getSimpleApplicationId(), new IndexId("avatar"), new IndexVersion("v1"));

    static public final SearchIndexFieldDefinition SEARCH_FIELD_ID = new SearchIndexFieldDefinition(FIELD_ID.getSimpleFieldName(), SearchIndexFieldType.ATOM);
    
    static public final SearchIndexFieldDefinition SEARCH_FIELD_NAME = new SearchIndexFieldDefinition(FIELD_NAME.getSimpleFieldName(), SearchIndexFieldType.TEXT);
    static public final SearchIndexFieldDefinition SEARCH_FIELD_IMAGE_ID = new SearchIndexFieldDefinition(FIELD_IMAGE_ID.getSimpleFieldName(), SearchIndexFieldType.ATOM);

    
    static public final SearchIndexDefinition INDEX_MAPPING;
    
    static
    {
        Builder builder = new Builder(SearchIndexDefinition.TYPE_NAME);
        
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_ID);
        
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_NAME);
        builder.add(SearchIndexDefinition.FIELD_FIELDS, SEARCH_FIELD_IMAGE_ID);
        
        builder.set(SearchIndexDefinition.FIELD_INDEX_DEFINITION, INDEX_DEFINITION);
        
        INDEX_MAPPING = builder.create(null);
    }
    
    
    
    /*** Fields ***/
    
    private ObjectId id; // Required
    
    private String name; // Required
    private ObjectId image_id; // Required
    
    
    public Avatar(ObjectParseTree tree)
    {
        id = tree.getStringable(FIELD_ID);
        
        name = tree.getString(FIELD_NAME);
        image_id = tree.getStringable(FIELD_IMAGE_ID);
    }

    
    /*** Getters ***/
    
    @Override
    public ObjectId getSimpleObjectId() { return id; }
    
    public String getSimpleName() { return name; }
    public ObjectId getSimpleImageId() { return image_id; }
    
    
    /*** StandardObject ***/

    @Override
    public void normalize()
    {
        name = Normalizer.trim(name);
    }

    @Override
    public void validate()
    {
        Validator.notNull(id, "Object ID");
        
        Validator.notNull(name, "Name");
        Validator.notNull(image_id, "Image ID");
    }

    @Override
    public void freeze()
    {
        // Nothing to do
    }

    @Override
    public boolean equals(Object obj)
    {
        if (! (obj instanceof Avatar)) return false;

        Avatar other = (Avatar) obj;

        if (! Objects.equals(id, other.id)) return false;
        
        if (! Objects.equals(name, other.name)) return false;
        if (! Objects.equals(image_id, other.image_id)) return false;
        
        return true;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, image_id);
    }

    @Override
    public int compareTo(Avatar other)
    {
        int ret = Comparison.startCompare();
        
        ret = Comparison.continueCompare(ret, name, other.name);
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
        
        writer.writeString(FIELD_NAME, name);
        writer.writeStringable(FIELD_IMAGE_ID, image_id);
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

        writer.writeText(SEARCH_FIELD_NAME, name);
        writer.writeAtom(SEARCH_FIELD_IMAGE_ID, image_id.getSimpleValue());
    }
}
