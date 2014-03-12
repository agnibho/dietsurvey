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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Assess {
    
    private double totalEnergyRequirement=0;
    private double totalProteinRequirement=0;
    private double totalIronRequirement=0;
    private double totalVitARequirement=0;
    private double totalThiamineRequirement=0;
    private double totalRiboflavinRequirement=0;
    private double totalVitCRequirement=0;
    
    private double totalEnergyConsumed=0;
    private double totalProteinConsumed=0;
    private double totalIronConsumed=0;
    private double totalVitAConsumed=0;
    private double totalThiamineConsumed=0;
    private double totalRiboflavinConsumed=0;
    private double totalVitCConsumed=0;
    
    private DocHandler dh;
    private Document input;
    private Document output;
    private Document rda;
    private Document food;
    private Document customFoods;
    private XPath xp=DocHandler.getXpInstance();

    public Assess(Document in) throws TransformerConfigurationException, SAXException, IOException, ParserConfigurationException, XPathExpressionException {
	dh=new DocHandler();
	
	input=in;
	output=dh.getDocInstance();
	rda=dh.getDocument(java.lang.ClassLoader.getSystemResourceAsStream("com/agnibho/code/resources/rda.xml"));
	food=dh.getDocument(java.lang.ClassLoader.getSystemResourceAsStream("com/agnibho/code/resources/food.xml"));
	
	Node node=output;
	node.appendChild(output.createElement("assessment"));
    }

    private void combine(){
	try{
	    customFoods=new DocHandler().getValidatedDocument(new File(UI.LOC+File.separator+"custom_food.xml"), java.lang.ClassLoader.getSystemResourceAsStream("com/agnibho/code/resources/customFood.xsd"));
	    NodeList custom=(NodeList) xp.evaluate("foodList/food", customFoods, XPathConstants.NODESET);
	    Node del;
	    for(int i=0; i<custom.getLength(); i++){
		if((boolean) xp.evaluate("/foodList/food[name='"+xp.evaluate("name", custom.item(i))+"']", food, XPathConstants.BOOLEAN)){
		    del=(Node) xp.evaluate("/foodList/food[name='"+xp.evaluate("name", custom.item(i))+"']", food, XPathConstants.NODE);
		    del.getParentNode().removeChild(del);
		}
		food.getLastChild().appendChild(food.importNode(custom.item(i), true));
	    }
	}catch(Exception e){
	    System.out.println(e);
	}
    }
    
    public Document getOutput() throws XPathExpressionException{
	combine();
	calculateRequirement();
	calculateConsumption();
	calculateStatus();
	return output;
    }
    
    /**
     * 
     * REQUIREMENT
     * 
     */
    
    private void calculateRequirement() throws XPathExpressionException{
	Node node;
	node=(Node) xp.evaluate("/assessment", output, XPathConstants.NODE);
	node.appendChild(output.createElement("requirement"));
	NodeList nl=(NodeList) xp.evaluate("/record/id/member", input, XPathConstants.NODESET);
	for(int i=0; i<nl.getLength(); i++){
	    memberRequirement(nl.item(i));
	}
	node=(Node) xp.evaluate("/assessment/requirement", output, XPathConstants.NODE);
	node.appendChild(output.createElement("total"));
	node=node.getLastChild();
	node.appendChild(output.createElement("energy"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalEnergyRequirement)));
	node.appendChild(output.createElement("protein"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalProteinRequirement)));
	node.appendChild(output.createElement("iron"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalIronRequirement)));
	node.appendChild(output.createElement("vitA"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalVitARequirement)));
	node.appendChild(output.createElement("thiamine"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalThiamineRequirement)));
	node.appendChild(output.createElement("riboflavin"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalRiboflavinRequirement)));
	node.appendChild(output.createElement("vitC"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalVitCRequirement)));
    }
    //Individual
    private void memberRequirement(Node inNode) throws XPathExpressionException{
	Node outNode=(Node) xp.evaluate("/assessment/requirement", output, XPathConstants.NODE);
	outNode.appendChild(output.createElement("member"));
	outNode=outNode.getLastChild();
	outNode.appendChild(output.importNode((Node) xp.evaluate("name", inNode, XPathConstants.NODE), true));
	
	/*
	 * ENERGY
	 */
	
	outNode.appendChild(output.createElement("energy"));
	outNode=outNode.getLastChild();
	if(xp.evaluate("infant", inNode, XPathConstants.NODE) != null){
	    int age=Integer.parseInt(xp.evaluate("infant", inNode));
	    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/energy/infant/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	}
	else if(xp.evaluate("sex", inNode).equals("male")){
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/energy/male/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	    }
	    else{
		if(xp.evaluate("occupation", inNode).equals("sedentary")){
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/energy/male/age/sedentary", rda)));
		}
		else if(xp.evaluate("occupation", inNode).equals("moderate"))
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/energy/male/age/moderate", rda)));
		else if(xp.evaluate("occupation", inNode).equals("heavy"))
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/energy/male/age/heavy", rda)));
	    }
	}
	else{
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    double energy=0;
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/energy/female/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	    }
	    else{
		if((boolean) xp.evaluate("pregnant", inNode, XPathConstants.BOOLEAN)){
		    energy=Double.parseDouble(xp.evaluate("/rda/energy/female/pregnantExtra", rda));
		}
		if((boolean) xp.evaluate("lactating", inNode, XPathConstants.BOOLEAN)){
		    if((boolean) xp.evaluate("beyond6m", inNode, XPathConstants.BOOLEAN))
			energy=Double.parseDouble(xp.evaluate("/rda/energy/female/lactatingExtra/above6m", rda));
		    else
			energy=Double.parseDouble(xp.evaluate("/rda/energy/female/lactatingExtra/below6m", rda));
		}
		if(xp.evaluate("occupation", inNode).equals("sedentary"))
		    outNode.appendChild(output.createTextNode(String.valueOf(energy+Double.parseDouble(xp.evaluate("/rda/energy/female/age/sedentary", rda)))));
		else if(xp.evaluate("occupation", inNode).equals("moderate"))
		    outNode.appendChild(output.createTextNode(String.valueOf(energy+Double.parseDouble(xp.evaluate("/rda/energy/female/age/moderate", rda)))));
		else if(xp.evaluate("occupation", inNode).equals("heavy"))
		    outNode.appendChild(output.createTextNode(String.valueOf(energy+Double.parseDouble(xp.evaluate("/rda/energy/female/age/heavy", rda)))));
	    }
	}
	totalEnergyRequirement=totalEnergyRequirement+Double.parseDouble(xp.evaluate(".", outNode));
	
	/*
	 * PROTEIN
	 */
	
	outNode=outNode.getParentNode();
	outNode.appendChild(output.createElement("protein"));
	outNode=outNode.getLastChild();
	if(xp.evaluate("infant", inNode, XPathConstants.NODE) != null){
	    int age=Integer.parseInt(xp.evaluate("infant", inNode));
	    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/protein/infant/age[@lower<="+age+"][@upper>"+age+"]", rda)));
	}
	else if(xp.evaluate("sex", inNode).equals("male")){
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/protein/male/age[@lower<="+age+"][@upper>"+age+"]", rda)));
	    }
	    else{
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/protein/male/age[@lower='18']", rda)));
	    }
	}
	else{
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/protein/female/age[@lower<="+age+"][@upper>"+age+"]", rda)));
	    }
	    else{
		if((boolean) xp.evaluate("pregnant", inNode, XPathConstants.BOOLEAN)){
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/protein/female/pregnant", rda)));
		}
		else if((boolean) xp.evaluate("lactating", inNode, XPathConstants.BOOLEAN)){
		    if((boolean) xp.evaluate("beyond6m", inNode, XPathConstants.BOOLEAN))
			outNode.appendChild(output.createTextNode(xp.evaluate("/rda/protein/female/lactatingAbove6m", rda)));
		    else
			outNode.appendChild(output.createTextNode(xp.evaluate("/rda/protein/female/lactatingBelow6m", rda)));
		}
		else
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/protein/female/age[@lower='18']", rda)));
	    }
	}
	totalProteinRequirement=totalProteinRequirement+Double.parseDouble(xp.evaluate(".", outNode));

	/*
	 * IRON
	 */
	
	outNode=outNode.getParentNode();
	outNode.appendChild(output.createElement("iron"));
	outNode=outNode.getLastChild();
	if(xp.evaluate("infant", inNode, XPathConstants.NODE) != null){
	    int age=Integer.parseInt(xp.evaluate("infant", inNode));
	    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/iron/infant/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	}
	else if(xp.evaluate("sex", inNode).equals("male")){
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/iron/male/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	    }
	    else{
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/iron/male/age[@lower='18']", rda)));
	    }
	}
	else{
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/iron/female/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	    }
	    else{
		if((boolean) xp.evaluate("pregnant", inNode, XPathConstants.BOOLEAN)){
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/iron/female/pregnant", rda)));
		}
		else if((boolean) xp.evaluate("lactating", inNode, XPathConstants.BOOLEAN)){
		    if((boolean) xp.evaluate("beyond6m", inNode, XPathConstants.BOOLEAN))
			outNode.appendChild(output.createTextNode(xp.evaluate("/rda/iron/female/lactatingAbove6m", rda)));
		    else
			outNode.appendChild(output.createTextNode(xp.evaluate("/rda/iron/female/lactatingBelow6m", rda)));
		}
		else
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/iron/female/age[@lower='18']", rda)));
	    }
	}
	totalIronRequirement=totalIronRequirement+Double.parseDouble(xp.evaluate(".", outNode));
	/*
	 * vitA
	 */
	
	outNode=outNode.getParentNode();
	outNode.appendChild(output.createElement("vitA"));
	outNode=outNode.getLastChild();
	if(xp.evaluate("infant", inNode, XPathConstants.NODE) != null){
	    int age=Integer.parseInt(xp.evaluate("infant", inNode));
	    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitA/infant/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	}
	else if(xp.evaluate("sex", inNode).equals("male")){
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitA/male/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	    }
	    else{
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitA/male/age[@lower='18']", rda)));
	    }
	}
	else{
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitA/female/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	    }
	    else{
		if((boolean) xp.evaluate("pregnant", inNode, XPathConstants.BOOLEAN)){
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitA/female/pregnant", rda)));
		}
		else if((boolean) xp.evaluate("lactating", inNode, XPathConstants.BOOLEAN)){
		    if((boolean) xp.evaluate("beyond6m", inNode, XPathConstants.BOOLEAN))
			outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitA/female/lactatingAbove6m", rda)));
		    else
			outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitA/female/lactatingBelow6m", rda)));
		}
		else
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitA/female/age[@lower='18']", rda)));
	    }
	}
	totalVitARequirement=totalVitARequirement+Double.parseDouble(xp.evaluate(".", outNode));
	
	/*
	 * THIAMINE
	 */
	
	outNode=outNode.getParentNode();
	outNode.appendChild(output.createElement("thiamine"));
	outNode=outNode.getLastChild();
	if(xp.evaluate("infant", inNode, XPathConstants.NODE) != null){
	    int age=Integer.parseInt(xp.evaluate("infant", inNode));
	    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/thiamine/infant/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	}
	else if(xp.evaluate("sex", inNode).equals("male")){
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/thiamine/male/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	    }
	    else{
		if(xp.evaluate("occupation", inNode).equals("sedentary")){
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/thiamine/male/age/sedentary", rda)));
		}
		else if(xp.evaluate("occupation", inNode).equals("moderate"))
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/thiamine/male/age/moderate", rda)));
		else if(xp.evaluate("occupation", inNode).equals("heavy"))
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/thiamine/male/age/heavy", rda)));
	    }
	}
	else{
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    double thiamine=0;
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/thiamine/female/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	    }
	    else{
		if((boolean) xp.evaluate("pregnant", inNode, XPathConstants.BOOLEAN)){
		    thiamine=Double.parseDouble(xp.evaluate("/rda/thiamine/female/pregnantExtra", rda));
		}
		if((boolean) xp.evaluate("lactating", inNode, XPathConstants.BOOLEAN)){
		    if((boolean) xp.evaluate("beyond6m", inNode, XPathConstants.BOOLEAN))
			thiamine=Double.parseDouble(xp.evaluate("/rda/thiamine/female/lactatingExtra/above6m", rda));
		    else
			thiamine=Double.parseDouble(xp.evaluate("/rda/thiamine/female/lactatingExtra/below6m", rda));
		}
		if(xp.evaluate("occupation", inNode).equals("sedentary"))
		    outNode.appendChild(output.createTextNode(String.valueOf(thiamine+Double.parseDouble(xp.evaluate("/rda/thiamine/female/age/sedentary", rda)))));
		else if(xp.evaluate("occupation", inNode).equals("moderate"))
		    outNode.appendChild(output.createTextNode(String.valueOf(thiamine+Double.parseDouble(xp.evaluate("/rda/thiamine/female/age/moderate", rda)))));
		else if(xp.evaluate("occupation", inNode).equals("heavy"))
		    outNode.appendChild(output.createTextNode(String.valueOf(thiamine+Double.parseDouble(xp.evaluate("/rda/thiamine/female/age/heavy", rda)))));
	    }
	}
	totalThiamineRequirement=totalThiamineRequirement+Double.parseDouble(xp.evaluate(".", outNode));

	/*
	 * RIBOFLAVIN
	 */
	
	outNode=outNode.getParentNode();
	outNode.appendChild(output.createElement("riboflavin"));
	outNode=outNode.getLastChild();
	if(xp.evaluate("infant", inNode, XPathConstants.NODE) != null){
	    int age=Integer.parseInt(xp.evaluate("infant", inNode));
	    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/riboflavin/infant/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	}
	else if(xp.evaluate("sex", inNode).equals("male")){
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/riboflavin/male/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	    }
	    else{
		if(xp.evaluate("occupation", inNode).equals("sedentary")){
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/riboflavin/male/age/sedentary", rda)));
		}
		else if(xp.evaluate("occupation", inNode).equals("moderate"))
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/riboflavin/male/age/moderate", rda)));
		else if(xp.evaluate("occupation", inNode).equals("heavy"))
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/riboflavin/male/age/heavy", rda)));
	    }
	}
	else{
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    double riboflavin=0;
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/riboflavin/female/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	    }
	    else{
		if((boolean) xp.evaluate("pregnant", inNode, XPathConstants.BOOLEAN)){
		    riboflavin=Double.parseDouble(xp.evaluate("/rda/riboflavin/female/pregnantExtra", rda));
		}
		if((boolean) xp.evaluate("lactating", inNode, XPathConstants.BOOLEAN)){
		    if((boolean) xp.evaluate("beyond6m", inNode, XPathConstants.BOOLEAN))
			riboflavin=Double.parseDouble(xp.evaluate("/rda/riboflavin/female/lactatingExtra/above6m", rda));
		    else
			riboflavin=Double.parseDouble(xp.evaluate("/rda/riboflavin/female/lactatingExtra/below6m", rda));
		}
		if(xp.evaluate("occupation", inNode).equals("sedentary"))
		    outNode.appendChild(output.createTextNode(String.valueOf(riboflavin+Double.parseDouble(xp.evaluate("/rda/riboflavin/female/age/sedentary", rda)))));
		else if(xp.evaluate("occupation", inNode).equals("moderate"))
		    outNode.appendChild(output.createTextNode(String.valueOf(riboflavin+Double.parseDouble(xp.evaluate("/rda/riboflavin/female/age/moderate", rda)))));
		else if(xp.evaluate("occupation", inNode).equals("heavy"))
		    outNode.appendChild(output.createTextNode(String.valueOf(riboflavin+Double.parseDouble(xp.evaluate("/rda/riboflavin/female/age/heavy", rda)))));
	    }
	}
	totalRiboflavinRequirement=totalRiboflavinRequirement+Double.parseDouble(xp.evaluate(".", outNode));

	/*
	 * VITAMIN-C
	 */
	
	outNode=outNode.getParentNode();
	outNode.appendChild(output.createElement("vitC"));
	outNode=outNode.getLastChild();
	if(xp.evaluate("infant", inNode, XPathConstants.NODE) != null){
	    int age=Integer.parseInt(xp.evaluate("infant", inNode));
	    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitC/infant/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	}
	else if(xp.evaluate("sex", inNode).equals("male")){
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitC/male/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	    }
	    else{
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitC/male/age[@lower='18']", rda)));
	    }
	}
	else{
	    int age=Integer.parseInt(xp.evaluate("age", inNode));
	    if(age<18){
		outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitC/female/age[@lower<="+age+"][@upper>="+age+"]", rda)));
	    }
	    else{
		if((boolean) xp.evaluate("pregnant", inNode, XPathConstants.BOOLEAN)){
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitC/female/pregnant", rda)));
		}
		else if((boolean) xp.evaluate("lactating", inNode, XPathConstants.BOOLEAN)){
		    if((boolean) xp.evaluate("beyond6m", inNode, XPathConstants.BOOLEAN))
			outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitC/female/lactatingAbove6m", rda)));
		    else
			outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitC/female/lactatingBelow6m", rda)));
		}
		else
		    outNode.appendChild(output.createTextNode(xp.evaluate("/rda/vitC/female/age[@lower='18']", rda)));
	    }
	}
	totalVitCRequirement=totalVitCRequirement+Double.parseDouble(xp.evaluate(".", outNode));
    }

    /**
     * 
     * CONSUMPTION
     * 
     */
    
    private void calculateConsumption() throws XPathExpressionException{
	Node node;
	node=(Node) xp.evaluate("/assessment", output, XPathConstants.NODE);
	node.appendChild(output.createElement("consumption"));
	NodeList nl=(NodeList) xp.evaluate("/record/intake/food", input, XPathConstants.NODESET);
	for(int i=0; i<nl.getLength(); i++){
	    perFoodNutrition(nl.item(i));
	}
	node=(Node) xp.evaluate("/assessment/consumption", output, XPathConstants.NODE);
	node.appendChild(output.createElement("total"));
	node=node.getLastChild();
	node.appendChild(output.createElement("energy"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalEnergyConsumed)));
	node.appendChild(output.createElement("protein"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalProteinConsumed)));
	node.appendChild(output.createElement("iron"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalIronConsumed)));
	node.appendChild(output.createElement("vitA"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalVitAConsumed)));
	node.appendChild(output.createElement("thiamine"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalThiamineConsumed)));
	node.appendChild(output.createElement("riboflavin"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalRiboflavinConsumed)));
	node.appendChild(output.createElement("vitC"));
	node.getLastChild().appendChild(output.createTextNode(String.valueOf(totalVitCConsumed)));
    }
    //Individual
    private void perFoodNutrition(Node inNode) throws XPathExpressionException{
	Node outNode=(Node) xp.evaluate("/assessment/consumption", output, XPathConstants.NODE);
	outNode.appendChild(output.createElement("food"));
	outNode=outNode.getLastChild();
	outNode.appendChild(output.importNode((Node) xp.evaluate("name", inNode, XPathConstants.NODE), true));
	
	/*
	 * ENERGY
	 */
	
	outNode.appendChild(output.createElement("energy"));
	double energy=Double.parseDouble(xp.evaluate("/foodList/food[name='"+xp.evaluate("name", inNode)+"']/energy", food))*Double.parseDouble(xp.evaluate("amount", inNode))/100;
	outNode.getLastChild().appendChild(output.createTextNode(String.valueOf(energy)));
	totalEnergyConsumed=totalEnergyConsumed+energy;

	/*
	 * PROTEIN
	 */
	
	outNode.appendChild(output.createElement("protein"));
	double protein=Double.parseDouble(xp.evaluate("/foodList/food[name='"+xp.evaluate("name", inNode)+"']/protein", food))*Double.parseDouble(xp.evaluate("amount", inNode))/100;
	outNode.getLastChild().appendChild(output.createTextNode(String.valueOf(protein)));
	totalProteinConsumed=totalProteinConsumed+protein;

	/*
	 * IRON
	 */
	
	outNode.appendChild(output.createElement("iron"));
	double iron=Double.parseDouble(xp.evaluate("/foodList/food[name='"+xp.evaluate("name", inNode)+"']/iron", food))*Double.parseDouble(xp.evaluate("amount", inNode))/100;
	outNode.getLastChild().appendChild(output.createTextNode(String.valueOf(iron)));
	totalIronConsumed=totalIronConsumed+iron;

	/*
	 * VIT-A
	 */
	
	outNode.appendChild(output.createElement("vitA"));
	double vitA=Double.parseDouble(xp.evaluate("/foodList/food[name='"+xp.evaluate("name", inNode)+"']/vitA", food))*Double.parseDouble(xp.evaluate("amount", inNode))/100;
	outNode.getLastChild().appendChild(output.createTextNode(String.valueOf(vitA)));
	totalVitAConsumed=totalVitAConsumed+vitA;

	/*
	 * THIAMINE
	 */
	
	outNode.appendChild(output.createElement("thiamine"));
	double thiamine=Double.parseDouble(xp.evaluate("/foodList/food[name='"+xp.evaluate("name", inNode)+"']/thiamine", food))*Double.parseDouble(xp.evaluate("amount", inNode))/100;
	outNode.getLastChild().appendChild(output.createTextNode(String.valueOf(thiamine)));
	totalThiamineConsumed=totalThiamineConsumed+thiamine;

	/*
	 * RIBOFLAVIN
	 */
	
	outNode.appendChild(output.createElement("riboflavin"));
	double riboflavin=Double.parseDouble(xp.evaluate("/foodList/food[name='"+xp.evaluate("name", inNode)+"']/riboflavin", food))*Double.parseDouble(xp.evaluate("amount", inNode))/100;
	outNode.getLastChild().appendChild(output.createTextNode(String.valueOf(riboflavin)));
	totalRiboflavinConsumed=totalRiboflavinConsumed+riboflavin;

	/*
	 * VIT-C
	 */
	
	outNode.appendChild(output.createElement("vitC"));
	double vitC=Double.parseDouble(xp.evaluate("/foodList/food[name='"+xp.evaluate("name", inNode)+"']/vitC", food))*Double.parseDouble(xp.evaluate("amount", inNode))/100;
	outNode.getLastChild().appendChild(output.createTextNode(String.valueOf(vitC)));
	totalVitCConsumed=totalVitCConsumed+vitC;
    }
    
    /**
     * 
     * STATUS
     * 
     */
    
    private void calculateStatus() throws XPathExpressionException{
	Node node;
	node=(Node) xp.evaluate("/assessment", output, XPathConstants.NODE);
	node.appendChild(output.createElement("nutritionalStatus"));
	node=node.getLastChild();
	
	//ENERGY
	node.appendChild(output.createElement("energy"));
	node=node.getLastChild();
	double energyR=Double.parseDouble(xp.evaluate("/assessment/requirement/total/energy", output));
	double energyC=Double.parseDouble(xp.evaluate("/assessment/consumption/total/energy", output));
	if(energyR>=energyC){
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("deficient"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(energyR-energyC)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((energyR-energyC)*100/energyR)));
	}
	else{
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("excess"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(energyC-energyR)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((energyC-energyR)*100/energyR)));
	}
	//PROTEIN
	node=node.getParentNode();
	node.appendChild(output.createElement("protein"));
	node=node.getLastChild();
	double proteinR=Double.parseDouble(xp.evaluate("/assessment/requirement/total/protein", output));
	double proteinC=Double.parseDouble(xp.evaluate("/assessment/consumption/total/protein", output));
	if(proteinR>=proteinC){
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("deficient"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(proteinR-proteinC)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((proteinR-proteinC)*100/proteinR)));
	}
	else{
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("excess"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(proteinC-proteinR)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((proteinC-proteinR)*100/proteinR)));
	}
	//IRON
	node=node.getParentNode();
	node.appendChild(output.createElement("iron"));
	node=node.getLastChild();
	double ironR=Double.parseDouble(xp.evaluate("/assessment/requirement/total/iron", output));
	double ironC=Double.parseDouble(xp.evaluate("/assessment/consumption/total/iron", output));
	if(ironR>=ironC){
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("deficient"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(ironR-ironC)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((ironR-ironC)*100/ironR)));
	}
	else{
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("excess"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(ironC-ironR)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((ironC-ironR)*100/ironR)));
	}
	//VIT-A
	node=node.getParentNode();
	node.appendChild(output.createElement("vitA"));
	node=node.getLastChild();
	double vitAR=Double.parseDouble(xp.evaluate("/assessment/requirement/total/vitA", output));
	double vitAC=Double.parseDouble(xp.evaluate("/assessment/consumption/total/vitA", output));
	if(vitAR>=vitAC){
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("deficient"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(vitAR-vitAC)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((vitAR-vitAC)*100/vitAR)));
	}
	else{
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("excess"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(vitAC-vitAR)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((vitAC-vitAR)*100/vitAR)));
	}
	//THIAMINE
	node=node.getParentNode();
	node.appendChild(output.createElement("thiamine"));
	node=node.getLastChild();
	double thiamineR=Double.parseDouble(xp.evaluate("/assessment/requirement/total/thiamine", output));
	double thiamineC=Double.parseDouble(xp.evaluate("/assessment/consumption/total/thiamine", output));
	if(thiamineR>=thiamineC){
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("deficient"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(thiamineR-thiamineC)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((thiamineR-thiamineC)*100/thiamineR)));
	}
	else{
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("excess"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(thiamineC-thiamineR)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((thiamineC-thiamineR)*100/thiamineR)));
	}
	//RIBOFLAVIN
	node=node.getParentNode();
	node.appendChild(output.createElement("riboflavin"));
	node=node.getLastChild();
	double riboflavinR=Double.parseDouble(xp.evaluate("/assessment/requirement/total/riboflavin", output));
	double riboflavinC=Double.parseDouble(xp.evaluate("/assessment/consumption/total/riboflavin", output));
	if(riboflavinR>=riboflavinC){
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("deficient"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(riboflavinR-riboflavinC)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((riboflavinR-riboflavinC)*100/riboflavinR)));
	}
	else{
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("excess"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(riboflavinC-riboflavinR)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((riboflavinC-riboflavinR)*100/riboflavinR)));
	}
	//VITAMIN-C
	node=node.getParentNode();
	node.appendChild(output.createElement("vitC"));
	node=node.getLastChild();
	double vitCR=Double.parseDouble(xp.evaluate("/assessment/requirement/total/vitC", output));
	double vitCC=Double.parseDouble(xp.evaluate("/assessment/consumption/total/vitC", output));
	if(vitCR>=vitCC){
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("deficient"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(vitCR-vitCC)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((vitCR-vitCC)*100/vitCR)));
	}
	else{
	    node.appendChild(output.createElement("status"));
	    node.getLastChild().appendChild(output.createTextNode("excess"));
	    node.appendChild(output.createElement("amount"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf(vitCC-vitCR)));
	    node.appendChild(output.createElement("percentage"));
	    node.getLastChild().appendChild(output.createTextNode(String.valueOf((vitCC-vitCR)*100/vitCR)));
	}
    }
    
    public InputStream getGraphImage() throws IOException{
	InputStream is;
	NutrientGraph ng=new NutrientGraph((totalEnergyConsumed/totalEnergyRequirement)*100, (totalProteinConsumed/totalProteinRequirement)*100,
		(totalIronConsumed/totalIronRequirement)*100, (totalVitAConsumed/totalVitARequirement)*100, (totalThiamineConsumed/totalThiamineRequirement)*100,
		(totalRiboflavinConsumed/totalRiboflavinRequirement)*100, (totalVitCConsumed/totalVitCRequirement)*100);
	JFrame frame = new JFrame();
	frame.setBackground(Color.white);
	frame.setUndecorated(true);
	frame.getContentPane().add(ng);
	frame.pack();
	frame.setVisible(true);
	BufferedImage image = new BufferedImage(ng.getWidth(), ng.getHeight(), BufferedImage.TYPE_INT_ARGB);
	Graphics2D graphics = image.createGraphics();
	ng.print(graphics);
	is=ng.getAsInputStream();
	graphics.dispose();
	frame.dispose();
	return is;
    }
}
