package com.nick.wood.hdd.gui_components;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.game_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.game_objects.SceneGraphNode;
import com.nick.wood.graphics_library.objects.game_objects.TransformSceneGraph;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class ChangeIndicator {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final Transform wholeTransform;

	public ChangeIndicator(SceneGraphNode parent, Vec3f position, QuaternionF rot) {

		MeshObject whiteMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.setScale(new Vec3f(0.01f, 0.01f, 0.25f))
						.build()).build();

		Transform wholeTransformPers = transformBuilder.reset()
				.setRotation(rot)
				.setPosition(position)
				.setScale(0.25f)
				.build();

		TransformSceneGraph wholeTransformGraphPers = new TransformSceneGraph(parent, wholeTransformPers);

		this.wholeTransform = transformBuilder.reset()
				.build();

		TransformSceneGraph wholeTransformGraph = new TransformSceneGraph(wholeTransformGraphPers, wholeTransform);

		// add 5 markers along the depth axis
		for (int i = 0; i < 5; i++) {

			Transform thisTransform = transformBuilder
					.setPosition(Vec3f.X.scale(i/20.0f))
					.setScale(new Vec3f(1, 1, 0.5f))
					.setRotation(QuaternionF.RotationX(Math.PI/2))
					.build();

			TransformSceneGraph thisTransformGraph = new TransformSceneGraph(wholeTransformGraph, thisTransform);

			MeshSceneGraph meshSceneGraph = new MeshSceneGraph(thisTransformGraph, whiteMarkers);

		}

	}

	public void setPitchStick(double pitchStick) {
		wholeTransform.setRotation(QuaternionF.RotationY(pitchStick));
	}
}

