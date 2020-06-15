package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.hdd.event_bus.busses.RenderBus;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;

public class SAController implements Subscribable {
	public SAController(SAView saView, RenderBus renderBus) {

	}

	@Override
	public void handle(Event<?> event) {

	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return false;
	}
}
