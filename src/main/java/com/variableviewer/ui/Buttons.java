package com.variableviewer.ui;

import com.variableviewer.services.RxPlugin;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.val;
import net.runelite.client.util.SwingUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public final class Buttons
{
	public static Observable<ActionEvent> onAction( AbstractButton button )
	{
		return Observable.<ActionEvent>create( observer ->
		{
			val onDispose = Disposable.fromAction( () -> button
				.removeActionListener( observer::onNext ) );

			button.addActionListener( observer::onNext );

			observer.setDisposable( onDispose );
		} );
	}

	public static Observable<Boolean> onSelected( AbstractButton button )
	{
		return Buttons.onAction( button )
			// This is hack. createToggleActionButton doesn't *immediately* toggle the selected state.
			// Gotta wait a frame.
			.observeOn( RxPlugin.uiScheduler() )
			.map( event -> button.isSelected() )
			.startWithItem( button.isSelected() );
	}

	public static JButton createActionButton(
		ImageIcon icon,
		ImageIcon rolloverIcon,
		String tooltip )
	{
		JButton button = new JButton();
		SwingUtil.removeButtonDecorations( button );
		button.setPreferredSize( new Dimension( 16, 16 ) );
		button.setIcon( icon );
		button.setRolloverIcon( rolloverIcon );
		button.setToolTipText( tooltip );
		return button;
	}

	public static JButton createToggleActionButton(
		ImageIcon onIcon,
		ImageIcon onRolloverIcon,
		ImageIcon offIcon,
		ImageIcon offRolloverIcon,
		String onTooltip,
		String offTooltip,
		boolean initialValue )
	{
		JButton button = createActionButton( offIcon, offRolloverIcon, offTooltip );
		SwingUtil.addModalTooltip( button, onTooltip, offTooltip );
		button.setSelectedIcon( onIcon );
		button.setRolloverSelectedIcon( onRolloverIcon );
		button.setSelected( initialValue );
		button.doClick();
		button.addActionListener( ev -> button
			.setSelected( !button.isSelected() ) );
		return button;
	}
}
