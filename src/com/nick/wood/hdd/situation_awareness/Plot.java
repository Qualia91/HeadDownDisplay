package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.vector.Vec3f;

public class Plot {

	private final Vec3f bra;
	private final QuaternionF orientation;

	public Plot(Vec3f bra, QuaternionF orientation) {
		this.bra = bra;
		this.orientation = orientation;
	}

	public Vec3f getBra() {
		return bra;
	}

	public QuaternionF getOrientation() {
		return orientation;
	}
}
