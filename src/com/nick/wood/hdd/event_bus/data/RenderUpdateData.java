package com.nick.wood.hdd.event_bus.data;

import com.nick.wood.hdd.event_bus.interfaces.RenderManagementData;

public class RenderUpdateData implements RenderManagementData {

	private final Runnable runnable;

	public RenderUpdateData(Runnable runnable) {
		this.runnable = runnable;
	}

	public Runnable getRunnable() {
		return runnable;
	}
}
