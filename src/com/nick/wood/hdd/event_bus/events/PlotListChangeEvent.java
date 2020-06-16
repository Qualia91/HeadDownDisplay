package com.nick.wood.hdd.event_bus.events;

import com.nick.wood.hdd.event_bus.data.AltimeterChangeData;
import com.nick.wood.hdd.event_bus.data.PlotsListChangeData;
import com.nick.wood.hdd.event_bus.event_types.AltimeterChangeDataType;
import com.nick.wood.hdd.event_bus.event_types.PlotListChangeDataType;
import com.nick.wood.hdd.event_bus.interfaces.Event;

public class PlotListChangeEvent implements Event<PlotsListChangeData> {

	private final PlotsListChangeData plotsListChangeData;
	private final PlotListChangeDataType plotListChangeDataType;

	public PlotListChangeEvent(PlotsListChangeData plotsListChangeData, PlotListChangeDataType plotListChangeDataType) {
		this.plotsListChangeData = plotsListChangeData;
		this.plotListChangeDataType = plotListChangeDataType;
	}

	@Override
	public PlotsListChangeData getData() {
		return plotsListChangeData;
	}

	@Override
	public PlotListChangeDataType getType() {
		return plotListChangeDataType;
	}
}
