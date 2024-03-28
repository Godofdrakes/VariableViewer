package com.variableviewer.services;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.runelite.client.callback.ClientThread;

import javax.swing.*;

/**
 * Creates Rx scheduler objects for use in RuneLite plugins
 */
public class RxPlugin
{
	public static Scheduler swingScheduler()
	{
		return Schedulers.from( SwingUtilities::invokeLater );
	}

	public static Scheduler clientScheduler( ClientThread clientThread )
	{
		return Schedulers.from( clientThread::invokeLater );
	}
}
