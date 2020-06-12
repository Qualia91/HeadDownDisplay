package com.nick.wood.hdd.event_bus.interfaces;

public interface Event<T> {
	T getData();
	BaseEventType getType();
}
