package com.variableviewer.services;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.runelite.client.callback.ClientThread;

import javax.swing.*;

/**
 * Creates Rx scheduler objects for use in RuneLite plugins
 */
public final class RxPlugin
{
	public static Scheduler uiScheduler()
	{
		return Schedulers.from( SwingUtilities::invokeLater );
	}

	public static Scheduler mainScheduler( ClientThread clientThread )
	{
		return Schedulers.from( clientThread::invokeLater );
	}

	public static Scheduler backgroundScheduler()
	{
		return Schedulers.computation();
	}

	private RxPlugin()
	{

	}
}
