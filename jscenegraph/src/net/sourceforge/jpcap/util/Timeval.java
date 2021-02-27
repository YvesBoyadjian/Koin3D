/**
 * @author Yves Boyadjian
 *
 */
/**************************************************************************
 * SBIR Data Rights (DFARS 252.227-7018)
 * Contract No.: W31P4Q-07-C-0022 
 * Contractor Name: Applied Visions, Inc.
 * Address: 6 Bayview Ave, Northport, NY 11768
 * Expiration of SBIR Rights Period: April 14, 2015 or 5 years after 
 * contract termination, whichever is later. 
 *
 * The Government’s rights to use, modify, reproduce, release, perform,
 * display or disclose technical data or computer software marked with
 * this legend are restricted during the period shown as provided in 
 * paragraph (b)(4) of the Rights in Noncommercial Technical Data and 
 * Computer Software – Small Business Innovation Research (SBIR) Program 
 * clause in the above identified contract. No restrictions apply after 
 * the expiration date shown above. Any reproduction of technical data, 
 * computer software, or portions thereof marked with this legend must 
 * also reproduce the markings.
 *
 * Copyright (c) 2009 Applied Visions, Inc. All Rights Reserved.
 * Author: Applied Visions, Inc. - KennyP
 * Project: MeerCAT
 * SubSystem: net.sourceforge.jpcap.util
 * FileName: Timeval.java
 *************************************************************************/
/*
**  $Id: Timeval.java,v 1.2 2008/04/04 23:09:54 matthewe Exp $
**
**  Copyright (C) 2001, Patrick Charles and Jonas Lehmann
**  Copyright (C) 2008, Secure Decisions, a division of Applied Visions, Inc.
**  
**  Distributed under the Mozilla Public License 1.1
**  http://www.mozilla.org/NPL/MPL-1.1.txt
*/
package net.sourceforge.jpcap.util;

import java.util.Date;


/**
 * POSIX.4 timeval for Java.
 * <p>
 * Container for java equivalent of c's struct timeval.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.2 $
 * @lastModifiedBy $Author: matthewe $
 * @lastModifiedAt $Date: 2008/04/04 23:09:54 $
 */
public class Timeval implements Comparable<Timeval>
{
  public Timeval(long seconds, int microseconds) {
    this.seconds = seconds;
    this.microseconds = microseconds;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(seconds);
    sb.append('.');
    sb.append(microseconds);
    sb.append('s');

    return sb.toString();
  }

  public int compareTo(Timeval tv) {
      if (tv == null)
          return 1;
      if (seconds < tv.getSeconds())
          return -1;
      if (seconds > tv.getSeconds())
          return 1;
      if (microseconds < tv.getMicroSeconds())
          return -1;
      if (microseconds > tv.getMicroSeconds())
          return 1;
      return 0;
  }
  
  /**
   * Convert this timeval to a java Date.
   */
  public Date getDate() {
    return new Date(seconds * 1000 + microseconds / 1000);
  }

  public long getSeconds() {
    return seconds;
  }

  public int getMicroSeconds() {
    return microseconds;
  }

  private long seconds;
  private int microseconds;

  public static final String _rcsid = 
  "$Id: Timeval.java,v 1.2 2008/04/04 23:09:54 matthewe Exp $";
}