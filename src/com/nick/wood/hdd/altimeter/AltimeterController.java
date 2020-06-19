package com.nick.wood.hdd.altimeter;

import com.nick.wood.hdd.event_bus.busses.RenderBus;
import com.nick.wood.hdd.event_bus.events.AltimeterChangeEvent;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;

import java.util.HashSet;
import java.util.Set;

public class AltimeterController implements Subscribable {

	private final Set<Class<?>> supports = new HashSet<>();
	private final AltimeterView altimeterView;
	private final RenderBus renderBus;

	public AltimeterController(AltimeterView altimeterView, RenderBus renderBus) {

		supports.add(AltimeterChangeEvent.class);

		this.altimeterView = altimeterView;
		this.renderBus = renderBus;

	}

	@Override
	public void handle(Event<?> event) {

	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return supports.contains(aClass);
	}
}
