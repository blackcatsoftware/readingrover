package net.jdkr.readingrover.util;

import java.util.Enumeration;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jimmutable.cloud.CloudExecutionEnvironment;
import org.jimmutable.cloud.logging.Log4jUtil;
import org.jimmutable.core.utils.Validator;

/**
 * Programmatic Log4j1 administration. Such as setting the logging levels of all
 * appenders.
 * 
 * @author avery.gonzales
 *
 */
public class Log4jOneUtil
{
	private static final Logger logger = LogManager.getLogger(Log4jOneUtil.class);

	/**
	 * Central topic for all log level change messages
	 */
	public static void setupListeners()
	{
		Log4JOneLevelChangeListener log_level_change_listener = new Log4JOneLevelChangeListener();
		CloudExecutionEnvironment.getSimpleCurrent().getSimpleSignalService().startListening(Log4jUtil.TOPIC_ID, log_level_change_listener);
	}

	/**
	 * Set the Log4j Level of all Loggers
	 * 
	 * @param level
	 *            The logging Level
	 */
	public static void setAllLoggerLevels(Level level)
	{
		Validator.notNull(level);

		org.apache.log4j.Level log4j_one_level = org.apache.log4j.Level.toLevel(level.toString(), null);
		if(log4j_one_level == null)
		{
			logger.error("Unable to convert Log4J2 " + level + " to Log4J1 Level. Set to default level, INFO");
			log4j_one_level  = org.apache.log4j.Level.INFO;
		}
		
		org.apache.log4j.Logger.getRootLogger().setLevel(log4j_one_level);
		Enumeration<?> loggers = org.apache.log4j.LogManager.getCurrentLoggers();
		while (loggers.hasMoreElements())
		{
			org.apache.log4j.Logger logger = (org.apache.log4j.Logger) loggers.nextElement();
			logger.setLevel(log4j_one_level);
		}
	
	}

}
