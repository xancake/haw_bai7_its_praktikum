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
}
