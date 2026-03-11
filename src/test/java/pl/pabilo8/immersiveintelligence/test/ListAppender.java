package pl.pabilo8.immersiveintelligence.test;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.08.2025
 */
public class ListAppender extends AbstractAppender
{
	private final List<LogEvent> logEvents = Collections.synchronizedList(new ArrayList<>());

	public ListAppender()
	{
		super("ListAppender", null, null, true);
		start();
	}

	@Override
	public void append(LogEvent event)
	{
		logEvents.add(event.toImmutable());
	}

	public List<LogEvent> getEvents()
	{
		return new ArrayList<>(logEvents);
	}

	public void clear()
	{
		logEvents.clear();
	}
}
