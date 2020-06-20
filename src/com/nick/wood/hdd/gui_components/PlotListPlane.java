package com.nick.wood.hdd.gui_components;

import com.nick.wood.graphics_library.objects.game_objects.TransformObject;
import com.nick.wood.hdd.situation_awareness.Plot;
import com.nick.wood.hdd.situation_awareness.PlotItemView;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

import java.util.ArrayList;

public class PlotListPlane {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final TransformObject parent;
	private final Transform transform;
	private final TransformObject transformGraph;
	private final Vec3f middle;
	private final float maxWidth;
	private final float maxHeight;

	private final ArrayList<PlotItemView> plotItemViews = new ArrayList<>();

	public PlotListPlane(TransformObject parent, Vec3f position, QuaternionF rotation, int maxWidth, int maxHeight, Vec3f middle) {

		this.parent = parent;
		this.middle = middle;

		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;

		this.transform = transformBuilder
				.reset()
				.setPosition(position)
				.setRotation(rotation)
				.build();

		this.transformGraph = new TransformObject(parent, transform);

	}

	public void drawPlotList(ArrayList<Plot> plots) {
		for (int index = 0; index < plots.size(); index++) {

			if (plotItemViews.size() > index) {
				plotItemViews.get(index).updateInformation(plots.get(index));
			} else {
				plotItemViews.add(new PlotItemView(
						plots.get(index).getBra(),
						plots.get(index).getOrientation(),
						transformGraph,
						this.maxWidth,
						this.maxHeight));
			}

		}

		for (int i = plots.size(); i < plotItemViews.size(); i++) {
			plotItemViews.get(i).hide();
		}

	}
}
