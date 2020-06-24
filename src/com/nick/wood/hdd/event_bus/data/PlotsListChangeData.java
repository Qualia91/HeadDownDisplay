package com.nick.wood.hdd.event_bus.data;

import com.nick.wood.hdd.situation_awareness.Plot;

import java.util.ArrayList;

public class PlotsListChangeData {

	Plot[] plotList;

	public PlotsListChangeData(Plot[] plotList) {
		this.plotList = plotList;
	}

	public Plot[] getPlotList() {
		return plotList;
	}

	public void setPlotList(Plot[] plotList) {
		this.plotList = plotList;
	}
}
