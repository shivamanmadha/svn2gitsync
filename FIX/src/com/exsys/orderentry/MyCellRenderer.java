package com.exsys.orderentry;

import com.exsys.common.business.*;
import com.exsys.common.trading.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class MyCellRenderer extends JLabel implements ListCellRenderer
{

	public Component getListCellRendererComponent( 
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus )
	{
		if( value != null )
		{
			String text = value.toString();
			setText( text );
		}

		if( isSelected )
		{
			setBackground( list.getSelectionBackground());
			setForeground( list.getSelectionForeground());
		}
		else
		{
			setBackground( list.getBackground() );
			setForeground( list.getForeground());

		}
		return this;
	}
}
