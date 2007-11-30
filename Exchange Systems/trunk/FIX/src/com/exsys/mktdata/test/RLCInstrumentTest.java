package com.exsys.mktdata.test;
import com.exsys.mktdata.message.*;

import java.util.*;

public class RLCInstrumentTest  {


    public  RLCInstrumentTest(String str)
    {
        	if ( str != null && str.length() >0)
        	{
             	RLCInstrument instr = new RLCInstrument(str.getBytes());
             	System.out.println(instr.toString());
            }

	}



 public static void main(String []args)
 {

		java.awt.datatransfer.Transferable t = java.awt.Toolkit.getDefaultToolkit()
							.getSystemClipboard()
							.getContents(new Object());
		if(t != null)
		{
			try
			{
				String str = (String)t.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
				System.out.println("Input FIX String is - "+ str);
				RLCInstrumentTest lmv = new RLCInstrumentTest(str);
		    }
		    catch(Exception e)
		    {
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("No data selected");
		}

  }


}