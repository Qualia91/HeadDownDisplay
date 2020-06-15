package com.nick.wood.hdd.gui_components;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class PlotListPlane {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final MeshObject trackMesh;
	private final TransformSceneGraph parent;
	private final Transform transform;
	private final TransformSceneGraph transformGraph;
	private final Vec3f middle;
	private final float scale;
	private final float maxWidth;
	private final float maxHeight;
	private final int gridWidth;
	private final int gridHeight;

	public PlotListPlane(TransformSceneGraph parent, Vec3f position, QuaternionF rotation, int width, int height, Vec3f middle, float scale) {

		this.parent = parent;
		this.middle = middle;
		this.scale = scale;
		this.maxWidth = width * scale;
		this.maxHeight = height * scale;
		this.gridWidth = width;
		this.gridHeight = height;

		this.trackMesh = new MeshBuilder()
				.setMeshType(MeshType.CUBOID).build();

		this.transform = transformBuilder
				.reset()
				.setPosition(position)
				.setRotation(rotation)
				.build();

		this.transformGraph = new TransformSceneGraph(parent, transform);

		addTrack(0, 200, 0);
		addTrack((float)Math.PI/2, 200, 0);
		addTrack((float)Math.PI, 200, 0);
		addTrack((float)(3*Math.PI/2), 200, 0);

	}

	public void addTrack(float bearing, float range, float altitude) {

		// calc position in grid
		// find "forward" and "side" locations
		float side = (float) (range * Math.cos(bearing)) / (2*scale);
		float forward = (float) (range * Math.sin(bearing)) / (2*scale);

		// check if in grid
		if (side < maxWidth && forward < maxHeight) {

			Transform trackTransform = transformBuilder
					.reset()
					.setPosition(new Vec3f(0, forward, side))
					.setScale(0.2f)
					.build();

			TransformSceneGraph trackTransformGraph = new TransformSceneGraph(parent, trackTransform);

			MeshSceneGraph meshSceneGraph = new MeshSceneGraph(trackTransformGraph, trackMesh);

		}

	}
}
