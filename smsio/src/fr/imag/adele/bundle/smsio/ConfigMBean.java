/*
 *  ConfigMBean Test Bundle
 *
 * provides a service with a MBean interface to (re)configure the internal state of the object
 *
 * Copyright (C) 2004  Didier Donsez (didier.donsez@ieee.org)
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
 * Contact: Didier Donsez (didier.donsez@ieee.org)
 * Contributor(s):
 *
**/

package fr.imag.adele.bundle.smsio;

/**
 * The MBean interface provides a service for configuring the instance with a JMX bundle
 * @author Didier Donsez (didier.donsez@ieee.org)
 */
public interface ConfigMBean {
	
	/**
	 * set the sender number (+33612344567 for France)
	 */
	public void setSender(String sender);

	/**
	 * get the current sender number
	 */
	public String getSender();
	

	/**
	 * set the trace
	 */
	public void setTrace(boolean trace);

	/**
	 * get the trace
	 */
	public boolean getTrace();

	
	/**
	 * reset the configuration
	 */
	public void reset();	
}

