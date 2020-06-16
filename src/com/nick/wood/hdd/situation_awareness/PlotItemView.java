package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.SceneGraphNode;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class PlotItemView {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final Transform trackTransform;
	private final float maxWidth;
	private final float maxHeight;

	public PlotItemView(Vec3f position, QuaternionF orientation, SceneGraphNode parent, MeshObject trackMesh, float maxWidth, float maxHeight) {

		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;

		// calc position in grid
		// find "forward" and "side" locations
		float forward = (float) (position.getY() * Math.cos(position.getX())) / maxWidth;
		float side = (float) (position.getY() * Math.sin(position.getX())) / maxHeight;

		this.trackTransform = transformBuilder
				.setPosition(new Vec3f(0, forward, side))
				.setRotation(orientation)
				.setScale(0.2f)
				.build();

		TransformSceneGraph trackTransformGraph = new TransformSceneGraph(parent, trackTransform);

		MeshSceneGraph meshSceneGraph = new MeshSceneGraph(trackTransformGraph, trackMesh);

	}

	public void updateInformation(Plot plot) {

		// calc position in grid
		// find "forward" and "side" locations
		float forward = (float) (plot.getBra().getY() * Math.cos(plot.getBra().getX())) / maxWidth;
		float side = (float) (plot.getBra().getY() * Math.sin(plot.getBra().getX())) / maxHeight;

		// check if in grid
		if (Math.abs(side) > 1) {
			side = Math.copySign(1, side);
		}
		if (Math.abs(forward) > 1) {
			forward = Math.copySign(1, forward);
		}
		this.trackTransform.setPosition(new Vec3f(0, -side, forward));
		this.trackTransform.setRotation(plot.getOrientation());
	}

	public void hide() {
		this.trackTransform.setPosition(new Vec3f(-100, 0, 0));
	}
}
