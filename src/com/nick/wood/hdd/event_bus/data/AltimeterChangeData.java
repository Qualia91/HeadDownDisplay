package com.nick.wood.hdd.event_bus.data;

public class AltimeterChangeData {

	// in radians
	private final float heading;
	private final float pitch;
	private final float roll;
	private final float altitude;
	private final float speed;
	private final float throttle;

	public AltimeterChangeData(float heading, float pitch, float roll, float altitude, float speed, float throttle) {
		this.heading = heading;
		this.pitch = pitch;
		this.roll = roll;
		this.altitude = altitude;
		this.speed = speed;
		this.throttle = throttle;
	}

	public float getHeading() {
		return heading;
	}

	public float getPitch() {
		return pitch;
	}

	public float getRoll() {
		return roll;
	}

	public float getAltitude() {
		return altitude;
	}

	public float getSpeed() {
		return speed;
	}

	public float getThrottle() {
		return throttle;
	}
}
