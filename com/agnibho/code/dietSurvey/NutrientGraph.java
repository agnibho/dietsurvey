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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class NutrientGraph extends JPanel {

    private double energy;
    private double protein;
    private double iron;
    private double vitA;
    private double thiamine;
    private double riboflavin;
    private double vitC;
    
    private int mag=2;
    
    private double base;
    private Point2D root;
    private Point2D maxX;
    private Point2D maxY;
    private Point2D norm;
    
    BufferedImage img;
    
    public NutrientGraph(){
	new NutrientGraph(0, 0, 0, 0, 0, 0, 0);
    }
    
    public NutrientGraph(double energy, double protein, double iron, double vitA, double thiamine, double riboflavin, double vitC){
	this.energy=energy;
	this.protein=protein;
	this.iron=iron;
	this.vitA=vitA;
	this.thiamine=thiamine;
	this.riboflavin=riboflavin;
	this.vitC=vitC;
	
	base=Math.max(energy, Math.max(protein, Math.max(iron, Math.max(vitA, Math.max(thiamine, Math.max(riboflavin, Math.max(vitC, 100)))))))*mag;
	root=new Point2D.Double(50, base+50);
	maxX=new Point2D.Double(490, base+50);
	maxY=new Point2D.Double(50, 50);
	norm=new Point2D.Double(root.getX(), root.getY()-(mag*100));
	
	img=new BufferedImage((int)maxX.getX()+50, (int)maxX.getY()+80, BufferedImage.TYPE_INT_RGB);
    }
    
    @Override
    public void paint(Graphics g){
	Graphics2D panelG2=(Graphics2D) g;
	Graphics2D g2=img.createGraphics();
	
	Font font=new Font(Font.SERIF, Font.PLAIN, 12);
	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
	
	g2.setPaint(Color.white);
	g2.fill(new Rectangle2D.Double(0, 0, maxX.getX()+50, maxX.getY()+80));
	g2.setPaint(Color.black);
	g2.draw(new Line2D.Double(root, maxX));
	g2.draw(new Line2D.Double(root, maxY));
	g2.draw(new Line2D.Double(norm.getX(), norm.getY(), maxX.getX(), norm.getY()));
	
	g2.setPaint(Color.blue);
	for(double i=root.getY(), per=0; i>=maxY.getY(); i=i-(mag*10), per=per+10){
	    g2.drawString(String.valueOf((int)per), (float)root.getX()-25, (float)i);
	}
	
	g2.drawString("Energy", (int)root.getX()+20, (int)root.getY()+20);
	g2.drawString("Protein", (int)root.getX()+80, (int)root.getY()+20);
	g2.drawString("Iron", (int)root.getX()+140, (int)root.getY()+20);
	g2.drawString("Vitamine A", (int)root.getX()+200, (int)root.getY()+20);
	g2.drawString("Thiamine", (int)root.getX()+260, (int)root.getY()+20);
	g2.drawString("Riboflavin", (int)root.getX()+320, (int)root.getY()+20);
	g2.drawString("Vitamin C", (int)root.getX()+380, (int)root.getY()+20);
	
	if(energy>=100)
	    g2.setPaint(Color.green);
	else
	    g2.setPaint(Color.red);
	g2.fill(new Rectangle2D.Double(root.getX()+20, root.getY()-(mag*energy), 40, mag*energy));
	g2.setPaint(Color.blue);
	g2.drawString(String.format("%.2f", energy)+"%", (float)root.getX()+20, (float)(root.getY()-(mag*energy)-5));
	if(protein>=100)
	    g2.setPaint(Color.green);
	else
	    g2.setPaint(Color.red);
	g2.fill(new Rectangle2D.Double(root.getX()+80, root.getY()-(mag*protein), 40, mag*protein));
	g2.setPaint(Color.blue);
	g2.drawString(String.format("%.2f", protein)+"%", (float)root.getX()+80, (float)(root.getY()-(mag*protein)-5));
	if(iron>=100)
	    g2.setPaint(Color.green);
	else
	    g2.setPaint(Color.red);
	g2.fill(new Rectangle2D.Double(root.getX()+140, root.getY()-(mag*iron), 40, mag*iron));
	g2.setPaint(Color.blue);
	g2.drawString(String.format("%.2f", iron)+"%", (float)root.getX()+140, (float)(root.getY()-(mag*iron)-5));
	if(vitA>=100)
	    g2.setPaint(Color.green);
	else
	    g2.setPaint(Color.red);
	g2.fill(new Rectangle2D.Double(root.getX()+200, root.getY()-(mag*vitA), 40, mag*vitA));
	g2.setPaint(Color.blue);
	g2.drawString(String.format("%.2f", vitA)+"%", (float)root.getX()+200, (float)(root.getY()-(mag*vitA)-5));
	if(thiamine>=100)
	    g2.setPaint(Color.green);
	else
	    g2.setPaint(Color.red);
	g2.fill(new Rectangle2D.Double(root.getX()+260, root.getY()-(mag*thiamine), 40, mag*thiamine));
	g2.setPaint(Color.blue);
	g2.drawString(String.format("%.2f", thiamine)+"%", (float)root.getX()+260, (float)(root.getY()-(mag*thiamine)-5));
	if(riboflavin>=100)
	    g2.setPaint(Color.green);
	else
	    g2.setPaint(Color.red);
	g2.fill(new Rectangle2D.Double(root.getX()+320, root.getY()-(mag*riboflavin), 40, mag*riboflavin));
	g2.setPaint(Color.blue);
	g2.drawString(String.format("%.2f", riboflavin)+"%", (float)root.getX()+320, (float)(root.getY()-(mag*riboflavin)-5));
	if(vitC>=100)
	    g2.setPaint(Color.green);
	else
	    g2.setPaint(Color.red);
	g2.fill(new Rectangle2D.Double(root.getX()+380, root.getY()-(mag*vitC), 40, mag*vitC));
	g2.setPaint(Color.blue);
	g2.drawString(String.format("%.2f", vitC)+"%", (float)root.getX()+380, (float)(root.getY()-(mag*vitC)-5));
	
	Hashtable<TextAttribute, Object> ht=new Hashtable<TextAttribute, Object>();
	ht.put(TextAttribute.SIZE, 18);
	font=font.deriveFont(ht);
	g2.setFont(font);
	g2.drawString("Fig: Bar diagram showing percentage intake of different nutrients", (float)root.getX(), (float)root.getY()+60);
	
	panelG2.drawImage(img, null, 0, 0);
	
	/*try {
	    ImageIO.write(img, "png", new File("E:\\test.png"));
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(this, "An error has occured.\n"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}*/
    }
    
    public BufferedImage getImage(){
	return img;
    }
    public InputStream getAsInputStream() throws IOException{
	ByteArrayOutputStream os = new ByteArrayOutputStream();
	ImageIO.write(img, "png", os);
	InputStream is = new ByteArrayInputStream(os.toByteArray());
	return is;
    }

}
