/*
 * Simple SMS Sender
 * send SMS with a GSM phone connected to the gateway
 * this bundle wrap the SMS library from http://smsj.sourceforge.net
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

import java.io.*;
import java.util.*;
import org.marre.sms.*;
import org.marre.sms.transport.gsm.*;

public class SMSSenderImpl implements fr.imag.adele.bundle.sms.SMSSender {
	static String DEFAULT_PROPERTIES_FILE="config.properties";

	private static Properties loadProperties(String filename) {
		if(filename==null) filename=System.getProperty("fr.imag.adele.bundle.util.configuration.file",DEFAULT_PROPERTIES_FILE);
		try{
			return loadProperties(new FileInputStream(filename));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private static Properties loadProperties(InputStream in) {
		Properties	prop=new Properties();
		try{
			prop.load(in);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return prop;
	}

	private SmsSender ss;

	private void _SMSSenderImpl(Properties props) {
		try {
			ss=new SmsSender(props.getProperty("sms.transportclassname"), props);
		} catch (Exception ex){
			ex.printStackTrace();
			return;
		}
	}

	public SMSSenderImpl(Properties props) {
		_SMSSenderImpl(props);
	}

	public SMSSenderImpl(String filename) {
		Properties props=loadProperties(filename);
		_SMSSenderImpl(props);
	}

	public SMSSenderImpl(InputStream in) {
		Properties props=loadProperties(in);
		_SMSSenderImpl(props);
	}

	public boolean send(String message, String receiver, String sender) {
		try{
			ss.sendTextSms(message,receiver,sender);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void close() {
		try{
			ss.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}



