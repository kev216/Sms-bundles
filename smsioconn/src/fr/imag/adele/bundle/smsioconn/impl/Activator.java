/*
 * Simple SMS Sender
 * send SMS with a GSM phone connected to the gateway
 * this bundle wrap the SMS library from http://smsj.sourceforge.org
 *
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
package fr.imag.adele.bundle.sms.impl;

import org.osgi.framework.*;
import java.io.*;
import java.util.*;
import fr.imag.adele.bundle.sms.SMSSender;

/**
 * This activator read configuration properties from a file
 * and initialize a SMSSender service with these properties
 * and register it
 *
 * @version 	1.00 22/03/2003 
 * @author 	Didier Donsez (Didier.Donsez@ieee.org)
 */


public class Activator implements BundleActivator {

	private static final String CONFIGFILE="/config.properties";

	private SMSSenderImpl smssender;

	protected BundleContext context=null;

	/**
	 * Called upon starting of the bundle. This method invokes initialize() which
	 * load the configuration file, creates the dependency managers and registers the
	 * eventual services.
	 *
	 * @param   context  The bundle context passed by the framework
	 * @exception   Exception
	 */
	public void start(BundleContext context) throws Exception
	{
		this.context=context;
		initialize();
	}

	/**
	 * Load the configuration properties and register e new service
	 */
	private void initialize() throws Exception {

		if(context==null)
			return;

		// Get the Config-Location value from the manifest

		String configLocation=null;
		Properties props=null;
		Dictionary dict=context.getBundle().getHeaders();
		Enumeration enum=dict.keys();
		while(enum.hasMoreElements()){
			Object nextKey=enum.nextElement();
			Object nextElem=dict.get(nextKey);
			if (nextKey.equals("Config-Location")){
				configLocation=nextElem.toString();
				break;
			}
		}
		if(configLocation==null) {
			configLocation=CONFIGFILE;
		}
		// System.out.println(this.getClass().getName()+" starting");

		// Load properties from configLocation file
		// and create and register a service
		InputStream sourcestream = getClass().getResourceAsStream(configLocation);		
		smssender=new SMSSenderImpl(sourcestream);
		//sourcestream.close(sourcestream);
		try {
			Hashtable attr = new Hashtable();
			attr.put( "Description", "A service to send SMS messages" );
			ServiceRegistration aReg =
			        context.registerService("fr.imag.adele.bundle.sms.SMSSender", smssender, attr);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		//System.out.println(this.getClass().getName()+" started");
	}

	public void stop(BundleContext context) throws Exception {
		// System.out.println(this.getClass().getName()+" stopped");
		smssender.close();
	}
}
