package com.nick.wood.hdd.altimeter;

import com.nick.wood.hdd.event_bus.busses.RenderBus;
import com.nick.wood.hdd.event_bus.data.RenderUpdateData;
import com.nick.wood.hdd.event_bus.event_types.RenderUpdateEventType;
import com.nick.wood.hdd.event_bus.events.AltimeterChangeEvent;
import com.nick.wood.hdd.event_bus.events.RenderUpdateEvents;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.vector.Vec3f;

import java.util.HashSet;
import java.util.Set;

public class AltimeterSceneController implements Subscribable {

	private final Set<Class<?>> supports = new HashSet<>();
	private final AltimeterSceneView altimeterSceneView;
	private final RenderBus renderBus;

	public AltimeterSceneController(AltimeterSceneView altimeterSceneView, RenderBus renderBus) {

		supports.add(AltimeterChangeEvent.class);

		this.altimeterSceneView = altimeterSceneView;
		this.renderBus = renderBus;

	}

	private void moveCamera(AltimeterChangeEvent altimeterChangeEvent) {
		QuaternionF rotation = QuaternionF.RotationZ(-altimeterChangeEvent.getData().getRoll())
				.multiply(QuaternionF.RotationY(altimeterChangeEvent.getData().getPitch()));

		altimeterSceneView.getSkyboxTransform().setRotation(rotation);
	}


	@Override
	public void handle(Event<?> event) {

		if (event instanceof AltimeterChangeEvent altimeterChangeEvent) {
			moveCamera(altimeterChangeEvent);
		}
		if (event instanceof AltimeterChangeEvent altimeterChangeEvent) {
			move(altimeterChangeEvent);
		}

	}

	private void move(AltimeterChangeEvent altimeterChangeEvent) {
		double angleToRotateHeading = (altimeterChangeEvent.getData().getHeading() % (2 * Math.PI));
		altimeterSceneView.getCylindricalHeadingTransform().setRotation(QuaternionF.RotationZ(angleToRotateHeading));

		double angleToRotatePitch = altimeterChangeEvent.getData().getPitch() % (2 * Math.PI);
		altimeterSceneView.getCylindricalPitchTransform().setRotation(QuaternionF.RotationZ(-angleToRotatePitch));

		renderBus.dispatch(new RenderUpdateEvents(
				new RenderUpdateData(() -> {
					altimeterSceneView.getRollTextItem().changeText(String.valueOf((int) Math.toDegrees(altimeterChangeEvent.getData().getRoll() % (2 * Math.PI))));
					altimeterSceneView.getRollTextTransform().setPosition(new Vec3f(0, 0, -altimeterSceneView.getRollTextItem().getWidth()/2.0f));
					altimeterSceneView.getAltTextItem().changeText(String.valueOf((int) altimeterChangeEvent.getData().getAltitude()));
				}),
				RenderUpdateEventType.FUNCTION));

	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return supports.contains(aClass);
	}
}
