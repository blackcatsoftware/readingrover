package net.jdkr.readingrover.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jimmutable.core.objects.Builder;
import org.jimmutable.core.objects.StandardObject;
import org.jimmutable.core.objects.common.ObjectId;
import org.jimmutable.core.serialization.Format;
import org.junit.Test;

import net.jdkr.readingrover.StubTest;


public class AvatarTest extends StubTest
{
    static private final ObjectId ID = new ObjectId(123);
    
    static private final String NAME = "animal";
    static private final ObjectId IMAGE_ID = new ObjectId(987);
    
    
    static private Builder createAndPopulateAvatarBuilder()
    {
        Builder builder = new Builder(Avatar.TYPE_NAME);
        
        builder.set(Avatar.FIELD_ID, ID);
        
        builder.set(Avatar.FIELD_NAME, NAME);
        builder.set(Avatar.FIELD_IMAGE_ID, IMAGE_ID);
        
        return builder;
    }
    
    
    @Test
    public void testBuilder()
    {
        Builder builder = createAndPopulateAvatarBuilder();
        
        Avatar avatar = builder.create(null);
        assertNotNull(avatar);

        assertEquals(Avatar.KIND, avatar.getSimpleKind());
        
        assertEquals(ID, avatar.getSimpleObjectId());
        assertEquals(NAME, avatar.getSimpleName());
        assertEquals(IMAGE_ID,avatar.getSimpleImageId());
        
        System.out.println(avatar.toJavaCode(Format.JSON_PRETTY_PRINT, "obj"));
    }
    
    @Test
    public void testSerialization()
    {
        String obj_string = String.format("%s\n%s\n%s\n%s\n%s\n%s"
                        , "{"
                        , "  \"type_hint\" : \"avatar\","
                        , "  \"id\" : \"0000-0000-0000-007b\","
                        , "  \"name\" : \"animal\","
                        , "  \"image_id\" : \"0000-0000-0000-03db\""
                        , "}"
                   );
        Avatar avatar = (Avatar) StandardObject.deserialize(obj_string);
        
        assertEquals(ID, avatar.getSimpleObjectId());
        assertEquals(NAME, avatar.getSimpleName());
        assertEquals(IMAGE_ID,avatar.getSimpleImageId());
        
        assertEquals(obj_string, avatar.serialize(Format.JSON_PRETTY_PRINT));
    }
    
    @Test
    public void testComparisonAndEquals()
    {
        Builder builder = createAndPopulateAvatarBuilder();
        Avatar avatar = builder.create(null);
        
        builder.set(Avatar.FIELD_NAME, "aaa");
        Avatar avatar_less = builder.create(null);
        
        builder.set(Avatar.FIELD_NAME, "zzz");
        Avatar avatar_more = builder.create(null);
        
        assertEquals(0, avatar.compareTo(avatar));
        assertTrue(1 <= avatar.compareTo(avatar_less));
        assertTrue(-1 >= avatar.compareTo(avatar_more));
        assertTrue(-1 >= avatar_less.compareTo(avatar_more));
    }
    
    @Test(expected = Exception.class)
    public void testRequiredFieldId()
    {
        Builder builder = createAndPopulateAvatarBuilder();
        builder.unset(Avatar.FIELD_ID);
        builder.create(null);
    }
    
    @Test(expected = Exception.class)
    public void testRequiredFieldName()
    {
        Builder builder = createAndPopulateAvatarBuilder();
        builder.unset(Avatar.FIELD_NAME);
        builder.create(null);
    }
    
    @Test(expected = Exception.class)
    public void testRequiredFieldImageId()
    {
        Builder builder = createAndPopulateAvatarBuilder();
        builder.unset(Avatar.FIELD_IMAGE_ID);
        builder.create(null);
    }
}
