/**
 * 
 */
package jscenegraph.database.inventor.libFL;

import jscenegraph.port.FLfontStruct;

/**
 * @author Yves Boyadjian
 *
 */
public class FLcontext {
	
/* This file should be included in those OpenGL programs which need Font  */
/* Library (FL) functions from the file /usr/lib/libFL.so.                */

/* The fontNamePreference parameter in a call to flCreateContext can have  */
/* one of the following values:                                            */
    public static final int FL_FONTNAME     =0;
    public static final int FL_FILENAME     =1;
    public static final int FL_XFONTNAME    =2;

/* The parameter hint in a call to flSetHint can have one of the following */
/* values:                                                                 */
    public static final int FL_HINT_AABITMAPFONTS   =1;      /* bound to font                    */
    public static final int FL_HINT_CHARSPACING     =2;      /* bound to font                    */
    public static final int FL_HINT_FONTTYPE        =3;      /* bound to font                    */
    public static final int FL_HINT_MAXAASIZE       =4;      /* bound to font                    */
    public static final int FL_HINT_MINOUTLINESIZE  =5;      /* bound to font                    */
    public static final int FL_HINT_ROUNDADVANCE    =6;      /* bound to font                    */
    public static final int FL_HINT_SCALETHRESH     =7;      /* bound to font                    */
    public static final int FL_HINT_TOLERANCE       =8;      /* bound to font                    */

    public static final int FL_FONTTYPE_ALL         =0;      /* use all types of fonts (default) */
    public static final int FL_FONTTYPE_BITMAP      =1;      /* use only bitmap fonts            */
    public static final int FL_FONTTYPE_OUTLINE     =2;      /* use only outline fonts           */

    public static final int FL_ASCII           =0;
    public static final int FL_ADOBE           =1;
    public static final int FL_JISC6226        =2;
    public static final int FL_CYRILLIC        =3;
    public static final int FL_HANGUL          =4;
    public static final int FL_DEVENAGERI      =5;
    public static final int FL_ISO88591        =6;    /* ISO 8859-1 */
    public static final int FL_DECTECH         =7;    /* DEC DECTECH */
    public static final int FL_JISX020819760   =8;     /* JISX0208.1976-0 */
    public static final int FL_JISX020119760   =9;     /* JISX0201.1976-0 */
    public static final int FL_SUNOLCURSOR1   =10;     /* SUN OPEN LOOK CURSOR 1 */
    public static final int FL_SUNOLGLYPH1    =11;     /* SUN OPEN LOOK GLYPH 1 */
    public static final int FL_SPECIFIC       =12;     /* FONT SPECIFIC */
    public static final int FL_JISX020819830  =13;     /* JISX0208.1983-0 */
    public static final int FL_KSC560119870   =14;     /* KSC5601.1987-0 */
    public static final int FL_GB231219800    =15;     /* GB2312.1980-0 */
    public static final int FL_78EUCH         =16;     /* CID 78-EUC-H */
    public static final int FL_78H            =17;     /* CID 78-H */
    public static final int FL_78RKSJH        =18;     /* CID 78-RKSJ-H */
    public static final int FL_83PVRKSJH      =19;     /* CID 83pv-RKSJ-H */
    public static final int FL_90MSRKSJH      =20;     /* CID 90ms-RKSJ-H */
    public static final int FL_90PVRKSJH      =21;     /* CID 90pv-RKSJ-H */
    public static final int FL_ADDH           =22;     /* CID Add-H */
    public static final int FL_ADDRKSJH       =23;     /* CID Add-RKSJ-H */
    public static final int FL_ADOBEJAPAN10   =24;     /* CID Adobe-Japan1-0 */
    public static final int FL_ADOBEJAPAN11   =25;     /* CID Adobe-Japan1-1 */
    public static final int FL_ADOBEJAPAN12   =26;     /* CID Adobe-Japan1-2 */
    public static final int FL_EUCH           =27;     /* CID EUC-H */
    public static final int FL_EXTH           =28;     /* CID Ext-H */
    public static final int FL_EXTRKSJH       =29;     /* CID Ext-RKSJ-H */
    public static final int FL_HIRAGANA       =30;     /* CID Hiragana */
    public static final int FL_KATAKANA       =31;     /* CID Katakana */
    public static final int FL_NWPH           =32;     /* CID NWP-H */
    public static final int FL_RKSJH          =33;     /* CID RKSJ-H */
    public static final int FL_ROMAN          =34;     /* CID Roman */
    public static final int FL_WPSYMBOL       =35;     /* CID WP-Symbol */
    public static final int FL_ADOBEJAPAN20   =36;     /* CID Adobe-Japan2-0 */
    public static final int FL_HOJOH          =37;     /* CID Hojo-H */
    public static final int FL_ISO88592       =38;     /* ISO 8859-2 */
    public static final int FL_ISO88593       =39;     /* ISO 8859-3 */
    public static final int FL_ISO88594       =40;     /* ISO 8859-4 */
    public static final int FL_ISO88595       =41;     /* ISO 8859-5 */
    public static final int FL_ISO88596       =42;     /* ISO 8859-6 */
    public static final int FL_ISO88597       =43;     /* ISO 8859-7 */
    public static final int FL_ISO88598       =44;     /* ISO 8859-8 */
    public static final int FL_ISO88599       =45;     /* ISO 8859-9 */
    public static final int FL_ISO885910      =46;     /* ISO 8859-10 */
    public static final int FL_BIG5           =47;
    public static final int FL_CNS1164319861  =48;     /* CNS11643.1986-1 */
    public static final int FL_CNS1164319862  =49;     /* CNS11643.1986-2 */
    public static final int FL_EUCCN          =50; 
    public static final int FL_EUCJP          =51; 
    public static final int FL_EUCKR          =52;
    public static final int FL_EUCTW          =53;
    public static final int FL_SJIS           =54;

/* font direction */
    public static final int FL_FONT_LEFTTORIGHT         =0;
    public static final int FL_FONT_RIGHTTOLEFT         =1;
    public static final int FL_FONT_BOTTOMTOTOP         =2;
    public static final int FL_FONT_TOPTOBOTTOM         =3;

    public static class FLbitmap {
    public int width;
    public int height;
    public float xorig;
    public float yorig;
    public float xmove;
    public float ymove;
    public byte[] bitmap;
};

	public String       fontPath;
	public int                 fontNamePreference;
	public String       fontNameRestriction;
	public float               pointsPerUMx;
	public float               pointsPerUMy;

	public int          current_font;   /* current font */
	public int                 numFont;        /* number of fonts in fontTable */
	public FLfontStruct[]        fontTable;      /* table of created font struct */
	public final float[]               hintValue = new float[9];   /* Hint 1 to 8 (see flclient.h) */

}
