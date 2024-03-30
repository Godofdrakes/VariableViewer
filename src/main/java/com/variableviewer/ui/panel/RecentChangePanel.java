package com.variableviewer.ui.panel;

import com.variableviewer.services.EventService;
import com.variableviewer.services.RxPlugin;
import com.variableviewer.services.VarbitNames;
import com.variableviewer.ui.EvictingListModel;
import com.variableviewer.ui.RecentChangeGroup;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.runelite.api.events.VarbitChanged;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

@Slf4j
public class RecentChangePanel
	extends JPanel
	implements Disposable
{
	private final CompositeDisposable disposable;

	@Inject
	public RecentChangePanel( @NonNull final EventService eventService )
	{
		disposable = new CompositeDisposable();

		val listModel = new EvictingListModel<VarbitChanged>( 100 );

		disposable.add( eventService
			.onEvent( VarbitChanged.class )
			.filter( event -> event.getVarbitId() != -1 )
			.observeOn( RxPlugin.uiScheduler() )
			.subscribe( listModel::add ) );

		val list = new JList<>( listModel );

		list.setCellRenderer( new CellRenderer() );

		val foo = new RecentChangeGroup( 42, "Meaning Of Life" );

		this.add( foo );

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
			val varbitName = VarbitNames.get( value.getVarbitId() );
			val varbitValue = value.getValue();
			val label = String.format( "%s changed to %d", varbitName, varbitValue );
			return new JTextArea( label );
		}
	}
}
