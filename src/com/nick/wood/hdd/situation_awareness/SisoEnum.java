package com.nick.wood.hdd.situation_awareness;

public class SisoEnum {
	int a;
	int b;
	int c;
	int d;
	int e;
	int f;
	int g;

	public SisoEnum(int a, int b, int c, int d, int e, int f, int g) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.g = g;
	}

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public int getC() {
		return c;
	}

	public int getD() {
		return d;
	}

	public int getE() {
		return e;
	}

	public int getF() {
		return f;
	}

	public int getG() {
		return g;
	}

	@Override
	public String toString() {
		return a +
				"," + b +
				"," + c +
				"," + d +
				"," + e +
				"," + f +
				"," + g;
	}
}
