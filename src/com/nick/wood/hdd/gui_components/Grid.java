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

public class Grid {

	private final TransformBuilder transformBuilder = new TransformBuilder();

	public Grid(SceneGraphNode parent, Vec3f position, QuaternionF rotation, int widthIncrements, int heightIncrements) {

		MeshObject whiteMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.build())
				.build();

		Transform gridTransform = transformBuilder
				.reset()
				.setPosition(position)
				.setRotation(rotation)
				.build();

		TransformSceneGraph gridTransformGraph = new TransformSceneGraph(parent, gridTransform);

		for (int i = -widthIncrements/2; i <= widthIncrements/2; i++) {

			Transform transform = transformBuilder
					.reset()
					.setPosition(new Vec3f(0, i, 0))
					.setScale(new Vec3f(0.05f, 0.05f, 10))
					.build();

			TransformSceneGraph stepTransformGraph = new TransformSceneGraph(gridTransformGraph, transform);

			MeshSceneGraph stepMeshGraph = new MeshSceneGraph(stepTransformGraph, whiteMarkers);

		}

		for (int i = -heightIncrements/2; i <= heightIncrements/2; i++) {

			Transform transform = transformBuilder
					.reset()
					.setPosition(new Vec3f(0, 0, i))
					.setScale(new Vec3f(0.05f, 10, 0.05f))
					.build();

			TransformSceneGraph stepTransformGraph = new TransformSceneGraph(gridTransformGraph, transform);

			MeshSceneGraph stepMeshGraph = new MeshSceneGraph(stepTransformGraph, whiteMarkers);

		}

	}

}
