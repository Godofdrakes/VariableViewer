package com.variableviewer.ui.panel;

import com.variableviewer.services.RxPlugin;
import com.variableviewer.ui.EvictingListModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.runelite.api.events.VarbitChanged;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RecentChangePanel
	extends JPanel
	implements Disposable
{
	private final CompositeDisposable disposable;

	@Inject
	public RecentChangePanel()
	{
		disposable = new CompositeDisposable();

		val random = new Random();

		val listModel = new EvictingListModel<VarbitChanged>( 10 );

		disposable.add( Observable
			.interval( 5, TimeUnit.SECONDS, RxPlugin.swingScheduler() )
			.map( l ->
			{
				val event = new VarbitChanged();
				event.setVarbitId( l.intValue() );
				event.setValue( random.nextInt() );
				return event;
			} )
			.subscribe( listModel::add ) );

		val list = new JList<>( listModel );

		list.setCellRenderer( new CellRenderer() );

		this.add( list );
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

	private static class CellRenderer implements ListCellRenderer<VarbitChanged>
	{
		@Override
		public Component getListCellRendererComponent(
			JList<? extends VarbitChanged> list,
			VarbitChanged value,
			int index,
			boolean isSelected,
			boolean cellHasFocus )
		{
			return new JTextArea( String.format( "%d changed to %d", value.getVarbitId(), value.getValue() ) );
		}
	}
}
