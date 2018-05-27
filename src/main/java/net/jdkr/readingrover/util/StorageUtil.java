package net.jdkr.readingrover.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jimmutable.cloud.CloudExecutionEnvironment;
import org.jimmutable.cloud.storage.ObjectIdStorageKey;
import org.jimmutable.cloud.storage.StorageKey;
import org.jimmutable.cloud.storage.StorageKeyExtension;
import org.jimmutable.core.objects.StandardObject;
import org.jimmutable.core.objects.common.Kind;
import org.jimmutable.core.objects.common.ObjectId;

public class StorageUtil
{
    private static final Logger LOGGER = LogManager.getLogger(StorageUtil.class);
    
    
    @SuppressWarnings("unchecked")
    static public <T extends StandardObject<T>> T getComplexCurrentVersion(Kind kind, ObjectId id, T default_value)
    {
        // TODO Integrate with StandardImmutableObjectCache
        
        T obj = null;
        try
        {
            StorageKey key = new ObjectIdStorageKey(kind, id, StorageKeyExtension.JSON);
            byte[] raw_obj = CloudExecutionEnvironment.getSimpleCurrent().getSimpleStorage().getCurrentVersion(key, null);
            if (null == raw_obj) return default_value;
            
            obj = (T) StandardObject.deserialize(new String(raw_obj, "UTF-8")); // TODO Why is UTF-8 specified? Is there a bug in the to/from serialization code?
            if (null == obj) return default_value;
        }
        catch (Exception e)
        {
            LOGGER.error(e);
            return default_value;
        }
        
        return obj;
    }
    
    
    private StorageUtil() {}
}
