package com.nick.wood.hdd.gui_components;

import com.nick.wood.graphics_library.objects.game_objects.TransformObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
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
	private final ModelManager modelManager;

	public PlotListPlane(TransformObject parent,
	                     Vec3f position,
	                     QuaternionF rotation,
	                     int maxWidth,
	                     int maxHeight,
	                     Vec3f middle,
	                     ModelManager modelManager) {

		this.modelManager = modelManager;

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

	public void drawPlotList(Plot[] plots, float playerHeading) {
		for (int index = 0; index < plots.length; index++) {

			if (!(plotItemViews.size() > index)) {
				plotItemViews.add(new PlotItemView(
						transformGraph,
						this.maxWidth,
						this.maxHeight,
						modelManager));
			}

			plotItemViews.get(index).updateInformation(plots[index], playerHeading);

		}

		for (int i = plots.length; i < plotItemViews.size(); i++) {
			plotItemViews.get(i).hide();
		}

	}
}
