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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

public class Zipper {
    
    private InputStream in;
    private FileOutputStream fos;
    private ZipOutputStream zos;
    private ZipEntry ze;

    public Zipper() {
	
    }

    public void writeZip(File output, List<InputStream> files) throws IOException{
	try {
	    byte[] buffer=new byte[1024];
	    
	    fos=new FileOutputStream(output);
	    zos=new ZipOutputStream(fos);
	    for(int i=0; i<files.size(); i++){
		if(i==0)
		    ze=new ZipEntry("input.xml");
		else if(i==1)
		    ze=new ZipEntry("assessment.xml");
		else if(i==2)
		    ze=new ZipEntry("report.html");
		else if(i==3)
		    ze=new ZipEntry("bar_diagram.png");
		else if(i==4)
		    ze=new ZipEntry("style.css");
		else
		    ze=new ZipEntry(String.valueOf(i)+".xml");
		zos.putNextEntry(ze);
		in=files.get(i);
		
		int len;
		while((len=in.read(buffer))>0){
		    zos.write(buffer, 0, len);
		}
		in.close();
		zos.closeEntry();
		Desktop.getDesktop().open(output);
	    }
	    zos.close();
	} catch (FileNotFoundException e) {
	    JOptionPane.showMessageDialog(null, "An error has occured.\n"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
    }
}
