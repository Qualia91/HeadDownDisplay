package com.nick.wood.hdd.event_bus.event_types;

import com.nick.wood.hdd.event_bus.interfaces.BaseEventType;

public enum RenderUpdateEventType implements BaseEventType {
	SCENE,
	HUD,
	CAMERA,
	FUNCTION;
}
