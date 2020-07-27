/**
 * 
 */
package jscenegraph.port;

/**
 * @author BOYADJIAN
 *
 */
public class CString {

	char[] str;
	int offset;
	
	public static CString create(String str) {
		if( null == str) {
			return null;
		}
		return new CString(str);
	}
	
	public static CString create(String str, int offset) {
		if( null == str) {
			return null;
		}
		return new CString(str, offset);
	}
	
	public static CString create(CString cstr, int index) {
		if( null == cstr) {
			return null;
		}
		return new CString(cstr, index);
	}

	private CString(String str, int offset) {
		int str_length = str.length();
		this.str = new char[str_length+1];
		for( int i=0;i<str_length;i++) {
			this.str[i] = str.charAt(i);
		}
		this.offset = offset;
	}

	private CString(CString cstr, int offset) {
		str = cstr.str;
		this.offset = cstr.offset + offset;
	}

	private CString(String str) {
		this(str,0);
	}
	
	@Override
	public String toString() {
		int str_length = str.length;
		StringBuilder sb = new StringBuilder();
		for( int i=offset;i<str_length;i++) {
			char c = str[i];
			if( 0 == c) {
				break;
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	public void plusPlus() {
		offset++;
	}

	public void star(char value) {
		str[offset] = value;
	}

}
