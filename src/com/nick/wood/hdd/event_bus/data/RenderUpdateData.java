package com.nick.wood.hdd.event_bus.data;

import com.nick.wood.hdd.event_bus.interfaces.RenderManagementData;

public class RenderUpdateData implements RenderManagementData {

	private final String text;

	public RenderUpdateData(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
