package com.nick.wood.hdd.gui_components;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.SceneGraphNode;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class SquareGrid {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final int widthMaxValue;
	private final int heightMaxValue;

	// player is in the middle
	// width and height increments are either side
	// make a grid with dimensions of 2 in y and z plane, center at 0
	public SquareGrid(SceneGraphNode parent,
	                  Vec3f position,
	                  QuaternionF rotation,
	                  int widthIncrements,
	                  int heightIncrements,
	                  int widthMaxValue,
	                  int heightMaxValue) {

		this.widthMaxValue = widthMaxValue;
		this.heightMaxValue = heightMaxValue;

		MeshObject whiteMarkers = new MeshBuilder()
				.setMeshType(MeshType.SQUARE)
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.build())
				.build();

		Transform gridTransform = transformBuilder
				.resetScale()
				.setPosition(position)
				.setRotation(rotation)
				.build();

		TransformSceneGraph gridTransformGraph = new TransformSceneGraph(parent, gridTransform);

		float widthStepSize = 1.0f/widthIncrements;
		float heightStepSize = 1.0f/heightIncrements;

		for (int i = -widthIncrements; i <= widthIncrements; i++) {

			Transform transform = transformBuilder
					.reset()
					.setPosition(new Vec3f(0, i * widthStepSize, 0))
					.setScale(new Vec3f(0.01f, 0.01f, 2))
					.build();

			TransformSceneGraph stepTransformGraph = new TransformSceneGraph(gridTransformGraph, transform);

			MeshSceneGraph stepMeshGraph = new MeshSceneGraph(stepTransformGraph, whiteMarkers);

		}

		for (int i = -heightIncrements; i <= heightIncrements; i++) {

			Transform transform = transformBuilder
					.reset()
					.setPosition(new Vec3f(0, 0, i * heightStepSize))
					.setScale(new Vec3f(0.01f, 2, 0.01f))
					.build();

			TransformSceneGraph stepTransformGraph = new TransformSceneGraph(gridTransformGraph, transform);

			MeshSceneGraph stepMeshGraph = new MeshSceneGraph(stepTransformGraph, whiteMarkers);

		}


	}

}
