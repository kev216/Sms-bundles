/*
 * SMS Command
 * command to send SMS with fr.imag.adele.bundle.sms.SMSSender service.
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
package fr.imag.adele.bundle.sms.cmd.impl;

import java.util.Vector;

import fr.imag.adele.bundle.sms.SMSSender;
/**
* This class provides methods to bind/unbind fr.imag.adele.bundle.sms.SMSSender services
*/

abstract public class AbstractBundleImpl {
	//private BundleContext					context;
	protected Vector/*<fr.imag.adele.bundle.sms.SMSSender>*/		smssenders;

	public AbstractBundleImpl(/*BundleContext context*/) {
		//this.context=context;
		this.smssenders=new Vector();
	}


	final public void bindService(SMSSender ref){
		try{
			println("bindService("+ref+")");
			if(!smssenders.contains(ref)){ smssenders.add(ref); }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	final public void unbindService(SMSSender ref){
		try{
			println("unbindService("+ref+")");
			while(smssenders.remove(ref)); // once in principle
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	//--------------
	private boolean debug=true;
	final protected void println(String msg){
		if(debug){ System.err.println(getClass().getName()+":"+msg); }
	}
}

