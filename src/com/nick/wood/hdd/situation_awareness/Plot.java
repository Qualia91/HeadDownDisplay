package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.vector.Vec3f;

public class Plot {

	private final TrackID trackID;
	private final SisoEnum sisoEnum;
	private final Vec3f bra;
	private final Vec3f hpr;
	private final boolean selected;
	private final Allegiance allegiance;

	public Plot(TrackID trackID, SisoEnum sisoEnum, Vec3f bra, Vec3f hpr, boolean selected, Allegiance allegiance) {
		this.trackID = trackID;
		this.sisoEnum = sisoEnum;
		this.bra = bra;
		this.hpr = hpr;
		this.selected = selected;
		this.allegiance = allegiance;
	}

	public TrackID getTrackID() {
		return trackID;
	}

	public SisoEnum getSisoEnum() {
		return sisoEnum;
	}

	public Vec3f getBra() {
		return bra;
	}

	public Vec3f getHpr() {
		return hpr;
	}

	public boolean isSelected() {
		return selected;
	}

	public Allegiance getAllegiance() {
		return allegiance;
	}
}
