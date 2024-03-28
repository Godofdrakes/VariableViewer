package com.variableviewer.services;

import lombok.val;
import net.runelite.api.Varbits;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VarbitNameService
{
	private final Map<Integer,String> nameMap;
	
	public VarbitNameService()
	{
		// Not sure if we _need_ thread safety here
		nameMap = new ConcurrentHashMap<>();

		for ( val field : Varbits.class.getFields() )
		{
			if ( field.getType() != Integer.class )
			{
				continue;
			}

			// todo: only use static public fields
			// not sure how to enforce that

			try
			{
				val varbitId = field.getInt( this );
				val varbitName = field.getName();
				nameMap.put( varbitId, varbitName );
			}
			catch ( Throwable e )
			{
				// Ignore and continue
			}
		}
	}

	public final String get( int varbitId )
	{
		return nameMap.get( varbitId );
 	}

	public final String get( Integer varbitId )
	{
		return nameMap.get( varbitId );
 	}
}
