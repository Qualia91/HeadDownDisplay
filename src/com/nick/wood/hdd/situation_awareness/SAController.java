package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.hdd.event_bus.busses.RenderBus;
import com.nick.wood.hdd.event_bus.data.RenderUpdateData;
import com.nick.wood.hdd.event_bus.event_types.RenderUpdateEventType;
import com.nick.wood.hdd.event_bus.events.*;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;
import com.nick.wood.maths.objects.QuaternionF;

import java.util.HashSet;
import java.util.Set;

public class SAController implements Subscribable {
	private final SAView saView;
	private final RenderBus renderBus;
	private Set<Class<?>> supports = new HashSet<>();

	public SAController(SAView saView, RenderBus renderBus) {
		supports.add(PlotListChangeEvent.class);
		supports.add(AltimeterChangeEvent.class);
		supports.add(GameObjectSelectedEvent.class);
		this.saView = saView;
		this.renderBus = renderBus;
	}

	private void updatePlotList(Plot[] plotList) {

		renderBus.dispatch(new RenderUpdateEvent(
				new RenderUpdateData(() -> saView.getPlotListPlane().drawPlotList(plotList)),
				RenderUpdateEventType.FUNCTION));

	}

	private void updateSAOrientation(float playerHeading) {

		renderBus.dispatch(new RenderUpdateEvent(
				new RenderUpdateData(() -> {
					QuaternionF rotation = QuaternionF.RotationX(-playerHeading);
					saView.getCircleGrid().getEditableTransformGraph().setRotation(rotation);
					saView.getPlotListPlane().orientateText(rotation);
				}),
				RenderUpdateEventType.FUNCTION));

	}

	@Override
	public void handle(Event<?> event) {

		if (event instanceof PlotListChangeEvent) {
			PlotListChangeEvent plotListChangeEvent = (PlotListChangeEvent) event;
			updatePlotList(plotListChangeEvent.getData().getPlotList());
		} else if (event instanceof AltimeterChangeEvent) {
			AltimeterChangeEvent plotListChangeEvent = (AltimeterChangeEvent) event;
			updateSAOrientation(plotListChangeEvent.getData().getHeading());
		} else if (event instanceof GameObjectSelectedEvent) {
			GameObjectSelectedEvent gameObjectSelectedEvent = (GameObjectSelectedEvent) event;
			for (PlotItemView plotItemView : saView.getPlotListPlane().getPlotItemViews()) {
				if (plotItemView.getTrackGameObject().getGameObjectData().getUuid().equals(gameObjectSelectedEvent.getData().getUuid())) {
					renderBus.dispatch(new TrackSelectedEvent(plotItemView.getTrackID()));
				}
			}
		}
	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return supports.contains(aClass);
	}
}
