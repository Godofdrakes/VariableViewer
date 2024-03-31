package com.variableviewer.ui;

import com.google.common.collect.EvictingQueue;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.NonNull;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class EvictingListModel<T>
	extends AbstractListModel<T>
	implements Disposable
{
	private final CompositeDisposable disposable;

	private final Queue<T> queue;

	private final int maxSize;

	public EvictingListModel( final int maxSize )
	{
		if ( maxSize < 1 )
		{
			throw new IllegalArgumentException( "maxSize must be at least 1" );
		}

		this.disposable = new CompositeDisposable();
		this.queue = new ArrayDeque<>( maxSize );
		this.maxSize = maxSize;
	}

	public EvictingListModel<T> add( @NonNull final T item )
	{
		while ( queue.size() >= maxSize )
		{
			queue.remove();
		}

		queue.add( item );

		fireContentsChanged( this, 0, queue.size() );

		return this;
	}

	@Override
	public int getSize()
	{
		return queue.size();
	}

	@Override
	public T getElementAt( int index )
	{
		// This is slow and bad but whatever it's fine for now
		return queue.stream()
			.skip( index )
			.findFirst()
			.orElse( null );
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
