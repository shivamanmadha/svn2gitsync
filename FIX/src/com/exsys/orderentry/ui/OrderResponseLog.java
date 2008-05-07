package com.exsys.orderentry.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

import com.exsys.common.trading.*;
import com.exsys.fix.message.*;
import com.exsys.common.exceptions.*;

public class OrderResponseLog {

  final JTextArea textareaOrdRespLog = new JTextArea();

  public Component createLogArea() {

    textareaOrdRespLog.setLineWrap(true);
    textareaOrdRespLog.setWrapStyleWord(true);
    textareaOrdRespLog.setEditable(false);

    final JScrollPane areaScrollPane = new JScrollPane(textareaOrdRespLog);

    //areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    areaScrollPane.setPreferredSize(new Dimension(500,300));
    areaScrollPane.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Execution Responses Log"),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ),
        areaScrollPane.getBorder()
      )
    );

    JPanel pane = new JPanel();
    pane.add(areaScrollPane);

    return pane;
  }
  public void displayText(String str) {
    textareaOrdRespLog.append(str + "\n");
  }
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) { }

    //Create the top-level container and add contents to it.
    JFrame frame = new JFrame("Trader Application - Execution Responses Log");
    OrderResponseLog traderRespLog = new OrderResponseLog();
    Component contents = traderRespLog.createLogArea();
    frame.getContentPane().add(contents, BorderLayout.CENTER);

    traderRespLog.displayText("Hello World");


    //Finish setting up the frame, and show it.
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frame.pack();
    frame.setVisible(true);
  }
}
