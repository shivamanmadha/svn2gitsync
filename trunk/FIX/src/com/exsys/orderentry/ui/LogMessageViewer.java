package com.exsys.orderentry.ui;

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
      

    Container container = null;

    public  LogMessageViewer()
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
        	if ( (f.canRead()) && (f.length() >0))                                               
        	{    
        	    try
             	    {
             		if(isFixLogFile(f)){
             		             			
                		// Read the contents of the file into a byte[] object.
                		FileInputStream in = new FileInputStream(f);
                		BufferedReader reader = new BufferedReader( new InputStreamReader(in));
                		String line=reader.readLine();
                		startPos =0;
                		endPos = line.indexOf("8=Fix",5);
                		String firstMsg =line.substring(0,endPos); 
                					                               				
                		// insert the data read from the file into TextArea                
                		ta.setText(firstMsg);                 		                		 
                		constructTable(firstMsg);  
                		countIndex =1;  
                		nextMsg = firstMsg;
                		prevMsg = firstMsg;                 		      			                            			                  
             		
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
        	  
        	ta.setText("");
        	String msg =getPrevLogMsg(f,countIndex);
        	ta.setText(msg);
        	constructTable(msg);         
		
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
        	
         	ta.setText("");   
         	String msg =  getNextLogMsg(f,countIndex);   	
        	ta.setText(msg);     
        	constructTable(msg);   
			
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
     	
     }
     public static void main(String []args){
	LogMessageViewer lmv = new LogMessageViewer();
	lmv.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        lmv.setTitle("LogMessageViewer");        
	lmv.pack();
        lmv.setVisible(true);

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
  private boolean isFixLogFile(File f) throws FileNotFoundException,IOException{
  
  	FileInputStream in = new FileInputStream(f);
  	BufferedReader reader = new BufferedReader( new InputStreamReader(in));
  	String line = reader.readLine();
  	System.out.println("Line read: "+line);
  	
  	if(line.startsWith("8=Fix"))
  	return true;
  	else 
  	return false;
  		 	
  }
  private String getNextLogMsg(File f,int count)throws FileNotFoundException,IOException {
  	FileInputStream in = new FileInputStream(f);
        BufferedReader reader = new BufferedReader( new InputStreamReader(in));
        String line;    
        //String nextMsg="";               			                                       
        long filelength = f.length();
        while ( (line = reader.readLine()) != null)
        { 
        	int nextstartPos = endPos;        	
        	int nextendPos= line.indexOf("8=Fix",nextstartPos+5);         	
        	if(nextendPos !=-1 ){      	
        	nextMsg =line.substring(nextstartPos,nextendPos);         	
        	startPos =nextstartPos;
        	endPos = nextendPos;        	
        	countIndex = countIndex+1;         	 
        	}  	
        	else{
        	JOptionPane.showMessageDialog(null, "Reached Last Log Message" ," Error",JOptionPane.ERROR_MESSAGE);
        	}
	}	
	return nextMsg;
  }
  
  
  private String getPrevLogMsg(File f,int count)throws FileNotFoundException,IOException {
  	FileInputStream in = new FileInputStream(f);
        BufferedReader reader = new BufferedReader( new InputStreamReader(in));
        String line;
        long filelength = f.length();
        while ( (line = reader.readLine()) != null)
        {
        	int prevendPos = startPos;
        	if((prevendPos ) >0){
        	String s = line.substring(0,prevendPos);
        	int prevstartPos = s.lastIndexOf("8=Fix");  
        	//System.out.println(len);        	                                                                          
        	prevMsg =line.substring(prevstartPos,prevendPos);
        	endPos = prevendPos;
        	startPos = prevstartPos;
        	countIndex = countIndex-1;          	
        	}
        	else{
        	JOptionPane.showMessageDialog(null, "Reached First Log Message" ," Error",JOptionPane.ERROR_MESSAGE);
        	}    	
	}	
	return prevMsg;
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
			tagName.add(fielddef.getTagName());			
			tagValue.add(field.getTagValue());
			//System.out.println(field.getTagNumberAsString() + " --- " + field.getTagValue());			
			rowData.add(field.getTagNumberAsString());
			rowData.add(fielddef.getTagName());
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
