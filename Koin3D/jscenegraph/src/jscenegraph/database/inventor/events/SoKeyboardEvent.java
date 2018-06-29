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
 * Copyright (C) 1990,91,92   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.2 $
 |
 |   Classes:
 |      SoKeyboardEvent
 |
 |   Author(s): David Mott, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.events;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;


////////////////////////////////////////////////////////////////////////////////
//! Keyboard key press and release events.
/*!
\class SoKeyboardEvent
\ingroup Errors
SoKeyboardEvent represents keyboard key press and release events
in the Inventor event model.

\par See Also
\par
SoEvent, SoButtonEvent, SoLocation2Event, SoMotion3Event, SoMouseButtonEvent, SoSpaceballButtonEvent, SoHandleEventAction, SoEventCallback, SoSelection, SoInteraction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author Yves Boyadjian
 *
 */
public class SoKeyboardEvent extends SoButtonEvent
{
    //! the keyboard keys...
    public enum Key
    {
        //! Special constant for any key
        ANY ( 0), 


        //! Modifiers

        LEFT_SHIFT ( 0xffe1), 
        RIGHT_SHIFT ( 0xffe2), 
        LEFT_CONTROL ( 0xffe3), 
        RIGHT_CONTROL ( 0xffe4), 
        LEFT_ALT ( 0xffe9), 
        RIGHT_ALT ( 0xffea), 
        
        
        //! Numbers
        
        NUMBER_0 ( 0x030), 
        NUMBER_1 ( 0x031), 
        NUMBER_2 ( 0x032), 
        NUMBER_3 ( 0x033), 
        NUMBER_4 ( 0x034), 
        NUMBER_5 ( 0x035), 
        NUMBER_6 ( 0x036), 
        NUMBER_7 ( 0x037), 
        NUMBER_8 ( 0x038), 
        NUMBER_9 ( 0x039), 
        
        
        //! Letters
        
        A ( 0x061), 
        B ( 0x062), 
        C ( 0x063), 
        D ( 0x064), 
        E ( 0x065), 
        F ( 0x066), 
        G ( 0x067), 
        H ( 0x068), 
        I ( 0x069), 
        J ( 0x06a), 
        K ( 0x06b), 
        L ( 0x06c), 
        M ( 0x06d), 
        N ( 0x06e), 
        O ( 0x06f), 
        P ( 0x070), 
        Q ( 0x071), 
        R ( 0x072), 
        S ( 0x073), 
        T ( 0x074), 
        U ( 0x075), 
        V ( 0x076), 
        W ( 0x077), 
        X ( 0x078), 
        Y ( 0x079), 
        Z ( 0x07a), 
        
        
        //! Cursor control & motion
        
        HOME ( 0xff50), 
        LEFT_ARROW ( 0xff51), 
        UP_ARROW ( 0xff52), 
        RIGHT_ARROW ( 0xff53), 
        DOWN_ARROW ( 0xff54), 
        PAGE_UP ( 0xff55), 
        PAGE_DOWN ( 0xff56), 
        PRIOR ( 0xff55), 
        NEXT ( 0xff56), 
        END ( 0xff57), 
        
                
        //! Keypad Functions
        
        PAD_ENTER ( 0xff8d), 
        PAD_F1 ( 0xff91), 
        PAD_F2 ( 0xff92), 
        PAD_F3 ( 0xff93), 
        PAD_F4 ( 0xff94), 
        
        PAD_0 ( 0xff9e),
        PAD_1 ( 0xff9c), 
        PAD_2 ( 0xff99), 
        PAD_3 ( 0xff9b), 
        PAD_4 ( 0xff96), 
        PAD_5 ( 0xff9d), 
        PAD_6 ( 0xff98), 
        PAD_7 ( 0xff95), 
        PAD_8 ( 0xff97), 
        PAD_9 ( 0xff9a), 
        
        PAD_ADD ( 0xffab), 
        PAD_SUBTRACT ( 0xffad), 
        PAD_MULTIPLY ( 0xffaa), 
        PAD_DIVIDE ( 0xffaf), 
        
        PAD_SPACE ( 0xff8d), 
        PAD_TAB ( 0xff89),
        PAD_INSERT ( 0xff9e), 
        PAD_DELETE ( 0xff9f), 
        PAD_PERIOD ( 0xff9f), 
        
        //! Function keys
        
