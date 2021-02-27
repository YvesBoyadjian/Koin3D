package jscenegraph.database.inventor;

import jscenegraph.coin3d.TidBits;
import jscenegraph.port.Util;

public class SoInputP {

// Helperfunctions to handle different filetypes (Inventor, VRML 1.0
// and VRML 2.0).
//
// VRML 1.0 identifiers are defined as:
//
//  VRML 1.0 Node names must not begin with a digit, and must
//  not contain spaces or control characters, single or double
//  quote characters, backslashes, curly braces, the sharp (#)
//  character, the plus (+) character or the period character.
//
//  Field names start with lower case letters, Node types start
//  with upper case. The remainder of the characters may be any
//  printable ascii (21H-7EH) except curly braces {}, square
//  brackets [], single ' or double " quotes, sharp #, backslash
//  \\ plus +, period . or ampersand &.
//
//  In addition to this, we found ',', '(', ')' and '|' to be
//  invalid characters in VRML 1.0 names. This was made apparent
//  when reading the following fields on an unknown node and
//  bit masks:
//
//   fields [SFString test, SFFloat length]
//
//   FontStyle { family SANS style (BOLD|ITALIC) size 10 }
//
//  If ',' is to be a valid character in a name, then the name
//  of the first field would become 'test,', and not just 'test'.
//  Likewise, the name of the first bit in the bitmask would
//  become 'BOLD|ITALIC)' instead of just 'BOLD'.
//
// The grammar for VRML2 identifiers is:
//
//  nodeNameId ::= Id ;
//  nodeTypeId ::= Id ;
//  fieldId ::= Id ;
//
//  Id ::= IdFirstChar | IdFirstChar IdRestChars ;
//
//  IdFirstChar ::= Any ISO-10646 character encoded using UTF-8
//  except: 0x30-0x39, 0x0-0x20, 0x22, 0x23, 0x27, 0x28, 0x29, 0x2b,
//  0x2c, 0x2d, 0x2e, 0x5b, 0x5c, 0x5d, 0x7b, 0x7c, 0x7d, 0x7f ;
//
//  IdRestChars ::= Any number of ISO-10646 characters except:
//  0x0-0x20, 0x22, 0x23, 0x27, 0x28, 0x29, 0x2c, 0x2e, 0x5b,
//  0x5c, 0x5d, 0x7b, 0x7c, 0x7d, 0x7f ;

    public static boolean isNameStartChar( char c, boolean validIdent)
    {
        if (validIdent) return SbName.isIdentStartChar(c);
        return (c > 0x20); // Not control characters
    }

    static char[] invalid_vrml1_table = new char[256];
    static char[] valid_ident_invalid_vrml1_table = new char[256];

    static boolean isNameStartCharVRML1Initialized = false;
    public static boolean isNameStartCharVRML1( char c, boolean validIdent)
    {
        if (!isNameStartCharVRML1Initialized) {
    final char invalid_vrml1[] = {
                    0x22, 0x23, 0x27, 0x28, 0x29, 0x2b, 0x2c, 0x2e, 0x5c, 0x7b, 0x7c, 0x7d, 0x00 }; // 0x7d = 125
            //'"',  '#',  ''',  '(',  ')',  '+',  ',',  '.',  '\',  '{',  '|',  '}'

            // Differences from invalid_vrml1: '&' , '[', and ']' are now invalid
    final char valid_ident_invalid_vrml1[] = {
                    0x22, 0x23, 0x26, 0x27, 0x28, 0x29, 0x2b, 0x2c, 0x2e, 0x5b, 0x5c, 0x5d, 0x7b, 0x7c, 0x7d, 0x00 }; // 0x7d = 125
            //'"',  '#',   '&', ''',  '(',  ')',  '+',  ',',  '.',  '[',  '\',   ']',  '{',  '|',  '}'

            for (int cc = 0; cc < 256; ++cc) {
                invalid_vrml1_table[cc] = 0;
                valid_ident_invalid_vrml1_table[cc] = 0;
            }

    int ptr = 0;//invalid_vrml1;
            while (/**ptr*/invalid_vrml1[ptr]!=0) { invalid_vrml1_table[invalid_vrml1[ptr]] = 1; ++ptr; }
            ptr = 0;//valid_ident_invalid_vrml1;
            while (valid_ident_invalid_vrml1[ptr]!=0) { valid_ident_invalid_vrml1_table[valid_ident_invalid_vrml1[ptr]] = 1; ++ptr; }

            isNameStartCharVRML1Initialized = true;
        }

        if (c <= 0x20) return false; // Control characters
        if (c >= 0x30 && c <= 0x39) return false; // Digits

        if (validIdent) return (valid_ident_invalid_vrml1_table[c] == 0);
        return (invalid_vrml1_table[c] == 0);
    }

    static final char[] invalid_vrml2_table = new char[256];
    static final char[] valid_ident_invalid_vrml2_table = new char[256];

    static boolean isNameStartCharVRML2Initialized = false;

    static int non_strict = -1;

