package com.nick.wood.hdd.event_bus.events;

import com.nick.wood.hdd.event_bus.data.GameObjectSelectedEventData;
import com.nick.wood.hdd.event_bus.event_types.GameObjectSelectedEventType;
import com.nick.wood.hdd.event_bus.interfaces.Event;

public class GameObjectSelectedEvent implements Event<GameObjectSelectedEventData> {

	private final GameObjectSelectedEventData data;
	private final GameObjectSelectedEventType type;

	public GameObjectSelectedEvent(GameObjectSelectedEventData data, GameObjectSelectedEventType type) {
		this.data = data;
		this.type = type;
	}

	@Override
	public GameObjectSelectedEventData getData() {
		return data;
	}

	@Override
	public GameObjectSelectedEventType getType() {
		return type;
	}
}
