package com.variableviewer;

import com.google.inject.Provider;
import com.google.inject.Provides;
import com.variableviewer.services.EventService;
import com.variableviewer.services.RxPlugin;
import com.variableviewer.services.VarbitNameService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

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
	private ClientThread clientThread;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private VariableViewerConfig config;

	@Inject
	private EventService eventService;

	@Inject
	private Provider<VariableViewerPanel> watchPanelProvider;

	@Inject
	@Named("developerMode")
	private boolean developerMode;

	@Override
	protected void startUp() throws Exception
	{
		if ( developerMode )
		{
			disposable.add(
				eventService.onConfigChanged( config )
					.observeOn( RxPlugin.clientScheduler( clientThread ) )
					.subscribe( event -> log
						.debug( "Config changed! {}.{}", event.getGroup(), event.getKey() ) )
			);
		}

		val navigationIcon = ImageUtil
			.loadImageResource( VariableViewerPlugin.class, "gear.png" );

		val navigationButton = NavigationButton.builder()
			.panel( watchPanelProvider.get() )
			.tooltip( "Variable Viewer" )
			.icon( navigationIcon )
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

	@Provides
	VariableViewerConfig provideConfig( ConfigManager configManager )
	{
		return configManager.getConfig( VariableViewerConfig.class );
	}

	@Provides
	EventService provideEvents( EventBus eventBus )
	{
		return new EventService( eventBus );
	}
	
	@Provides
	VarbitNameService provideNameService()
	{
		return new VarbitNameService();
	}
}
