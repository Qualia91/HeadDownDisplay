package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.hdd.event_bus.busses.RenderBus;
import com.nick.wood.hdd.event_bus.data.RenderUpdateData;
import com.nick.wood.hdd.event_bus.event_types.RenderUpdateEventType;
import com.nick.wood.hdd.event_bus.events.AltimeterChangeEvent;
import com.nick.wood.hdd.event_bus.events.PlotListChangeEvent;
import com.nick.wood.hdd.event_bus.events.RenderUpdateEvent;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;

import java.util.HashSet;
import java.util.Set;

public class SAController implements Subscribable {
	private final SAView saView;
	private final RenderBus renderBus;
	private Set<Class<?>> supports = new HashSet<>();
	private float playerHeading = 0;
	private Plot[] recentPlots = new Plot[0];

	public SAController(SAView saView, RenderBus renderBus) {
		supports.add(PlotListChangeEvent.class);
		supports.add(AltimeterChangeEvent.class);
		this.saView = saView;
		this.renderBus = renderBus;
	}

	private void update() {


		renderBus.dispatch(new RenderUpdateEvent(
				new RenderUpdateData(() -> {
					saView.getPlotListPlane().drawPlotList(recentPlots, playerHeading);
				}),
				RenderUpdateEventType.FUNCTION));


	}

	@Override
	public void handle(Event<?> event) {

		if (event instanceof PlotListChangeEvent) {
			PlotListChangeEvent plotListChangeEvent = (PlotListChangeEvent) event;
			recentPlots = plotListChangeEvent.getData().getPlotList();
			update();
		} else if (event instanceof AltimeterChangeEvent) {
			AltimeterChangeEvent plotListChangeEvent = (AltimeterChangeEvent) event;
			this.playerHeading = plotListChangeEvent.getData().getHeading();
			update();
		}
	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return supports.contains(aClass);
	}
}
