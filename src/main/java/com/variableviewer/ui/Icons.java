package com.variableviewer.ui;

import net.runelite.client.plugins.config.ConfigPlugin;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import java.awt.image.BufferedImage;

public final class Icons
{
	public static final ImageIcon SECTION_EXPAND_ICON;
	public static final ImageIcon SECTION_EXPAND_ICON_HOVER;
	public static final ImageIcon SECTION_RETRACT_ICON;
	public static final ImageIcon SECTION_RETRACT_ICON_HOVER;

	static
	{
		BufferedImage sectionRetractIcon = ImageUtil.loadImageResource( ConfigPlugin.class, "/util/arrow_right.png" );
		sectionRetractIcon = ImageUtil.luminanceOffset( sectionRetractIcon, -121 );
		final BufferedImage sectionExpandIcon = ImageUtil.rotateImage( sectionRetractIcon, Math.PI / 2 );

		SECTION_EXPAND_ICON = new ImageIcon( sectionRetractIcon );
		SECTION_EXPAND_ICON_HOVER = new ImageIcon( ImageUtil.alphaOffset( sectionRetractIcon, -100 ) );
		SECTION_RETRACT_ICON = new ImageIcon( sectionExpandIcon );
		SECTION_RETRACT_ICON_HOVER = new ImageIcon( ImageUtil.alphaOffset( sectionExpandIcon, -100 ) );
	}
}