        F1 ( 0xffbe), 
        F2 ( 0xffbf), 
        F3 ( 0xffc0), 
        F4 ( 0xffc1), 
        F5 ( 0xffc2), 
        F6 ( 0xffc3), 
        F7 ( 0xffc4),
        F8 ( 0xffc5), 
        F9 ( 0xffc6), 
        F10 ( 0xffc7), 
        F11 ( 0xffc8), 
        F12 ( 0xffc9), 
        
        
        //! Misc Functions
        
        BACKSPACE   ( 0xff08), 
        TAB         ( 0xff09), 
        RETURN      ( 0xff0d), 
        ENTER       ( 0xff0d), 
        PAUSE       ( 0xff13), 
        SCROLL_LOCK ( 0xff14), 
        ESCAPE      ( 0xff1b), 
        // We do not support the legacy "DELETE" value anymore),
        // since it can't be supported on Windows.
        KEY_DELETE  ( 0xffff),
        PRINT       ( 0xff61), 
        INSERT      ( 0xff63), 
        NUM_LOCK    ( 0xff7f), 
        CAPS_LOCK   ( 0xffe5), 
        SHIFT_LOCK  ( 0xffe6), 
        
        SPACE       ( 0x020), 
        EXCLAM      ( 0x021),
        QUOTEDBL    ( 0x022),
        NUMBERSIGN  ( 0x023),
        DOLLAR      ( 0x024),
        PERCENT     ( 0x025),
        AMPERSAND   ( 0x026),
        APOSTROPHE  ( 0x027), 
        PARENLEFT   ( 0x028),
        PARENRIGHT  ( 0x029),
        ASTERISK    ( 0x02a),
        PLUS        ( 0x02b),
        COMMA       ( 0x02c), 
        MINUS       ( 0x02d), 
        PERIOD      ( 0x02e), 
        SLASH       ( 0x02f), 
        
        COLON       ( 0x03a),
        SEMICOLON   ( 0x03b), 
        LESS        ( 0x03c),
        EQUAL       ( 0x03d), 
        GREATER     ( 0x03e),
        QUESTION    ( 0x03f),
        AT          ( 0x040),
        
        BRACKETLEFT ( 0x05b), 
        BACKSLASH   ( 0x05c), 
        BRACKETRIGHT( 0x05d), 
        ASCIICIRCUM ( 0x05e),
        UNDERSCORE  ( 0x05f),
        GRAVE       ( 0x060),
        
        BRACELEFT   ( 0x07b),
        BAR         ( 0x07c),
        BRACERIGHT  ( 0x07d),
        ASCIITILDE  ( 0x07e);
        
        
        private int value;
                
        private Key(final int val) {
            this.value = val;
        }
                
        public static Key fromValue(final int val) {
        	Key[] values = Key.values();
        	
        	for( Key value : values) {
        		if(value.getValue() == val) {
        			return value;
        		}
        	}
            return null;
        }
        
        public int getValue() {
            return this.value;
        }
    }
    
	   private  Key key;    //!< which key
		    
	   private boolean autoRepeat;
		             
    public SoKeyboardEvent() {
        key = SoKeyboardEvent.Key.ANY;
        autoRepeat = false;
    }
    
