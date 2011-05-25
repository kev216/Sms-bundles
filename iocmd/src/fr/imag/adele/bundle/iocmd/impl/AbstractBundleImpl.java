/*
 * IO Command
 * Copyright (C) 2005  Didier Donsez
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
package fr.imag.adele.bundle.iocmd.impl;

import org.osgi.framework.BundleContext;
import org.osgi.service.io.ConnectorService;
import org.ungoverned.gravity.servicebinder.ServiceBinderContext;
/**
* This class provides methods to bind/unbind fr.imag.adele.bundle.sms.SMSSender services
*/

abstract public class AbstractBundleImpl {
	//private BundleContext					context;
	protected ConnectorService connectorService;

	protected BundleContext bundleContext;
	
	public AbstractBundleImpl(ServiceBinderContext serviceBinderContext) {
		this.bundleContext=serviceBinderContext.getBundleContext();
		this.connectorService=null;
	}

	final public void bindService(ConnectorService ref){
		connectorService=ref;
	}

	final public void unbindService(ConnectorService ref){
		connectorService=null;
	}
}

