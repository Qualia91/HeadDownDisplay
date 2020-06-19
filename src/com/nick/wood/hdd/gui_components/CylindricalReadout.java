package com.nick.wood.hdd.gui_components;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.game_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.game_objects.SceneGraphNode;
import com.nick.wood.graphics_library.objects.game_objects.TransformSceneGraph;
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
	                          SceneGraphNode parent,
	                          double radius,
	                          MeshObject markerMesh,
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

		TransformSceneGraph cylindricalHeadingTransformObjectPers = new TransformSceneGraph(parent, cylindricalTransformPers);
		TransformSceneGraph cylindricalHeadingTransformObject = new TransformSceneGraph(cylindricalHeadingTransformObjectPers, cylindricalTransform);

		// create boxes all around at regular angular intervals
		for (int i = -intervals/2; i < intervals/2; i++) {
			double angleRad = i * angleStepSize;

			float posX = (float) (Math.cos(angleRad) * radius);
			float posY = (float) (Math.sin(angleRad) * radius);

			Transform transformMesh = new TransformBuilder()
					.setPosition(new Vec3f(posX, posY, 0))
					.setRotation(QuaternionF.RotationZ(angleRad))
					.build();

			TransformSceneGraph meshTransform = new TransformSceneGraph(cylindricalHeadingTransformObject, transformMesh);

			MeshSceneGraph meshSceneGraph = new MeshSceneGraph(
					meshTransform,
					markerMesh
			);

			if (hasText) {
				// text
				TextItem text = (TextItem) new MeshBuilder()
						.setMeshType(MeshType.TEXT)
						.setFontFile("/font/verandaGreenBold.png")
						.setText(stringFunction.apply(angleRad))
						.build();
				Transform textTransform = textTransformFunction.apply(angleRad, text);

				TransformSceneGraph textSceneGraph = new TransformSceneGraph(meshTransform, textTransform);
				MeshSceneGraph textMeshObject = new MeshSceneGraph(textSceneGraph, text);
			}
		}
	}

	public void setRotation(double angle) {
		cylindricalTransform.setRotation(QuaternionF.RotationZ(angle));
	}
}
