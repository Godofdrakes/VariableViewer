package com.variableviewer.ui.panel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

import javax.swing.*;

public class WatchPanel
	extends JPanel
	implements Disposable
{
	private final CompositeDisposable disposable;

	public WatchPanel()
	{
		disposable = new CompositeDisposable();
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
