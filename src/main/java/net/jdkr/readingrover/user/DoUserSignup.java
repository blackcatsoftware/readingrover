package net.jdkr.readingrover.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.util.ByteSource;
import org.jimmutable.cloud.CloudExecutionEnvironment;
import org.jimmutable.cloud.servlet_utils.common_objects.GeneralResponseError;
import org.jimmutable.cloud.servlet_utils.upsert.UpsertResponseOK;
import org.jimmutable.cloud.servlets.util.RequestPageData;
import org.jimmutable.cloud.servlets.util.ServletUtil;
import org.jimmutable.core.objects.Builder;
import org.jimmutable.core.objects.common.Day;
import org.jimmutable.core.objects.common.ObjectId;
import org.jimmutable.core.serialization.Format;
import org.jimmutable.core.serialization.reader.HandReader;
import org.jimmutable.core.utils.Validator;

import net.jdkr.readingrover.auth.DoLogin;
import net.jdkr.readingrover.util.AuthUtil;

// TODO Make this DoUpsertUser - Requires authenticating the current user and authorizing them to make changes for the affected user

@SuppressWarnings("serial")
public class DoUserSignup extends HttpServlet
{
    private static final Logger LOGGER = LogManager.getLogger(DoUserSignup.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        RequestPageData page_data = ServletUtil.getPageDataFromPost(request, new RequestPageData());

        if (page_data.isEmpty())
        {
            LOGGER.error("Request contains no data");
            ServletUtil.writeSerializedResponse(response, new GeneralResponseError("Request failed"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        HandReader reader = new HandReader(page_data.getOptionalDefaultJSONData(""));

        try
        {
            Builder builder = new Builder(User.TYPE_NAME);
            builder.set(User.FIELD_ID, ObjectId.createRandomId());
            
            builder.set(User.FIELD_USERNAME, reader.readString(User.FIELD_USERNAME.getSimpleFieldName().getSimpleName(), null));
            builder.set(User.FIELD_EMAIL_ADDRESS, reader.readString(User.FIELD_EMAIL_ADDRESS.getSimpleFieldName().getSimpleName(), null));
            
            builder.set(User.FIELD_FIRST_NAME, reader.readString(User.FIELD_FIRST_NAME.getSimpleFieldName().getSimpleName(), null));
            builder.set(User.FIELD_LAST_INITIAL, reader.readString(User.FIELD_LAST_INITIAL.getSimpleFieldName().getSimpleName(), null));
            
            String birthday_raw = reader.readString(User.FIELD_BIRTHDAY.getSimpleFieldName().getSimpleName(), null);
            Validator.notNull(birthday_raw, "Birthday");
            
            builder.set(User.FIELD_BIRTHDAY, new Day(birthday_raw));
            
            // TODO Send real avatar ID
            builder.set(User.FIELD_AVATAR_ID, ObjectId.createRandomId());
            
            String password = reader.readString("password", null);
            if (null != password)
            {
                ByteSource salt = AuthUtil.newSalt();
                
                String hashed_password = AuthUtil.hashPasswordAndEncode(password, salt);
                
                builder.set(User.FIELD_PASSWORD_HASH, hashed_password);
                builder.set(User.FIELD_PASSWORD_SALT, AuthUtil.encodeSalt(salt));
            }
            
            User new_user = builder.create(null);
            
            LOGGER.debug("New User JSON\n" + new_user.serialize(Format.JSON_PRETTY_PRINT));
            
            if (! CloudExecutionEnvironment.getSimpleCurrent().getSimpleStorage().upsert(new_user, Format.JSON_PRETTY_PRINT))
            {
                String error_message = String.format("Failed to upsert new object to storage! Kind:%s ObjectId:%s", new_user.getSimpleKind(), new_user.getSimpleObjectId());
                LOGGER.error(error_message);
                ServletUtil.writeSerializedResponse(response, new GeneralResponseError(error_message), GeneralResponseError.HTTP_STATUS_CODE_ERROR);
                return;
            }
            
            // Note: We need to use a sync upsert to make sure login works
            if (! CloudExecutionEnvironment.getSimpleCurrent().getSimpleSearch().upsertDocument(new_user))
            {
                String error_message = String.format("Failed to upsert new object to search! Kind:%s ObjectId:%s", new_user.getSimpleKind(), new_user.getSimpleObjectId());
                LOGGER.error(error_message);
                ServletUtil.writeSerializedResponse(response, new GeneralResponseError(error_message), GeneralResponseError.HTTP_STATUS_CODE_ERROR);
                return;
            }
            
            // TODO There is almost certainly a race condition here b/c Search and Storage are eventually consistent
            // Log the new user in
            if (! DoLogin.login(new_user.getSimpleUsername(), password, true))
            {
                LOGGER.warn(String.format("Unable to automatically log in %s (%s) after account creation", new_user.getSimpleUsername(), new_user.getSimpleObjectId()));
            }
            
            ServletUtil.writeSerializedResponse(response, new UpsertResponseOK("Success", new_user), UpsertResponseOK.HTTP_STATUS_CODE_OK);
            return;
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to create new object!", e);
            
            String error_msg = e.getMessage();

            Throwable cause = e.getCause();
            while (null != cause)
            {
                cause = cause.getCause();
            }
            
            if (null != cause)
            {
                error_msg = cause.getMessage();
            }
            
            GeneralResponseError error = new GeneralResponseError(error_msg);
            ServletUtil.writeSerializedResponse(response, error, GeneralResponseError.HTTP_STATUS_CODE_ERROR);
        }
    }
}
