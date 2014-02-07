package br.miike;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import br.miike.tlv.*;
import br.miike.util.HexadecimalUtils;
import br.miike.util.TLVUtils;

/**
 * 
 * @author Rodrigo Miike
 */
public class TLVParser {
	
	public static List<DataObject> parse( String string ) {
		List<DataObject> parsed = new ArrayList<DataObject>();
		List<Byte> hexaList = HexadecimalUtils.convert2List( string );
		
		Iterator<Byte> iterator = hexaList.iterator();
		
		while( iterator.hasNext() ) {
			Byte aByte = iterator.next();
			
			if( TLVUtils.isPrimitive( aByte ) )
				parsed.add( buildPrimitive( aByte, iterator ) );
			else
				parsed.add( buildConstructed( aByte, iterator ) );
			
		}
		
		return parsed;
	}
	
	private static DataObject buildConstructed( Byte firstTag, Iterator<Byte> iterator ) {
		DataObject constructed = new ConstructedObject();
		
		List<Byte> tagList = getNextTag( firstTag, iterator );
		constructed.setTag( list2byte( tagList ) );
		
		List<Byte> lengthList = getNextLength( iterator );
		
		int length = translateLengthByteArray( lengthList );
		
		List<Byte> valueList = new ArrayList<Byte>();
		for( int counter = 0; counter < length; counter++ )
			valueList.add( iterator.next() );
		
		Iterator<Byte> innerIterator = valueList.iterator();
		while( innerIterator.hasNext() ) {
			Byte followingTag = innerIterator.next();
			if( TLVUtils.isPrimitive( followingTag ) )
				constructed.add( buildPrimitive( followingTag, innerIterator ) );
			else
				constructed.add( buildConstructed( followingTag, innerIterator ) );
		}
		
		return constructed;
	}
	
	private static DataObject buildPrimitive( Byte firstTag, Iterator<Byte> iterator ) {
		DataObject primitive = new PrimitiveObject();
		
		List<Byte> tagList = getNextTag( firstTag, iterator );
		primitive.setTag( list2byte( tagList ) );
		
		//System.out.println( HexadecimalUtils.convert2Hexadecimal( primitive.getTag() ) );
		
		List<Byte> lengthList = getNextLength( iterator );
		
		int length = translateLengthByteArray( lengthList );
		
		/* Solving value
		 **/
		List<Byte> valueList = getNextValue( length, iterator );
		
		primitive.setValue( list2byte( valueList ) );
		
		return primitive;
	}
	
	private static List<Byte> getNextTag( Byte firstTag, Iterator<Byte> iterator ) {
		List<Byte> tagList = new ArrayList<Byte>();
		tagList.add( firstTag );
		
		if( TLVUtils.firstTagExistsSubsequentByte( firstTag ) ) {
			while( iterator.hasNext() ) {
				Byte nextTag = iterator.next();
				tagList.add( nextTag );
				if( TLVUtils.tagExistsSubsequentByte( nextTag ) )
					continue;
				else
					break;
			}
		}
		
		return tagList;
	}
	
	private static List<Byte> getNextLength( Iterator<Byte> iterator ) {
		List<Byte> lengthList = new ArrayList<Byte>();
		lengthList.add( iterator.next() );
		
		if( TLVUtils.lengthExistsSubsequentByte( lengthList.get( 0 ) ) ) {
			byte subsequentBytesLength = ( byte )( lengthList.get( 0 ) & 0x7F );
			for( byte b=1; b<=subsequentBytesLength; b++ )
				lengthList.add( iterator.next() );
		}
		
		return lengthList;
	}
	
	private static int translateLengthByteArray( List<Byte> lengthList ) {
		int length = 0; // quantity of bytes
		
		if( lengthList.size() == 1 ) {
			length = lengthList.get( 0 ) & 0xFF;
		} else {
			for( int index = 1; index < lengthList.size(); index++ )
				length += lengthList.get( index ) & 0xFF;
		}
		
		return length;
	}
	
	private static List<Byte> getNextValue( int length, Iterator<Byte> iterator ) {
		List<Byte> valueList = new ArrayList<Byte>();
		for( int counter = 0; counter < length; counter++ )
			valueList.add( iterator.next() );
		return valueList;
	}
	
	private static byte[] list2byte( List<Byte> list ) {
		return ArrayUtils.toPrimitive( list2Byte( list ) );
	}
	
	private static Byte[] list2Byte( List<Byte> list ) {
		return list.toArray( new Byte[ list.size() ] );
	}
	
}
