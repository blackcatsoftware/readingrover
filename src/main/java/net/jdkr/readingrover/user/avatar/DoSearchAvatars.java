package net.jdkr.readingrover.user.avatar;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jimmutable.cloud.elasticsearch.IndexDefinition;
import org.jimmutable.cloud.elasticsearch.SearchIndexFieldDefinition;
import org.jimmutable.cloud.servlet_utils.search.Sort;
import org.jimmutable.cloud.servlet_utils.search.SortBy;
import org.jimmutable.cloud.servlet_utils.search.SortDirection;
import org.jimmutable.cloud.servlets.common.DoSearch;
import org.jimmutable.core.fields.FieldArrayList;


@SuppressWarnings("serial")
public class DoSearchAvatars extends DoSearch
{
    @SuppressWarnings("unused")
    static private final Logger LOGGER = LogManager.getLogger(DoSearchAvatars.class);

    @Override
    protected IndexDefinition getSearchIndexDefinition()
    {
        return Avatar.INDEX_DEFINITION;
    }

    @Override
    protected Sort getSort(Sort default_value)
    {
        Collection<SortBy> sort_by = new FieldArrayList<>();
        
        sort_by.add(new SortBy(new SearchIndexFieldDefinition(Avatar.SEARCH_FIELD_NAME.getSimpleFieldName(), Avatar.SEARCH_FIELD_NAME.getSimpleType()), SortDirection.ASCENDING));
        
        return new Sort(sort_by);
    }
}
