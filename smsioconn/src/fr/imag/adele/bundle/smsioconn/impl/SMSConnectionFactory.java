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

package fr.imag.adele.bundle.smsioconn.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.io.Connection;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.io.ConnectionFactory;
import org.osgi.service.io.ConnectorService;

public class DataConnectionFactory implements BundleActivator, ConnectionFactory {

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
			Hashtable properties= new Hashtable();
			properties.put(IO_SCHEME, new String[] { "sms" });
			ServiceRegistration aReg =
				context.registerService(ConnectorService.class.getName(), this, properties);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		//System.out.println(this.getClass().getName()+" started");
	}

	public void stop(BundleContext context) throws Exception {
		// System.out.println(this.getClass().getName()+" stopped");
		smssender.close();
	}


	/* (non-Javadoc)
	 * @see org.osgi.service.io.ConnectionFactory#createConnection(java.lang.String, int, boolean)
	 */
	public Connection createConnection(String uri, int mode, boolean timeouts) throws IOException {
		return new SMSConnection(uri);
	}
}

class SMSConnection implements javax.microedition.io.InputConnection {
	String uri;
	SMSConnection(String uri) {
		this.uri= uri;
	}

	public DataInputStream openDataInputStream() throws IOException {
		return new DataInputStream(openInputStream());
	}

	public InputStream openInputStream() throws IOException {
		byte[] buf= uri.getBytes();
		return new ByteArrayInputStream(buf, 5, buf.length - 5);
	}
	public void close() {
	
	}
}