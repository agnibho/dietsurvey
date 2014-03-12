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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

@SuppressWarnings("serial")
public class IdPanel extends JPanel implements ActionListener {
    private GroupLayout layout;
    
    private JPanel multiPanel;
    private JScrollPane multiScroll;
    private GroupLayout multiLayout;

    private JLabel hofLabel;
    private JTextField hofField;
    private JLabel dateLabel;
    private JTextField dateField;
    private JButton add;
    private JButton remove;
    private List<Member> members;

    public IdPanel() {
	hofLabel=new JLabel("<html><h4>Enter the name of Head of Family:</h4></html>", JLabel.TRAILING);
	hofField=new JTextField();
	hofField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	dateLabel=new JLabel("<html><h4>Enter the date:</h4></html>", JLabel.TRAILING);
	dateField=new JTextField();
	dateField.setText("dd-mm-yyyy");
	dateField.addFocusListener(new java.awt.event.FocusAdapter() {
	    public void focusGained(java.awt.event.FocusEvent evt) {
		SwingUtilities.invokeLater( new Runnable() {
		    @Override
		    public void run() {
			dateField.selectAll();		
		    }
		});
	    }
	});
	dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	add=new JButton("Add Family Member");
	add.setActionCommand("add");
	add.addActionListener(this);
	remove=new JButton("Remove Selected Members");
	remove.setActionCommand("remove");
	remove.addActionListener(this);
	
	multiPanel=new JPanel();
	multiScroll=new JScrollPane(multiPanel);
	
	members=new ArrayList<Member>();
	members.add(new Member());
	refreshLayout();
    }
    private void refreshLayout(){
	this.removeAll();
	multiPanel.removeAll();
	
	layout=new GroupLayout(this);
	this.setLayout(layout);
	layout.setAutoCreateGaps(true);
	layout.setAutoCreateContainerGaps(true);
	
	multiLayout=new GroupLayout(multiPanel);
	multiPanel.setLayout(multiLayout);
	multiLayout.setAutoCreateGaps(true);
	multiLayout.setAutoCreateContainerGaps(true);
	
	GroupLayout.ParallelGroup pg=multiLayout.createParallelGroup();
	GroupLayout.SequentialGroup sg=multiLayout.createSequentialGroup();
	
	for(int i=0; i<members.size(); i++){
	    members.get(i).updateNum(i+1);
	    pg.addComponent(members.get(i));
	    sg.addComponent(members.get(i));
	}
	sg.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE);
	multiLayout.setVerticalGroup(sg);
	multiLayout.setHorizontalGroup(pg);
	
