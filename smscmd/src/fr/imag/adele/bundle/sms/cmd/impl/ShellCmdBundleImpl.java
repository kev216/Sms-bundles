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

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.StringTokenizer;

import org.ungoverned.osgi.service.shell.Command;

import fr.imag.adele.bundle.sms.SMSSender;

/**
 * This class creates a shell command
 */
public class ShellCmdBundleImpl extends AbstractBundleImpl implements Command {
	//private BundleContext context;

	public ShellCmdBundleImpl(/* BundleContext context */) {
		super(/* BundleContext context */);
	}

	public String getName() {
		return "sms";
	}

	public String getUsage() {
		return "sms [-a] <sender> <receiver> <message>";
	}

	public String getShortDescription() {
		return "send messages with GSM connected to the gateway. (sender and receiver are international gsm phone numbers, for instance +336751234567)";
	}

	public void execute(String s, PrintStream out, PrintStream err) {
		StringTokenizer st = new StringTokenizer(s, " ");

		// Ignore the command name.
		st.nextToken();

		boolean optionIsAll = false;
		String sender = null;
		String receiver = null;
		String message = "";

		/*
		 * if(st.hasMoreTokens()){ String arg=st.nextToken();
		 * if(arg.equals("-a")) { optionIsAll=true; } else { } } else { }
		 */
		if (st.hasMoreTokens()) {
			sender = st.nextToken();
		}
		if (st.hasMoreTokens()) {
			receiver = st.nextToken();
		}

		// message extraction should be improved
		while (st.hasMoreTokens()) {
			message = message + " " + st.nextToken();
		}

		if (sender == null || receiver == null || message == null) {
			out.println(getUsage());
			return;
		}

		boolean sentToFirst = false;
		for (Enumeration e = smssenders.elements(); e.hasMoreElements();) {
			SMSSender smssender = (SMSSender) e.nextElement();
			if (!sentToFirst) {
				if (smssender.send(message, receiver, sender)) {
					out.println("Message \"" + message + "\"\n sent from "
							+ sender + " to " + receiver);
				} else {
					out.println("Error occurs while sending message \""
							+ message + "\"\n from " + sender + " to "
							+ receiver);
				}
				if (optionIsAll)
					sentToFirst = true;
			}
		}
	}
}

