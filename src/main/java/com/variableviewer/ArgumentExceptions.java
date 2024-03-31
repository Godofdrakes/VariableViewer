package com.variableviewer;

public final class ArgumentExceptions
{
	public static void ThrowIfNull( final Object o )
	{
		if ( o == null )
		{
			throw new IllegalArgumentException( "Argument must not be null" );
		}
	}
}
