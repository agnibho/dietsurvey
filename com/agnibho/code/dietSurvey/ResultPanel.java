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

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.GroupLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;

@SuppressWarnings("serial")
public class ResultPanel extends JPanel {
    private GroupLayout layout;

    private JEditorPane display;
    private JScrollPane scroll;

    public ResultPanel(){

	display=new JEditorPane();
	display.setContentType("text/html;charset=UTF-8");
	display.setEditorKit(new HTMLEditorKit());
	display.setEditable(false);
	scroll=new JScrollPane(display);

	layout=new GroupLayout(this);
	this.setLayout(layout);
	layout.setAutoCreateGaps(true);
	layout.setAutoCreateContainerGaps(true);
	layout.setVerticalGroup(layout.createSequentialGroup().addComponent(scroll));
	layout.setHorizontalGroup(layout.createParallelGroup().addComponent(scroll));
    }
    
    public void display(InputStream in) throws IOException{
	
	Scanner scanner = new Scanner(in,"UTF-8");
	String content=scanner.useDelimiter("\\A").next();
	scanner.close();
	display.setText(content);
	display.setCaretPosition(0);
    }
}
