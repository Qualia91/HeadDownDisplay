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


		double angleToRotateHeading = altimeterChangeEvent.getData().getHeading();
		double angleToRotatePitch = altimeterChangeEvent.getData().getPitch();
		altimeterSceneView.getHeadingReadout().setAngle(-angleToRotateHeading);
		altimeterSceneView.getPitchReadout().setAngle(angleToRotatePitch);
		altimeterSceneView.getThrottleReadout().setPercent(altimeterChangeEvent.getData().getThrottle());
		altimeterSceneView.getRollReadout().setRollStick(altimeterChangeEvent.getData().getRollStick());
		altimeterSceneView.getPitchChangeIndicator().setPitchStick(altimeterChangeEvent.getData().getPitchStick());
		altimeterSceneView.getYawChangeIndicator().setPitchStick(altimeterChangeEvent.getData().getYawStick());

		renderBus.dispatch(new RenderUpdateEvents(
				new RenderUpdateData(() -> {
					altimeterSceneView.getRollReadout().setRoll(altimeterChangeEvent.getData().getRoll());
					altimeterSceneView.getAltitudeReadout().setAltitude(altimeterChangeEvent.getData().getAltitude());
					altimeterSceneView.getThrottleReadout().getTextItem().changeText(String.valueOf((int) (altimeterChangeEvent.getData().getThrottle() * 100)));
				}),
				RenderUpdateEventType.FUNCTION));

	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return supports.contains(aClass);
	}
}
