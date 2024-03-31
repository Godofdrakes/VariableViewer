package com.variableviewer.ui;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.runelite.client.util.SwingUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.WeakHashMap;

public final class Buttons
{
	/**
	 * @hidden Originally I was using something similar to RL and Watchdog, usurping the isSelected state.
	 * That got messy when I wanted to both check and change the state on the same frame.
	 * This avoids that mess. The state is toggled, and then broadcast to observers.
	 */
	private static final WeakHashMap<AbstractButton, Observable<Boolean>> toggleState =
		new WeakHashMap<>();

	@Getter
	@RequiredArgsConstructor
	public static final class ButtonState
	{
		private final String tooltip;
		private final ImageIcon icon;
		private final ImageIcon rolloverIcon;

		public static ButtonState toggleButtonOff()
		{
			return new Buttons.ButtonState(
				"Expand",
				Icons.SECTION_EXPAND_ICON,
				Icons.SECTION_EXPAND_ICON_HOVER );
		}

		public static ButtonState toggleButtonOn()
		{
			return new Buttons.ButtonState(
				"Collapse",
				Icons.SECTION_RETRACT_ICON,
				Icons.SECTION_RETRACT_ICON_HOVER );
		}
	}

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

	private static Observable<Boolean> onToggle( AbstractButton button, boolean initialState )
	{
		return toggleState.computeIfAbsent( button, b -> Buttons
			// On button action
			.onAction( b )
			// Toggle state
			.scan( initialState, ( state, event ) -> !state )
			// Cache the last known state
			.replay( 1 )
			// Replay is a published observable. It must be connected.
			// This has the bonus of ensuring we only track one state per button.
			.autoConnect() );
	}

	public static Observable<Boolean> onToggle( AbstractButton button )
	{
		return toggleState.get( button );
	}

	public static JButton createActionButton( final ButtonState state )
	{
		final JButton button = new JButton();

		SwingUtil.removeButtonDecorations( button );

		button.setPreferredSize( new Dimension( 16, 16 ) );
		button.setIcon( state.getIcon() );
		button.setRolloverIcon( state.getRolloverIcon() );
		button.setToolTipText( state.getTooltip() );

		return button;
	}

	public static JButton createToggleActionButton(
		final ButtonState offState,
		final ButtonState onState,
		boolean initialState )
	{
		final JButton button = new JButton();

		SwingUtil.removeButtonDecorations( button );

		button.setPreferredSize( new Dimension( 16, 16 ) );

		// Technically we should track the subscription and dispose of it.
		// But the button is subscribing to itself so whatever it's fine.
		// noinspection ResultOfMethodCallIgnored
		Buttons.onToggle( button, initialState )
			.map( state -> state ? onState : offState )
			.subscribe( state ->
			{
				button.setIcon( state.getIcon() );
				button.setRolloverIcon( state.getRolloverIcon() );
				button.setToolTipText( state.tooltip );
			} );

		return button;
	}

	public static JButton createToggleActionButton( boolean initialState )
	{
		return createToggleActionButton(
			Buttons.ButtonState.toggleButtonOff(),
			Buttons.ButtonState.toggleButtonOn(),
			initialState );
	}
}
