
package com.dmz.yzt.util;

/**  
 * native2ascii.exe Java code implementation.  
 * @author Danny
 */
public class Native2AsciiUtils
{

	/**  
	 * prefix of ascii string of native character  
	 */
	private static String PREFIX = "\\u";

	/**  
	 * Native to ascii string. It's same as execut native2ascii.exe.  
	 *   
	 * @param str   native string  
	 * @return ascii string  
	 */
	public static String native2Ascii(String str)
	{
		char[] chars = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++)
		{
			sb.append(char2Ascii(chars[i]));
		}
		return sb.toString();
	}

	/**  
	 * Native character to ascii string.  
	 *   
	 * @param c  
	 *            native character  
	 * @return ascii string  
	 */
	private static String char2Ascii(char c)
	{
		if (c > 255)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(PREFIX);
			int code = (c >> 8);
			String tmp = Integer.toHexString(code);
			if (tmp.length() == 1)
			{
				sb.append("0");
			}
			sb.append(tmp);
			code = (c & 0xFF);
			tmp = Integer.toHexString(code);
			if (tmp.length() == 1)
			{
				sb.append("0");
			}
			sb.append(tmp);
			return sb.toString();
		}
		else
		{
			return Character.toString(c);
		}
	}

	/**  
	 * Ascii to native string. It's same as execut native2ascii.exe -reverse.  
	 *   
	 * @param str ascii string  
	 * @return native string  
	 */
	public static String ascii2Native(String str)
	{
		StringBuffer sb = new StringBuffer();
		int begin = 0;
		int index = str.indexOf(PREFIX);
		while (index != -1)
		{
			sb.append(str.substring(begin, index));
			sb.append(ascii2Char(str.substring(index, index + 6)));
			begin = index + 6;
			index = str.indexOf(PREFIX, begin);
		}
		sb.append(str.substring(begin));
		return sb.toString();
	}

	/**  
	 * Ascii to native character.  
	 * @param str  ascii string  
	 * @return native character  
	 */
	private static char ascii2Char(String str)
	{
		if (str.length() != 6)
		{
			throw new IllegalArgumentException("Ascii string of a native character must be 6 character.");
		}
		if (!PREFIX.equals(str.substring(0, 2)))
		{
			throw new IllegalArgumentException("Ascii string of a native character must start with \"\\u\".");
		}
		String tmp = str.substring(2, 4);
		int code = Integer.parseInt(tmp, 16) << 8;
		tmp = str.substring(4, 6);
		code += Integer.parseInt(tmp, 16);
		return (char) code;
	}

	public static void main(String[] args)
	{
		System.out.println(Native2AsciiUtils.native2Ascii("交易失败"));
		System.out.println(Native2AsciiUtils.ascii2Native("\u56e0\u653e\u6b3e\u5361\u53f7\u539f\u56e0\u5bfc\u81f4\u8f6c\u8d26\u5931\u8d25"));
	}
}
