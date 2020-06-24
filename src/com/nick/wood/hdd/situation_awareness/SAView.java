package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.game_objects.MeshGameObject;
import com.nick.wood.graphics_library.objects.game_objects.TransformObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.hdd.gui_components.CircleGrid;
import com.nick.wood.hdd.gui_components.ModelManager;
import com.nick.wood.hdd.gui_components.SquareGrid;
import com.nick.wood.hdd.gui_components.PlotListPlane;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class SAView {
	private final TransformObject saTransformGraph;
	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final PlotListPlane plotListPlane;
	private final CircleGrid circleGrid;

	public SAView(TransformObject saTransformGraph,
	              int width,
	              int height,
	              ModelManager modelManager) {

		this.saTransformGraph = saTransformGraph;

		this.circleGrid = new CircleGrid(
				saTransformGraph,
				new Vec3f(0, 0, 0),
				QuaternionF.Identity,
				5,
				width);

		// aircraft
		MeshObject build = new MeshBuilder()
				.setTexture("/textures/green.png").build();

		Transform transform = transformBuilder
				.reset()
				.setScale(0.05f)
				.build();

		TransformObject stepTransformGraph = new TransformObject(saTransformGraph, transform);

		MeshGameObject stepMeshGraph = new MeshGameObject(stepTransformGraph, build);

		this.plotListPlane = new PlotListPlane(
				circleGrid.getEditableTransformGraph(),
				new Vec3f(0, 0, 0),
				QuaternionF.Identity,
				width,
				height,
				Vec3f.ZERO,
				modelManager
		);

	}

	public PlotListPlane getPlotListPlane() {
		return plotListPlane;
	}

	public CircleGrid getCircleGrid() {
		return circleGrid;
	}
}
