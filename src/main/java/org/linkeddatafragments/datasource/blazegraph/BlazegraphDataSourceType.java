package org.linkeddatafragments.datasource.blazegraph;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.linkeddatafragments.datasource.IDataSource;
import org.linkeddatafragments.datasource.IDataSourceType;
import org.linkeddatafragments.exceptions.DataSourceCreationException;

/**
 * The type of Blazegraph-based Triple Pattern Fragment data sources.
 * 
 * @author <a href="http://olafhartig.de">Olaf Hartig</a>
 */
public class BlazegraphDataSourceType implements IDataSourceType
{
    @Override
    public IDataSource createDataSource( final String title,
                                         final String description,
                                         final JsonObject settings )
                                             throws DataSourceCreationException
    {
        final Properties props = new Properties();

        final Iterator<Map.Entry<String,JsonElement>> it =
                                                settings.entrySet().iterator();
        while ( it.hasNext() )
        {
            final Map.Entry<String,JsonElement> entry = it.next();
            props.setProperty( entry.getKey(), entry.getValue().getAsString() );
        }

        try {
            return new BlazegraphDataSource( title, description, props );
        }
        catch ( Exception ex ) {
            throw new DataSourceCreationException(ex);
        }
    }

}