    ////////////////////////////////////////////////////////////////////////
     //
     // Class initialization
     //
     // SoINTERNAL public
     //
    public static void
     initClass()
     //
     ////////////////////////////////////////////////////////////////////////
     {
         // Allocate a new event type id
         classTypeId = SoType.createType(
             SoButtonEvent.getClassTypeId(), new SbName("KeyboardEvent"));
     }
     
        
    public static SoType getClassTypeId()        /* Returns class type id */   
                                    { return classTypeId; }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
    	return classTypeId; 
    }
  private static SoType       classTypeId;             /* Type id */        
    
  /**
   * set/get which key generated the event (e.g. SoKeyboardEvent.Key.A) 
   * 
   * @param whichKey
   */
    public void setKey(final Key whichKey) {
    	 key = whichKey; 
    }
    
    /**
     * convenience routines to see if an SoEvent is a press or release of the passed keyboard key 
     * 
     * @param e
     * @param whichKey
     * @return
     */
    ////////////////////////////////////////////////////////////////////////
     //
     // Convenience routine - this returns TRUE if the event is a key release
     // event matching the passed key.
     //
     // static public
     //
        public static boolean isKeyReleaseEvent(final SoEvent e, final Key whichKey) {
            boolean isMatch = false;
             
                 // is it a keyboard event?
                 if (e.isOfType(SoKeyboardEvent.getClassTypeId())) {
                     final SoKeyboardEvent ke = ( SoKeyboardEvent ) e;
             
                     // is it a release event?
                     if (ke.getState() == SoButtonEvent.State.UP) {
             
                         // did the caller want any key release? or do they match?
                         if ((whichKey == SoKeyboardEvent.Key.ANY) ||
                             (ke.getKey() == whichKey))
                             isMatch = true;
                     }
                 }
             
                 return isMatch;
                }
    
    public Key getKey() {
    	 return key; 
    }
    
    //! convenience routines to see if an SoEvent is a press or release
    //! of the passed keyboard key
    ////////////////////////////////////////////////////////////////////////
     //
     // Convenience routine - this returns TRUE if the event is a key press
     // event matching the passed key.
     //
     // static public
     //
         public static boolean isKeyPressEvent(final SoEvent e, final Key whichKey) {
             boolean isMatch = false;
              
                  // is it a keyboard event?
                  if (e.isOfType(SoKeyboardEvent.getClassTypeId())) {
                      final SoKeyboardEvent ke = ( SoKeyboardEvent ) e;
              
                      // is it a press event?
                     if (ke.getState() == SoButtonEvent.State.DOWN) {
             
                         // did the caller want any key press? or do they match?
                         if ((whichKey == SoKeyboardEvent.Key.ANY) ||
                             (ke.getKey().getValue() == whichKey.getValue()))
                             isMatch = true;
                     }
                 }
             
                 return isMatch;
                }
    
         // These store characters corresponding to shifted numeric keys,
         // symbol keys, and shifted symbol keys:
         private static String shiftNumberChars      = ")!@#$%^&*(";
         private static String symbolChars           = " ',-./;=[\\]`";
         private static String shiftSymbolChars      = " \"<_>?:+{|}~";
     
         /**
          * Convenience routine that returns the character representing the key, 
          * if it's printable. If not, this returns NULL ('\0'). 
          * 
          * @return
          */
    public byte getPrintableCharacter() {
        int         offset;
         
             switch (getKey()) {
         
               case A:
               case B:
               case C:
               case D:
               case E:
               case F:
               case G:
               case H:
               case I:
               case J:
               case K:
               case L:
               case M:
               case N:
               case O:
               case P:
               case Q:
               case R:
               case S:
               case T:
               case U:
               case V:
               case W:
               case X:
               case Y:
               case Z:
         
                 // This relies on the letter codes being consecutive:
                 offset = getKey().getValue() - Key.A.getValue();
                 return (byte)(offset + (wasShiftDown() ? 'A' : 'a'));
         
               case NUMBER_0:
               case NUMBER_1:
               case NUMBER_2:
               case NUMBER_3:
               case NUMBER_4:
               case NUMBER_5:
               case NUMBER_6:
               case NUMBER_7:
               case NUMBER_8:
               case NUMBER_9:
         
                 // This relies on the number codes being consecutive:
                 offset = getKey().getValue() - Key.NUMBER_0.getValue();
                 return (byte)(wasShiftDown() ? shiftNumberChars.charAt(offset) : ('0' + offset));
         
               case SPACE:
               case APOSTROPHE:
               case COMMA:
               case MINUS:
               case PERIOD:
               case SLASH:
               case SEMICOLON:
               case EQUAL:
               case BRACKETLEFT:
               case BACKSLASH:
               case BRACKETRIGHT:
               case GRAVE:
                 // This relies on these codes being consecutive:
                 offset = getKey().getValue() - Key.SPACE.getValue();
                 return (byte)(wasShiftDown() ? shiftSymbolChars.charAt(offset) : symbolChars.charAt(offset));
         
               default:
                 return '\0';
             }
            }
    
//! some convenience macros for determining if an event matches

    public static boolean SO_KEY_PRESS_EVENT(SoEvent EVENT, SoKeyboardEvent.Key KEY) {
    return (SoKeyboardEvent.isKeyPressEvent(EVENT,KEY));
    }
    public static boolean SO_KEY_RELEASE_EVENT(SoEvent EVENT, SoKeyboardEvent.Key KEY) { 
    return (SoKeyboardEvent.isKeyReleaseEvent(EVENT,KEY));
    }
    
    }
