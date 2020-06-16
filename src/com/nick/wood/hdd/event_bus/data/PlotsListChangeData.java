package com.nick.wood.hdd.event_bus.data;

import com.nick.wood.hdd.situation_awareness.Plot;

import java.util.ArrayList;

public class PlotsListChangeData {

	ArrayList<Plot> plotList;

	public PlotsListChangeData(ArrayList<Plot> plotList) {
		this.plotList = plotList;
	}

	public ArrayList<Plot> getPlotList() {
		return plotList;
	}

	public void setPlotList(ArrayList<Plot> plotList) {
		this.plotList = plotList;
	}
}
