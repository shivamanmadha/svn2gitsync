package com.exsys.mktdata.tools;

import com.exsys.mktdata.message.*;

import com.exsys.common.exceptions.*;
public class RLCMessageHelper  {


    public  RLCMessageHelper(String str)
    {
        	if ( str != null && str.length() >0)
        	{
             		System.out.println("Msg is " + str);
             		processRLCMessage(str);
            }

	}

	public void processRLCMessage(String msg)
	{


		RLCMessage rlcMsg = null;
		try
		{
			rlcMsg = RLCMessageFactory.createRLCMessage(
					msg.getBytes());
			System.out.println(rlcMsg.toString());
		}
		catch(RLCProtocolError e)
		{
			String fmsg = "MDPMsgProcessor - processRLCMessage - RLC Protocol Error - " +e.getMessage();
			System.out.println("RLC Msg Error - " + fmsg);
			//Logger.error("Fix Msg Received - " + message);
			//Logger.error(fmsg);
			//Logger.error(e.getExternalMessage());
			//error(fmsg + "\n" + e.getExternalMessage()+"\n"+message);
			return;
		}
		
		if(rlcMsg.getMessageType().equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MO))
		{
			RLCMOMessage moMsg = (RLCMOMessage)rlcMsg;
			RLCInstrument instr = new RLCInstrument(moMsg.getCompleteInstrumentCode().getBytes());
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
				//System.out.println("Input FIX String is - "+ str);
				RLCMessageHelper lmv = new RLCMessageHelper(str);
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