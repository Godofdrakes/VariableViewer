package com.variableviewer;

import com.google.inject.Provider;
import com.google.inject.Provides;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

import javax.inject.Inject;
import javax.inject.Named;

@Slf4j
@PluginDescriptor(
	name = "VariableViewer"
)
public class VariableViewerPlugin extends Plugin
{
	private final CompositeDisposable disposable = new CompositeDisposable();

	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private VariableViewerConfig config;

	@Inject
	private EventSchedulers schedulers;

	@Inject
	private EventService eventService;

	@Inject
	private Provider<WatchPanel> watchPanelProvider;

	@Inject
	@Named("developerMode")
	private boolean developerMode;

	@Override
	protected void startUp() throws Exception
	{
		if ( developerMode )
		{
			disposable.add(
				eventService.OnConfigChanged( config )
					.observeOn( schedulers.ClientThread )
					.subscribe( event -> log
						.debug( "Config changed! {}.{}", event.getGroup(), event.getKey() ) )
			);
		}

		val navigationButton = NavigationButton.builder()
			.tooltip( "Variable Viewer" )
			.panel( watchPanelProvider.get() )
			.priority( Integer.MAX_VALUE )
			.build();

		clientToolbar.addNavigation( navigationButton );

		disposable.add( Disposable.fromAction( () -> clientToolbar
			.removeNavigation( navigationButton ) ) );
	}

	@Override
	protected void shutDown() throws Exception
	{
		disposable.dispose();
	}

	@Subscribe
	public void onGameStateChanged( GameStateChanged gameStateChanged )
	{
		if ( gameStateChanged.getGameState() == GameState.LOGGED_IN )
		{
			client.addChatMessage( ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null );
		}
	}

	@Provides
	VariableViewerConfig provideConfig( ConfigManager configManager )
	{
		return configManager.getConfig( VariableViewerConfig.class );
	}

	@Provides
	EventSchedulers provideSchedulers( ClientThread clientThread )
	{
		return new EventSchedulers( clientThread );
	}

	@Provides
	EventService provideEvents( EventBus eventBus )
	{
		return new EventService( eventBus );
	}
}
