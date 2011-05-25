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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.StringTokenizer;

import javax.microedition.io.Connection;
import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

import org.osgi.service.io.ConnectorService;
import org.ungoverned.gravity.servicebinder.ServiceBinderContext;
import org.ungoverned.osgi.service.shell.Command;

import fr.imag.adele.bundle.util.impl.Util;

/**
 * This class creates a shell command
 */
public class ShellCmdBundleImpl extends AbstractBundleImpl implements Command {
	public ShellCmdBundleImpl(ServiceBinderContext serviceBinderContext) {
		super(serviceBinderContext);
	}

	public String getName() {
		return "io";
	}

	public String getHelp() {
		return 	"io help                   - display this help\n"
		+		"io list                   - list factories and connectors\n"	
		+		"io read <url>             - read the open input stream\n"
		+		"io write <url> <message>  - write into the open output stream";
	}

	public String getUsage() {
		return 	"io [help|list|read|write]";
	}

	
	public String getShortDescription() {
		return "read/write message from/to an open connection";
	}

	public void execute(String s, PrintStream out, PrintStream err) {
		StringTokenizer st = new StringTokenizer(s, " ");

		// Ignore the command name.
		st.nextToken();

		boolean optionIsAll = false;
		String url = null;
		String option = null;

		if (st.hasMoreTokens()) {
			option = st.nextToken();
		} else {
			err.println(getUsage());
			return;
		}

		if(option.equals("help")){
			out.println(getHelp());
			return;			
		} else if(option.equals("list")){
	        // list the ConnectionFactories
	        out.println("==========================================");
	        out.println("org.osgi.service.io.ConnectionFactory");
	        out.println("==========================================");
	        try {
				Util.listServices(bundleContext,
				        "(objectClass=org.osgi.service.io.ConnectionFactory)");
			} catch (Exception e) {
				e.printStackTrace();
			}

	        // list the ConnectorServices
	        out.println("==========================================");
	        out.println("org.osgi.service.io.ConnectorService");
	        out.println("==========================================");
	        try {
				Util.listServices(bundleContext,
				        "(objectClass=org.osgi.service.io.ConnectorService)");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;			
			
		} else if(option.equals("read")){
			if (st.hasMoreTokens()) {
				url = st.nextToken();
			}

			if (url == null) {
				err.println(getUsage());
				return;
			}

			Connection connection = null;
			try {
				connection = connectorService.open(url, ConnectorService.READ,
						false);
				if (connection != null) {
					if (connection instanceof OutputConnection) {
						DataInputStream is = ((InputConnection) connection)
								.openDataInputStream();
						String str = is.readUTF();
						out.println(str);
					}
					connection.close();
				} else {
					err.println("No connection for this scheme");
				}
			} catch (IOException e) {
				e.printStackTrace(err);
			}			
		} else if(option.equals("write")){
			if (st.hasMoreTokens()) {
				url = st.nextToken();
			}

			String message = "";

			// message extraction should be improved
			while (st.hasMoreTokens()) {
				message = message + " " + st.nextToken();
			}

			if (url == null || message == null) {
				err.println(getUsage());
				return;
			}

			Connection connection = null;
			try {
				connection = connectorService.open(url, ConnectorService.WRITE,
						false);
				if (connection != null) {
					if (connection instanceof OutputConnection) {
						DataOutputStream os = ((OutputConnection) connection)
								.openDataOutputStream();
						os.writeUTF(message);
					}
					connection.close();
				} else {
					err.println("No connection for this scheme");
				}
			} catch (IOException e) {
				e.printStackTrace(err);
			}
			
		} else {
			err.println(getUsage());
			return;			
		}
		
	}
}

