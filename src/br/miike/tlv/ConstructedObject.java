package br.miike.tlv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.apache.commons.lang3.ArrayUtils;

import br.miike.util.HexadecimalUtils;

/**
 * A class that represents a constructed object. <br />
 * It consists of a tag, a length, and a value field composed of one or more BER-TLV data objects. <br />
 * |Tag|Length| Primitive or Constructed data object | ... | Primitive or Constructed data object | 
 * @author Rodrigo Miike
 */
public class ConstructedObject extends DataObject {
	private byte[] tag;
	private List<DataObject> dataObjects;
	
	public ConstructedObject() {
		dataObjects = new ArrayList<DataObject>();
	}
	
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
		if( dataObjects.isEmpty() )
			return new byte[]{ 0x0 };
		
		int length = getValue().length;
		String hexaLength = Integer.toHexString( length );
		
		byte[] byteLength = HexadecimalUtils.convert2bytes( hexaLength );
		if( length <= 127 )
			return byteLength;
		
		byte[] fstByte = { ( byte )( byteLength.length | 0x80 ) };
		
		return ArrayUtils.addAll( fstByte, byteLength );
	}
	
	@Override
	public byte[] getValue() {
		List<Byte> value = new ArrayList<Byte>();
		
		for( DataObject dataObject : dataObjects ) {
			for( byte b : dataObject.getTag() )
				value.add( b );
			for( byte b : dataObject.getLength() )
				value.add( b );
			for( byte b : dataObject.getValue() )
				value.add( b );
		}
		
		return ArrayUtils.toPrimitive( value.toArray( new Byte[ value.size() ] ) );
	}
	
	@Override
	public void add( DataObject dataObject ) {
		dataObjects.add( dataObject );
	}
	
	@Override
	public Iterator<DataObject> getIterator() {
		return new CompositeIterator();
	}
	
	private class CompositeIterator implements Iterator<DataObject> {
		private Stack< Iterator<DataObject> > iterators;
		
		public CompositeIterator() {
			iterators = new Stack< Iterator<DataObject> >();
			iterators.add( dataObjects.iterator() );
		}
		
		@Override
		public boolean hasNext() {
			if( iterators.empty() )
				return false;
			
			if( iterators.peek().hasNext() ) {
				return true;
			} else {
				iterators.pop();
				return hasNext();
			}
		}
		
		@Override
		public DataObject next() {
			if( iterators.empty() )
				throw new NoSuchElementException();
			
			if( iterators.peek().hasNext() ) {
				DataObject component = iterators.peek().next(); 
				iterators.push( component.getIterator() );
				
				return component;
			} else {
				iterators.pop();
				return next();
			}
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}