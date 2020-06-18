package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.hdd.gui_components.SquareGrid;
import com.nick.wood.hdd.gui_components.PlotListPlane;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class SAView {
	private final TransformSceneGraph saTransformGraph;
	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final PlotListPlane plotListPlane;

	public SAView(TransformSceneGraph saTransformGraph) {

		this.saTransformGraph = saTransformGraph;

		SquareGrid squareGrid = new SquareGrid(
				saTransformGraph,
				new Vec3f(0, 0, 0),
				QuaternionF.Identity,
				2,
				2,
				1000,
				1000);

		// aircraft
		MeshObject build = new MeshBuilder()
				.setTexture("/textures/green.png").build();

		Transform transform = transformBuilder
				.reset()
				.setScale(0.05f)
				.build();

		TransformSceneGraph stepTransformGraph = new TransformSceneGraph(saTransformGraph, transform);

		MeshSceneGraph stepMeshGraph = new MeshSceneGraph(stepTransformGraph, build);

		this.plotListPlane = new PlotListPlane(
				saTransformGraph,
				new Vec3f(0, 0, 0),
				QuaternionF.Identity,
				1000,
				1000,
				Vec3f.ZERO
		);

	}

	public PlotListPlane getPlotListPlane() {
		return plotListPlane;
	}
}
