package com.variableviewer.services;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import lombok.NonNull;
import lombok.val;
import net.runelite.api.Client;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.ConfigChanged;

import javax.inject.Inject;

public final class EventService
{
	private final EventBus eventBus;
	private final ClientThread clientThread;

	@Inject
	public EventService(
		@NonNull final EventBus eventBus,
		@NonNull final ClientThread clientThread )
	{
		this.eventBus = eventBus;
		this.clientThread = clientThread;
	}

	public <T> Observable<T> onEvent( final Class<T> eventClass )
	{
		return Observable.<T>create( observer ->
			{
				val subscriber = eventBus.register( eventClass, observer::onNext, Integer.MIN_VALUE );
				val dispose = Disposable.fromAction( () -> eventBus.unregister( subscriber ) );
				observer.setDisposable( dispose );
			} )
			// It's not clear to me if eventBus.register is actually thread safe.
			// Let's play it safe and perform the subscription on the client thread.
			.subscribeOn( RxPlugin.mainScheduler( clientThread ) );
	}

	public Observable<VarbitChanged> onVarbitChanged()
	{
		return onEvent( VarbitChanged.class );
	}

	public Observable<Integer> onVarbitChanged( final int varbitId )
	{
		return onEvent( VarbitChanged.class )
			.filter( event -> event.getVarbitId() == varbitId )
			.map( VarbitChanged::getValue );
	}

	public Observable<Integer> onVarpChanged( final int varpId )
	{
		return onEvent( VarbitChanged.class )
			.filter( event -> event.getVarpId() == varpId )
			.map( VarbitChanged::getValue );
	}

	public <T extends Config> Observable<ConfigChanged> onConfigChanged(
		@NonNull final T config )
	{
		final var configGroup = config.getClass()
			.getInterfaces()[0]
			.getAnnotation( ConfigGroup.class )
			.value();

		return onEvent( ConfigChanged.class )
			.filter( event -> event.getGroup().equals( configGroup ) );
	}

	public <T extends Config, U> Observable<U> onConfigChanged(
		@NonNull final T config,
		@NonNull final String configKey,
		@NonNull final Function<T, U> selector )
	{
		return onConfigChanged( config )
			.filter( event -> event.getKey().equals( configKey ) )
			.map( event -> selector.apply( config ) );
	}

	public Observable<Integer> onClientTick(
		@NonNull final Client client )
	{
		return onEvent( ClientTick.class )
			.map( event -> client.getTickCount() );
	}
}
