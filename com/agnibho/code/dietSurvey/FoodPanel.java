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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class FoodPanel extends JPanel implements ActionListener {
    private GroupLayout layout;
    
    private JPanel multiPanel;
    private JScrollPane multiScroll;
    private GroupLayout multiLayout;

    private JLabel label;
    private JButton add;
    private JButton remove;
    private List<Food> foods;
    
    private XPath xp=DocHandler.getXpInstance();
    Document availableFoods;
    Document customFoods;
    List<String> foodList;
    
    public FoodPanel() throws TransformerConfigurationException, SAXException, IOException, ParserConfigurationException, XPathExpressionException {
	foods=new ArrayList<Food>();
	
	label=new JLabel("<html><h4>Enter list of foods consumed in last 24 hours:</h4></html>");
	add=new JButton("Add Food");
	add.setActionCommand("add");
	add.addActionListener(this);
	
	remove=new JButton("Remove selected foods");
	remove.setActionCommand("remove");
	remove.addActionListener(this);
	
	multiPanel=new JPanel();
	multiScroll=new JScrollPane(multiPanel);
	
	loadData();
	foods.add(new Food());
	
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
	for(int i=0; i<foods.size(); i++){
	    foods.get(i).updateNum(i+1);
	    pg.addComponent(foods.get(i));
	    sg.addComponent(foods.get(i));
	}
	
	multiLayout.setVerticalGroup(sg);
	multiLayout.setHorizontalGroup(pg);
	
	layout.setVerticalGroup(layout.createSequentialGroup()
		.addComponent(label)
		.addGroup(layout.createParallelGroup()
			.addComponent(add)
			.addComponent(remove))
			.addComponent(multiScroll));
	layout.setHorizontalGroup(layout.createParallelGroup()
		.addComponent(label)
		.addGroup(layout.createSequentialGroup()
			.addComponent(add)
			.addComponent(remove))
			.addComponent(multiScroll));
    }
    private void addFood(){
	foods.add(new Food());
	refreshLayout();
    }
    private void removeFood(){
	int i=0;
	while(i<foods.size()){
	    if(foods.get(i).isSelected())
		foods.remove(i);
	    else
		i++;
	}
	refreshLayout();
    }
    private void combine() throws XPathExpressionException{
	NodeList custom=(NodeList) xp.evaluate("foodList/food", customFoods, XPathConstants.NODESET);
	Node del;
	for(int i=0; i<custom.getLength(); i++){
	    System.out.println(xp.evaluate("name", custom.item(i)));
	    if((boolean) xp.evaluate("/foodList/food[name='"+xp.evaluate("name", custom.item(i))+"']", availableFoods, XPathConstants.BOOLEAN)){
		del=(Node) xp.evaluate("/foodList/food[name='"+xp.evaluate("name", custom.item(i))+"']", availableFoods, XPathConstants.NODE);
		del.getParentNode().removeChild(del);
	    }
	    availableFoods.getLastChild().appendChild(availableFoods.importNode(custom.item(i), true));
	}
    }
    public void loadData() throws TransformerConfigurationException, SAXException, IOException, ParserConfigurationException, XPathExpressionException{
	availableFoods=new DocHandler().getDocument(java.lang.ClassLoader.getSystemResourceAsStream("com/agnibho/code/resources/food.xml"));
	try{
	    customFoods=new DocHandler().getValidatedDocument(new File(UI.LOC+File.separator+"custom_food.xml"), java.lang.ClassLoader.getSystemResourceAsStream("com/agnibho/code/resources/customFood.xsd"));
	    combine();
	}catch(IOException | TransformerConfigurationException | SAXException | ParserConfigurationException | XPathExpressionException e){
	    System.out.println(e);
	}
	NodeList nl;
	nl=(NodeList) xp.evaluate("/foodList/food", availableFoods, XPathConstants.NODESET);
	foodList=new ArrayList<String>();
	for(int i=0; i<nl.getLength(); i++){
	    foodList.add(xp.evaluate("name", nl.item(i)));
	}
	Collections.sort(foodList);

	for(int i=0; i<foods.size(); i++){
	    foods.get(i).updateBox();
	}
	refreshLayout();
    }
    public boolean isComplete(){
	if(foods.size()==0){
	    JOptionPane.showMessageDialog(this, "No food was found.", "Error", JOptionPane.ERROR_MESSAGE);
	    return false;
	}
	for(int i=0; i<foods.size(); i++){
	    if(!foods.get(i).isComplete()){
		return false;
	    }
	}
	return true;
    }
    public Document copyToDoc(Document doc) throws XPathExpressionException{
	for(int i=0; i<foods.size(); i++){
	    doc=foods.get(i).copyToDoc(doc);
	}
	return doc;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	if(ae.getActionCommand().equals("add")){
	    addFood();
	}
	else if(ae.getActionCommand().equals("remove")){
	    removeFood();
	}
    }

    private class Food extends JPanel {
	GroupLayout layout;
	
	JCheckBox selection;
	JLabel nameLabel;
	JComboBox<String> names;
	JLabel amountLabel;
	JTextField amount;
	
	public Food(){
	    selection=new JCheckBox("Select this food");
	    nameLabel=new JLabel("<html><b>Enter name of food:</b></html>");
	    names=new JComboBox<String>();
	    for(int i=0; i<foodList.size(); i++)
		names.addItem(foodList.get(i));
	    names.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	    amountLabel=new JLabel("<html><b>Enter consumed amount:<br>(in gram or ml)</b></html>");
	    amount=new JTextField();
	    amount.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

	    layout=new GroupLayout(this);
	    this.setLayout(layout);
	    layout.setAutoCreateGaps(true);
	    layout.setAutoCreateContainerGaps(true);
	    layout.setVerticalGroup(layout.createSequentialGroup()
		    .addComponent(selection)
		    .addGroup(layout.createParallelGroup()
			    .addComponent(nameLabel)
			    .addComponent(names))
			    .addGroup(layout.createParallelGroup()
				    .addComponent(amountLabel)
				    .addComponent(amount)));
	    layout.setHorizontalGroup(layout.createParallelGroup()
		    .addComponent(selection)
		    .addGroup(layout.createSequentialGroup()
			    .addComponent(nameLabel)
			    .addComponent(names))
			    .addGroup(layout.createSequentialGroup()
				    .addComponent(amountLabel)
				    .addComponent(amount)));
	    layout.linkSize(nameLabel, amountLabel);
	    this.setBorder(BorderFactory.createLineBorder(Color.gray));
	}
	public void updateNum(int num){
	    selection.setText("<html><h4>Food "+String.valueOf(num));
	}
	public boolean isSelected(){
	    return selection.isSelected();
	}
	public void updateBox(){
	    names.removeAllItems();
	    for(int i=0; i<foodList.size(); i++)
		names.addItem(foodList.get(i));
	}
	public boolean isComplete(){
	    if(names.getSelectedItem().toString().trim().length()==0){
		JOptionPane.showMessageDialog(this, "Please enter valid name of food", "Error", JOptionPane.ERROR_MESSAGE);
		return false;
	    }
	    try{
		if(Double.parseDouble(amount.getText().trim())<0){
		    JOptionPane.showMessageDialog(this, "Please enter valid amount of food", "Error", JOptionPane.ERROR_MESSAGE);
		    return false;
		}
	    }catch(NumberFormatException e){
		JOptionPane.showMessageDialog(this, "Please enter valid amount of food", "Error", JOptionPane.ERROR_MESSAGE);
		return false;
	    }
	    return true;
	}
	public Document copyToDoc(Document doc) throws XPathExpressionException{
	    Node node=(Node) xp.evaluate("/record/intake", doc, XPathConstants.NODE);
	    node.appendChild(doc.createElement("food"));
	    node=node.getLastChild();
	    node.appendChild(doc.createElement("name"));
	    node.getLastChild().appendChild(doc.createTextNode(names.getSelectedItem().toString().toString()));
	    node.appendChild(doc.createElement("amount"));
	    node.getLastChild().appendChild(doc.createTextNode(amount.getText().toString().trim()));
	    return doc;
	}
    }
}
