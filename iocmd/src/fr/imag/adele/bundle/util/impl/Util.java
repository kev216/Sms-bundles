/*
 * OSGi utility class
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

package fr.imag.adele.bundle.util.impl;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * This class helps to list services
 *
 * @version 	1.00 22/03/2003 
 * @author 	Didier Donsez (Didier.Donsez@ieee.org)
 */

public class Util {
	/**
	 * list a subset of services and their properties
	 */
	public static void listServices(BundleContext bundleContext, String filter)
		throws Exception {
		ServiceReference[] tempRefs = null;
		tempRefs = bundleContext.getServiceReferences(null, filter);

		if (tempRefs != null) {
			for (int i = 0; i < tempRefs.length; i++) {
				ServiceReference sr = tempRefs[i];

				System.out.print("Bundle#" + sr.getBundle().getBundleId());
				System.out.print("," + sr.getClass().getName());
				System.out.println(":");

				String[] keys = sr.getPropertyKeys();
				for (int j = 0; j < keys.length; j++) {
					System.out.print("\t" + keys[j] + "=");
					Object value = sr.getProperty(keys[j]);
					if (value.getClass().isArray()) {
						Object[] array = (Object[]) value;
						int k = 0;
						while (k < array.length) {
							System.out.print(array[k++]);
							if (k < array.length)
								System.out.print(',');
						}
						System.out.println();
					} else {
						System.out.println(value);
					}
				}
				sr = null;
			}
			tempRefs = null;
		} else {
			System.out.println("No Service with the filter:" + filter);
		}
	}
}