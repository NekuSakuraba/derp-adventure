package br.miike.tlv.iterator;

import java.util.Iterator;

/**
 * A generic class that represents an iterator of null objects.
 * @author Rodrigo Miike
 */
public class NullIterator<E> implements Iterator<E> {

	public boolean hasNext() {
		return false;
	}

	public E next() {
		return null;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}