package com.nick.wood.hdd.altimeter;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class AltimeterView {

	private final TransformBuilder transformBuilder = new TransformBuilder();

	public AltimeterView(TransformSceneGraph parentTransformGraph) {

		// sphere
		Transform sphereTransform = transformBuilder
				.reset()
				.build();
		TransformSceneGraph sphereTransformSceneGraph = new TransformSceneGraph(parentTransformGraph, sphereTransform);
		MeshObject sphere = new MeshBuilder()
				.setMeshType(MeshType.SQUARE)
				.setTransform(transformBuilder
						.setRotation(QuaternionF.RotationX(Math.PI/2))
						.setScale(Vec3f.ONE.scale(5))
						.build())
				.setTextureViaFbo(true)
				.build();
		MeshSceneGraph sphereMeshObject = new MeshSceneGraph(sphereTransformSceneGraph, sphere);

	}

}