    public static boolean isNameStartCharVRML2( char c, boolean validIdent)
    {
        if (!isNameStartCharVRML2Initialized) {
    final char invalid_vrml2[] = {
                    0x22, 0x23, 0x27, 0x28, 0x29, 0x2b, 0x2c, 0x2d, 0x2e, 0x5b, 0x5c, 0x5d, 0x7b, 0x7c, 0x7d, 0x7f, 0x00 }; // 0x7f = 127
            //'"',  '#',  ''',  '(',  ')',  '+',  ',',  '-',  '.',  '[',  '\',  ']',  '{',  ,'|',  '}',  ''

    final int valid_ident_invalid_vrml2 = 0;//invalid_vrml2;

            for (int cc = 0; cc < 256; ++cc) {
                invalid_vrml2_table[cc] = 0;
                valid_ident_invalid_vrml2_table[cc] = 0;
            }

    int ptr = 0;//invalid_vrml2;
            while (invalid_vrml2[ptr]!=0) { invalid_vrml2_table[invalid_vrml2[ptr]] = 1; ++ptr; }
            ptr = valid_ident_invalid_vrml2;
            while (invalid_vrml2[ptr]!=0) { valid_ident_invalid_vrml2_table[invalid_vrml2[ptr]] = 1; ++ptr; }

            isNameStartCharVRML2Initialized = true;
        }

        if (c <= 0x20) return false; // Control characters
        if (c >= 0x30 && c <= 0x39) return false; // Digits

        if (validIdent) return (valid_ident_invalid_vrml2_table[c] == 0);

        // For Coin to be able to load VRML97 (invalid) files that have
        // been generated with illegal names, '+' is considered a valid
        // startcharacter.
        if (non_strict == -1) {
            String env = TidBits.coin_getenv("COIN_NOT_STRICT_VRML97");
            non_strict = (env != null && (Util.atoi(env) > 0)) ? 1 : 0;
        }

        if (c == '+' && non_strict != 0) // '+' is considered valid
            return true;

        return (invalid_vrml2_table[c] == 0);
    }

// Helperfunction to handle different filetypes (Inventor, VRML 1.0
// and VRML 2.0).
//
// See SoInputP::isIdentStartChar for more information
    public static boolean isNameChar( char c, boolean validIdent)
    {
        if (validIdent) return SbName.isIdentChar(c);
        return (c > 0x20); // Not control characters
    }

    static final char[] invalid_vrml1_table_2 = new char[256];
    static final char[] valid_ident_invalid_vrml1_table_2 = new char[256];

    static boolean isNameCharVRML1Initialized = false;

    public static boolean isNameCharVRML1( char c, boolean validIdent)
    {
        if (!isNameCharVRML1Initialized) {
    final char invalid_vrml1[] = {
                    0x22, 0x23, 0x27, 0x28, 0x29, 0x2b, 0x2c, 0x2e, 0x5c, 0x7b, 0x7c, 0x7d, 0x00 }; // 0x7d = 125
            //'"',  '#',  ''',  '(',  ')',  '+',  ',',  '.',  '\',  '{',  ,'|',  '}'

            // Differences from invalid_vrml1: '&' , '[', and ']' are now invalid
    final char valid_ident_invalid_vrml1[] = {
                    0x22, 0x23, 0x26, 0x27, 0x28, 0x29, 0x2b, 0x2c, 0x2e, 0x5b, 0x5c, 0x5d, 0x7b, 0x7c, 0x7d, 0x00 }; // 0x7d = 125
            //'"',  '#',   '&', ''',  '(',  ')',  '+',  ',',  '.',  '[',  '\',   ']',  '{',  ,'|',  '}'

            for (int cc = 0; cc < 256; ++cc) {
                invalid_vrml1_table_2[cc] = 0;
                valid_ident_invalid_vrml1_table_2[cc] = 0;
            }

    int ptr = 0;//invalid_vrml1;
            while (invalid_vrml1[ptr]!=0) { invalid_vrml1_table_2[invalid_vrml1[ptr]] = 1; ++ptr; }
            ptr = 0;//valid_ident_invalid_vrml1;
            while (valid_ident_invalid_vrml1[ptr]!=0) { valid_ident_invalid_vrml1_table_2[valid_ident_invalid_vrml1[ptr]] = 1; ++ptr; }

            isNameCharVRML1Initialized = true;
        }

        if (c <= 0x20) return false; // Control characters

        if (validIdent) return (valid_ident_invalid_vrml1_table_2[c] == 0);
        return (invalid_vrml1_table_2[c] == 0);
    }

    static final char[] invalid_vrml2_table_2 = new char[256];
    static final char[] valid_ident_invalid_vrml2_table_2 = new char[256];

    static boolean isNameCharVRML2Initialized = false;

    public static boolean isNameCharVRML2(char c, boolean validIdent)
    {
        if (!isNameCharVRML2Initialized) {
            // Compared to isIdentStartChar, '+' and '-' have now become valid characters.
    final char invalid_vrml2[] = {
                    0x22, 0x23, 0x27, 0x28, 0x29, 0x2c, 0x2e, 0x5b, 0x5c, 0x5d, 0x7b, 0x7c, 0x7d, 0x7f, 0x00 }; // 0x7f = 127
            //'"',  '#',  ''',  '(',  ')',  ',',  '.',  '[',  '\',  ']',  '{',  ,'|',  '}',  ''

    int valid_ident_invalid_vrml2 = 0;//invalid_vrml2;

            for (int cc = 0; cc < 256; ++cc) {
                invalid_vrml2_table_2[cc] = 0;
                valid_ident_invalid_vrml2_table_2[cc] = 0;
            }

    int ptr = 0;//invalid_vrml2;
            while (invalid_vrml2[ptr]!=0) { invalid_vrml2_table_2[invalid_vrml2[ptr]] = 1; ++ptr; }
            ptr = 0;//valid_ident_invalid_vrml2;
            while (invalid_vrml2[ptr]!=0) { valid_ident_invalid_vrml2_table_2[invalid_vrml2[ptr]] = 1; ++ptr; }

            isNameCharVRML2Initialized = true;
        }

        if (c <= 0x20) return false; // Control characters

        if (validIdent) return (valid_ident_invalid_vrml2_table_2[c] == 0);
        return (invalid_vrml2_table_2[c] == 0);
    }
}
