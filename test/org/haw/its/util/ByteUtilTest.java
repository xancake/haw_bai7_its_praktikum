package org.haw.its.util;

import org.junit.Assert;
import org.junit.Test;

public class ByteUtilTest {
	@Test
	public void testLongToBytes_CAFEBABE() {
		long l = 0xCA_FE_BA_BEL;
		byte[] b = ByteUtils.toBytes(l);
		assertBytes(b, 0x0, 0x0, 0x0, 0x0, 0xCA, 0xFE, 0xBA, 0xBE);
	}
	
	@Test
	public void testLongToBytes_Random1() {
		long l = 0x5A_2D_4A_67L;
		byte[] b = ByteUtils.toBytes(l);
		assertBytes(b, 0x0, 0x0, 0x0, 0x0, 0x5A, 0x2D, 0x4A, 0x67);
	}
	
	@Test
	public void testLongToBytes_Random2() {
		long l = 0x5A_2D_4A_67_96_A4_F9_88L;
		byte[] b = ByteUtils.toBytes(l);
		assertBytes(b, 0x5A, 0x2D, 0x4A, 0x67, 0x96, 0xA4, 0xF9, 0x88);
	}
	
	@Test
	public void testLongToBytes_LeadingOnes1() {
		long l = 0x0F_2D_FA_67L;
		byte[] b = ByteUtils.toBytes(l);
		assertBytes(b, 0x0, 0x0, 0x0, 0x0, 0x0F, 0x2D, 0xFA, 0x67);
	}
	
	@Test
	public void testLongToBytes_LeadingOnes2() {
		long l = 0xFF_2D_FA_67L;
		byte[] b = ByteUtils.toBytes(l);
		assertBytes(b, 0x0, 0x0, 0x0, 0x0, 0xFF, 0x2D, 0xFA, 0x67);
	}
	
	private void assertBytes(byte[] actual, int... expected) {
		byte[] exp = new byte[expected.length];
		for(int i=0; i<exp.length; i++) {
			exp[i] = (byte)expected[i];
		}
		assertBytes(exp, actual);
	}
	
	private void assertBytes(byte[] expected, byte[] actual) {
		boolean equal = true;
		for(int i=0; i<expected.length && equal; i++) {
			equal = expected[i] == actual[i];
		}
		if(!equal) {
			Assert.fail("expected: <" + format(expected) + "> but was: <" + format(actual) + ">");
		}
	}
	
	private String format(byte[] bytes) {
		String s = "[";
		for(int i=0; i<bytes.length; i++) {
			s += String.format("%2x", bytes[i]);
			if(i<bytes.length-1) {
				s += ", ";
			}
		}
		s += "]";
		return s;
	}
}
