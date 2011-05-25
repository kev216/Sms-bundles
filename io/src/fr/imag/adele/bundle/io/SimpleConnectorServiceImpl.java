/*
 * A simple Implementation of IO Connector Service
 * Specifications OSGi v 3, chapter 13
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
package fr.imag.adele.bundle.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import javax.microedition.io.Connection;
import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.io.ConnectionFactory;
import org.osgi.service.io.ConnectorService;

/**
 * This activator implements 
 *
 * @version 	0.1, 9/04/2003 
 * @author 	Didier Donsez (Didier.Donsez@ieee.org)
 */

public class SimpleConnectorServiceImpl
	implements ConnectorService, BundleActivator {

	protected BundleContext bundleContext = null;

	/**
	 * Called upon starting of the bundle.
	 *
	 * @param   context  The bundle context passed by the framework
	 * @exception   Exception
	 */
	public void start(BundleContext bundleContext) throws BundleException {
		this.bundleContext = bundleContext;
		bundleContext.registerService(
			ConnectorService.class.getName(),
			this,
			null);
	}

	public void stop(BundleContext bundleContext) throws BundleException {
	}

	/**
	 * choose the "best" connection factory for the scheme of the URL
	 * Choice rule : 1) highest service.ranking 2) si equals lowest service.id
	 */
	private ServiceReference getBestConnectionFactoryServiceReference(String scheme)
		throws org.osgi.framework.InvalidSyntaxException {
		ServiceReference cfsr = null;
		Long serviceId = null;
		Integer serviceRanking = null;
		ServiceReference[] srs =
			bundleContext.getServiceReferences(
				ConnectionFactory.class.getName(),
				null);
		if (srs == null)
			return null;

		for (int s = 0; s < srs.length; s++) {
			Object o = srs[s].getProperty(ConnectionFactory.IO_SCHEME);
			if(o==null || !(o instanceof String[])) continue;
			String[] schemes = (String[]) o;
				boolean found = false;
				for (int i = 0; i < schemes.length && !found; i++)
					if (scheme.equals(schemes[i]))
						found = true;
				if (!found)
					continue;

				Long m_serviceId =
					(Long) srs[s].getProperty(Constants.SERVICE_ID);
				Integer m_serviceRanking =
					(Integer) srs[s].getProperty(Constants.SERVICE_RANKING);
				if (cfsr == null) {
					cfsr = srs[s];
					serviceId = m_serviceId;
					serviceRanking = m_serviceRanking;
					continue;
				}
				if (m_serviceRanking == null)
					continue;

				if (serviceRanking != null) {
					int m_src = m_serviceRanking.intValue();
					int m_srp = serviceRanking.intValue();
					if (m_srp >= m_src)
						continue;
					if (m_src == m_srp) {
						if (m_serviceId == null)
							continue;

						if (serviceId != null) {
							long m_sic = m_serviceId.longValue();
							long m_sip = serviceId.longValue();
							if (m_sip <= m_sic)
								continue;
						}
					}
				}

				cfsr = srs[s];
				serviceId = m_serviceId;
				serviceRanking = m_serviceRanking;
		}

		return cfsr;

	}

	/* (non-Javadoc)
	 * @see org.osgi.service.io.ConnectorService#open(java.lang.String)
	 */
	public Connection open(String name) throws IOException {
		// check if mode=READ_WRITE is correct
		// check if timeouts=false is correct
		return open(name, READ_WRITE, false);
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.io.ConnectorService#open(java.lang.String, int)
	 */
	public Connection open(String name, int mode) throws IOException {
		// check if timeouts=false is correct
		return open(name, mode, false);
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.io.ConnectorService#open(java.lang.String, int, boolean)
	 */
	public Connection open(String uri, int mode, boolean timeouts)
		throws IOException {
		ServiceReference sr;
		// get the scheme
		int doubledotpos = uri.indexOf(":");
		if (doubledotpos == -1)
			throw new MalformedURLException();
		String scheme = uri.substring(0, doubledotpos);

		// get the "best" Connection factory
		try {
			sr = getBestConnectionFactoryServiceReference(scheme);
		} catch (InvalidSyntaxException e) {
			throw new IOException(e.toString());
		}
		if (sr == null)
			throw new IOException("No ConnectionFactory for this scheme");

		// create a connexion
		ConnectionFactory cf = (ConnectionFactory) bundleContext.getService(sr);
		Connection cnx = cf.createConnection(uri, mode, timeouts);

		// release the service
		bundleContext.ungetService(sr);
		return cnx;
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.io.ConnectorService#openInputStream(java.lang.String)
	 */
	public InputStream openInputStream(String name) throws IOException {
		InputConnection cnx = (InputConnection) open(name, READ);
		if (cnx == null)
			return null;
		return cnx.openInputStream();
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.io.ConnectorService#openDataInputStream(java.lang.String)
	 */
	public DataInputStream openDataInputStream(String name)
		throws IOException {
		InputConnection cnx = (InputConnection) open(name, READ);
		if (cnx == null)
			return null;
		return cnx.openDataInputStream();
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.io.ConnectorService#openOutputStream(java.lang.String)
	 */
	public OutputStream openOutputStream(String name) throws IOException {
		OutputConnection cnx = (OutputConnection) open(name, WRITE);
		if (cnx == null)
			return null;
		return cnx.openOutputStream();
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.io.ConnectorService#openDataOutputStream(java.lang.String)
	 */
	public DataOutputStream openDataOutputStream(String name)
		throws IOException {
		OutputConnection cnx = (OutputConnection) open(name, WRITE);
		if (cnx == null)
			return null;
		return cnx.openDataOutputStream();
	}
}
