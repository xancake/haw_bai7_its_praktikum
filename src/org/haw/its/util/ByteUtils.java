package org.haw.its.util;

public class ByteUtils {
	private ByteUtils() {}
	
	public static byte[] xor(byte[] a, byte[] b) {
		if(a.length != b.length) {
			throw new IllegalArgumentException("");
		}
		byte[] out = new byte[a.length];
		for(int i=0; i<a.length; i++) {
			out[i] = (byte)(a[i] ^ b[i]);
		}
		return out;
	}
	
	public static byte[] toBytes(long l) {
		byte[] b = new byte[8];
		for(int i=0; i<b.length; i++) {
			b[b.length-1-i] = (byte)(0xFF & l);
			l >>>= 8;
		}
		return b;
	}
}
