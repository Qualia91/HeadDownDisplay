package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.hdd.gui_components.Grid;
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

		Grid grid = new Grid(
				saTransformGraph,
				new Vec3f(0, 0, 0),
				QuaternionF.Identity,
				5,
				5);

		// aircraft
		MeshObject build = new MeshBuilder().build();

		Transform transform = transformBuilder
				.reset()
				.setScale(0.1f)
				.build();

		TransformSceneGraph stepTransformGraph = new TransformSceneGraph(saTransformGraph, transform);

		MeshSceneGraph stepMeshGraph = new MeshSceneGraph(stepTransformGraph, build);

		this.plotListPlane = new PlotListPlane(
				saTransformGraph,
				new Vec3f(0, 0, 0),
				QuaternionF.Identity,
				500,
				500,
				Vec3f.ZERO
		);

	}

	public PlotListPlane getPlotListPlane() {
		return plotListPlane;
	}
}
