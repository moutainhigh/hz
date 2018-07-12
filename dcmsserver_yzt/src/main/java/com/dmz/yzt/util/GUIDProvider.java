
package com.dmz.yzt.util;

import java.net.InetAddress;
import java.security.SecureRandom;

public class GUIDProvider
{
	private static SecureRandom seeder;
	private static String midValue;
	private static String midValueUnformatted;

	private static int getInt(byte[] abyte0)
	{
		int i = 0;
		int j = 24;
		for (int k = 0; j >= 0; ++k)
		{
			int l = abyte0[k] & 0xFF;
			i += (l << j);
			j -= 8;
		}
		return i;
	}

	private static String hexFormat(int i, int j)
	{
		String s = Integer.toHexString(i);
		return padHex(s, j) + s;
	}

	private static String padHex(String s, int i)
	{
		StringBuffer stringbuffer = new StringBuffer();
		if (s.length() < i)
		{
			for (int j = 0; j < i - s.length(); ++j)
				stringbuffer.append("0");
		}
		return stringbuffer.toString();
	}

	private static String getVal(String s)
	{
		long l = System.currentTimeMillis();
		int i = (int) l & 0xFFFFFFFF;
		int j = seeder.nextInt();
		String value = hexFormat(i, 8) + s + hexFormat(j, 8);
		return value;
	}

	public static String getFormattedGUID()
	{
		return getVal(midValue);
	}

	public static String getRawGUID()
	{
		return getVal(midValueUnformatted);
	}

	public static String getGUID()
	{
		return getRawGUID().toUpperCase();
	}

	public static String genUID(int length) throws Exception
	{
		if (length > 15)
		{
			throw new Exception("UUID length out of range:" + length);
		}
		double d = Math.random();
		String str = String.valueOf(d);
		return str.substring(2, 2 + length);
	}

	public static String genHostUID()
	{
		try
		{
			return genUID(8);
		}
		catch (Exception e)
		{
		}
		return null;
	}

	public static void main(String[] args)
	{
		System.out.println(getFormattedGUID());
	}

	static
	{
		StringBuffer formatted;
		StringBuffer unformatted;
		InetAddress inetaddress;
		byte[] abyte;
		String s;
		String s1;
		try
		{
			formatted = new StringBuffer();
			unformatted = new StringBuffer();
			seeder = new SecureRandom();
			inetaddress = InetAddress.getLocalHost();
			abyte = inetaddress.getAddress();
			s = hexFormat(getInt(abyte), 8);
			s1 = hexFormat(new Object().hashCode(), 8);
			formatted.append("-");
			unformatted.append(s.substring(0, 4));
			formatted.append(s.substring(0, 4));
			formatted.append("-");
			unformatted.append(s.substring(4));
			formatted.append(s.substring(4));
			formatted.append("-");
			unformatted.append(s1.substring(0, 4));
			formatted.append(s1.substring(0, 4));
			formatted.append("-");
			unformatted.append(s1.substring(4));
			formatted.append(s1.substring(4));
			midValue = formatted.toString();
			midValueUnformatted = unformatted.toString();
			seeder.nextInt();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		finally
		{
			formatted = null;
			unformatted = null;
			inetaddress = null;
			abyte = null;
			s = null;
			s1 = null;
		}
	}
}
