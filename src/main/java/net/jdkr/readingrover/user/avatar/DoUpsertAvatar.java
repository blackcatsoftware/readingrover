package net.jdkr.readingrover.user.avatar;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jimmutable.cloud.servlet_utils.common_objects.GeneralResponseError;
import org.jimmutable.cloud.servlet_utils.upsert.UpsertResponseOK;
import org.jimmutable.cloud.servlet_utils.upsert.UpsertResponseValidationError;
import org.jimmutable.cloud.servlets.util.PageDataHandler;
import org.jimmutable.cloud.servlets.util.RequestPageData;
import org.jimmutable.cloud.servlets.util.ServletUtil;
import org.jimmutable.cloud.servlets.util.VisitedPageDataElement;
import org.jimmutable.cloud.storage.StorageKey;

@SuppressWarnings("serial")
public class DoUpsertAvatar extends HttpServlet
{
    @SuppressWarnings("unused")
    static private final Logger LOGGER = LogManager.getLogger(DoUpsertAvatar.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        ServletUtil.handlePageDataFromPost(request, new PageDataHandler()
        {
            @Override
            public void handle(VisitedPageDataElement element)
            {
                if (element.hasInputStream())
                {
                    StorageKey key = upsertAvatar(element.getOptionalInputStream(null), null);

                    if (key != null)
                    {
                        ServletUtil.writeSerializedResponse(response, new UpsertResponseOK(String.format("Upserted %s", element.getOptionalFilename("?")), key.getSimpleName()), UpsertResponseOK.HTTP_STATUS_CODE_OK);
                        return;
                    }
                }
                else if (element.hasJSONData())
                {
                    
                }
                
                ServletUtil.writeSerializedResponse(response, new UpsertResponseValidationError(String.format("The file failed to upsert. Ensure it is an allowed extension %s and check the system logs.", ALLOWED_IMG_EXTENSIONS.toString()), "file"), UpsertResponseValidationError.HTTP_STATUS_CODE_ERROR);
                return;
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
    }
}
