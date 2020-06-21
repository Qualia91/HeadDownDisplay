package com.nick.wood.hdd.altimeter;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.game_objects.MeshGameObject;
import com.nick.wood.graphics_library.objects.game_objects.TransformObject;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class AltimeterView {

	private final TransformBuilder transformBuilder = new TransformBuilder();

	public AltimeterView(TransformObject parentTransformGraph) {

		// sphere
		Transform sphereTransform = transformBuilder
				.reset()
				.build();
		TransformObject sphereTransformObject = new TransformObject(parentTransformGraph, sphereTransform);
		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject sphere = new MeshBuilder()
				.setMeshType(MeshType.SQUARE)
				.setTransform(transformBuilder
						.setRotation(QuaternionF.RotationX(-Math.PI/2))
						.setScale(Vec3f.ONE.scale(5))
						.build())
				.setTextureViaFbo(true)
				.build();
		MeshGameObject sphereMeshGameObject = new MeshGameObject(sphereTransformObject, sphere);

	}

}
