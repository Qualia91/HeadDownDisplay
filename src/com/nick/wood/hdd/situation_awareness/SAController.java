package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.hdd.event_bus.busses.RenderBus;
import com.nick.wood.hdd.event_bus.events.AltimeterChangeEvent;
import com.nick.wood.hdd.event_bus.events.PlotListChangeEvent;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;

import java.util.HashSet;
import java.util.Set;

public class SAController implements Subscribable {
	private final SAView saView;
	private final RenderBus renderBus;
	private Set<Class<?>> supports = new HashSet<>();

	public SAController(SAView saView, RenderBus renderBus) {
		supports.add(PlotListChangeEvent.class);
		this.saView = saView;
		this.renderBus = renderBus;
	}

	private void update(PlotListChangeEvent plotListChangeEvent) {

		saView.getPlotListPlane().drawPlotList(plotListChangeEvent.getData().getPlotList());

	}

	@Override
	public void handle(Event<?> event) {

		if (event instanceof PlotListChangeEvent plotListChangeEvent) {
			update(plotListChangeEvent);
		}
	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return supports.contains(aClass);
	}
}
