package br.miike.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

/**
 * A class used to convert from hexadecimal number String to array of bytes/list of bytes, or vice-versa.
 * @author Rodrigo Miike
 */
public class HexadecimalUtils {
	
	/**
	 * Fill the string to an even number
	 */
	private static String fillIt( String hexadecimal ) {
		if( hexadecimal == null )
			return "";
		int length = hexadecimal.length();
		return length % 2 == 0? hexadecimal : "0" + hexadecimal; 
	}
	
	/**
	 * Convert an array of bytes to hexadecimal representation. <br />
	 * Example: bytes = { 0x0, 0xA0, 0x1F } returns "00A01F"
	 */
	public static String convert2Hexadecimal( byte[] bytes ) {
		String hexadecimal = "";
		for( byte b : bytes ) {
			hexadecimal += fillIt( Integer.toHexString( b & 0xff ) );
		}
		return hexadecimal;
	}
	
	/**
	 * @see #convert2Hexadecimal( byte[] )
	 */
	public static String convert2Hexadecimal( Byte[] bytes ) {
		return convert2Hexadecimal( ArrayUtils.toPrimitive( bytes ) );
	}
	
	/**
	 * Convert from a list of bytes to String
	 * @see #convert2Hexadecimal( byte[] )
	 */
	public static String convert2Hexadecimal( List<Byte> bytes ) {
		return convert2Hexadecimal( bytes.toArray( new Byte[ bytes.size() ] ) );
	}
	
	/**
	 * Convert a hexadecimal string to an array of bytes. <br />
	 * Example: hexadecimal = "00A01F" return an array of bytes = { 0x0, 0xA0, 0x1F }
	 */
	public static byte[] convert2bytes( String hexadecimal ) {
		String hexa = fillIt( hexadecimal );
		
		int hexadecimalLength = hexa.length();
		
		byte[] b = new byte[hexadecimalLength / 2];
		int index = 0;
		for( int i=0; i<hexadecimalLength; i=i+2 ) {
			b[ index++ ] = ( byte )Integer.parseInt( hexa.substring( i, i+2 ), 16 );
		}
		
		return b;
	}
	
	/**
	 * @see #convert2bytes( String )
	 */
	public static Byte[] convert2Bytes( String hexadecimal ) {
		return ArrayUtils.toObject( convert2bytes( hexadecimal ) );
	}
	
	/**
	 * Convert from String to a list of bytes
	 * @see #convert2List( String )
	 */
	public static List<Byte> convert2List( String hexadecimal ) {
		return Arrays.asList( convert2Bytes( hexadecimal ) );
	}
	
}