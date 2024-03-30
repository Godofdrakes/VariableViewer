package com.variableviewer.ui;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class RecentChangeGroup
	extends JPanel
	implements Disposable
{
	private final CompositeDisposable disposable = new CompositeDisposable();

	public RecentChangeGroup( final int varbitId, final String varbitName )
	{
		val toggleButton = Buttons.createToggleActionButton(
			Icons.SECTION_RETRACT_ICON,
			Icons.SECTION_RETRACT_ICON_HOVER,
			Icons.SECTION_EXPAND_ICON,
			Icons.SECTION_EXPAND_ICON_HOVER,
			"Collapse",
			"Expand",
			true
		);

		val label = new JLabel( varbitName );

		val details = new JTextArea( String.format( "VarbitId %d", varbitId ) );

		this.setLayout( new BorderLayout() );

		this.add( toggleButton, BorderLayout.WEST );
		this.add( label, BorderLayout.EAST );
		this.add( details, BorderLayout.SOUTH );

		disposable.add(
			Buttons.onSelected( toggleButton )
				.subscribe( details::setVisible )
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
