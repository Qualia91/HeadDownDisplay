package com.nick.wood.hdd.event_bus.events;

import com.nick.wood.hdd.event_bus.interfaces.RenderManagementData;
import com.nick.wood.hdd.event_bus.event_types.RenderManagementEventType;
import com.nick.wood.hdd.event_bus.interfaces.Event;

public class RenderManagementEvent implements Event<RenderManagementData> {

	private final RenderManagementData renderManagementData;
	private final RenderManagementEventType renderManagementEventType;

	public RenderManagementEvent(RenderManagementData renderManagementData, RenderManagementEventType renderManagementEventType) {
		this.renderManagementData = renderManagementData;
		this.renderManagementEventType = renderManagementEventType;
	}

	@Override
	public RenderManagementData getData() {
		return renderManagementData;
	}

	@Override
	public RenderManagementEventType getType() {
		return renderManagementEventType;
	}
}
