package com.variableviewer;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.val;
import net.runelite.api.Varbits;

import javax.swing.*;

public class RecentPanel
	extends JPanel
	implements Disposable
{
	private final CompositeDisposable disposable;

	public RecentPanel()
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
