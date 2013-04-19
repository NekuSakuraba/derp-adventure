package br.miike.tlv;

import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;

import br.miike.tlv.iterator.NullIterator;
import br.miike.util.HexadecimalUtils;

/**
 * A class that represents the primitive objects. <br />
 * A primitive objects is structured as |Tag|Length|Value|
 * @author Rodrigo Miike
 */
public class PrimitiveObject extends DataObject {
	private byte[] tag;
	private byte[] value;
	
	@Override
	public byte[] getTag() {
		return tag;
	}
	@Override
	public void setTag( byte[] tag ) {
		this.tag = tag;
	}
	
	@Override
	public byte[] getLength() {
		/*
		 * Value with the size from Zero to 127 can be represented with 1 byte.
		 * From 128, 2 or more bytes are necessary to represent the length
		 */
		if( value == null )
			return new byte[]{ 0x0 };
		
		int length = value.length;
		String hexaLength = Integer.toHexString( length );
		
		byte[] byteLength = HexadecimalUtils.convert2bytes( hexaLength );
		if( length <= 127 )
			return byteLength;
		
		byte[] fstByte = { ( byte )( byteLength.length | 0x80 ) };
		
		return ArrayUtils.addAll( fstByte, byteLength );
	}
	
	@Override
	public byte[] getValue() {
		return value;
	}
	@Override
	public void setValue( byte[] value ) {
		this.value = value;
	}
	
	@Override
	public Iterator<DataObject> getIterator() {
		return new NullIterator<DataObject>();
	}
	
	@Override
	public String toString() {
		return super.toString() + "=>"
				+ new String( getValue() );
	}
	
}