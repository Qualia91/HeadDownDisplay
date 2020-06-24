package com.nick.wood.hdd.event_bus.events;

import com.nick.wood.hdd.event_bus.event_types.TrackSelectedEventType;
import com.nick.wood.hdd.event_bus.interfaces.BaseEventType;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.situation_awareness.TrackID;

public class TrackSelectedEvent implements Event<TrackID> {

	private final TrackID trackID;

	public TrackSelectedEvent(TrackID trackID) {
		this.trackID = trackID;
	}

	@Override
	public TrackID getData() {
		return trackID;
	}

	@Override
	public TrackSelectedEventType getType() {
		return TrackSelectedEventType.SELECTED;
	}
}
