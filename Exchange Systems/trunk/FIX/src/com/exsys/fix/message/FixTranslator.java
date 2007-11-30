package com.exsys.fix.message;

import java.io.*;
/**
 * Interface definition for FixTranslator
 * Creation date: (11/18/01 6:57:53 PM)
 * @author: Administrator
 */
public interface FixTranslator 
{
	public void translate( FixMessage msg ) throws IOException ;
	public void translateBody( FixMessage msg )  throws IOException ;

	public void translateHeader( FixMessage msg, boolean includeMsgType )throws IOException ;
}
