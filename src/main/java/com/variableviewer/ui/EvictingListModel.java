package com.variableviewer.ui;

import com.google.common.collect.EvictingQueue;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.NonNull;

import javax.swing.*;

public class EvictingListModel<T>
	extends AbstractListModel<T>
	implements Disposable
{
	private final CompositeDisposable disposable;

	private final EvictingQueue<T> items;

	public EvictingListModel( int bufferSize )
	{
		disposable = new CompositeDisposable();

		items = EvictingQueue.create( bufferSize );
	}

	public EvictingListModel<T> add( @NonNull final T item )
	{
		items.add( item );

		fireContentsChanged( this, 0, items.size() );

		return this;
	}

	@Override
	public int getSize()
	{
		return items.size();
	}

	@Override
	public T getElementAt( int index )
	{
		// This is slow and bad but whatever it's fine for now
		return items.stream()
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
