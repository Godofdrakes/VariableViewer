package com.variableviewer;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.runelite.client.callback.ClientThread;

import javax.swing.*;

/**
 * Creates Rx scheduler objects for use in RuneLite plugins
 */
public class EventSchedulers
{
	public EventSchedulers( ClientThread clientThread )
	{
		this.Client = Schedulers.from( clientThread::invoke );
		this.Swing = Schedulers.from( SwingUtilities::invokeLater );
	}

	public final Scheduler Client;
	public final Scheduler Swing;
}
