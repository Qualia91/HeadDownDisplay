package com.nick.wood.hdd.altimeter;

import com.nick.wood.graphics_library.lighting.Attenuation;
import com.nick.wood.graphics_library.lighting.PointLight;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class AltimeterView {

	private final TransformBuilder transformBuilder = new TransformBuilder();

	public AltimeterView(TransformSceneGraph parentTransformGraph) {

		// text
		Transform textTransform = transformBuilder
				.setScale(Vec3f.ONE.scale(10))
				.setRotation(QuaternionF.RotationX((float) Math.PI / 2))
				.setPosition(new Vec3f(-5, 0, 5))
				.build();
		TransformSceneGraph textTransformSceneGraph = new TransformSceneGraph(parentTransformGraph, textTransform);
		TextItem textItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.build();
		MeshSceneGraph textMeshObject = new MeshSceneGraph(textTransformSceneGraph, textItem);


		// sphere
		Transform sphereTransform = transformBuilder

				.setScale(Vec3f.ONE)
				.setRotation(QuaternionF.Identity)
				.setPosition(Vec3f.ZERO)

				.build();
		TransformSceneGraph sphereTransformSceneGraph = new TransformSceneGraph(parentTransformGraph, sphereTransform);
		MeshObject sphere = new MeshBuilder()

				.setMeshType(MeshType.SPHERE)
				.setTriangleNumber(11)
				.setTransform(transformBuilder
						.setRotation(QuaternionF.RotationZ(Math.PI/2.0))
						.setScale(Vec3f.ONE.scale(5)).build())

				.setTextureViaFbo(true)
				.build();
		MeshSceneGraph sphereMeshObject = new MeshSceneGraph(sphereTransformSceneGraph, sphere);

	}
}
