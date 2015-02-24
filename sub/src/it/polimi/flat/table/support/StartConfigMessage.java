package it.polimi.flat.table.support;

import java.io.Serializable;

public class StartConfigMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1367520972843480713L;
	
	private byte[] dek;
	private byte[] kek0;
	private byte[] kek1;
	private byte[] kek2;
	//private byte[] test; //[DEBUG] To test if pubic key work


	public StartConfigMessage() {
		super();
	}
	
	
	public void setDeK(byte[] deK) {
		dek = deK;
	}


	public void setKeK0(byte[] keK0) {
		kek0 = keK0;
	}


	public void setKeK1(byte[] keK1) {
		kek1 = keK1;
	}


	public void setKeK2(byte[] keK2) {
		kek2 = keK2;
	}


	public byte[] getDeK() {
		return dek;
	}

	public byte[] getKeK0() {
		return kek0;
	}

	public byte[] getKeK1() {
		return kek1;
	}

	public byte[] getKeK2() {
		return kek2;
	}

	/*
	public byte[] getTest() {
		return test;
	}


	public void setTest(byte[] test) {
		this.test = test;
	}
	*/

}
