package com.variableviewer;

import com.variableviewer.ui.panel.RecentChangePanel;
import com.variableviewer.ui.panel.WatchPanel;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.val;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;

public class VariableViewerPanel
	extends PluginPanel
	implements Disposable
{
	private final CompositeDisposable disposable;

	@Inject
	public VariableViewerPanel()
	{
		disposable = new CompositeDisposable();

		val watchPanel = new WatchPanel();

		disposable.add( watchPanel );

		val recentPanel = new RecentChangePanel();

		disposable.add( recentPanel );

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
