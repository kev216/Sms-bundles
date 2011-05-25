/*
 * SMS Servlet
 * servlet to send SMS with fr.imag.adele.bundle.sms.SMSSender service.
 * Copyright (C) 2003  Didier Donsez
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Contact: Didier Donsez (Didier.Donsez@ieee.org)
 * Contributor(s):
 *
**/
package fr.imag.adele.bundle.sms.servlet.impl;

import org.osgi.framework.*;
import org.osgi.service.http.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.net.*;

import fr.imag.adele.bundle.sms.SMSSender;

class SMSServlet extends HttpServlet implements HttpContext {

	private Vector smssenders = null;

	public SMSServlet(Vector smssenders)
	{
		trace("Constructor");
		this.smssenders = smssenders;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		doGet(request, response);
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		trace("doGet/doPost");


		String action	= (String) request.getParameter("action");
		String sender	= (String) request.getParameter("sender");
		String receiver = (String) request.getParameter("receiver");
		String message = (String) request.getParameter("message");

		String servletcontextpath=request.getContextPath();

		boolean optionIsAll=false;

		if(action==null || !action.equals("form")){
			response.setContentType("text/html");

			PrintStream out = new PrintStream(response.getOutputStream());
			out.println("<html>");
			out.println("<head><title>SMSSender</title></head>");
			out.println("<body>");
			out.println("<font face='Verdana,Helvetica,Arial'>");

			if(		sender==null	|| sender.equals("")
			                || 	receiver==null	|| receiver.equals("")
			                ||	message==null	|| message.equals("")
			  ){

				out.println("<h1><font color='red'>Error</font></h1>");

				if(sender==null	|| sender.equals("")) {
					out.println("Sender GSM phone number is missing<BR>");
				}
				if(receiver==null	|| receiver.equals("")) {
					out.println("Receiver GSM phone number is missing<BR>");
				}
				if(message==null	|| message.equals("")) {
					out.println("Message is empty<BR>");
				}


			} else if(action!=null && !action.equals("send")){
				out.println("<h1><font color='red'>Error</font></h1>");
				out.println("Unknown action<BR>");
			} else {


				boolean sentToFirst=false;
				for (Enumeration e = smssenders.elements() ; e.hasMoreElements() ;) {
					SMSSender smssender=(SMSSender) e.nextElement();
					if(!sentToFirst) {
						if(smssender.send(message,receiver,sender)) {
							out.println("<h1><font color='green'>Success</font></h1>");
							out.println("Message \""+message+"\"\n sent from "+sender+" to "+receiver);
						} else {
							out.println("<h1><font color='red'>Error</font></h1>");
							out.println("Error occurs while sending message \""+message+"\"\n from "+sender+" to "+receiver);
						}
						if(optionIsAll) sentToFirst=true;
					}
				}
				out.println("</font>");

				// button to return back in the history
				out.println("<center><input type=button Value='Back to form' onClick='history.back()'></center>");

				out.println("</body>");
				out.println("</html>");
			}
		} else {
			response.setContentType("text/xml");

			PrintStream out = new PrintStream(response.getOutputStream());

			out.println("<?xml version='1.0' encoding='ISO-8859-1' ?>");
			out.println("<?xml-stylesheet type='text/xsl' href='/sms/smsform.xsl'?>");
			out.println("<sms>");
			out.println("<sender>"+(sender==null?"+":sender)+"</sender>");
			out.println("<receiver>"+(receiver==null?"+":receiver)+"</receiver>");
			out.println("<message>"+(message==null?"":message)+"</message>");
			out.println("</sms>");
		}
	}
	//
	// HttpContext interface methods.
	//

	public boolean handleSecurity(
	        HttpServletRequest request, HttpServletResponse response)
	throws IOException
	{
		return true;
	}

	public URL getResource(String name) {
		try {
			return this.getClass().getResource(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public String getMimeType(String name)
	{
		return null;
	}

	//--------------
	private boolean debug=true;
	public void trace(String msg){
		if(debug){ System.err.println(getClass().getName()+":"+msg); }
	}
}

