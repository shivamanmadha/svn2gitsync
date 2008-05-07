package com.exsys.fix.tools;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Date;
import java.io.*;
import javax.swing.table.*;
import javax.swing.event.*;

import com.exsys.common.trading.*;
import com.exsys.fix.message.*;
import com.exsys.common.exceptions.*;
import com.exsys.service.*;
import com.exsys.orderentry.*;
import com.exsys.fix.specification.*;

public class LogMessageViewer extends JFrame {

    private JLabel lblSelectFile;    
    private JLabel lblMsgType;
    private JLabel lblMsgName;
    private JLabel lblMsgTypeVal;
    private JLabel lblMsgNameVal;
    private JTextField tfSelectFile;    
    private JButton btnSelectFile;
    private JButton btnNext;
    private JButton btnPrevious;
    private JButton btnCancel;
    private JTextArea ta;
    private JTable table;    
    private boolean isStdLogFile;
    private File f;
    private int startPos; 
    private int endPos;       
    private int countIndex;
    private String nextMsg;
    private String prevMsg;
    private DefaultTableModel dm;
    private String column[]={"TagNumber","TagName","TagValue"}; 
    private Vector colHeaders;
    private ArrayList fixMessages = new ArrayList();
    private int currIndex = 0;
    private int maxIndex = 0;
    
  	private FileInputStream in = null;
    private BufferedReader reader = null;
    
    
      

    Container container = null;

