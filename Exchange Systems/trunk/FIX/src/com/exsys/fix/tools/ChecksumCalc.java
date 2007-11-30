package com.exsys.fix.tools;

import java.util.*;

public class ChecksumCalc  {


    public  ChecksumCalc(String str)
    {
        	if ( str != null && str.length() >0)
        	{
             		System.out.println("Checksum is " + checksum(str));
             	}

	}


public static String checksum(String message)
{
     char[] cArray = message.toCharArray();
     long cks = 0;

     for(int i=0; i<cArray.length;cks += (short)cArray[i++]);
     System.out.println("Total is = " + cks);

     short num = (short)(cks%256);
     return (prePad(String.valueOf(num),'0',3));


}

public static String prePad(String value, char pad, int width) {
	int length = value.length();
	if (length < width) {
		StringBuffer buffer = new StringBuffer(value);
		while (length < width) {
			buffer.insert(0, pad);
			length++;
		}
		return buffer.toString();
	} else {
		return value;
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
				ChecksumCalc lmv = new ChecksumCalc(str);
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