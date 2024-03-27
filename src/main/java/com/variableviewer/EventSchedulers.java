package com.variableviewer;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.runelite.client.callback.ClientThread;

/**
 * Creates Rx scheduler objects for use in RuneLite plugins
 */
public class EventSchedulers
{
	public EventSchedulers( ClientThread clientThread )
	{
		this.ClientThread = Schedulers.from( clientThread::invoke );
	}

	/**
	 * Scheduler representing the "main" thread
	 */
	public final Scheduler ClientThread;
}