	layout.setVerticalGroup(layout.createSequentialGroup()
		.addGroup(layout.createParallelGroup()
			.addComponent(hofLabel)
			.addComponent(hofField))
			.addGroup(layout.createParallelGroup()
				.addComponent(dateLabel)
				.addComponent(dateField))
				.addGroup(layout.createParallelGroup()
					.addComponent(add)
					.addComponent(remove))
					.addComponent(multiScroll));
	layout.setHorizontalGroup(layout.createParallelGroup()
		.addGroup(layout.createSequentialGroup()
			.addComponent(hofLabel)
			.addComponent(hofField))
			.addGroup(layout.createSequentialGroup()
				.addComponent(dateLabel)
				.addComponent(dateField))
				.addGroup(layout.createSequentialGroup()
					.addComponent(add)
					.addComponent(remove))
					.addComponent(multiScroll));
	layout.linkSize(hofLabel, dateLabel);
    }
    private void addMember(){
	members.add(new Member());
	refreshLayout();
    }
    private void removeMember(){
	int i=0;
	while(i<members.size()){
	    if(members.get(i).isSelected())
		members.remove(i);
	    else
		i++;
	}
	refreshLayout();
    }
    private boolean checkDate(String date){
	date=date.trim();
	String[] parts;
	if(date.contains("/"))
	    parts=date.split("/");
	else if(date.contains("."))
	    parts=date.split(".");
	else if(date.contains("-"))
	    parts=date.split("-");
	else
	    return false;
	if(parts.length!=3)
	    return false;
	try{
	    if(Integer.parseInt(parts[0])<1||Integer.parseInt(parts[0])>31)
		return false;
	    if(Integer.parseInt(parts[1])<1||Integer.parseInt(parts[1])>12)
		return false;
	    if(!((Integer.parseInt(parts[2])>=1950&&Integer.parseInt(parts[2])<=2050)||(Integer.parseInt(parts[2])>=0||Integer.parseInt(parts[2])<=99)))
		return false;
	}catch(NumberFormatException e){
	    return false;
	}
	return true;
    }
    public boolean isComplete(){
	if(hofField.getText().toString().trim().length()==0){
	    JOptionPane.showMessageDialog(this, "Please enter the name of Head of Family", "Error", JOptionPane.ERROR_MESSAGE);
	    return false;
	}
	if(!checkDate(dateField.getText())){
	    JOptionPane.showMessageDialog(this, "Invalid date.", "Error", JOptionPane.ERROR_MESSAGE);
	    return false;
	}
	if(members.size()==0){
	    JOptionPane.showMessageDialog(this, "No member was found.", "Error", JOptionPane.ERROR_MESSAGE);
	    return false;
	}
	for(int i=0; i<members.size(); i++){
	    if(!members.get(i).isComplete())
		return false;
	}
	return true;
    }
    public Document copyToDoc(Document doc) throws XPathExpressionException, ParserConfigurationException{
	XPath xp=DocHandler.getXpInstance();
	Node node=(Node) xp.evaluate("/record/id", doc, XPathConstants.NODE);
	node.appendChild(doc.createElement("hof"));
	node.getFirstChild().appendChild(doc.createTextNode(hofField.getText().toString().trim()));
	node.appendChild(doc.createElement("date"));
	if(checkDate(dateField.getText()))
	    node.getLastChild().appendChild(doc.createTextNode(dateField.getText().toString().trim()));
	for(int i=0; i<members.size(); i++){
	    doc=members.get(i).copyToDoc(doc);
	}
	return doc;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	if(ae.getActionCommand().equals("add"))
	    addMember();
	else if(ae.getActionCommand().equals("remove"))
	    removeMember();
    }
    
    private class Member extends JPanel implements ActionListener {
	GroupLayout layout;

	JCheckBox selection;
	JLabel nameLabel;
	JTextField nameField;
	JLabel ageLabel;
	JTextField ageField;
	JLabel sexLabel;
	ButtonGroup sex;
	JRadioButton male;
	JRadioButton female;
	JLabel jobLabel;
	JComboBox<String> jobBox;
	JLabel pregLabel;
	ButtonGroup preg;
	JRadioButton yesP;
	JRadioButton noP;
	JLabel lacLabel;
	ButtonGroup lac;
	JRadioButton yesLbelow6;
	JRadioButton yesLabove6;
	JRadioButton noL;

	Member(){
	    selection=new JCheckBox();
	    
	    nameLabel=new JLabel("<html><b>Enter name:</b></html>");
	    nameLabel.setPreferredSize(new Dimension(120, 20));
	    nameField=new JTextField(30);
	    nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	    
	    ageLabel=new JLabel("<html><b>Enter age:</b></html>");
	    ageField=new JTextField();
	    ageField.setActionCommand("check");
	    ageField.addActionListener(this);
	    ageField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	    
	    sexLabel=new JLabel("<html><b>Enter Sex:</b></html>");
	    male=new JRadioButton("Male");
	    male.setActionCommand("check");
	    male.addActionListener(this);
	    female=new JRadioButton("Female");
	    female.setActionCommand("check");
	    female.addActionListener(this);
	    sex=new ButtonGroup();
	    sex.add(male);
	    sex.add(female);
	    
	    jobLabel=new JLabel("<html><b>Enter Work Type:</b></html>");
	    jobLabel.setVisible(false);
	    String[] jobCat={"Sedentary", "Moderate", "Heavy"};
	    jobBox=new JComboBox<String>(jobCat);
	    jobBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	    jobBox.setVisible(false);
	    
	    pregLabel=new JLabel("<html><b>Is she pregnant?</b></html>");
	    pregLabel.setVisible(false);
	    yesP=new JRadioButton("Yes");
	    yesP.setActionCommand("check");
	    yesP.addActionListener(this);
	    yesP.setVisible(false);
	    noP=new JRadioButton("No");
	    noP.setVisible(false);
	    preg=new ButtonGroup();
	    preg.add(yesP);
	    preg.add(noP);
	    
	    lacLabel=new JLabel("<html><b>Is she lactating?</b></html>");
	    lacLabel.setVisible(false);
	    yesLbelow6=new JRadioButton("Yes, below 6 months");
	    yesLbelow6.setActionCommand("check");
	    yesLbelow6.addActionListener(this);
	    yesLbelow6.setVisible(false);
	    yesLabove6=new JRadioButton("Yes, above 6 months");
	    yesLabove6.setActionCommand("check");
	    yesLabove6.addActionListener(this);
	    yesLabove6.setVisible(false);
	    noL=new JRadioButton("No");
	    noL.setVisible(false);
	    lac=new ButtonGroup();
	    lac.add(yesLbelow6);
	    lac.add(yesLabove6);
	    lac.add(noL);

	    layout=new GroupLayout(this);
	    this.setLayout(layout);
	    layout.setAutoCreateGaps(true);
	    layout.setAutoCreateContainerGaps(true);
	    layout.setVerticalGroup(
		    layout.createSequentialGroup()
		    .addComponent(selection)
		    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
		    .addGroup(layout.createParallelGroup()
			    .addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			    .addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
			    .addGroup(layout.createParallelGroup()
				    .addComponent(ageLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(ageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				    .addGroup(layout.createParallelGroup()
					    .addComponent(sexLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					    .addComponent(male, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					    .addComponent(female, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					    .addGroup(layout.createParallelGroup()
						    .addComponent(jobLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						    .addComponent(jobBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						    .addGroup(layout.createParallelGroup()
							    .addComponent(pregLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							    .addComponent(yesP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							    .addComponent(noP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							    .addGroup(layout.createParallelGroup()
								    .addComponent(lacLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								    .addComponent(yesLbelow6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								    .addComponent(yesLabove6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								    .addComponent(noL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								    //.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		    );
	    layout.setHorizontalGroup(
		    layout.createParallelGroup()
		    .addComponent(selection)
		    .addGroup(layout.createSequentialGroup()
			    .addComponent(nameLabel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			    .addComponent(nameField, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			    .addGroup(layout.createSequentialGroup()
				    .addComponent(ageLabel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				    .addComponent(ageField, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				    .addGroup(layout.createSequentialGroup()
					    .addComponent(sexLabel)
					    .addComponent(male)
					    .addComponent(female))
					    .addGroup(layout.createSequentialGroup()
						    .addComponent(jobLabel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						    .addComponent(jobBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						    .addGroup(layout.createSequentialGroup()
							    .addComponent(pregLabel)
							    .addComponent(yesP)
							    .addComponent(noP))
							    .addGroup(layout.createSequentialGroup()
								    .addComponent(lacLabel)
								    .addComponent(yesLbelow6)
								    .addComponent(yesLabove6)
								    .addComponent(noL)));
	    layout.linkSize(nameLabel, ageLabel, sexLabel, jobLabel, pregLabel, lacLabel);
	    this.setBorder(BorderFactory.createLineBorder(Color.gray));
	}
	
	public void updateNum(int num){
	    selection.setText("<html><h4>Member "+String.valueOf(num)+"</h4></html>");
	}
	
	public boolean isSelected(){
	    return selection.isSelected();
	}
	
	public void check(){
	    if(!ageField.getText().toString().trim().endsWith("m")){
		try{
		    if(Integer.parseInt(ageField.getText().toString().trim())>=18){
			jobLabel.setVisible(true);
			jobBox.setVisible(true);
		    }
		    else{
			jobLabel.setVisible(false);
			jobBox.setVisible(false);
			pregLabel.setVisible(false);
			yesP.setVisible(false);
			noP.setVisible(false);
			lacLabel.setVisible(false);
			yesLbelow6.setVisible(false);
			yesLabove6.setVisible(false);
			noL.setVisible(false);
		    }
		    if(female.isSelected()&&Integer.parseInt(ageField.getText().toString().trim())>=18&&Integer.parseInt(ageField.getText().toString().trim())<=45){
			pregLabel.setVisible(true);
			yesP.setVisible(true);
			noP.setVisible(true);
			lacLabel.setVisible(true);
			yesLbelow6.setVisible(true);
			yesLabove6.setVisible(true);
			noL.setVisible(true);
		    }
		    else{
			pregLabel.setVisible(false);
			yesP.setVisible(false);
			noP.setVisible(false);
			lacLabel.setVisible(false);
			yesLbelow6.setVisible(false);
			yesLabove6.setVisible(false);
			noL.setVisible(false);
		    }
		    if(yesP.isSelected())
			noL.setSelected(true);
		    else if(yesLbelow6.isSelected()||yesLabove6.isSelected())
			noP.setSelected(true);
		}catch(NumberFormatException e){
		    
		}
	    }
	    else{
		jobLabel.setVisible(false);
		jobBox.setVisible(false);
		pregLabel.setVisible(false);
		yesP.setVisible(false);
		noP.setVisible(false);
		lacLabel.setVisible(false);
		yesLbelow6.setVisible(false);
		yesLabove6.setVisible(false);
		noL.setVisible(false);
	    }
	}
	
	public boolean isComplete(){
	    String age=ageField.getText().toString().trim();
	    try{
		if(Integer.parseInt(age)>=18 && !jobBox.isVisible()){
		    JOptionPane.showMessageDialog(this, "Please enter the work type.", "Error", JOptionPane.ERROR_MESSAGE);
		    check();
		    return false;
		}
		if(age.endsWith("m")){
		    if(Integer.parseInt(age.substring(0, age.length()-1).trim())<0 && Integer.parseInt(age.substring(0, age.length()-2).trim())>11){
			JOptionPane.showMessageDialog(this, "Invalid age. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		    }
		}
		else{
		    if(Integer.parseInt(age)<1){
			JOptionPane.showMessageDialog(this, "Invalid age. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		    }
		}
	    }catch(NumberFormatException e){
		JOptionPane.showMessageDialog(this, "Invalid age. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
		return false;
	    }
	    
	    check();
	    
	    if(!(male.isSelected()||female.isSelected())){
		JOptionPane.showMessageDialog(this, "Please enter sex", "Error", JOptionPane.ERROR_MESSAGE);
		return false;
	    }
	    if(pregLabel.isVisible()&&!(yesP.isSelected()||noP.isSelected())){
		JOptionPane.showMessageDialog(this, "Please enter pregnancy status.", "Error", JOptionPane.ERROR_MESSAGE);
		return false;
	    }
	    if(lacLabel.isVisible()&&!(yesLbelow6.isSelected()||yesLabove6.isSelected()||noL.isSelected())){
		JOptionPane.showMessageDialog(this, "Please enter lactation status", "Error", JOptionPane.ERROR_MESSAGE);
		return false;
	    }
	    return true;
	}
	
	public Document copyToDoc(Document doc) throws XPathExpressionException{
	    XPath xp=DocHandler.getXpInstance();
	    Node node=(Node) xp.evaluate("/record/id", doc, XPathConstants.NODE);
	    node.appendChild(doc.createElement("member"));
	    node=node.getLastChild();
	    if(nameField.getText().toString().trim().length()>0){
		node.appendChild(doc.createElement("name"));
		node.getLastChild().appendChild(doc.createTextNode(nameField.getText().toString().trim()));
	    }
	    
	    String age=ageField.getText().toString().trim();
	    if(age.endsWith("m")){
		node.appendChild(doc.createElement("infant"));
		node.getLastChild().appendChild(doc.createTextNode(age.substring(0, age.length()-1).trim()));
	    }
	    else{
		node.appendChild(doc.createElement("age"));
		node.getLastChild().appendChild(doc.createTextNode(age));
	    }
	    node.appendChild(doc.createElement("sex"));
	    if(male.isSelected())
		node.getLastChild().appendChild(doc.createTextNode("male"));
	    else
		node.getLastChild().appendChild(doc.createTextNode("female"));
	    if(jobBox.isVisible()){
		node.appendChild(doc.createElement("occupation"));
		node.getLastChild().appendChild(doc.createTextNode(jobBox.getSelectedItem().toString().trim().toLowerCase()));
	    }
	    if(pregLabel.isVisible()){
		node.appendChild(doc.createElement("pregnant"));
		if(yesP.isSelected())
		    node.getLastChild().appendChild(doc.createTextNode("true"));
		else
		    node.getLastChild().appendChild(doc.createTextNode("false"));
	    }
	    if(lacLabel.isVisible()){
		node.appendChild(doc.createElement("lactating"));
		if(yesLbelow6.isSelected()||yesLabove6.isSelected()){
		    node.getLastChild().appendChild(doc.createTextNode("true"));
		    node.appendChild(doc.createElement("beyond6m"));
		    if(yesLabove6.isSelected())
			node.getLastChild().appendChild(doc.createTextNode("true"));
		    else
			node.getLastChild().appendChild(doc.createTextNode("false"));
		}
		else
		    node.getLastChild().appendChild(doc.createTextNode("false"));
	    }
	    return doc;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
	    if(ae.getActionCommand().equals("check")){
		check();
	    }
	}
    }
}
