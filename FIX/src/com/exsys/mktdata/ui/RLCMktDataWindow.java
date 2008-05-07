package com.exsys.mktdata.ui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

import com.exsys.application.*;
import com.exsys.service.*;
import com.exsys.common.exceptions.*;
import com.exsys.common.trading.*;
import com.exsys.common.business.*;



public class RLCMktDataWindow
  extends BaseGUIApplication

{
  // ========================================================================
  // ========================================================================
  public RLCMktDataWindow(String args[]) throws ConfigFileNotFound, Exception
  {
    super(args);
      processConfig();
      //initializeSession(); // initialize jms session -- do this first...
      createWindows();
      

      mTheFrame = new JFrame("Market Data Window");
        mTheFrame.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        });
      mTheFrame.setContentPane(this);
      mTheFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      mTheFrame.pack();
      mTheFrame.setVisible(true);

  }

  // ========================================================================
  // ========================================================================
  private void createWindows() throws Exception
  {
    setLayout(new GridLayout(0,1));
    setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("< RLC Market Data Window >"),
        BorderFactory.createEmptyBorder(0,0,0,0)
      )
    );
    add(createTabbedPanes());
    //add(createResponseLog());
  }
  // ========================================================================
  // ========================================================================
  private Component createTabbedPanes() throws Exception
  {
  	 mTabbedPane = new JTabbedPane();
    if(mEnableMO)
    {
	    mMOWindow = new RLCMOWindow();
	    mTabbedPane.addTab(tagMO, mMOWindow.createComponent());
    }
    if(mEnableMA)
    {
	    mMAWindow = new RLCMAWindow();
	    mTabbedPane.addTab(tagMA, mMAWindow.createComponent());
	    mMABookWindow = new RLCMABookWindow();
	    mTabbedPane.addTab(tagMABook, mMABookWindow.createComponent());
	    
    }
    if(mEnableMY)
    {
	    mMYWindow = new RLCMYWindow();
	    mTabbedPane.addTab(tagMY, mMYWindow.createComponent());
	    mMYBookWindow = new RLCMYBookWindow();
	    mTabbedPane.addTab(tagMYBook, mMYBookWindow.createComponent());
	    
    }

	if(mEnableMY && mEnableMA)
	{
	    mBookWindow = new RLCBookWindow();
	    mTabbedPane.addTab(tagBook, mBookWindow.createComponent());		
	}    
	
    if(mEnableM5)
    {
	    mM5Window = new RLCM5Window();
	    mTabbedPane.addTab(tagM5, mM5Window.createComponent());	    
    }   
    if(mEnableM6)
    {
	    mM6Window = new RLCM6Window();
	    mTabbedPane.addTab(tagM6, mM6Window.createComponent());	    
    } 
    if(mEnableM8)
    {
	    mM8Window = new RLCM8Window();
	    mTabbedPane.addTab(tagM8, mM8Window.createComponent());	    
    }  
    if(mEnableMG)
    {
	    mMGWindow = new RLCMGWindow();
	    mTabbedPane.addTab(tagMG, mMGWindow.createComponent());	    
    } 
    if(mEnableMZero)
    {
	    mMZeroWindow = new RLCMZeroWindow();
	    mTabbedPane.addTab(tagMZero, mMZeroWindow.createComponent());	    
    } 
    if(mEnable23)
    {
	    m23Window = new RLC23Window();
	    mTabbedPane.addTab(tag23, m23Window.createComponent());	    
    }
    if(mEnableM7)
    {
	    mM7Window = new RLCM7Window();
	    mTabbedPane.addTab(tagM7, mM7Window.createComponent());	    
    }
    if(mEnableMJ)
    {
	    mMJWindow = new RLCMJWindow();
	    mTabbedPane.addTab(tagMJ, mMJWindow.createComponent());	    
    }
    if(mEnableMI)
    {
	    mMIWindow = new RLCMIWindow();
	    mTabbedPane.addTab(tagMI, mMIWindow.createComponent());	    
    } 
    if(mEnableMH)
    {
	    mMHWindow = new RLCMHWindow();
	    mTabbedPane.addTab(tagMH, mMHWindow.createComponent());	    
    } 
    if(mEnableMK)
    {
	    mMKWindow = new RLCMKWindow();
	    mTabbedPane.addTab(tagMK, mMKWindow.createComponent());	    
    } 
    if(mEnableM9)
    {
	    mM9Window = new RLCM9Window();
	    mTabbedPane.addTab(tagM9, mM9Window.createComponent());	    
    } 
    if(mEnableMX)
    {
	    mMXWindow = new RLCMXWindow();
	    mTabbedPane.addTab(tagMX, mMXWindow.createComponent());	    
    }                                                
    
    //mTabbedPane.addTab(tagDETAIL, getContent(tagDETAIL));
    return mTabbedPane;
  }
  // ========================================================================
  // ========================================================================
  private Component getContent(String title)
  {
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
    return contentPane;
  }
  // ========================================================================
  // ========================================================================

  // ========================================================================
  // ========================================================================

  // ========================================================================
  // ========================================================================
  private void processConfig()
  {
    try
    {


 	// mkt data related
	  String moFlag = ConfigurationService.getValue("EnableMO","N");
	  if(moFlag.equals("Y"))
	  {
	  	mEnableMO = true;
    	System.out.println("EnableMO is " + moFlag );
	  }


	  String maFlag = ConfigurationService.getValue("EnableMA","N");
	  if(maFlag.equals("Y"))
	  {
	  	mEnableMA = true;
    	System.out.println("EnableMA is " + maFlag );
	  }

	  String myFlag = ConfigurationService.getValue("EnableMY","N");
	  if(myFlag.equals("Y"))
	  {
	  	mEnableMY = true;
    	System.out.println("EnableMY is " + myFlag );
	  }
	  	  
	  String m5Flag = ConfigurationService.getValue("EnableM5","N");
	  if(m5Flag.equals("Y"))
	  {
	  	mEnableM5 = true;
    	System.out.println("EnableM5 is " + m5Flag );
	  }	  
	  String m6Flag = ConfigurationService.getValue("EnableM6","N");
	  if(m6Flag.equals("Y"))
	  {
	  	mEnableM6 = true;
    	System.out.println("EnableM6 is " + m6Flag );
	  }	
	  String m8Flag = ConfigurationService.getValue("EnableM8","N");
	  if(m8Flag.equals("Y"))
	  {
	  	mEnableM8 = true;
    	System.out.println("EnableM8 is " + m8Flag );
	  }	    

	  String mgFlag = ConfigurationService.getValue("EnableMG","N");
	  if(mgFlag.equals("Y"))
	  {
	  	mEnableMG = true;
    	System.out.println("EnableMG is " + mgFlag );
	  }
	  String mZeroFlag = ConfigurationService.getValue("EnableMZero","N");
	  if(mZeroFlag.equals("Y"))
	  {
	  	mEnableMZero = true;
    	System.out.println("EnableMZero is " + mZeroFlag );
	  }
	  String m23Flag = ConfigurationService.getValue("Enable23","N");
	  if(m23Flag.equals("Y"))
	  {
	  	mEnable23 = true;
    	System.out.println("Enable23 is " + m23Flag );
	  }	  	  

	  String m7Flag = ConfigurationService.getValue("EnableM7","N");
	  if(m7Flag.equals("Y"))
	  {
	  	mEnableM7 = true;
    	System.out.println("EnableM7 is " + m7Flag );
	  }
	  String miFlag = ConfigurationService.getValue("EnableMI","N");
	  if(miFlag.equals("Y"))
	  {
	  	mEnableMI = true;
    	System.out.println("EnableMI is " + miFlag );
	  }
	  String mjFlag = ConfigurationService.getValue("EnableMJ","N");
	  if(mjFlag.equals("Y"))
	  {
	  	mEnableMJ = true;
    	System.out.println("EnableMJ is " + mjFlag );
	  }	  	  

	  String mhFlag = ConfigurationService.getValue("EnableMH","N");
	  if(mhFlag.equals("Y"))
	  {
	  	mEnableMH = true;
    	System.out.println("EnableMH is " + mhFlag );
	  }
	  String mkFlag = ConfigurationService.getValue("EnableMK","N");
	  if(mkFlag.equals("Y"))
	  {
	  	mEnableMK = true;
    	System.out.println("EnableMK is " + mkFlag );
	  }
	  String m9Flag = ConfigurationService.getValue("EnableM9","N");
	  if(m9Flag.equals("Y"))
	  {
	  	mEnableM9 = true;
    	System.out.println("EnableM9 is " + m9Flag );
	  }
	  String mxFlag = ConfigurationService.getValue("EnableMX","N");
	  if(mxFlag.equals("Y"))
	  {
	  	mEnableMX = true;
    	System.out.println("EnableMX is " + mxFlag );
	  }	  	  	  



    }
    catch( ConfigAttributeNotFound exc )
    {
      System.out.println("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }





  // ========================================================================
  // ========================================================================
  public static void main(String args[])
  {
    try
    {
      /*
      JFrame frame = new JFrame("Exchange API Trading Suite");
      frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });
      frame.setContentPane(new RLCMktDataWindow(args));
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
      */
      new RLCMktDataWindow(args);
    }
    catch(Exception exc)
    {
      exc.printStackTrace();
      System.exit(-1);
    }
  }

  private JFrame mTheFrame = null;
  private JTabbedPane mTabbedPane = null;
  //MO
  private boolean mEnableMO = false;
  private RLCMOWindow mMOWindow = null;
  private final static String tagMO = "MO";


  //MA
  private boolean mEnableMA = false;
  private RLCMAWindow mMAWindow = null;
  private final static String tagMA = "MA";
  private RLCMABookWindow mMABookWindow = null;
  private final static String tagMABook = "MA Book";  

  //MY
  private boolean mEnableMY = false;
  private RLCMYWindow mMYWindow = null;
  private final static String tagMY = "MY";
  private RLCMYBookWindow mMYBookWindow = null;
  private final static String tagMYBook = "MY Book";

  //M5
  private boolean mEnableM5 = false;
  private RLCM5Window mM5Window = null;
  private final static String tagM5 = "M5";
  
  //M6
  private boolean mEnableM6 = false;
  private RLCM6Window mM6Window = null;
  private final static String tagM6 = "M6";
  
  //M8
  private boolean mEnableM8 = false;
  private RLCM8Window mM8Window = null;
  private final static String tagM8 = "M8";  


  //MG
  private boolean mEnableMG = false;
  private RLCMGWindow mMGWindow = null;
  private final static String tagMG = "MG";  
  
  //MZero
  private boolean mEnableMZero = false;
  private RLCMZeroWindow mMZeroWindow = null;
  private final static String tagMZero = "MZero";  
  
  //23
  private boolean mEnable23 = false;
  private RLC23Window m23Window = null;
  private final static String tag23 = "23";  
  
  
  //M7
  private boolean mEnableM7 = false;
  private RLCM7Window mM7Window = null;
  private final static String tagM7 = "M7"; 
  
  
  //MI
  private boolean mEnableMI = false;
  private RLCMIWindow mMIWindow = null;
  private final static String tagMI = "MI"; 
  
  
  //MH
  private boolean mEnableMH = false;
  private RLCMHWindow mMHWindow = null;
  private final static String tagMH = "MH"; 
  

  //MK
  private boolean mEnableMK = false;
  private RLCMKWindow mMKWindow = null;
  private final static String tagMK = "MK"; 

  //M9
  private boolean mEnableM9 = false;
  private RLCM9Window mM9Window = null;
  private final static String tagM9 = "M9"; 

  //MX
  private boolean mEnableMX = false;
  private RLCMXWindow mMXWindow = null;
  private final static String tagMX = "MX"; 

  //MJ
  private boolean mEnableMJ = false;
  private RLCMJWindow mMJWindow = null;
  private final static String tagMJ = "MJ"; 


  //Book
  private RLCBookWindow mBookWindow = null;
  private final static String tagBook = "Book"; 

}

