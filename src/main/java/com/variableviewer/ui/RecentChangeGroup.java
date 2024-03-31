package com.variableviewer.ui;

import com.variableviewer.VarbitNames;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.ui.components.DragAndDropReorderPane;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

@Slf4j
public class RecentChangeGroup
	extends JPanel
	implements Disposable
{
	private final CompositeDisposable disposable = new CompositeDisposable();

	public RecentChangeGroup(
		final int clientTick,
		@NonNull final Collection<VarbitChanged> changes )
	{
		val toggleButton = Buttons.createToggleActionButton(
			Buttons.ButtonState.toggleButtonOff(),
			Buttons.ButtonState.toggleButtonOn(),
			false
		);

		val label = new JLabel( String.format( "Tick %d", clientTick ) );

		val list = new DefaultListModel<VarbitChanged>();

		list.addAll( changes );

		val changeList = new JList<>( list );

		DragAndDropReorderPane foo;

		changeList.setCellRenderer( new Renderer() );

		this.setLayout( new BorderLayout() );

		this.add( toggleButton, BorderLayout.WEST );
		this.add( label, BorderLayout.EAST );
		this.add( changeList, BorderLayout.SOUTH );

		disposable.addAll(
			Buttons.onToggle( toggleButton )
				.subscribe( changeList::setVisible )
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

	private static final class Renderer
		implements ListCellRenderer<VarbitChanged>
	{
		@Override
		public Component getListCellRendererComponent( JList<? extends VarbitChanged> list,
			VarbitChanged value,
			int index,
			boolean isSelected,
			boolean cellHasFocus )
		{
			val varbitName = VarbitNames.get( value.getVarbitId() );
			val varbitValue = Integer.toString( value.getValue() );

			val panel = new JPanel( new BorderLayout() );

			panel.add( new JLabel( varbitName ), BorderLayout.WEST );
			panel.add( new JLabel( varbitValue ), BorderLayout.EAST );

			return panel;
		}
	}
}
