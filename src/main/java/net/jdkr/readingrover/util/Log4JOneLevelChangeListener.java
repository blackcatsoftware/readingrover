package net.jdkr.readingrover.util;

import org.jimmutable.cloud.logging.LogLevelMessageOnChange;
import org.jimmutable.cloud.messaging.signal.SignalListener;
import org.jimmutable.core.objects.StandardObject;

/**
 * Listens for Level changes to logging for Log4j1. Originally created for AdWords logging.
 * 
 * @author avery.gonzales
 *
 */
public class Log4JOneLevelChangeListener implements SignalListener
{
    @Override
	@SuppressWarnings("rawtypes")
	public void onMessageReceived( StandardObject message )
	{
		if ( message instanceof LogLevelMessageOnChange )
		{
			LogLevelMessageOnChange log_level_message = (LogLevelMessageOnChange) message;
			Log4jOneUtil.setAllLoggerLevels(log_level_message.getSimpleLogLevel().getSimpleLevel());
		}
	}

}