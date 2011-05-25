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

/**
* This class creates a servlet
* and provide methods to bind/unbind org.osgi.service.http.HttpService 
* and fr.imag.adele.bundle.sms.SMSSender services
*/
public class HttpBundleImpl extends AbstractBundleImpl {
	//private BundleContext					context;
	private Vector/*<org.osgi.service.http.HttpService>*/	https;
	private SMSServlet 					servlet;

	final static String WEBROOT = "/webroot";
	final static String WEBROOT_ALIAS = "/sms";
	final static String SERVLET_ALIAS = "/sms/send";

	public HttpBundleImpl(/*BundleContext context*/) {
		super(/*BundleContext context*/);
		try{
			this.https=new Vector();

			// create a servlet
			this.servlet = new SMSServlet(smssenders);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void bindHttpService(HttpService ref) {
		try{
			if(!https.contains(ref)){
				https.add(ref);
				try {

					HttpContext docsContext = new HttpContext() {
						                          public String getMimeType(String name) {
							                          return null;
						                          }

						                          public boolean handleSecurity(HttpServletRequest req,
						                                                        HttpServletResponse resp) {
							                          return true;
						                          }

						                          public URL getResource(String name) {
							                          URL u = this.getClass().getResource(name);
							                          System.out.println(this.getClass().getName());
							                          return u;
						                          }
					                          };

					ref.registerResources( WEBROOT_ALIAS, WEBROOT, docsContext );
					ref.registerServlet(SERVLET_ALIAS, servlet, null, servlet);

					trace("bindHttpService("+ref+") register servlet and ressources");

				} catch(javax.servlet.ServletException e) {
					trace("bindHttpService("+ref+") exception ("+e+")during register servlet");
				} catch(org.osgi.service.http.NamespaceException e) {
					trace("bindHttpService("+ref+") exception ("+e+")during register servlet");
				}
			} else {
				trace("bindHttpService("+ref+") but does not register servlet");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void unbindHttpService(HttpService ref) {
		try{
			trace("unbindHttpService("+ref+")");
			while(https.remove(ref)); // once in principle
			ref.unregister(SERVLET_ALIAS);
			ref.unregister(WEBROOT_ALIAS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

