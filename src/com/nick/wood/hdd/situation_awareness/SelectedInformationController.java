package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.hdd.event_bus.busses.RenderBus;
import com.nick.wood.hdd.event_bus.data.RenderUpdateData;
import com.nick.wood.hdd.event_bus.event_types.RenderUpdateEventType;
import com.nick.wood.hdd.event_bus.events.PlotListChangeEvent;
import com.nick.wood.hdd.event_bus.events.RenderUpdateEvents;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

public class SelectedInformationController implements Subscribable {
	private final SelectedInformationView selectedInformationView;
	private final RenderBus renderBus;
	private Set<Class<?>> supports = new HashSet<>();

	private final NumberFormat formatter = new DecimalFormat("##.00");

	public SelectedInformationController(SelectedInformationView selectedInformationView, RenderBus renderBus) {
		supports.add(PlotListChangeEvent.class);
		this.selectedInformationView = selectedInformationView;
		this.renderBus = renderBus;
	}

	private void update(PlotListChangeEvent plotListChangeEvent) {

		renderBus.dispatch(new RenderUpdateEvents(
				new RenderUpdateData(() -> {
					for (Plot plot : plotListChangeEvent.getData().getPlotList()) {
						if (plot.isSelected()) {
							selectedInformationView.getIdText().changeText(String.valueOf(plot.getId()));
							selectedInformationView.getTypeText().changeText(plot.getSisoEnum().toString());
							selectedInformationView.getBearingText().changeText(String.valueOf(formatter.format(Math.toDegrees(plot.getBra().getX()))));
							selectedInformationView.getRangeText().changeText(String.valueOf(formatter.format(plot.getBra().getY())));
							selectedInformationView.getAltText().changeText(String.valueOf(formatter.format(plot.getBra().getZ())));
							selectedInformationView.getEnemyHeadingText().changeText(String.valueOf(formatter.format(Math.toDegrees(plot.getOrientation().getI()))));
							selectedInformationView.getEnemyPitchText().changeText(String.valueOf(formatter.format(Math.toDegrees(plot.getOrientation().getJ()))));
							selectedInformationView.getEnemyRollText().changeText(String.valueOf(formatter.format(Math.toDegrees(plot.getOrientation().getK()))));
							selectedInformationView.getAllegianceText().changeText(plot.getAllegiance().toString());
						}
					}

				}),
				RenderUpdateEventType.FUNCTION));


	}

	@Override
	public void handle(Event<?> event) {

		if (event instanceof PlotListChangeEvent plotListChangeEvent) {
			update(plotListChangeEvent);
		}
	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return supports.contains(aClass);
	}
}