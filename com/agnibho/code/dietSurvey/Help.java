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

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class Help extends JFrame implements ListSelectionListener {
    private static Help instance=null;

    private JSplitPane splitPane;
    private JList<String> list;
    private JScrollPane listScroll;
    private String[] index;
    private JEditorPane show;
    private JScrollPane showScroll;
    private String[] content;
    
    public static JFrame getInstance(){
	if(instance==null){
	    instance=new Help();
	}
	return instance;
    }
    
    private Help(){
	setup();
	
	list = new JList<String>(index);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        
        listScroll=new JScrollPane(list);
        
        show=new JEditorPane();
	show.setEditable(false);
	show.setContentType("text/html");
	show.addHyperlinkListener(link);
	show.setText(content[0]);
	
	showScroll=new JScrollPane(show);
	showScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	showScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	showScroll.setPreferredSize(new Dimension(300, 400));

	splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, showScroll);
	splitPane.setOneTouchExpandable(true);
	splitPane.setContinuousLayout(true);
	splitPane.setDividerLocation(150);

	Dimension minimumSize = new Dimension(50, 100);
	listScroll.setMinimumSize(minimumSize);
	showScroll.setMinimumSize(minimumSize);

	splitPane.setPreferredSize(new Dimension(800, 400));
	
	this.getContentPane().add(splitPane);
	
	setTitle("Help");
	pack();
	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	setLocationRelativeTo(null);
    }
    
    public void valueChanged(ListSelectionEvent e) {
        if(list.getSelectedIndex()==4){
            if(Update.currentStatus.equals(Update.Status.NOT_CHECKED)){
        	show.setText(content[4]+"<h2 align='center'>Checking for update...</h2></html>");
        	SwingWorker<Void, Void> worker=new SwingWorker<Void, Void>() {
        	    @Override
        	    protected Void doInBackground() throws Exception {
        		Update.checkUpdate();
        		return null;
        	    }
        	    @Override
        	    protected void done(){
        		if(list.getSelectedIndex()==4){
        		    if(Update.currentStatus.equals(Update.Status.NEW_VERSION_AVAILABLE))
        			show.setText(content[4]+"<h2 align='center'>A new version is available</h2></html>");
        		    else if(Update.currentStatus.equals(Update.Status.UP_TO_DATE))
        			show.setText(content[4]+"<h2 align='center'>The software is up to date.</h2></html>");
        		}
        	    }
        	};
        	worker.execute();
            }
            else if(Update.currentStatus.equals(Update.Status.NEW_VERSION_AVAILABLE))
        	show.setText(content[4]+"<h2 align='center'>A new version is available</h2></html>");
            else if(Update.currentStatus.equals(Update.Status.UP_TO_DATE))
		show.setText(content[4]+"<h2 align='center'>The software is up to date.</h2></html>");
        }
        else
            updateContent(list.getSelectedIndex());
    }
    
    private void updateContent (int id) {
        show.setText(content[id]);
        show.setCaretPosition(0);
    }
    
    public JSplitPane getSplitPane() {
        return splitPane;
    }
    
    HyperlinkListener link = new HyperlinkListener() {
	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
	    if (HyperlinkEvent.EventType.ACTIVATED == event.getEventType()) {
		try {
		    Desktop.getDesktop().browse(event.getURL().toURI());
		} catch (IOException | URISyntaxException e) {
		    e.printStackTrace();
		}
	    }
	}
    };
    
    private void setup(){
	index=new String[6];
	content=new String[6];
	
	index[0]="Introduction";
	content[0]="<html>"
		+"<h1 align='center'>Diet Survey</h1>"
		+"<h2 align='center'>Diet survey based on 24 hours recall</h2>"
		+"<p align='center'>Calculate daily requirement and nutritional status of a family based on food consumption in last 24 hours.</p>"
		+"</html>";
	index[1]="Survey";
	content[1]="<html>"
		+"<h3>Follow the steps to complete the survey</h3>"
		+"<ol>"
		+"<li>Enter the name of head of the family. It is used to identify the family.</li>"
		+"<li>Enter the date of survey.</li>"
		+"<li>Enter details of all the family members. One member field is provided by default. Use the 'Add member' and 'Remove member' button to add or remove famiy members respectively. The check box at the top right corner of each member entry can be used to select the respective entry. Enter the age in years, to enter age below 1 year enter the age in months followed by the letter 'm'.</li>"
		+"<li>Click 'Next Section' to enter the name of foods consumed in last 24 hours. If necessary you can come back to the first page by clicking 'Previous Section' button</li>"
		+"<li>Enter details for each food consumed in last 24 hours. Use 'Add food' and 'Remove food' to add or delete food items respectively. The check box at the top right corner of each member entry can be used to select the respective entry.</li>"
		+"<li>Click 'Finish' to get the result of the survey. only the summary is shown on the screen</li>"
		+"<li>Click 'View full Report' to open the detailed report in a web browser.</li>"
		+"<li>To save the detailed report click on 'Save' button and select the folder and a filename. The file will be saved as a zip archive.</li>"
		+"<li>The 'report.html' file inside the zip archive can be opened in a web browser to view the full report.</li>"
		+"</ol>"
		+"</html>";
	
	index[2]="Custom Food List";
	content[2]="<html>"
		+"<h3>To enter your own food list follow the steps as below</h3>"
		+"<ol>"
		+"<li>At the family panel (where the information about the family memebers are entered) click on the 'Edit' button</li>"
		+"<li>The food editor panel will open up and it will load any existing custom food if present.</li>"
		+"<li>To enter new food select '***Add new food item' from the combo box and enter food details. Click 'Save' to save the new food.</li>"
		+"<li>To modify existing food select the food name from the combo box and modify the values of nutrients. Click 'Save' to save the changes.</li>"
		+"<li>Use the 'Remove' button to remove the selected food in the combo box</li>"
		+"</ol>"
		+"<h3>More about custom food</h3>"
		+"<ul>"
		+"<li>The custom foods are saved in the 'custom_food.xml' file in the same folder as this application.</li>"
		+"<li>The 'custom_food.xml' file can be modified directly but it must be in the proper format (valid against the schema)</li>"
		+"<li>The entries in the 'custom_food.xml' overrides the default foods (If the file contains an entry with the same name as a default food, the entry in the 'custom_food.xml' will be used)</li>"
		+"</html>";
	
	index[3]="References";
	content[3]="<html>"
		+"<dl>"
		+"<dt><h3>For Recommended Dietary Allowance (RDA)</h3></dt>"
		+"<dd>NUTRIENT REQUIREMENTS AND RECOMMENDED DIETARY ALLOWANCES FOR INDIANS- A Report of the Expert Group of the Indian Council of Medical Research 2009, NATIONAL INSTITUTE OF NUTRITION, Indian Council of Medical Research</dd>"
		+"<dt><h3>For nutrient contents of foods</h3></dt>"
		+"<dd>Nutritive Value of Indian Foods, ICMR, 1989</dd>"
		+"</dl>"
		+"</html>";
	index[4]="Update";
	content[4]="<html>"
		+"<h1 align='center'>Diet Survey</h1>"
		+"<h2 align='center'>Version: "+Update.VERSION+"</h2>"
		+"<h3 align='center'>For latest updates visit <a href='http://code.agnibho.com/'>http://code.agnibho.com/</a>";
	index[5]="About";
	content[5]="<html>"
		+"<h2>Developed by:</h2>"
		+"<h1>Agnibho Mondal</h1>"
		+"<h3><a href='http://www.agnibho.com/'>www.agnibho.com</a></h3>"
		+"<h3><a href='http://code.agnibho.com/'>Visit page to view source code</a></h3>"
		+"<hr>"
		+"Diet-Survey Copyright (C) 2013  Agnibho Mondal<br>"
		+"This program comes with ABSOLUTELY NO WARRANTY<br>"
		+"This is free software, and you are welcome to redistribute it under certain conditions"
		+"</html>";
    }
}
