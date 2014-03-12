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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DocHandler {
    static final String JAXP_SCHEMA_LANGUAGE="http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA="http://www.w3.org/2001/XMLSchema";
    static final String JAXP_SCHEMA_SOURCE ="http://java.sun.com/xml/jaxp/properties/schemaSource";
    
    private DocumentBuilderFactory dbf;
    private DocumentBuilder db;
    private static XPathFactory xpf;
    
    private TransformerFactory tff;
    private Transformer tf;

    public DocHandler() throws ParserConfigurationException, TransformerConfigurationException {
	dbf=DocumentBuilderFactory.newInstance();
	db=dbf.newDocumentBuilder();
	xpf=XPathFactory.newInstance();
	
	tff=TransformerFactory.newInstance();
	tf=tff.newTransformer();
	tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	tf.setOutputProperty(OutputKeys.INDENT, "yes");
    }

    public Document getDocInstance(){
	return db.newDocument();
    }
    public Document getDocument(InputStream in) throws SAXException, IOException{
	return db.parse(in);
    }
    public Document getDocument(File file) throws SAXException, IOException{
	return db.parse(file);
    }
    public Document getValidatedDocument(File file, InputStream schema) throws ParserConfigurationException, SAXException, IOException{
	DocumentBuilderFactory vDbf=DocumentBuilderFactory.newInstance();
	vDbf.setValidating(true);
	vDbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
	vDbf.setAttribute(JAXP_SCHEMA_SOURCE, schema);
	DocumentBuilder vDb=vDbf.newDocumentBuilder();
	vDb.setErrorHandler(new EH());
	Document doc=vDb.parse(file);
	return doc;
    }
    public static XPath getXpInstance(){
	return xpf.newXPath();
    }
    public void writeToDisk(Document doc, File file) throws TransformerException{
	DOMSource source=new DOMSource(doc);
	StreamResult result=new StreamResult(file);
	tf.transform(source, result);
    }
    public InputStream getAsInputStream(Document doc) throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError, UnsupportedEncodingException{
	DOMSource source=new DOMSource(doc);
	StringWriter writer=new StringWriter();
	StreamResult result=new StreamResult(writer);
	tf.transform(source, result);
	ByteArrayInputStream inputStream = new ByteArrayInputStream(writer.toString().getBytes("UTF-8"));
	return inputStream;
    }
    public InputStream generateSummary(Document doc) throws TransformerException, UnsupportedEncodingException{
	DOMSource source=new DOMSource(doc);
	StringWriter writer=new StringWriter();
	StreamResult result=new StreamResult(writer);
	Transformer trans=tff.newTransformer(new StreamSource(java.lang.ClassLoader.getSystemResourceAsStream("com/agnibho/code/resources/summary.xsl")));
	trans.transform(source, result);
	ByteArrayInputStream inputStream = new ByteArrayInputStream(writer.toString().getBytes("UTF-8"));
	return inputStream;
    }
    public InputStream generateReport(Document input, Document assess) throws TransformerException, UnsupportedEncodingException{
	DOMSource source=new DOMSource(combine(input, assess));
	StringWriter writer=new StringWriter();
	StreamResult result=new StreamResult(writer);
	Transformer trans=tff.newTransformer(new StreamSource(java.lang.ClassLoader.getSystemResourceAsStream("com/agnibho/code/resources/style.xsl")));
	trans.transform(source, result);
	ByteArrayInputStream inputStream = new ByteArrayInputStream(writer.toString().getBytes("UTF-8"));
	return inputStream;
    }
    public Document combine(Document in1, Document in2){
	Document out=getDocInstance();
	out.appendChild(out.createElement("ds"));
	out.getFirstChild().appendChild(out.importNode(in1.getFirstChild(), true));
	out.getFirstChild().appendChild(out.importNode(in2.getFirstChild(), true));
	return out;
    }
    
    private class EH implements ErrorHandler {

	@Override
	public void error(SAXParseException exception) throws SAXException {
	    throw new SAXException();
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
	    throw new SAXException();
	}

	@Override
	public void warning(SAXParseException exception) throws SAXException {
	    throw new SAXException();
	}
    }
}
