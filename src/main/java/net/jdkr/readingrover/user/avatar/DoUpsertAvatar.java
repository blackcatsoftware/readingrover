package net.jdkr.readingrover.user.avatar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jimmutable.cloud.CloudExecutionEnvironment;
import org.jimmutable.cloud.servlet_utils.common_objects.GeneralResponseError;
import org.jimmutable.cloud.servlet_utils.upsert.UpsertResponseOK;
import org.jimmutable.cloud.servlet_utils.upsert.UpsertResponseValidationError;
import org.jimmutable.cloud.servlets.avatar.DoGetAvatar;
import org.jimmutable.cloud.servlets.util.PageDataHandler;
import org.jimmutable.cloud.servlets.util.ServletUtil;
import org.jimmutable.cloud.servlets.util.VisitedPageDataElement;
import org.jimmutable.cloud.storage.ObjectIdStorageKey;
import org.jimmutable.cloud.storage.StorageKey;
import org.jimmutable.core.objects.common.ObjectId;
import org.jimmutable.core.utils.Optional;


@SuppressWarnings("serial")
public class DoUpsertAvatar extends HttpServlet
{
    static private final Logger LOGGER = LogManager.getLogger(DoUpsertAvatar.class);

    static private final String SVG_MIME_TYPE = "image/svg+xml";
    static private final Set<String> ALLOWED_IMG_EXTENSIONS = new HashSet<>();
    static
    {
        ALLOWED_IMG_EXTENSIONS.add(SVG_MIME_TYPE);
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        ServletUtil.handlePageDataFromPost(request, new PageDataHandler()
        {
            @Override
            public void handle(VisitedPageDataElement element)
            {
                if (element.hasInputStream())
                {
                    try
                    {
                        InputStream is = element.getOptionalInputStream(null);
                        String type = Optional.getOptional(URLConnection.guessContentTypeFromStream(is), null, null);
                        if (SVG_MIME_TYPE.equalsIgnoreCase(type))
                        {
                            StorageKey key = new ObjectIdStorageKey(DoGetAvatar.KIND, ObjectId.createRandomId(), DoGetAvatar.EXTENSION);
                            if (CloudExecutionEnvironment.getSimpleCurrent().getSimpleStorage().upsertStreaming(key, is, false))
                            {
                                ServletUtil.writeSerializedResponse(response, new UpsertResponseOK(String.format("Upserted %s", element.getOptionalFilename("?")), key.getSimpleName()), UpsertResponseOK.HTTP_STATUS_CODE_OK);
                                return;
                            }
                            else
                            {
                                LOGGER.error(String.format("Failed to upsert %s/%s.%s to storage!", key.getSimpleKind().getSimpleValue(), key.getSimpleName().getSimpleValue(), key.getSimpleExtension().getSimpleValue()));
                            }
                        }
                        else
                        {
                            LOGGER.error(String.format("Unsupported image extension %s", type));
                        }                    
                    }
                    catch (IOException e)
                    {
                        LOGGER.error(e);
                    }
                }
                else if (element.hasJSONData())
                {
                    // TODO
                }
            }

            @Override
            public void onWarning(String message)
            {
                LOGGER.warn(message);
            }

            @Override
            public void onError(String message)
            {
                ServletUtil.writeSerializedResponse(response, new GeneralResponseError(message), GeneralResponseError.HTTP_STATUS_CODE_ERROR);
            }
        });
        
        // TODO Only fail if everything didn't assemble properly
        ServletUtil.writeSerializedResponse(response, new UpsertResponseValidationError(String.format("The file failed to upsert. Ensure it is an allowed extension %s and check the system logs.", ALLOWED_IMG_EXTENSIONS.toString()), "file"), UpsertResponseValidationError.HTTP_STATUS_CODE_ERROR);
        return;
    }
}
