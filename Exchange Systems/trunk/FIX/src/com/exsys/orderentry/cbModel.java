package com.exsys.orderentry;

import com.exsys.common.business.*;
import com.exsys.common.extrading.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class cbModel extends AbstractListModel implements ComboBoxModel
{
	private String[] elements;
	private String selection;

	public cbModel( String[] comp )
	{
		super();
		elements = comp;
	}
	public Object getElementAt( int index )
	{
		return elements[index];
	}
	public Object getSelectedItem()
	{
		return selection;
	}
	public int getSize()
	{
		return elements.length;
	}
	public void setSelectedItem( Object item )
	{
		selection = (String)item;
	}
}
