package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.vector.Vec3f;

public class Plot {

	private final int id;
	private final SisoEnum sisoEnum;
	private final Vec3f bra;
	private final QuaternionF orientation;
	private final boolean selected;
	private final Allegiance allegiance;

	public Plot(int id, SisoEnum sisoEnum, Vec3f bra, QuaternionF orientation, boolean selected, Allegiance allegiance) {
		this.id = id;
		this.sisoEnum = sisoEnum;
		this.bra = bra;
		this.orientation = orientation;
		this.selected = selected;
		this.allegiance = allegiance;
	}

	public int getId() {
		return id;
	}

	public SisoEnum getSisoEnum() {
		return sisoEnum;
	}

	public Vec3f getBra() {
		return bra;
	}

	public QuaternionF getOrientation() {
		return orientation;
	}

	public boolean isSelected() {
		return selected;
	}

	public Allegiance getAllegiance() {
		return allegiance;
	}
}
