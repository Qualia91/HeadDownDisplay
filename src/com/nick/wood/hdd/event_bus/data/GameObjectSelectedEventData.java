package com.nick.wood.hdd.event_bus.data;

import java.util.UUID;

public class GameObjectSelectedEventData {
	private final UUID uuid;

	public GameObjectSelectedEventData(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}
}
