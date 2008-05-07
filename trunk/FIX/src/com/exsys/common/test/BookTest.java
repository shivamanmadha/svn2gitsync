package com.exsys.common.test;

import com.exsys.common.config.*;
import com.exsys.common.exceptions.*;
import com.exsys.common.mktdata.*;

/**
 * Insert the type's description here.
 * Creation date: (6/2/01 10:21:33 AM)
 * @author: Administrator
 */
public class BookTest {
	public static int bookSize = 5;
/**
 * CommonTest constructor comment.
 */
public BookTest() {
	super();
}
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args)
{

	BookRow bidRows[] = new BookRow[bookSize];
	for (int i=0; i<bookSize; i++)
		System.out.println(bidRows[i]);


}
}
