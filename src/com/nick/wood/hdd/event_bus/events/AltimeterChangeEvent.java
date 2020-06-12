package com.nick.wood.hdd.event_bus.events;

import com.nick.wood.hdd.event_bus.data.AltimeterChangeData;
import com.nick.wood.hdd.event_bus.event_types.AltimeterChangeDataType;
import com.nick.wood.hdd.event_bus.interfaces.Event;

public class AltimeterChangeEvent implements Event<AltimeterChangeData> {

	private final AltimeterChangeData altimeterChangeData;
	private final AltimeterChangeDataType altimeterChangeDataType;

	public AltimeterChangeEvent(AltimeterChangeData altimeterChangeData, AltimeterChangeDataType altimeterChangeDataType) {
		this.altimeterChangeDataType = altimeterChangeDataType;
		this.altimeterChangeData = altimeterChangeData;
	}

	@Override
	public AltimeterChangeData getData() {
		return altimeterChangeData;
	}

	@Override
	public AltimeterChangeDataType getType() {
		return altimeterChangeDataType;
	}
}