    public  LogMessageViewer(String[] args) throws IOException
    {
      	Container container = getContentPane(); 	
      	container.setBackground( Color.white );
      	container.setForeground( Color.blue );	       	
      	setResizable(false);
      	     	
      	JPanel pane = new JPanel();
      	pane.setBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("LogMessageViewer"),
          BorderFactory.createEmptyBorder(5,5,5,5)));
      	pane.setLayout(new BorderLayout());
      	
        JPanel fileSelect = new JPanel();
        fileSelect.setBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("LogFile Chooser"),
          BorderFactory.createEmptyBorder(5,5,5,5)));
        GridBagLayout gridbag = new GridBagLayout();
      	GridBagConstraints  c = new GridBagConstraints();
        fileSelect.setLayout(gridbag); 
      	lblSelectFile = new JLabel("File :");      	      	
      	setDimensions(lblSelectFile,gridbag,c,0,0,1,1,0,0,GridBagConstraints.HORIZONTAL,GridBagConstraints.WEST,0,10,0,10);
      	fileSelect.add(lblSelectFile);	
	
      	tfSelectFile = new JTextField("");
      	tfSelectFile.setColumns(25);      	
      	setDimensions(tfSelectFile,gridbag,c,1,0,1,1,0,0,GridBagConstraints.HORIZONTAL,GridBagConstraints.WEST,0,10,0,10);
      	fileSelect.add(tfSelectFile);	
      	
      	
      	final JFileChooser fcSelectFile = new JFileChooser();  
      	fcSelectFile.setMultiSelectionEnabled(false);
        fcSelectFile.setDialogTitle("File Loader");
        fcSelectFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        ta = new JTextArea(3,35);   
      	ta.setEditable(false);        	
      	JScrollPane tascroll = new JScrollPane(ta);	
      	setDimensions(tascroll,gridbag,c,0,1,3,1,0,0,GridBagConstraints.HORIZONTAL,GridBagConstraints.WEST,0,10,0,10);		   	      		
	fileSelect.add(tascroll);
      	
      	btnSelectFile = new JButton("Open");      	      	
      	setDimensions(btnSelectFile,gridbag,c,2,0,1,1,0,0,GridBagConstraints.HORIZONTAL,GridBagConstraints.WEST,0,10,0,10);		
      	btnSelectFile.addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){ 
      	ta.setText(""); 
      	    	   	
      	int returnVal = fcSelectFile.showOpenDialog(LogMessageViewer.this);
    	if (returnVal == JFileChooser.APPROVE_OPTION){    	
        f = fcSelectFile.getSelectedFile(); 

		try
		{
			readFromFile(f);
		}
		catch(Exception e1)
		{
             	JOptionPane.showMessageDialog(null, "Cannot read from file '" +
                                           f.getAbsolutePath() + "'",
                                           "Read Error -"+e1.getMessage(),
                                           JOptionPane.ERROR_MESSAGE);
 			
		}
         	
      
        tfSelectFile.setText(f.getAbsolutePath());  	
		}     
        }
      	});
      	
      	fileSelect.add(btnSelectFile);
      	      	      	      	
      	JPanel cntrPanel = new JPanel();
      	cntrPanel.setLayout(new BorderLayout()); 
      	cntrPanel.setBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Each LogMessage in LogFile "),
          BorderFactory.createEmptyBorder(5,5,5,5))); 
      	    	      	
      	
	
	  	
	JPanel scntrPanel = new JPanel();	
	scntrPanel.setLayout(gridbag);
	colHeaders = new Vector();
	colHeaders.addElement("TagNumber");
	colHeaders.addElement("TagName ");
	colHeaders.addElement("TagValue");
	dm = new DefaultTableModel();
	dm.setColumnIdentifiers(colHeaders);
      	table = new JTable(dm);      	  	
	JScrollPane tbscroll = new JScrollPane( table );
	table.setPreferredScrollableViewportSize(new Dimension(380, 200));
	setDimensions(tbscroll,gridbag,c,1,0,1,1,0,0,GridBagConstraints.HORIZONTAL,GridBagConstraints.WEST,5,10,5,10);		
	scntrPanel.add(tbscroll);	
	cntrPanel.add("South",scntrPanel);
	
	
	JPanel ncntrPanel = new JPanel();
	ncntrPanel.setLayout(gridbag);	
	lblMsgType  = new JLabel("Fix Message Type  :");
	lblMsgName  = new JLabel("Fix Message Name  :");
	
	lblMsgTypeVal = new JLabel("");
	lblMsgNameVal = new JLabel("");		
		
	setDimensions(lblMsgType,gridbag,c,0,0,1,1,0,0,GridBagConstraints.HORIZONTAL,GridBagConstraints.WEST,5,10,5,10);
	ncntrPanel.add(lblMsgType);
	setDimensions(lblMsgTypeVal,gridbag,c,1,0,1,1,0,0,GridBagConstraints.HORIZONTAL,GridBagConstraints.WEST,5,10,5,10);
	ncntrPanel.add(lblMsgTypeVal);
	setDimensions(lblMsgName,gridbag,c,0,1,1,1,0,0,GridBagConstraints.HORIZONTAL,GridBagConstraints.WEST,5,10,5,10);
	ncntrPanel.add(lblMsgName);
	setDimensions(lblMsgNameVal,gridbag,c,1,1,1,1,0,0,GridBagConstraints.HORIZONTAL,GridBagConstraints.WEST,5,10,5,10);
	ncntrPanel.add(lblMsgNameVal);
	cntrPanel.add("West",ncntrPanel);
	
	JPanel btnPanel = new JPanel();	
	btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	btnPrevious = new JButton("Previous");
	btnNext = new JButton("Next");
	btnCancel = new JButton("Cancel");
		        
	btnPanel.add(btnPrevious);
	btnPrevious.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {            
        try
        { 
        	  
        	//ta.setText("");

        	String msg =getPrevLogMsg(f,countIndex);
        	if(msg != null)
        	{        	
        	ta.setText(msg);
        	constructTable(msg);         
        	}
		
	}
        catch (java.io.FileNotFoundException ex)
        {
         System.out.println(ex);
         ex.printStackTrace();       		
        }
        catch (java.io.IOException ex)
        {
         System.out.println(ex);
         ex.printStackTrace();         		
        }          
        }
        });
	btnPanel.add(btnNext);
	btnNext.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {           
        try
        { 
        	
         	//ta.setText("");   
         	String msg =  getNextLogMsg(f,countIndex);
         	if( msg != null )
         	{   	
        		ta.setText(msg);     
        		constructTable(msg);   
         	}
			
        }
        catch (java.io.FileNotFoundException ex)
        {
         System.out.println(ex);
         ex.printStackTrace();       		
        }
        catch (java.io.IOException ex)
        {
         System.out.println(ex);
         ex.printStackTrace();         		
        }          
        }
        });
	btnPanel.add(btnCancel);
	btnCancel.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {    
        System.exit(0);	      
        }
        });
	
	
	pane.add("North",fileSelect);
      	pane.add("Center", cntrPanel);  
      	pane.add("South",btnPanel);    	
     	container.add(  pane );
     	
      	if(args[0] != null)
      	{
      		f = new File(args[0]);
      		readFromFile(f);
      	}
     	
     	
     }
     public static void main(String []args){
     	try
     	{
		LogMessageViewer lmv = new LogMessageViewer(args);
		lmv.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });        
        lmv.setTitle("LogMessageViewer");        
		lmv.pack();
        lmv.setVisible(true);
     	}
     	catch(Exception e)
     	{
     		e.printStackTrace();
     	}

     }
    private void setDimensions(Component c, GridBagLayout gl, GridBagConstraints gc, 
                   int x, int y, int w, int h,int weightx,int weighty,int fill,int anchor,int top, int left, int bottom, int right ) {
    gc.gridx = x;
    gc.gridy = y;
    gc.gridwidth = w;
    gc.gridheight = h;
    gc.weightx=weightx;
    gc.weighty=weighty;
    gc.fill=fill;
    gc.anchor=anchor;   
    gc.insets = new Insets(top,left,bottom,right);
    gl.setConstraints(c, gc);    
  }
  
  private void readFromFile(File f) throws IOException
  {
        	if ( (f.canRead()) && (f.length() >0))                                               
        	{    
        	    try
             	    {
             		if(isFixLogFile(f)){
             		             			
                		// Read the contents of the file into a byte[] object.
                		in = new FileInputStream(f);
                		reader = new BufferedReader( new InputStreamReader(in));
                		String line=reader.readLine();
                		
                		startPos =0;
                		int lengthPos = line.indexOf(";");
                		//System.out.println("First delim is "+ line.substring(startPos,lengthPos-1));
                		endPos = line.indexOf(";",lengthPos+1);
                		//System.out.println("Second delim is "+ line.substring(lengthPos+1,endPos));
                		//System.out.println(line.substring(lengthPos+1,endPos));
                		int length = Integer.parseInt(line.substring(lengthPos+1,endPos));
                		//endPos = line.indexOf("8=Fix",5);
                		String firstMsg =line.substring(endPos+1,endPos+1+length); 
                		//System.out.println(firstMsg);
                					                               				
                		// insert the data read from the file into TextArea                
                		ta.setText(firstMsg);                 		                		 
                		constructTable(firstMsg);  
                		countIndex =1;  
                		nextMsg = firstMsg;
                		prevMsg = firstMsg;
                		currIndex = 0;
                		maxIndex = 0;
                		fixMessages.add(firstMsg);                 		      			                            			                  
             		
           		}
           		
           		else{
                		JOptionPane.showMessageDialog(null, "Not a Valid Fix LogFile '" +
                                              f.getAbsolutePath() + "':" ,
                                             "Not Valid Fix LogFile",
                                              JOptionPane.ERROR_MESSAGE);
           		}
         	}
             		catch (java.io.FileNotFoundException ex)
           		{
                		JOptionPane.showMessageDialog(null, "Cannot find '" +
                                              f.getAbsolutePath() + "'",
                                              "Read Error",
                                              JOptionPane.ERROR_MESSAGE);
           		}
             		catch (java.io.IOException ex)
             		{
                		JOptionPane.showMessageDialog(null, "Error reading from '" +
                                              f.getAbsolutePath() + "':" +
                                            ex.getMessage(), "Read Error",
                                              JOptionPane.ERROR_MESSAGE);
             		}
         	}         	
          else
         {
             	JOptionPane.showMessageDialog(null, "Cannot read from file '" +
                                           f.getAbsolutePath() + "'",
                                           "Read Error",
                                           JOptionPane.ERROR_MESSAGE);
         }  	
  }
  private boolean isFixLogFile(File f) throws FileNotFoundException,IOException{
  
  	FileInputStream in = new FileInputStream(f);
  	BufferedReader reader = new BufferedReader( new InputStreamReader(in));
  	String line = reader.readLine();
  	
  	//if(line.startsWith("8=Fix"))
  	//System.out.println(line);

  	int index = line.indexOf("8=FIX");
  	//System.out.println(index);  	
  	if(index != -1)
  	return true;
  	else 
  	return false;
  		 	
  }
  private String getNextLogMsg(File f,int count)throws FileNotFoundException,IOException {
  	
  		if(currIndex < maxIndex)
  		{
  			return (String)fixMessages.get(++currIndex);
  		}
  		//FileInputStream in = new FileInputStream(f);
        //BufferedReader reader = new BufferedReader( new InputStreamReader(in));
        //String line;    
        //String nextMsg="";               			                                       
        //long filelength = f.length();
        String line = reader.readLine();
        if(line != null)
        { 
        	
            int lengthPos = line.indexOf(";");
            System.out.println("First delim is "+ line.substring(startPos,lengthPos-1));
            int endPos = line.indexOf(";",lengthPos+1);
            System.out.println("Second delim is "+ line.substring(lengthPos+1,endPos));
            System.out.println(line.substring(lengthPos+1,endPos));
            int length = Integer.parseInt(line.substring(lengthPos+1,endPos));
            
            String nextMsg =line.substring(endPos+1,endPos+1+length); 
 			++currIndex;
 			++maxIndex;
			fixMessages.add(nextMsg);        	              	
			return nextMsg;
        	
        }  	
        else
        {
        	JOptionPane.showMessageDialog(null, "Reached Last Log Message" ," Error",JOptionPane.ERROR_MESSAGE);
        	return null;
        }
	
  }
  
  
  private String getPrevLogMsg(File f,int count)throws FileNotFoundException,IOException {
  	//FileInputStream in = new FileInputStream(f);
        //BufferedReader reader = new BufferedReader( new InputStreamReader(in));
        //String line;
        //long filelength = f.length();
        if(currIndex > 0)
        {

        	return (String)fixMessages.get(--currIndex);
        	
        }
        else
        {
        	JOptionPane.showMessageDialog(null, "Reached First Log Message" ," Error",JOptionPane.ERROR_MESSAGE);
        	return null;
        }    	
		

  }
  private void constructTable(String msg){
  		Vector tagNum = new Vector();
        	Vector tagName = new Vector();
        	Vector tagValue = new Vector();     
        	 
        	try{  	
        	FixSpecification spec = FixSpecification.getSpecification();
		FixMessage fm = FixMessageFactory.createFixMessageWithoutValidation(msg.getBytes());
		String fixMessageType = fm.getMessageType();
		FixMessageDef messagedef = spec.getFixMessageInfo(fixMessageType);
		String fixMessageName = messagedef.getMessageName();
		lblMsgTypeVal.setText(fixMessageType);
		lblMsgNameVal.setText(fixMessageName);		
		// first get header field map		
		TreeMap headerMap = fm.getHeaderMap();
		System.out.println("Header Fields:");
			
		int i=dm.getRowCount();		
		if(i>0){			
			for(int j=i-1;j>=0;j--){			
			dm.removeRow(j);			
			}
		}	
		
		System.out.println("UU");
		for( Iterator enum1=headerMap.values().iterator(); enum1.hasNext();)
		{	Vector rowData = new Vector();
			FixField field = (FixField)enum1.next();
			tagNum.add(field.getTagNumberAsString());			
			FixFieldDef fielddef =spec.getFixFieldInfo(Integer.parseInt(field.getTagNumberAsString()));
			tagName.add(fielddef.getTagName());			
			tagValue.add(field.getTagValue());
											
			//System.out.println(field.getTagNumberAsString() + " --- " + field.getTagValue());
			rowData.add(field.getTagNumberAsString());
			rowData.add(fielddef.getTagName());
			rowData.add(field.getTagValue());			
			dm.addRow(rowData);									
			
		}
	
		// printing body fields
		TreeMap bodyMap = fm.getBodyFieldMap();
		System.out.println("Body Fields:");
		
		for( Iterator enum1=bodyMap.values().iterator(); enum1.hasNext();)
		{	
			Vector rowData = new Vector();
			FixField field = (FixField)enum1.next();
			tagNum.add(field.getTagNumberAsString());
			//FixSpecification spec = FixSpecification.getSpecification();
			FixFieldDef fielddef =spec.getFixFieldInfo(Integer.parseInt(field.getTagNumberAsString()));

			tagName.add(fielddef==null?"unknown":fielddef.getTagName());			
			tagValue.add(field.getTagValue());
			//System.out.println(field.getTagNumberAsString() + " --- " + field.getTagValue());			
			rowData.add(field.getTagNumberAsString());
			rowData.add(fielddef==null?"unknown":fielddef.getTagName());
			rowData.add(field.getTagValue());			
			dm.addRow(rowData);
		}	
		/*dm.addColumn("TagNumber",tagNum);
		dm.addColumn("TagName",tagName);
		dm.addColumn("TagValue",tagValue);*/			
		
		}catch( Exception ex )
    		{
			ex.printStackTrace();
    		}	   	
  }
  
}
