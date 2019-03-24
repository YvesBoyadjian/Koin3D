/**
 * 
 */
package jsceneviewerglfw;

/**
 * @author Yves Boyadjian
 *
 */
public interface SWT {

	int Hide = 23;
	int Show = 22;
	int Resize = 11;
	int Dispose = 12;
	
	/**
	 * Keyboard and/or mouse event mask indicating that the ALT_GR key
	 * was pushed on the keyboard when the event was generated
	 * (value is 1 &lt;&lt 15).
	 *
	 * @since 3.108
	 */
	public static final int ALT_GR = 1 << 15;

	/**
	 * keyboard and/or mouse event mask indicating that the ALT key
	 * was pushed on the keyboard when the event was generated
	 * (value is 1&lt;&lt;16).
	 */
	public static final int ALT = 1 << 16;

	/**
	 * Keyboard and/or mouse event mask indicating that the SHIFT key
	 * was pushed on the keyboard when the event was generated
	 * (value is 1&lt;&lt;17).
	 */
	public static final int SHIFT = 1 << 17;

	/**
	 * Keyboard and/or mouse event mask indicating that the CTRL key
	 * was pushed on the keyboard when the event was generated
	 * (value is 1&lt;&lt;18).
	 */
	public static final int CTRL = 1 << 18;

	/**
	 * Keyboard and/or mouse event mask indicating that the CTRL key
	 * was pushed on the keyboard when the event was generated. This
	 * is a synonym for CTRL (value is 1&lt;&lt;18).
	 */
	public static final int CONTROL = CTRL;

	/**
	 * Keyboard and/or mouse event mask indicating that the COMMAND key
	 * was pushed on the keyboard when the event was generated
	 * (value is 1&lt;&lt;22).
	 *
	 * @since 2.1
	 */
	public static final int COMMAND = 1 << 22;

	/**
	 * Keyboard and/or mouse event mask indicating that mouse button one (usually 'left')
	 * was pushed when the event was generated. (value is 1&lt;&lt;19).
	 */
	public static final int BUTTON1 = 1 << 19;

	/**
	 * Keyboard and/or mouse event mask indicating that mouse button two (usually 'middle')
	 * was pushed when the event was generated. (value is 1&lt;&lt;20).
	 */
	public static final int BUTTON2 = 1 << 20;

	/**
	 * Keyboard and/or mouse event mask indicating that mouse button three (usually 'right')
	 * was pushed when the event was generated. (value is 1&lt;&lt;21).
	 */
	public static final int BUTTON3 = 1 << 21;

	/**
	 * Keyboard and/or mouse event mask indicating that mouse button four
	 * was pushed when the event was generated. (value is 1&lt;&lt;23).
	 *
	 * @since 3.1
	 */
	public static final int BUTTON4 = 1 << 23;

	/**
	 * Keyboard and/or mouse event mask indicating that mouse button five
	 * was pushed when the event was generated. (value is 1&lt;&lt;25).
	 *
	 * @since 3.1
	 */
	public static final int BUTTON5 = 1 << 25;

	int MODIFIER_MASK = ALT | SHIFT | CTRL | COMMAND | ALT_GR;
	int BUTTON_MASK = BUTTON1 | BUTTON2 | BUTTON3 | BUTTON4 | BUTTON5;
	/**
	 * A constant known to be zero (0), typically used in operations
	 * which take bit flags to indicate that "no bits are set".
	 */
	public static final int NONE = 0;
	
	public static final int KEYPAD = 1 << 1;

}
