package br.miike.util;

public class TLVUtils {

	/**
	 * @return true if exists a subsequent length byte
	 */
	public static boolean lengthExistsSubsequentByte( byte length ) {
		return ( length & 0x80 ) == 0x80;
	} 
	
	/**
	 * @return true if it's a primitive tag
	 */
	public static boolean isPrimitive( byte tag ) {
		return !( ( tag & 0x20 ) == 0x20 );
	}
	
	/**
	 * @return true if exists a subsequent tag byte after the first tag
	 */
	public static boolean firstTagExistsSubsequentByte( byte tag ) {
		return ( tag & 0x1F ) == 0x1F;
	}
	
	/**
	 * @return true if exists a subsequent tag byte
	 */
	public static boolean tagExistsSubsequentByte( byte tag ) {
		return ( tag & 0x80 ) == 0x80;
	}
	
}