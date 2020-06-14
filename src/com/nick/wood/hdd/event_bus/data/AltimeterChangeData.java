package com.nick.wood.hdd.event_bus.data;

public class AltimeterChangeData {

	// in radians
	private final float heading;
	private final float pitch;
	private final float roll;
	private final float altitude;
	private final float speed;
	private final float throttle;
	private final float rollStick;
	private final float pitchStick;
	private final float yawStick;

	public AltimeterChangeData(float heading, float pitch, float roll, float altitude, float speed, float throttle, float rollStick, float pitchStick, float yawStick) {
		this.heading = heading;
		this.pitch = pitch;
		this.roll = roll;
		this.altitude = altitude;
		this.speed = speed;
		this.throttle = throttle;
		this.rollStick = rollStick;
		this.pitchStick = pitchStick;
		this.yawStick = yawStick;
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

	public float getRollStick() {
		return rollStick;
	}

	public float getPitchStick() {
		return pitchStick;
	}

	public float getYawStick() {
		return yawStick;
	}
}
