package com.variableviewer.services;

import lombok.NonNull;
import lombok.val;
import net.runelite.api.Varbits;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class VarbitNames
{
	private static final Map<Integer, String> nameMap;

	static
	{
		val map = new ConcurrentHashMap<Integer, String>();

		for ( val field : Varbits.class.getFields() )
		{
			if ( field.getType() != int.class )
			{
				continue;
			}

			// todo: only use static public fields
			// not sure how to enforce that

			try
			{
				val varbitId = field.getInt( null );
				val varbitName = field.getName();
				map.put( varbitId, varbitName );
			}
			catch ( Throwable e )
			{
				// Ignore and continue
			}
		}

		nameMap = map;
	}

	public static String put(
		@NonNull final Integer varbitId,
		@NonNull final String varbitName )
	{
		return nameMap.put( varbitId, varbitName );
	}

	public static String get( @NonNull final Integer varbitId )
	{
		String name = nameMap.get( varbitId );

		if ( name == null )
		{
			name = put( varbitId, String.format( "Varbit %d", varbitId ) );
		}

		return name;
	}
}
