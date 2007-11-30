package com.exsys.fix.message;


/**
 * Base class for all types of fix repeated groups.
 * This class defines basic methods that need to be implemented
 * by the derived classes
 * Creation date: (11/11/01 6:18:27 PM)
 * @author: Administrator
 */

public class FixRepeatedGroup extends FixMessage {


public boolean isFirstField(String tagNum)
{
	return false;
}
public  boolean isMemberField(String tagNum)
{
	return false;
}
public  String getRepeatedGroupTag()
{
	return null;
}
public String getFirstField()
{
	return null;
}


}
