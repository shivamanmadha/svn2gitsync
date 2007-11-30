package com.exsys.mktdata.session;

import java.io.*;
import java.util.*;
import com.exsys.mktdata.message.*;
import com.exsys.service.*;
/**
 * Insert the type's description here.
 * Creation date: (11/22/01 2:15:22 PM)
 * @author: Administrator
 */
public class RLCFileManager {
	private boolean addNL = true;

	private String logDirectory = null;

	public final static String FIX_MESSAGE_DELIM = ";";
	public final static char NL = '\n';



/**
 * RLCFileManager constructor comment.
 */
public RLCFileManager()
{

}


public static ArrayList getRLCMessagesList(String file, String msgType) {

	int charRead;
	StringBuffer msg = new StringBuffer();
	ArrayList list=new ArrayList(10);
	try
	{
		String inFileName = file;

		FileInputStream stream= new FileInputStream(inFileName);

		while( (charRead=stream.read()) != -1 )
		{
			if(charRead == NL )
				continue;

			msg.setLength(0);
			while( (charRead=stream.read()) != -1 )
			{

				if( (char)charRead == '[' )
				{
					break;
				}
			}

			while( (charRead=stream.read()) != -1 )
			{
				if( (char)charRead != ']' )
				{
					msg.append( (char)charRead );
				}
				else
				  break;
			}
			//System.out.println("Message is : " + msg);
			RLCMessage rlcMsg = RLCMessageFactory.createRLCMessage(msg.toString().getBytes());
			if(rlcMsg.getMessageType().equals(msgType))
			{
				list.add(rlcMsg);
			}
			else
			{
				// ignore
			}


		}
		stream.close();


	}
	catch( Exception e )
	{
		e.printStackTrace();
	}

	//}

	return list;


}


}
