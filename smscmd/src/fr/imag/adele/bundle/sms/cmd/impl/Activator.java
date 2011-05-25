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

import org.osgi.framework.BundleContext;

/**
* This inherits of the GenericActivator of ServiceBinder
* to profile dynamic binding
*/

public class Activator extends org.ungoverned.gravity.servicebinder.GenericActivator {

    public void start(BundleContext context) throws Exception {
		try{
			super.start(context);	
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
    }
}

