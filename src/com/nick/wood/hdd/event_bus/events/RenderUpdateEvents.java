package com.nick.wood.hdd.event_bus.events;

import com.nick.wood.hdd.event_bus.data.RenderUpdateData;
import com.nick.wood.hdd.event_bus.event_types.RenderUpdateEventType;
import com.nick.wood.hdd.event_bus.interfaces.Event;

public class RenderUpdateEvents implements Event<RenderUpdateData> {

	private final RenderUpdateData renderUpdateData;
	private final RenderUpdateEventType renderUpdateEventType;

	public RenderUpdateEvents(RenderUpdateData renderUpdateData, RenderUpdateEventType renderUpdateEventType) {
		this.renderUpdateData = renderUpdateData;
		this.renderUpdateEventType = renderUpdateEventType;
	}

	@Override
	public RenderUpdateData getData() {
		return renderUpdateData;
	}

	@Override
	public RenderUpdateEventType getType() {
		return renderUpdateEventType;
	}

}
