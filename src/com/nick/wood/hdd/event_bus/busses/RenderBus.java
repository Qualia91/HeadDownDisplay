package com.nick.wood.hdd.event_bus.busses;

import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.EventBus;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;

import java.util.ArrayList;
import java.util.List;

public class RenderBus implements EventBus {

	private final ArrayList<Subscribable> subscribables = new ArrayList<>();

	@Override
	public void register(Subscribable subscribable) {
		subscribables.add(subscribable);
	}

	@Override
	public void unregister(Subscribable subscribable) {
		subscribables.remove(subscribable);
	}

	@Override
	public void dispatch(Event<?> event) {
		for (Subscribable subscribable : subscribables) {
			if (subscribable.supports(event.getClass())) {
				subscribable.handle(event);
			}
		}
	}

	@Override
	public List<Subscribable> getSubscribers() {
		return subscribables;
	}
}
