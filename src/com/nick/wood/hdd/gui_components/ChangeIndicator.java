package com.nick.wood.hdd.gui_components;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.game_objects.MeshGameObject;
import com.nick.wood.graphics_library.objects.game_objects.GameObject;
import com.nick.wood.graphics_library.objects.game_objects.TransformObject;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class ChangeIndicator {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final Transform wholeTransform;

	public ChangeIndicator(GameObject parent, Vec3f position, QuaternionF rot) {

		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject whiteMarkers = new MeshBuilder()
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

		TransformObject wholeTransformGraphPers = new TransformObject(parent, wholeTransformPers);

		this.wholeTransform = transformBuilder.reset()
				.build();

		TransformObject wholeTransformGraph = new TransformObject(wholeTransformGraphPers, wholeTransform);

		// add 5 markers along the depth axis
		for (int i = 0; i < 5; i++) {

			Transform thisTransform = transformBuilder
					.setPosition(Vec3f.X.scale(i/20.0f))
					.setScale(new Vec3f(1, 1, 0.5f))
					.setRotation(QuaternionF.RotationX(Math.PI/2))
					.build();

			TransformObject thisTransformGraph = new TransformObject(wholeTransformGraph, thisTransform);

			MeshGameObject meshGameObject = new MeshGameObject(thisTransformGraph, whiteMarkers);

		}

	}

	public void setPitchStick(double pitchStick) {
		wholeTransform.setRotation(QuaternionF.RotationY(pitchStick));
	}
}

