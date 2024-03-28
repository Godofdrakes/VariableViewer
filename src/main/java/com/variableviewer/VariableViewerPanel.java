package com.variableviewer;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.val;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;

public class VariableViewerPanel
	extends PluginPanel
	implements Disposable
{
	private final CompositeDisposable disposable;

	public VariableViewerPanel()
	{
		disposable = new CompositeDisposable();

		val watchPanel = new WatchPanel();
		
		val recentPanel = new RecentPanel();

		val tabPane = new JTabbedPane();

		tabPane.add( "Watch", watchPanel );
		tabPane.add( "Recent", recentPanel );

		this.add( tabPane );
	}

	@Override
	public void dispose()
	{
		disposable.dispose();
	}

	@Override
	public boolean isDisposed()
	{
		return disposable.isDisposed();
	}
}
