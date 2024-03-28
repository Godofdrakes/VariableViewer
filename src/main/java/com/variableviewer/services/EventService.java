package com.variableviewer.services;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import lombok.val;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.ConfigChanged;

public class EventService
{
	private final EventBus eventBus;

	public EventService( final EventBus eventBus )
	{
		this.eventBus = eventBus;
	}

	public <T> Observable<T> onEvent( final Class<T> eventClass )
	{
		return Observable.create( observer ->
		{
			val subscriber = eventBus.register( eventClass, observer::onNext, Integer.MIN_VALUE );
			val dispose = Disposable.fromAction( () -> eventBus.unregister( subscriber ) );
			observer.setDisposable( dispose );
		} );
	}

	public <T extends Config> Observable<ConfigChanged> onConfigChanged( final T config )
	{
		final var configGroup = config.getClass()
			.getInterfaces()[0]
			.getAnnotation( ConfigGroup.class )
			.value();

		return onEvent( ConfigChanged.class )
			.filter( event -> event.getGroup().equals( configGroup ) );
	}

	public <T extends Config, U> Observable<U> onConfigChanged(
		final T config,
		final String configKey,
		final Function<T, U> selector )
	{
		return onConfigChanged( config )
			.filter( event -> event.getKey().equals( configKey ) )
			.map( event -> selector.apply( config ) );
	}
}
