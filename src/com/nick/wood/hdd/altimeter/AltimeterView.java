package com.nick.wood.hdd.altimeter;

import com.nick.wood.graphics_library.lighting.Attenuation;
import com.nick.wood.graphics_library.lighting.PointLight;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.SceneGraphNode;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.graphics_library.utils.Creation;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class AltimeterView {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final Transform cylindricalHeadingTransform;
	private final double headingAngleStepSize;
	private final double radiusOfHeadingCylinder = 3;
	private final TextItem rollTextItem;
	private final Transform rollTextTransform;
	private final TextItem altTextItem;
	private final Transform altTextTransform;
	private final float radiusOfPitchCylinder = 2.7f;
	private final Transform cylindricalPitchTransform;
	private final double pitchAngleStepSize;

	public AltimeterView(TransformSceneGraph parentTransformGraph) {

		// roll text
		this.rollTextItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.build();
		Transform persistentRollTextTransform = transformBuilder
				.setScale(Vec3f.ONE)
				.setRotation(QuaternionF.RotationX((float) Math.PI / 2))
				.setPosition(new Vec3f(-4, 0, 0.6f))
				.build();
		this.rollTextTransform = transformBuilder
				.resetRotation()
				.setPosition(new Vec3f(0, 0, -rollTextItem.getWidth()/2.0f))
				.build();
		TransformSceneGraph textTransformSceneGraphPersistent = new TransformSceneGraph(parentTransformGraph, persistentRollTextTransform);
		TransformSceneGraph textTransformSceneGraph = new TransformSceneGraph(textTransformSceneGraphPersistent, rollTextTransform);
		MeshSceneGraph textMeshObject = new MeshSceneGraph(textTransformSceneGraph, rollTextItem);

		// text
		this.altTextItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setText("HELLO")
				.build();
		Transform persistentAltTextTransform = transformBuilder
				.setScale(Vec3f.ONE)
				.resetRotation()
				.setRotation(QuaternionF.RotationX((float) Math.PI / 2))
				.setPosition(new Vec3f(-4, 1, -altTextItem.getHeight()/2f))
				.build();
		this.altTextTransform = transformBuilder
				.setPosition(new Vec3f(0, 0, 0))
				.resetRotation()
				.build();
		TransformSceneGraph textTransformSceneGraphPersistentAlt = new TransformSceneGraph(parentTransformGraph, persistentAltTextTransform);
		TransformSceneGraph textTransformSceneGraphAlt = new TransformSceneGraph(textTransformSceneGraphPersistentAlt, altTextTransform);
		MeshSceneGraph textMeshObjectAlt = new MeshSceneGraph(textTransformSceneGraphAlt, altTextItem);

		// sphere
		Transform sphereTransform = transformBuilder
				.reset()
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

		// arrow
		MeshObject arrowMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("D:\\Software\\Programming\\projects\\Java\\GraphicsLibrary\\src\\main\\resources\\models\\arrow.obj")
				.setTransform(
						transformBuilder
								.setScale(Vec3f.ONE.scale(0.1f))
								.setPosition(new Vec3f(-1, 2.5f, 0))
								.setRotation(QuaternionF.RotationZ((float) -Math.PI / 2))
								.build()
				)
				.build();
		MeshSceneGraph arrowMeshSceneGraph = new MeshSceneGraph(sphereTransformSceneGraph, arrowMesh);


		MeshObject levelBlackMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/black.png")
				.setTransform(transformBuilder
						.setRotation(QuaternionF.RotationZ(Math.PI/2.0))
						.setScale(new Vec3f(0.05f, 0.5f, 0.05f)).build()).build();

		MeshObject whiteMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.resetPosition()
						.setScale(new Vec3f(0.01f, 0.01f, 0.25f))
						.build()).build();

		// Level marker
		Creation.CreateObject(Vec3f.Z.scale(-radiusOfPitchCylinder + 1).add(new Vec3f(1, 0, 0)), parentTransformGraph, levelBlackMarkers);
		Creation.CreateObject(Vec3f.Z.scale(-radiusOfPitchCylinder + 1).add(new Vec3f(0, 0, 0)), parentTransformGraph, levelBlackMarkers);
		Creation.CreateObject(Vec3f.Z.scale(-radiusOfPitchCylinder + 1).add(new Vec3f(-1, 0, 0)), parentTransformGraph, levelBlackMarkers);


		// pitch cylinder
		this.cylindricalPitchTransform = transformBuilder.reset().build();
		TransformSceneGraph cylindricalAltitudeTransformObject = new TransformSceneGraph(parentTransformGraph, cylindricalPitchTransform);

		// create boxes all around at regular angular intervals
		int pitchIntervals = 36;
		this.pitchAngleStepSize = (2 * Math.PI / pitchIntervals);
		for (int i = -pitchIntervals/2; i < pitchIntervals/2; i++) {
			double angleRad = i * pitchAngleStepSize;

			float posX = (float) -(Math.sin(angleRad) * radiusOfPitchCylinder);
			float posZ = (float) (Math.cos(angleRad) * radiusOfPitchCylinder);

			MeshSceneGraph meshSceneGraph = Creation.CreateObjectAndGetSceneObject(
					new Vec3f(posX, 0, posZ),
					QuaternionF.RotationX(Math.PI/2),
					cylindricalAltitudeTransformObject,
					whiteMarkers);

			// text
			TextItem pitchText = (TextItem) new MeshBuilder()
					.setMeshType(MeshType.TEXT)
					.setText(String.valueOf((int)(Math.round( Math.toDegrees(-angleRad + Math.PI/2) / 10.0) * 10)))
					.build();
			Transform headingNumberText = transformBuilder
					.setPosition(new Vec3f(0, 0, -pitchText.getWidth()/2))
					.setRotation(QuaternionF.RotationZ(angleRad - Math.PI/2))
					.setScale(Vec3f.ONE)
					.build();
			TransformSceneGraph headingNumberTextSceneGraph = new TransformSceneGraph(meshSceneGraph, headingNumberText);
			MeshSceneGraph headingTextMeshObject = new MeshSceneGraph(headingNumberTextSceneGraph, pitchText);
		}

		// Heading indicator
		this.cylindricalHeadingTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(0, 0, -1)).build();
		TransformSceneGraph cylindricalHeadingTransformObject = new TransformSceneGraph(parentTransformGraph, cylindricalHeadingTransform);

		// create boxes all around at regular angular intervals
		int headingIntervals = 36;
		this.headingAngleStepSize = (2 * Math.PI / headingIntervals);
		for (int i = 0; i < headingIntervals; i++) {
			double angleRad = i * headingAngleStepSize;

			float posY = (float) (Math.sin(angleRad) * radiusOfHeadingCylinder);
			float posX = (float) (Math.cos(angleRad) * radiusOfHeadingCylinder);

			MeshSceneGraph meshSceneGraph = Creation.CreateObjectAndGetSceneObject(
					new Vec3f(posX, posY, 0),
					QuaternionF.RotationZ(angleRad - Math.PI),
					cylindricalHeadingTransformObject,
					whiteMarkers);

			// text
			TextItem headingText = (TextItem) new MeshBuilder()
					.setMeshType(MeshType.TEXT)
					.setText(String.valueOf((int)(Math.round( Math.toDegrees((angleRad + Math.PI) % Math.PI) / 10.0) * 10)))
					.build();
			Transform headingNumberText = transformBuilder
					.setPosition(new Vec3f(0, headingText.getWidth(), 0.15f))
					.setRotation(QuaternionF.RotationX(Math.PI/2.0))
					.setScale(Vec3f.ONE.scale(2))
					.build();
			TransformSceneGraph headingNumberTextSceneGraph = new TransformSceneGraph(meshSceneGraph, headingNumberText);
			MeshSceneGraph headingTextMeshObject = new MeshSceneGraph(headingNumberTextSceneGraph, headingText);
		}

	}

	private void createDataCylinder(Vec3f origin,
	                                SceneGraphNode parent,
	                                int intervals,
	                                float radius,
	                                MeshObject markerMesh,
	                                QuaternionF cylinderPersistentRotation,
	                                boolean hasText,
	                                float textScale) {

		Transform cylindricalTransformPers = transformBuilder
				.reset()
				.setPosition(origin)
				.setRotation(cylinderPersistentRotation)
				.build();

		Transform cylindricalTransform = transformBuilder
				.reset()
				.build();

		TransformSceneGraph cylindricalHeadingTransformObjectPers = new TransformSceneGraph(parent, cylindricalTransformPers);
		TransformSceneGraph cylindricalHeadingTransformObject = new TransformSceneGraph(cylindricalHeadingTransformObjectPers, cylindricalTransform);

		// create boxes all around at regular angular intervals
		double stepSize = (2 * Math.PI / intervals);
		for (int i = 0; i < intervals; i++) {
			double angleRad = i * stepSize;

			float posY = (float) (Math.sin(angleRad) * radius);
			float posX = (float) (Math.cos(angleRad) * radius);

			Transform transformMesh = new TransformBuilder()
					.setPosition(new Vec3f(posX, posY, 0))
					.setRotation(QuaternionF.RotationZ(angleRad - Math.PI))
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
						.setText(String.valueOf((int) (Math.round(Math.toDegrees((angleRad + Math.PI) % Math.PI) / 10.0) * 10)))
						.build();
				Transform textTransform = transformBuilder
						.setPosition(new Vec3f(0, text.getWidth(), 0.15f))
						.setRotation(QuaternionF.RotationX(Math.PI / 2.0))
						.setScale(Vec3f.ONE.scale(textScale))
						.build();
				TransformSceneGraph textSceneGraph = new TransformSceneGraph(meshSceneGraph, textTransform);
				MeshSceneGraph textMeshObject = new MeshSceneGraph(textSceneGraph, text);
			}
		}
	}

	public Transform getCylindricalHeadingTransform() {
		return cylindricalHeadingTransform;
	}

	public double getHeadingAngleStepSize() {
		return headingAngleStepSize;
	}

	public TextItem getRollTextItem() {
		return rollTextItem;
	}

	public Transform getRollTextTransform() {
		return rollTextTransform;
	}

	public TextItem getAltTextItem() {
		return altTextItem;
	}

	public Transform getAltTextTransform() {
		return altTextTransform;
	}

	public TransformBuilder getTransformBuilder() {
		return transformBuilder;
	}

	public double getRadiusOfHeadingCylinder() {
		return radiusOfHeadingCylinder;
	}

	public float getRadiusOfPitchCylinder() {
		return radiusOfPitchCylinder;
	}

	public Transform getCylindricalPitchTransform() {
		return cylindricalPitchTransform;
	}

	public double getPitchAngleStepSize() {
		return pitchAngleStepSize;
	}
}
