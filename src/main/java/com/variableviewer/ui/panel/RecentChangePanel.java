package com.variableviewer.ui.panel;

import com.variableviewer.services.EventService;
import com.variableviewer.services.RxPlugin;
import com.variableviewer.ui.RecentChangeGroup;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.runelite.api.Client;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

@Slf4j
public class RecentChangePanel
	extends JPanel
	implements Disposable
{
	private final int maxSize = 100;
	private final CompositeDisposable disposable;

	@Inject
	public RecentChangePanel(
		@NonNull final Client client,
		@NonNull final EventService eventService )
	{
		disposable = new CompositeDisposable();

		var listPanel = new JPanel( new BorderLayout() );

		listPanel.setPreferredSize( new Dimension( Integer.MAX_VALUE, 0 ) );

		var scrollPanel = new JScrollPane(
			listPanel,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );

		this.add( scrollPanel );

		disposable.add( eventService
			// On any varbit change
			.onVarbitChanged()
			.filter( event -> event.getVarbitId() != -1 )
			// Batch changes by tick
			.window( eventService.onClientTick( client ) )
			// For each batch
			.concatMapMaybe( batch -> batch.toList()
				// Skip empty batches
				.filter( items -> !items.isEmpty() )
				// Create a change group item
				.map( items -> new RecentChangeGroup( client.getTickCount(), items ) )
			)
			// On the UI thread
			.observeOn( RxPlugin.uiScheduler() )
			// Add to list
			.subscribe( group -> listPanel.add( group, BorderLayout.CENTER ) )
		);
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
