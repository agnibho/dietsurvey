/*
    Diet-Survey: Perform diet survey based on 24 hours recall
    Copyright (C) 2013  Agnibho Mondal
    
    This file is part of Diet-Survey.

    Diet-Survey is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Diet-Survey is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Diet-Survey.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.agnibho.code.dietSurvey;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SPARK
 */
@SuppressWarnings("serial")
public class UI extends JFrame implements ActionListener {
    
    public static final String LOC=getAppLocation();
    
    GroupLayout layout;
    
    private boolean unsaved=false;
    private Path path=null;
    
    private Zipper zip;
    private DocHandler dh;
    private Document input;
    private Document output;
    private Node node;
    
    private JFileChooser fc;
    
    private Assess assesser;
    private List<InputStream> report;

    private JLabel heading;
    private IdPanel id;
    private FoodPanel food;
    private ResultPanel result;
    private CustomFood custom;
    private JFrame helpFrame;
    private JButton next;
    private JButton prev;
    private JButton finish;
    private JButton save;
    private JButton view;
    private JButton reset;
    private JButton customEdit;
    private JButton help;

    public UI() throws JAXBException, SAXException, ParserConfigurationException, XPathExpressionException, IOException, TransformerException{
	zip=new Zipper();
	dh=new DocHandler();
	input=dh.getDocInstance();
	node=input;
	node.appendChild(input.createElement("record"));
	node.getFirstChild().appendChild(input.createElement("id"));
	node.getFirstChild().appendChild(input.createElement("intake"));
	
	fc=new JFileChooser();
	fc.setFileFilter(new FileNameExtensionFilter("Zip Archive", "zip"));
	
	
	heading=new JLabel("Diet Survey", JLabel.CENTER);
	heading.setFont(new java.awt.Font(null, 0, 24));
	
	id=new IdPanel();
	
	food=new FoodPanel();
	
	result=new ResultPanel();
	
	helpFrame=Help.getInstance();
	
	next=new JButton("Next Section>");
	next.setActionCommand("next");
	next.addActionListener(this);
	
	prev=new JButton("<Previous Section");
	prev.setActionCommand("prev");
	prev.addActionListener(this);
	prev.setEnabled(false);
	
	finish=new JButton("Finish");
	finish.setActionCommand("finish");
	finish.addActionListener(this);
	finish.setEnabled(false);
	
	save=new JButton("Save Report to File");
	save.setActionCommand("save");
	save.addActionListener(this);
	save.setEnabled(false);
	
	view=new JButton("View Full Report");
	view.setActionCommand("view");
	view.addActionListener(this);
	view.setEnabled(false);
	
	reset=new JButton("Restart Survey");
	reset.setActionCommand("reset");
	reset.addActionListener(this);
	
	customEdit=new JButton("Edit");
	customEdit.setToolTipText("Edit Custom Food List");
	customEdit.setActionCommand("customEdit");
	customEdit.addActionListener(this);
	
	help=new JButton("Help");
	help.setActionCommand("help");
	help.addActionListener(this);

	layout=new GroupLayout(getContentPane());
	getContentPane().setLayout(layout);
	layout.setAutoCreateGaps(true);
	layout.setAutoCreateContainerGaps(true);
	layout.setVerticalGroup(
		layout.createSequentialGroup()
		.addComponent(heading, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
		.addComponent(id, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		.addGroup(layout.createParallelGroup()
			.addComponent(prev)
			.addComponent(next)
			.addComponent(finish)
			.addComponent(customEdit)
			.addComponent(help))
			.addGap(25)
		);
	layout.setHorizontalGroup(
		layout.createParallelGroup()
		.addComponent(heading, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		.addComponent(id, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		.addGroup(layout.createSequentialGroup()
			.addComponent(prev)
			.addComponent(next)
			.addComponent(finish)
			.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addComponent(customEdit)
			.addComponent(help))
		);
	
	try{
	    custom=new CustomFood();
	}catch(Exception e){
	    JOptionPane.showMessageDialog(this, "Problem opening custom_food.xml\nFile may contain error."+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	    customEdit.setEnabled(false);
	}
	
	setTitle("Diet Survey");
	pack();
	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	addWindowListener(new WindowAdapter(){
	    @Override
	    public void windowClosing(WindowEvent we){
		int ret;
		if(unsaved){
		    ret=JOptionPane.showConfirmDialog(null, "Exiting may result in loss of unsaved data.\nDo you want to exit?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		    if(ret!=JFileChooser.APPROVE_OPTION)
			return;
		}
		System.exit(0);
	    }
	});
	SwingWorker<Void, Void> worker=new SwingWorker<Void, Void>() {
	    @Override
	    protected Void doInBackground() throws Exception {
		Update.checkUpdate();
		return null;
	    }
	};
	worker.execute();
	setSize(800, 600);
	setLocationRelativeTo(null);
	setResizable(true);
    }
    
    private static String getAppLocation(){
	try {
	    String loc;
	    loc=new File(UI.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
	    String[] array=loc.split(File.separator+File.separator);
	    loc="";
	    for(int i=0; i<array.length; i++){
		if(array[i].endsWith(".jar")||array[i].endsWith(".zip")||array[i].endsWith(".exe"))
		    return loc;
		else
		    loc=loc+array[i]+File.separator;
	    }
	    loc=loc.substring(0, loc.length()-1);
	    return loc;
	}
	catch (Exception e){
	    e.printStackTrace();
	    return "";
	}
    }

    public void saveFile(File file){
	report=new ArrayList<InputStream>();
	try {
	    report.add(dh.getAsInputStream(input));
	    report.add(dh.getAsInputStream(output));
	    report.add(dh.generateReport(input, output));
	    report.add(assesser.getGraphImage());
	    report.add(java.lang.ClassLoader.getSystemResourceAsStream("com/agnibho/code/resources/style.css"));
	    zip.writeZip(file, report);
	    unsaved=false;
	} catch (TransformerException | TransformerFactoryConfigurationError | IOException e) {
	    JOptionPane.showMessageDialog(this, "An error has occured.\n"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

    @Override
    public void actionPerformed(ActionEvent ae){
	if(ae.getActionCommand().equals("next")){
	    if(id.isComplete()){
		layout.replace(id, food);
		try {
		    food.loadData();
		} catch (TransformerConfigurationException | XPathExpressionException | SAXException | IOException | ParserConfigurationException e) {
		    JOptionPane.showMessageDialog(this, "An error has occured.\n"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		}
		prev.setEnabled(true);
		next.setEnabled(false);
		finish.setEnabled(true);
		customEdit.setVisible(false);
	    }
	}
	else if(ae.getActionCommand().equals("prev")){
	    layout.replace(food, id);
	    prev.setEnabled(false);
	    next.setEnabled(true);
	    finish.setEnabled(false);
	}
	else if(ae.getActionCommand().equals("finish")){
	    if(id.isComplete()&&food.isComplete()){
		try {
		    input=id.copyToDoc(input);
		    input=food.copyToDoc(input);
		    assesser=new Assess(input);
		    output=assesser.getOutput();
		    layout.replace(food, result);
		    layout.replace(prev, save);
		    save.setEnabled(true);
		    layout.replace(next, view);
		    view.setEnabled(true);
		    layout.replace(finish, reset);
		    reset.setEnabled(true);
		    result.display(dh.generateSummary(output));
		} catch (XPathExpressionException | ParserConfigurationException | TransformerException | SAXException | IOException e) {
		    JOptionPane.showMessageDialog(this, "An error has occured.\n"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		}
		unsaved=true;
		JOptionPane.showMessageDialog(this, "Survey Complete!", "Complete", JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	else if(ae.getActionCommand().equals("save")){
	    int ret=fc.showSaveDialog(this);
	    if(ret==JFileChooser.APPROVE_OPTION){
		File file=fc.getSelectedFile();
		if(!file.getName().endsWith(".zip"))
		    file=new File(file.getAbsolutePath()+".zip");
		if(file.exists()){
		    ret=JOptionPane.showConfirmDialog(this, "The file you selected already exists.\nDo you want to overwrite the file?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		    if(ret==JOptionPane.YES_OPTION){
			saveFile(file);
		    }
		}
		else{
		    saveFile(file);
		}
	    }
	}
	else if(ae.getActionCommand().equals("view")){
	    try {
		if(path==null){
		    path=Files.createTempDirectory(Paths.get(UI.LOC), null);
		    path.toFile().deleteOnExit();
		}
		
		int read = 0;
		byte[] bytes = new byte[1024];
		InputStream is;
		FileOutputStream fos;
		File file;
		
		is=dh.generateReport(input, output);
		file=new File(path.toString()+File.separator+"report.html");
		fos=new FileOutputStream(file);
		while ((read=is.read(bytes))!=-1) {
		    fos.write(bytes, 0, read);
		}
		fos.close();
		file.deleteOnExit();
		
		is=assesser.getGraphImage();
		file=new File(path.toString()+File.separator+"bar_diagram.png");
		fos=new FileOutputStream(file);
		while ((read=is.read(bytes))!=-1) {
		    fos.write(bytes, 0, read);
		}
		fos.close();
		file.deleteOnExit();
		
		is=java.lang.ClassLoader.getSystemResourceAsStream("com/agnibho/code/resources/style.css");
		file=new File(path.toString()+File.separator+"style.css");
		fos=new FileOutputStream(file);
		while ((read=is.read(bytes))!=-1) {
		    fos.write(bytes, 0, read);
		}
		fos.close();
		file.deleteOnExit();

		Desktop.getDesktop().open(new File(path.toString()+File.separator+"report.html"));
	    } catch (IOException | TransformerException e) {
		e.printStackTrace();
	    }
	}
	else if(ae.getActionCommand().equals("reset")){
	    try {
		int ret;
		if(unsaved){
		    ret=JOptionPane.showConfirmDialog(this, "Restarting survey may result in loss of unsaved data.\nDo you want to restart the survey?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		    if(ret!=JFileChooser.APPROVE_OPTION)
			return;
		}
		id=new IdPanel();
		layout.replace(result, id);
		layout.replace(save, prev);
		prev.setEnabled(false);
		layout.replace(view, next);
		next.setEnabled(true);
		layout.replace(reset, finish);
		finish.setEnabled(false);
		customEdit.setVisible(true);
		food=new FoodPanel();
		result=new ResultPanel();
		
		input=dh.getDocInstance();
		node=input;
		node.appendChild(input.createElement("record"));
		node.getFirstChild().appendChild(input.createElement("id"));
		node.getFirstChild().appendChild(input.createElement("intake"));
	    } catch (TransformerConfigurationException | XPathExpressionException | SAXException | IOException | ParserConfigurationException e) {
		JOptionPane.showMessageDialog(this, "An error has occured.\n"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	    }
	}
	else if(ae.getActionCommand().equals("help")){
	    if(helpFrame.isVisible())
		helpFrame.toFront();
	    else
		helpFrame.setVisible(true);
	}
	else if(ae.getActionCommand().equals("customEdit"))
	    custom.setVisible(true);
    }



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable(){
	    public void run(){
		try{
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception ex){
		    JOptionPane.showMessageDialog(null, "An error has occured.\n"+ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		    ex.printStackTrace();
		}
		try {
		    new UI().setVisible(true);
		} catch (JAXBException | SAXException | ParserConfigurationException | XPathExpressionException | IOException | TransformerException e) {
		    JOptionPane.showMessageDialog(null, "An error has occured.\n"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		}
	    }
	});
    }
}
