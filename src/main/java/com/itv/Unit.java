package com.itv;

public class Unit {
	private final long price;
	private final long remainder;
	
	public Unit(final long price, final long remainder) {
		this.price = price;
		this.remainder = remainder;
	}

	public long price() {
		return price;
	}

	public long remainder() {
		return remainder;
	}

}
