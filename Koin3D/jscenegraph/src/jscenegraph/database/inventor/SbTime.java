/*
 *
 *  Copyright (C) 2000 Silicon Graphics, Inc.  All Rights Reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  Further, this software is distributed without any warranty that it is
 *  free of the rightful claim of any third person regarding infringement
 *  or the like.  Any license provided herein, whether implied or
 *  otherwise, applies only to this software file.  Patent licenses, if
 *  any, provided herein do not apply to combinations of this program with
 *  other software, or any other product whatsoever.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Contact information: Silicon Graphics, Inc., 1600 Amphitheatre Pkwy,
 *  Mountain View, CA  94043, or:
 *
 *  http://www.sgi.com
 *
 *  For further information regarding this notice, see:
 *
 *  http://oss.sgi.com/projects/GenInfo/NoticeExplan/
 *
 */

/*
 * Copyright (C) 1990,91   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.3 $
 |
 |   Description:
 |      This file defines the SbTime class for manipulating times
 |
 |   Classes:
 |      SbTime
 |
 |   Author(s)          : Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import jscenegraph.port.Mutable;
import net.sourceforge.jpcap.util.Timeval;

////////////////////////////////////////////////////////////////////////////////
//! Class for representation of a time.
/*!
\class SbTime
\ingroup Basics
This class represents and performs operations on time. Operations may be
done in seconds, seconds and microseconds, or using a
<tt>struct timeval</tt>
(defined in <em>/usr/include/sys/time.h</em>).

\par See Also
\par
cftime
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbTime implements Mutable {

	private Timeval t;

	// Default constructor.
	public SbTime() {
		t = new Timeval(0, 0);
	}

	// Constructor taking seconds.
	public SbTime(double sec) {
		if (sec >= 0) {
			long tv_sec = (long) (sec);
			int tv_usec = (int) (0.5 + (sec - tv_sec) * 1000000.0);
			t = new Timeval(tv_sec, tv_usec);
		} else
			this.copyFrom((new SbTime(-sec)).operator_minus());

	}

	// Constructor taking seconds + microseconds.
	public SbTime(long sec, int usec) {
		t = new Timeval(sec, usec);
	}

	// java port
	public SbTime(SbTime src) {
		t = new Timeval(src.t.getSeconds(), src.t.getMicroSeconds());
	}

	// Get a zero time.
	public static SbTime zero() {
		return new SbTime(0, 0);
	}

	// Get the current time (seconds since Jan 1, 1970).
	public static SbTime getTimeOfDay() {
		Instant now = Instant.now();
		long sec = now.getEpochSecond();
		int nanos = now.getNano();
		int usec = nanos / 1000;
		return new SbTime(sec, usec);
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Sets to the current time.
	//
	// Use: public

	public void setToTimeOfDay()
	//
	////////////////////////////////////////////////////////////////////////
	{
		// #ifdef WIN32
		this.copyFrom(SbTime.getTimeOfDay());
		// #else
		// if (-1 == gettimeofday(&t, NULL))
		// perror("gettimeofday");
		// #endif
	}

	// ! Get time in seconds as a double
	public double getValue() {
		return (double) t.getSeconds() + (double) t.getMicroSeconds() / 1000000.0;
	}

	// ! Get time in milliseconds (for Xt).
	public long getMsecValue() // System long
	{
		return t.getSeconds() * 1000 + t.getMicroSeconds() / 1000;
	}

	// ! Equality operators.
	public boolean operator_not_equal(final SbTime tm) {
		return !(this.operator_equal(tm));
	}

	// comparison operator
	public boolean operator_equal(SbTime other) {
		return t.compareTo(other.t) == 0;
	}

	// add operator (java port)
	public SbTime operator_add(SbTime t1) {
		long tm_sec = t.getSeconds() + t1.t.getSeconds();
		long tm_usec = (long) t.getMicroSeconds() + t1.t.getMicroSeconds();

		if (tm_usec >= 1000000) {
			tm_sec += 1;
			tm_usec -= 1000000;
		}

		SbTime tm = new SbTime(tm_sec, (int) tm_usec);

		return tm;
	}

	public boolean operator_less_or_equals(SbTime tm) {
		int comparison = t.compareTo(tm.t); // java port
		return comparison <= 0;
	}

	// Unary negation.
	public SbTime operator_minus() {
		return (t.getMicroSeconds() == 0) ? new SbTime(-t.getSeconds(), 0)
				: new SbTime(-t.getSeconds() - 1, 1000000 - t.getMicroSeconds());
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	//
	// Use: public

	public SbTime operator_minus(final SbTime t1)
	//
	////////////////////////////////////////////////////////////////////////
	{
		final SbTime t0 = this;

		long sec;
		long usec; // System long

		sec = t0.t.getSeconds() - t1.t.getSeconds();
		usec = t0.t.getMicroSeconds() - t1.t.getMicroSeconds();

		while (usec < 0 && sec > 0) {
			usec += 1000000;
			sec -= 1;
		}

		return new SbTime(sec, (int) usec);
	}

	// ! division by another time
	public double operator_div(final SbTime tm) {
		return getValue() / tm.getValue();
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	//
	// Use: public

	public SbTime operator_mul(double s)
	//
	////////////////////////////////////////////////////////////////////////
	{
		final SbTime tm = this;

		return new SbTime(tm.getValue() * s);
	}
	

public boolean
operator_less(final SbTime tm)
{
    if ((t.getSeconds() < tm.t.getSeconds()) ||
        (t.getSeconds() == tm.t.getSeconds() && t.getMicroSeconds() < tm.t.getMicroSeconds()))
        return true;
    else
        return false;
}	

	public boolean
operator_greater(final SbTime tm)
{
    if ((t.getSeconds() > tm.t.getSeconds()) ||
        (t.getSeconds() == tm.t.getSeconds() && t.getMicroSeconds() > tm.t.getMicroSeconds()))
        return true;
    else
        return false;
}	
	
	public boolean operator_greater_equal(SbTime tm) {
	    if ((t.getSeconds() >= tm.t.getSeconds()) ||
	            (t.getSeconds() == tm.t.getSeconds() && t.getMicroSeconds() >= tm.t.getMicroSeconds()))
	            return true;
	        else
	            return false;
	}

	
	    //! Destructive multiplication and division by scalar.
    public SbTime operator_mul_equal(double s)
        { this.copyFrom(this.operator_mul(s)); return this; }
    

	public SbTime operator_add_equal(SbTime s) {
		 this.copyFrom(this.operator_add(s)); return this; 	
	}


	public SbTime operator_minus_equal(SbTime s) {
		 this.copyFrom(this.operator_minus(s)); return this; 	
	}    

    //! Equality operators.
    public boolean                           operator_equal_equal(final SbTime tm) 
        { return (t.getSeconds() == tm.t.getSeconds()) && (t.getMicroSeconds() == tm.t.getMicroSeconds()); }

    /**
     * Java port
     * @param tm
     * @return
     */
    public boolean operator_equal_equal(float tm) {
    	return operator_equal_equal(new SbTime(tm));
    }
    

	@Override
	public void copyFrom(Object other) {
		SbTime otherTime = (SbTime) other;
		t = otherTime.t;
	}

	// ! Set time from milliseconds.
	public void setMsecValue(long msec) // System long
	{
		t = new Timeval(msec / 1000, (int) (1000 * (msec % 1000)));
	}

	// ! Convert to a string. The default format is seconds with 3 digits of
	// fraction
	// ! precision. \p fmt is a character string that consists of field
	// descriptors and
	// ! text characters, in a manner analogous to cftime (3C) and printf (3S).
	// ! Each field descriptor consists of a % character followed by another
	// character
	// ! which specifies the replacement for the field descriptor. All other
	// characters
	// ! are copied from \p fmt into the result. The following field descriptors
	// are
	// ! supported:
	// ! \code
	// ! % the `%' character
	// ! D total number of days
	// ! H total number of hours
	// ! M total number of minutes
	// ! S total number of seconds
	// ! I total number of milliseconds
	// ! U total number of microseconds
	// ! h hours remaining after the days (00-23)
	// ! m minutes remaining after the hours (00-59)
	// ! s seconds remaining after the minutes (00-59)
	// ! i milliseconds remaining after the seconds (000-999)
	// ! u microseconds remaining after the seconds (000000-999999)
	// ! \endcode
	// ! The uppercase descriptors are formatted with a leading `em' for
	// negative
	// ! times; the lowercase descriptors are formatted fixed width, with
	// leading
	// ! zeros. For example, a reasonable format string might be
	// ! "elapsedtime:%Mminutes,%sseconds". The default value of \p fmt,
	// ! "%S.%i", formats the time as seconds with 3 digits of fractional
	// precision.
	// java port
	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Converts to a formatted string. The format string supports the following:
	//
	// %% the '%' character
	// %D total number of days
	// %H total number of hours
	// %M total number of minutes
	// %S total number of seconds
	// %I total number of milliseconds
	// %U total number of microseconds
	// %h 00-23 hours remaining after the days
	// %m 00-59 minutes remaining after the hours
	// %s 00-59 seconds remaining after the minutes
	// %i 000-999 milliseconds remaining after the seconds
	// %u 000000-999999 microseconds remaining after the seconds
	//
	// uppercase descriptors are formatted with a leading '-' for negative times
	// lowercase descriptors are formatted fixed width with leading zeros
	//
	// Use: public

	public String format() {
		return format("%S.%i");
	}

	public String format(String fmtStr) {
		boolean negative;
		Timeval tv;

		// turn into sign-magnitude form
		if (t.getSeconds() >= 0) {
			negative = false;
			tv = t;
		} else {
			negative = true;
			tv = this.operator_minus().t;
		}

		// first calculate total durations
		int tday = (int) (tv.getSeconds() / (60 * 60 * 24));
		int thour = (int) (tv.getSeconds() / (60 * 60));
		int tmin = (int) (tv.getSeconds() / 60);
		int tsec = (int) (tv.getSeconds());
		int tmilli = (int) (1000 * tv.getSeconds() + tv.getMicroSeconds() / 1000);
		int tmicro = (int) (1000000 * tv.getSeconds() + tv.getMicroSeconds());

		// then calculate remaining durations
		int rhour = thour - 24 * tday;
		int rmin = tmin - 60 * thour;
		int rsec = tsec - 60 * tmin;
		int rmilli = tmilli - 1000 * tsec;
		int rmicro = tmicro - 1000000 * tsec;

		char[] buf = new char[200];
		int s = 0;
		int fmt = 0;

		for (; fmt < fmtStr.length(); fmt++) {
			if (fmtStr.charAt(fmt) != '%') {
				buf[s] = fmtStr.charAt(fmt);
				s++;
			} else {
				fmt++;
				switch (fmtStr.charAt(fmt)) {
				case 0:
					fmt--; // trailing '%' in format string
					break;

				case '%':
					buf[s] = '%'; // "%%" in format string
					s++;
					break;

				case 'D':
					if (negative) {
						buf[s] = '-';
						s++;
					}
					s += sprintf(s, "%ld", tday, buf);
					break;

				case 'H':
					if (negative) {
						buf[s] = '-';
						s++;
					}
					s += sprintf(s, "%ld", thour, buf);
					break;

				case 'M':
					if (negative) {
						buf[s] = '-';
						s++;
					}
					s += sprintf(s, "%ld", tmin, buf);
					break;

				case 'S':
					if (negative) {
						buf[s] = '-';
						s++;
					}
					s += sprintf(s, "%ld", tsec, buf);
					break;

				case 'I':
					if (negative) {
						buf[s] = '-';
						s++;
					}
					s += sprintf(s, "%ld", tmilli, buf);
					break;

				case 'U':
					if (negative) {
						buf[s] = '-';
						s++;
					}
					s += sprintf(s, "%ld", tmicro, buf);
					break;

				case 'h':
					s += sprintf(s, "%.2ld", rhour, buf);
					break;

				case 'm':
					s += sprintf(s, "%.2ld", rmin, buf);
					break;

				case 's':
					s += sprintf(s, "%.2ld", rsec, buf);
					break;

				case 'i':
					s += sprintf(s, "%.3ld", rmilli, buf);
					break;

				case 'u':
					s += sprintf(s, "%.6ld", rmicro, buf);
					break;

				default:
					buf[s] = '%';
					s++; // echo any bad '%?'
					buf[s] = fmtStr.charAt(fmt);
					s++; // specifier
				}
			}
			if (s >= buf.length - 7) // don't overshoot the buffer
				break;
		}
		buf[s] = 0;

		return new String(buf);
	}

	private int sprintf(final int s, String format, int value, char[] buf) {
		String result = String.format(format, value);
		int len = result.length();
		int sc = s;
		for (int i = 0; i < len; i++) {
			char c = result.charAt(i);
			buf[sc] = c;
			sc++;
		}
		return sc - s;
	}

	// ! Convert to a date string, interpreting the time as seconds since
	// ! Jan 1, 1970. The default format gives "Tuesday, 01/26/93 11:23:41 AM".
	// ! See the cftime() reference page for explanation of the format string.
	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Formats as an absolute date/time, using unix "strftime" mechanism.
	//
	// Use: public

	// java port
	public String formatDate() {
		return formatDate("%tA, %tD %tr");
	}

	public String formatDate(String fmt)
	//
	////////////////////////////////////////////////////////////////////////
	{
		String buf;

		Date date = t.getDate();

		// strftime(buf, sizeof(buf), fmt, localtime(&seconds));
		Formatter formatter = new Formatter(Locale.US);
		buf = formatter.format(fmt, date, date, date).toString();
		formatter.close();
		return buf;
	}

	// ! Set time from a double (in seconds).
	public void setValue(double sec) {
		long tv_sec = (long) (sec);
		int tv_usec = (int) ((sec - tv_sec) * 1000000.0);
		t = new Timeval(tv_sec, tv_usec);
	}
}
