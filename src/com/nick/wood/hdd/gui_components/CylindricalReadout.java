package com.nick.wood.hdd.gui_components;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.game_objects.MeshGameObject;
import com.nick.wood.graphics_library.objects.game_objects.GameObject;
import com.nick.wood.graphics_library.objects.game_objects.TransformObject;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CylindricalReadout {

	private final TransformBuilder transformBuilder = new TransformBuilder();

	private final double radius;
	private final double angleStepSize;
	private final Transform cylindricalTransform;

	public CylindricalReadout(int intervals,
	                          Vec3f origin,
	                          GameObject parent,
	                          double radius,
	                          com.nick.wood.graphics_library.objects.mesh_objects.MeshObject markerMesh,
	                          QuaternionF cylinderPersistentRotation,
	                          boolean hasText,
	                          Function<Double, String> stringFunction,
	                          BiFunction<Double, TextItem, Transform> textTransformFunction) {

		this.radius = radius;

		this.angleStepSize = (2 * Math.PI / intervals);

		Transform cylindricalTransformPers = transformBuilder
				.reset()
				.setPosition(origin)
				.setRotation(cylinderPersistentRotation)
				.build();

		this.cylindricalTransform = transformBuilder
				.reset()
				.build();

		TransformObject cylindricalHeadingTransformObjectPers = new TransformObject(parent, cylindricalTransformPers);
		TransformObject cylindricalHeadingTransformObject = new TransformObject(cylindricalHeadingTransformObjectPers, cylindricalTransform);

		// create boxes all around at regular angular intervals
		for (int i = -intervals/2; i < intervals/2; i++) {
			double angleRad = i * angleStepSize;

			float posX = (float) (Math.cos(angleRad) * radius);
			float posY = (float) (Math.sin(angleRad) * radius);

			Transform transformMesh = new TransformBuilder()
					.setPosition(new Vec3f(posX, posY, 0))
					.setRotation(QuaternionF.RotationZ(angleRad))
					.build();

			TransformObject meshTransform = new TransformObject(cylindricalHeadingTransformObject, transformMesh);

			MeshGameObject meshGameObject = new MeshGameObject(
					meshTransform,
					markerMesh
			);

			if (hasText) {
				// text
				TextItem text = (TextItem) new MeshBuilder()
						.setMeshType(MeshType.TEXT)
						.setFontFile("/fonts/verandaGreenBold.png")
						.setText(stringFunction.apply(angleRad))
						.build();
				Transform textTransform = textTransformFunction.apply(angleRad, text);

				TransformObject textSceneGraph = new TransformObject(meshTransform, textTransform);
				MeshGameObject textMeshGameObject = new MeshGameObject(textSceneGraph, text);
			}
		}
	}

	public void setRotation(double angle) {
		cylindricalTransform.setRotation(QuaternionF.RotationZ(angle));
	}
}
