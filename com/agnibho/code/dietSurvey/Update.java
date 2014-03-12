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

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Update {

    public static final String VERSION="1.0.1";
    public static final String UPDATE_URL="http://cdn.agnibho.com/java/diet-survey/version/";
    
    public enum Status {NOT_CHECKED, UP_TO_DATE, NEW_VERSION_AVAILABLE}

    public static String updateVersion;
    public static Status currentStatus=Status.NOT_CHECKED;

    private Update() {

    }

    public static void checkUpdate() throws IOException, TransformerConfigurationException, SAXException, ParserConfigurationException, XPathExpressionException {
	URL url=new URL(Update.UPDATE_URL);
	InputStream in=url.openStream();
	Document doc=new DocHandler().getDocument(in);
	updateVersion=DocHandler.getXpInstance().evaluate("/version/current", doc);
	in.close();

	int[] usingVer=new int[3];
	int[] updateVer=new int[3];

	String[] array;
	array=VERSION.split("\\.");
	usingVer[0]=Integer.parseInt(array[0]);
	usingVer[1]=Integer.parseInt(array[1]);
	usingVer[2]=Integer.parseInt(array[2]);
	array=updateVersion.split("\\.");
	updateVer[0]=Integer.parseInt(array[0]);
	updateVer[1]=Integer.parseInt(array[1]);
	updateVer[2]=Integer.parseInt(array[2]);

	if(usingVer[0]<updateVer[0]){
	    currentStatus=Status.NEW_VERSION_AVAILABLE;
	    showUpdate();
	}
	else if(usingVer[1]<updateVer[1]){
	    currentStatus=Status.NEW_VERSION_AVAILABLE;
	    showUpdate();
	}
	else if(usingVer[2]<updateVer[2]){
	    currentStatus=Status.NEW_VERSION_AVAILABLE;
	    showUpdate();
	}
	else{
	    currentStatus=Status.UP_TO_DATE;
	}
    }
    public static void showUpdate(){
	int ret=JOptionPane.showConfirmDialog(null, "Version "+updateVersion+" is available.\nVisit the download page now?", "New Version Available", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
	if(ret==JOptionPane.YES_OPTION){
	    try {
		Desktop.getDesktop().browse(new URL("http://code.agnibho.com/diet-survey/").toURI());
	    } catch (IOException | URISyntaxException e) {
		e.printStackTrace();
	    }
	}
    }
    public static void showUpToDate(){
	JOptionPane.showMessageDialog(null, "The program is up to date.", "Up to date", JOptionPane.INFORMATION_MESSAGE);
    }

}
