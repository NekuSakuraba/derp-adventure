package br.miike.tlv;

import java.util.Iterator;

import br.miike.util.HexadecimalUtils;

/**
 * An abstract representation of BER-TLV data object defined in ISO/IEC 8825. <br />
 * It consists of 2-3 consecutive fields: the tag field, the length field and the value field. <br />
 * A data object belongs to one of the following two categories: <br />
 * ~> A primitive data object where the value field contains a data element for <br />
 * 	financial transaction interchange. <br />
 * ~> A constructed data object where the value field contains one or more primitive or constructed <br />
 * 	data objects. The value field of a constructed data object is called a template.
 * 
 * @author Rodrigo Miike
 */
public abstract class DataObject {
	
	public byte[] getTag() {
		throw new UnsupportedOperationException();
	}
	
	public void setTag( byte[] tag ) {
		throw new UnsupportedOperationException();
	}
	
	public byte[] getLength() {
		throw new UnsupportedOperationException();
	}
	
	public byte[] getValue() {
		throw new UnsupportedOperationException();
	}
	public void setValue( byte[] value ) {
		throw new UnsupportedOperationException();
	}
	
	public void add( DataObject dataObject ) {
		throw new UnsupportedOperationException();
	}
	
	public void remove( DataObject dataObject ) {
		throw new UnsupportedOperationException();
	}
	
	public DataObject find( DataObject tag ) {
		throw new UnsupportedOperationException();
	}
	
	public Iterator<DataObject> getIterator() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return HexadecimalUtils.convert2Hexadecimal( getTag() );
	}
	
}