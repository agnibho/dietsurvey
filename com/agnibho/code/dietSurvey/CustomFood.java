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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class CustomFood extends JFrame implements ActionListener {
    private static final String newFood="***Add New Food Item";

    GroupLayout layout;

    File file;
    DocHandler dh;
    Document doc;
    XPath xp=DocHandler.getXpInstance();
    Node node;

    JLabel heading;
    JComboBox<String> listBox;
    JButton save;
    JButton remove;
    JLabel nameLabel;
    JTextField nameField;
    JLabel groupLabel;
    JComboBox<String> groupBox;
    JLabel energyLabel;
    JTextField energyField;
    JLabel proteinLabel;
    JTextField proteinField;
    JLabel ironLabel;
    JTextField ironField;
    JLabel vitALabel;
    JTextField vitAField;
    JLabel thiamineLabel;
    JTextField thiamineField;
    JLabel riboflavinLabel;
    JTextField riboflavinField;
    JLabel vitCLabel;
    JTextField vitCField;

    String[] foodList;
    String[] groups={"Cereals", "Pulses", "Green leafy vegetables", "Roots and tubers", "Other vegetatables",
	    "Fruits", "Milk and milk products", "Flesh foods", "Fats and oils", "Sugar and jaggery", "Nuts and oilseeds", "Spices and condiments",
	    "Miscellaneous"};

    public CustomFood() throws TransformerConfigurationException, ParserConfigurationException, SAXException, IOException, XPathExpressionException {
	file=new File(UI.LOC+File.separator+"custom_food.xml");
	dh=new DocHandler();
	if(file.exists())
	    doc=new DocHandler().getValidatedDocument(new File(UI.LOC+File.separator+"custom_food.xml"), java.lang.ClassLoader.getSystemResourceAsStream("com/agnibho/code/resources/customFood.xsd"));
	else{
	    doc=dh.getDocInstance();
	    doc.appendChild(doc.createElement("foodList"));
	}
	NodeList nl=(NodeList) xp.evaluate("/foodList/food", doc, XPathConstants.NODESET);
	foodList=new String[nl.getLength()+1];
	foodList[0]=newFood;
	for(int i=0; i<nl.getLength(); i++){
	    foodList[i+1]=xp.evaluate("name", nl.item(i));
	}


	listBox=new JComboBox<String>(foodList);
	listBox.setActionCommand("select");
	listBox.addActionListener(this);
	listBox.setPreferredSize(new Dimension(100, 40));

	save=new JButton("Save");
	save.setActionCommand("save");
	save.addActionListener(this);

	remove=new JButton("Remove");
	remove.setEnabled(false);
	remove.setActionCommand("remove");
	remove.addActionListener(this);

	heading=new JLabel("<html><h2>Enter Food Details</h2></html>", SwingConstants.CENTER);
	heading.setPreferredSize(new Dimension(200, 40));

	nameLabel=new JLabel("<html><b>Enter Name:</b></html>");
	nameLabel.setPreferredSize(new Dimension(200, 20));
	nameField=new JTextField(30);
	nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

	groupLabel=new JLabel("<html><b>Food Group:</b></html>");
	nameLabel.setPreferredSize(new Dimension(200, 20));
	groupBox=new JComboBox<String>(groups);
	groupBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

	energyLabel=new JLabel("<html><b>Energy(kcal/100g):</b></html>");
	energyLabel.setPreferredSize(new Dimension(200, 20));
	energyField=new JTextField(30);
	energyField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

	proteinLabel=new JLabel("<html><b>Protein(g/100g)</b></html>");
	proteinLabel.setPreferredSize(new Dimension(200, 20));
	proteinField=new JTextField(30);
	proteinField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

	ironLabel=new JLabel("<html><b>Iron(mg/100g)</b></html>");
	ironLabel.setPreferredSize(new Dimension(200, 20));
	ironField=new JTextField(30);
	ironField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

	vitALabel=new JLabel("<html><b>Vitamin A(mcg/100g):</b></html>");
	vitALabel.setPreferredSize(new Dimension(200, 20));
	vitAField=new JTextField(30);
	vitAField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

	thiamineLabel=new JLabel("<html><b>Thiamine(mg/100g):</b></html>");
	thiamineLabel.setPreferredSize(new Dimension(200, 20));
	thiamineField=new JTextField(30);
	thiamineField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

	riboflavinLabel=new JLabel("<html><b>Riboflavin(mg/100g)</b></html>");
	riboflavinLabel.setPreferredSize(new Dimension(200, 20));
	riboflavinField=new JTextField(30);
	riboflavinField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

	vitCLabel=new JLabel("<html><b>Vitamin C(mg/100g):</b></html>");
	vitCLabel.setPreferredSize(new Dimension(200, 20));
	vitCField=new JTextField(30);
	vitCField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

	layout=new GroupLayout(getContentPane());
	getContentPane().setLayout(layout);
	layout.setAutoCreateGaps(true);
	layout.setAutoCreateContainerGaps(true);
	layout.setVerticalGroup(layout.createSequentialGroup()
		.addComponent(heading)
		.addGroup(layout.createParallelGroup()
			.addComponent(listBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			.addComponent(save)
			.addComponent(remove))
			.addGroup(layout.createParallelGroup()
				.addComponent(nameLabel)
				.addComponent(nameField))
				.addGroup(layout.createParallelGroup()
					.addComponent(groupLabel)
					.addComponent(groupBox))
					.addGroup(layout.createParallelGroup()
						.addComponent(energyLabel)
						.addComponent(energyField))
						.addGroup(layout.createParallelGroup()
							.addComponent(proteinLabel)
							.addComponent(proteinField))
							.addGroup(layout.createParallelGroup()
								.addComponent(ironLabel)
								.addComponent(ironField))
								.addGroup(layout.createParallelGroup()
									.addComponent(vitALabel)
									.addComponent(vitAField))
									.addGroup(layout.createParallelGroup()
										.addComponent(thiamineLabel)
										.addComponent(thiamineField))
										.addGroup(layout.createParallelGroup()
											.addComponent(riboflavinLabel)
											.addComponent(riboflavinField))
											.addGroup(layout.createParallelGroup()
												.addComponent(vitCLabel)
												.addComponent(vitCField))
		);
	layout.setHorizontalGroup(layout.createParallelGroup()
		.addComponent(heading)
		.addGroup(layout.createSequentialGroup()
			.addComponent(listBox)
			.addComponent(save)
			.addComponent(remove))
			.addGroup(layout.createSequentialGroup()
				.addComponent(nameLabel)
				.addComponent(nameField))
				.addGroup(layout.createSequentialGroup()
					.addComponent(groupLabel)
					.addComponent(groupBox))
					.addGroup(layout.createSequentialGroup()
						.addComponent(energyLabel)
						.addComponent(energyField))
						.addGroup(layout.createSequentialGroup()
							.addComponent(proteinLabel)
							.addComponent(proteinField))
							.addGroup(layout.createSequentialGroup()
								.addComponent(ironLabel)
								.addComponent(ironField))
								.addGroup(layout.createSequentialGroup()
									.addComponent(vitALabel)
									.addComponent(vitAField))
									.addGroup(layout.createSequentialGroup()
										.addComponent(thiamineLabel)
										.addComponent(thiamineField))
										.addGroup(layout.createSequentialGroup()
											.addComponent(riboflavinLabel)
											.addComponent(riboflavinField))
											.addGroup(layout.createSequentialGroup()
												.addComponent(vitCLabel)
												.addComponent(vitCField))
		);
	layout.linkSize(nameLabel, groupLabel, energyLabel, proteinLabel, ironLabel, vitALabel, thiamineLabel, riboflavinLabel, vitCLabel);
	layout.linkSize(SwingConstants.VERTICAL, listBox, save, remove);
	layout.linkSize(SwingConstants.HORIZONTAL, save, remove);

	setTitle("Custom Food Item");
	pack();
	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	setSize(600, 480);
	setLocationRelativeTo(null);
	setResizable(false);
	setAlwaysOnTop(true);
    }

    private boolean isComplete(){
	String name=nameField.getText().trim();
	name=name.replaceAll("\\s+", " ");
	if(!(name.length()>0)){
	    JOptionPane.showMessageDialog(this, "Please enter the name of the food.", "Error", JOptionPane.ERROR_MESSAGE);
	    return false;
	}
	try{
	    Double.parseDouble(energyField.getText());
	    Double.parseDouble(proteinField.getText());
	    Double.parseDouble(ironField.getText());
	    Double.parseDouble(vitAField.getText());
	    Double.parseDouble(thiamineField.getText());
	    Double.parseDouble(riboflavinField.getText());
	    Double.parseDouble(vitCField.getText());
	}catch(NumberFormatException e){
	    JOptionPane.showMessageDialog(this, "Invalid number detected.\nPlease try again.", "Error", JOptionPane.ERROR_MESSAGE);
	    return false;
	}
	nameField.setText(name);
	return true;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	try{
	    if(ae.getActionCommand().equals("select")){
		if(listBox.getSelectedItem().equals(newFood)){
		    remove.setEnabled(false);
		    nameField.setEditable(true);
		    nameField.setText("");
		    groupBox.setSelectedIndex(0);;
		    energyField.setText("");
		    proteinField.setText("");
		    ironField.setText("");
		    vitAField.setText("");
		    thiamineField.setText("");
		    riboflavinField.setText("");
		    vitCField.setText("");
		}
		else{
		    String name=listBox.getSelectedItem().toString();
		    remove.setEnabled(true);
		    nameField.setEditable(false);
		    nameField.setText(xp.evaluate("foodList/food[name='"+name+"']/name", doc));
		    groupBox.setSelectedItem(xp.evaluate("foodList/food[name='"+name+"']/group", doc));
		    energyField.setText(xp.evaluate("foodList/food[name='"+name+"']/energy", doc));
		    proteinField.setText(xp.evaluate("foodList/food[name='"+name+"']/protein", doc));
		    ironField.setText(xp.evaluate("foodList/food[name='"+name+"']/iron", doc));
		    vitAField.setText(xp.evaluate("foodList/food[name='"+name+"']/vitA", doc));
		    thiamineField.setText(xp.evaluate("foodList/food[name='"+name+"']/thiamine", doc));
		    riboflavinField.setText(xp.evaluate("foodList/food[name='"+name+"']/riboflavin", doc));
		    vitCField.setText(xp.evaluate("foodList/food[name='"+name+"']/vitC", doc));
		}
	    }
	    else if(ae.getActionCommand().equals("save")){
		if(isComplete()){
		    if(listBox.getSelectedItem().equals(newFood)){
			node=(Node) xp.evaluate("/foodList", doc, XPathConstants.NODE);
			node.appendChild(doc.createElement("food"));
			node=node.getLastChild();
			node.appendChild(doc.createElement("name"));
			node.getLastChild().appendChild(doc.createTextNode(nameField.getText().trim()));
			node.appendChild(doc.createElement("group"));
			node.getLastChild().appendChild(doc.createTextNode(groupBox.getSelectedItem().toString()));
			node.appendChild(doc.createElement("energy"));
			node.getLastChild().appendChild(doc.createTextNode(energyField.getText().trim()));
			node.appendChild(doc.createElement("protein"));
			node.getLastChild().appendChild(doc.createTextNode(proteinField.getText().trim()));
			node.appendChild(doc.createElement("iron"));
			node.getLastChild().appendChild(doc.createTextNode(ironField.getText().trim()));
			node.appendChild(doc.createElement("vitA"));
			node.getLastChild().appendChild(doc.createTextNode(vitAField.getText().trim()));
			node.appendChild(doc.createElement("thiamine"));
			node.getLastChild().appendChild(doc.createTextNode(thiamineField.getText().trim()));
			node.appendChild(doc.createElement("riboflavin"));
			node.getLastChild().appendChild(doc.createTextNode(riboflavinField.getText().trim()));
			node.appendChild(doc.createElement("vitC"));
			node.getLastChild().appendChild(doc.createTextNode(vitCField.getText().trim()));
			listBox.addItem(nameField.getText().toString());
			listBox.setSelectedItem(nameField.getText().toString());
		    }
		    else{
			node=(Node) xp.evaluate("/foodList/food['"+nameField.getText()+"']", doc, XPathConstants.NODE);
			node.getParentNode().removeChild(node);
			node=(Node) xp.evaluate("/foodList", doc, XPathConstants.NODE);
			node.appendChild(doc.createElement("food"));
			node=node.getLastChild();
			node.appendChild(doc.createElement("name"));
			node.getLastChild().appendChild(doc.createTextNode(nameField.getText().trim()));
			node.appendChild(doc.createElement("group"));
			node.getLastChild().appendChild(doc.createTextNode(groupBox.getSelectedItem().toString()));
			node.appendChild(doc.createElement("energy"));
			node.getLastChild().appendChild(doc.createTextNode(energyField.getText().trim()));
			node.appendChild(doc.createElement("protein"));
			node.getLastChild().appendChild(doc.createTextNode(proteinField.getText().trim()));
			node.appendChild(doc.createElement("iron"));
			node.getLastChild().appendChild(doc.createTextNode(ironField.getText().trim()));
			node.appendChild(doc.createElement("vitA"));
			node.getLastChild().appendChild(doc.createTextNode(vitAField.getText().trim()));
			node.appendChild(doc.createElement("thiamine"));
			node.getLastChild().appendChild(doc.createTextNode(thiamineField.getText().trim()));
			node.appendChild(doc.createElement("riboflavin"));
			node.getLastChild().appendChild(doc.createTextNode(riboflavinField.getText().trim()));
			node.appendChild(doc.createElement("vitC"));
			node.getLastChild().appendChild(doc.createTextNode(vitCField.getText().trim()));
		    }
		    dh.writeToDisk(doc, file);
		    listBox.setSelectedItem(nameField.getText().toString());
		}
	    }
	    else if(ae.getActionCommand().equals("remove")){
		node=(Node) xp.evaluate("/foodList/food['"+nameField.getText().toString()+"']", doc, XPathConstants.NODE);
		node.getParentNode().removeChild(node);
		dh.writeToDisk(doc, file);
		listBox.removeItem(nameField.getText().toString());
	    }
	} catch (XPathExpressionException | TransformerException e) {
	    JOptionPane.showMessageDialog(this, "An error has occured.\n"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }

}
