package com.nick.wood.hdd.altimeter;

import com.nick.wood.hdd.event_bus.events.AltimeterChangeEvent;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;
import com.nick.wood.maths.objects.QuaternionF;

import java.util.HashSet;
import java.util.Set;

public class AltimeterSceneController implements Subscribable {

	private final Set<Class<?>> supports = new HashSet<>();
	private final AltimeterSceneView altimeterSceneView;

	public AltimeterSceneController(AltimeterSceneView altimeterSceneView) {

		supports.add(AltimeterChangeEvent.class);

		this.altimeterSceneView = altimeterSceneView;

	}

	private void moveCamera(AltimeterChangeEvent altimeterChangeEvent) {
		QuaternionF rotation = QuaternionF.RotationZ(-altimeterChangeEvent.getData().getRoll())
				.multiply(QuaternionF.RotationX(altimeterChangeEvent.getData().getPitch()))
				.multiply(QuaternionF.RotationY(altimeterChangeEvent.getData().getHeading()));

		altimeterSceneView.getFobCameraTransform().setRotation(rotation);

		double angleToRotate = (altimeterChangeEvent.getData().getAltitude() / 1000.0 * altimeterSceneView.getAltAngleStepSize()) % (2 * Math.PI);

		altimeterSceneView.getCylindricalAltitudeTransform().setRotation(QuaternionF.RotationX(angleToRotate));
	}

	@Override
	public void handle(Event<?> event) {

		if (event instanceof AltimeterChangeEvent altimeterChangeEvent) {
			moveCamera(altimeterChangeEvent);
		}

	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return supports.contains(aClass);
	}
}
