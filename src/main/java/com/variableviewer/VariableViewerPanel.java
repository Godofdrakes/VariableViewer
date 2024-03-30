package com.variableviewer;

import com.google.inject.Provider;
import com.variableviewer.ui.panel.RecentChangePanel;
import com.variableviewer.ui.panel.WatchPanel;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.NonNull;
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
	public VariableViewerPanel(
		@NonNull final Provider<RecentChangePanel> changePanelProvider,
		@NonNull final Provider<WatchPanel> watchPanelProvider )
	{
		disposable = new CompositeDisposable();

		val recentPanel = changePanelProvider.get();

		disposable.add( recentPanel );

		val watchPanel = watchPanelProvider.get();

		disposable.add( watchPanel );

		val tabPane = new JTabbedPane();

		tabPane.add( "Recent", recentPanel );
		tabPane.add( "Watch", watchPanel );

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
