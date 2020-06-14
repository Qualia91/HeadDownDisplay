package com.nick.wood.hdd.altimeter;

import com.nick.wood.graphics_library.input.DirectTransformController;
import com.nick.wood.graphics_library.lighting.PointLight;
import com.nick.wood.graphics_library.objects.Camera;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.scene_graph_objects.*;
import com.nick.wood.graphics_library.utils.Creation;
import com.nick.wood.hdd.Main;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

import java.util.function.BiFunction;
import java.util.function.Function;

public class AltimeterSceneView {

	private final Transform fobCameraTransform;
	private final TextItem rollTextItem;
	private final Transform rollTextTransform;
	private final double headingAngleStepSize;
	private final TextItem altTextItem;
	private final Transform altTextTransform;
	private final Transform cylindricalPitchTransform;
	private final Transform cylindricalHeadingTransform;
	private final float radiusOfHeadingCylinder = 2f;
	private final float radiusOfPitchCylinder = 2.5f;
	private final Transform skyboxTransform;
	private double pitchAngleStepSize;
	private TransformBuilder transformBuilder = new TransformBuilder();

	public AltimeterSceneView(SceneGraphNode fboViewTransformGraph) {

		TransformBuilder transformBuilder = new TransformBuilder();

		// camera for viewing thing rendered to fbo
		Camera fboCamera = new Camera(CameraType.FBO_CAMERA, 1024, 1024, 1.5708f, 0.01f, 100f);
		Transform persistentFobCameraTransform = transformBuilder
				.setPosition(new Vec3f(0, 0, 0))
				.setScale(Vec3f.ONE)
				.setRotation(Main.CAMERA_ROTATION)
				.build();
		TransformSceneGraph persistentFboCameraTransformGameObject = new TransformSceneGraph(fboViewTransformGraph, persistentFobCameraTransform);
		this.fobCameraTransform = transformBuilder
				.resetRotation()
				.build();
		TransformSceneGraph fboCameraTransformGameObject = new TransformSceneGraph(persistentFboCameraTransformGameObject, fobCameraTransform);
		CameraSceneGraph fboCameraGameObject = new CameraSceneGraph(fboCameraTransformGameObject, fboCamera, CameraType.FBO_CAMERA);


		MeshObject levelBlackMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/black.png")
				.setTransform(transformBuilder
						.setRotation(QuaternionF.RotationZ(Math.PI/2.0))
						.setScale(new Vec3f(0.05f, 0.5f, 0.05f)).build()).build();


		// Level marker
		Creation.CreateObject(Vec3f.Z.scale(-radiusOfPitchCylinder - 1).add(new Vec3f(1, 0, 0)), fboCameraTransformGameObject, levelBlackMarkers);
		Creation.CreateObject(Vec3f.Z.scale(-radiusOfPitchCylinder - 1).add(new Vec3f(0, 0, 0)), fboCameraTransformGameObject, levelBlackMarkers);
		Creation.CreateObject(Vec3f.Z.scale(-radiusOfPitchCylinder - 1).add(new Vec3f(-1, 0, 0)), fboCameraTransformGameObject, levelBlackMarkers);

		// skybox
		this.skyboxTransform = transformBuilder
				.setScale(Vec3f.ONE)
				.setPosition(new Vec3f(0, 0, 0))
				.setRotation(QuaternionF.Identity)
				.build();
		TransformSceneGraph sphereTransformSceneGraph = new TransformSceneGraph(fboViewTransformGraph, skyboxTransform);
		MeshObject sphere = new MeshBuilder()
				.setMeshType(MeshType.SPHERE)
				.setTexture("/textures/altimeterSphere.png")
				.setTriangleNumber(10)
				.setInvertedNormals(true)
				.setTransform(transformBuilder
						.setPosition(Vec3f.ZERO)
						.setRotation(QuaternionF.RotationY(Math.PI))
						.setScale(Vec3f.ONE.scale(100)).build())
				.build();
		MeshSceneGraph sphereMeshObject = new MeshSceneGraph(sphereTransformSceneGraph, sphere);

		PointLight pointLight = new PointLight(
				Vec3f.ONE,
				1
		);
		Creation.CreateLight(pointLight, fboCameraTransformGameObject, transformBuilder
				.setPosition(Vec3f.ZERO)
				.setRotation(QuaternionF.Identity).build());// roll text
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
		TransformSceneGraph textTransformSceneGraphPersistent = new TransformSceneGraph(fboViewTransformGraph, persistentRollTextTransform);
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
		TransformSceneGraph textTransformSceneGraphPersistentAlt = new TransformSceneGraph(fboViewTransformGraph, persistentAltTextTransform);
		TransformSceneGraph textTransformSceneGraphAlt = new TransformSceneGraph(textTransformSceneGraphPersistentAlt, altTextTransform);
		MeshSceneGraph textMeshObjectAlt = new MeshSceneGraph(textTransformSceneGraphAlt, altTextItem);

		// arrow
		MeshObject arrowMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("D:\\Software\\Programming\\projects\\Java\\GraphicsLibrary\\src\\main\\resources\\models\\arrow.obj")
				.setTransform(
						transformBuilder
								.setScale(Vec3f.ONE.scale(0.1f))
								.setRotation(QuaternionF.RotationZ((float) -Math.PI / 2))
								.build()
				)
				.build();

		// arrow
		MeshObject curveMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("D:\\Software\\Programming\\projects\\Java\\GraphicsLibrary\\src\\main\\resources\\models\\curve.obj")
				.setTexture("/textures/black.png")
				.setTransform(
						transformBuilder
								.reset()
								.setScale(0.2f)
								.setRotation(QuaternionF.RotationX(Math.PI/2))
								.build()
				)
				.build();

		TransformSceneGraph curveMeshTransformSceneGraph = new TransformSceneGraph(fboViewTransformGraph,
				transformBuilder
						.reset()
				.setPosition(new Vec3f(1, 0, 0.5f)).build());

		MeshSceneGraph curveMeshSceneGraph = new MeshSceneGraph(curveMeshTransformSceneGraph, curveMesh);


		MeshObject whiteMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.resetPosition()
						.setScale(new Vec3f(0.01f, 0.01f, 0.25f))
						.build()).build();

		int pitchIntervals = 36;
		this.pitchAngleStepSize = (2 * Math.PI / pitchIntervals);
		this.cylindricalPitchTransform = createDataCylinder(
				Vec3f.ZERO,
				fboViewTransformGraph,
				pitchAngleStepSize,
				radiusOfPitchCylinder,
				whiteMarkers,
				QuaternionF.RotationX(Math.PI/2),
				true,
				-pitchIntervals/2,
				pitchIntervals/2,
				(val) -> String.valueOf((int)(Math.round( Math.toDegrees(val) / 10.0) * 10)),
				(angle, textItem) -> transformBuilder
						.setPosition(new Vec3f(0, 0, -textItem.getWidth()))
						.resetRotation()
						.setScale(Vec3f.ONE.scale(2))
						.build());

		int headingIntervals = 36;
		this.headingAngleStepSize = (2 * Math.PI / headingIntervals);
		this.cylindricalHeadingTransform = createDataCylinder(
				new Vec3f(0, 0, -1),
				fboViewTransformGraph,
				headingAngleStepSize,
				radiusOfHeadingCylinder,
				whiteMarkers,
				QuaternionF.Identity,
				true,
				-headingIntervals/2,
				headingIntervals/2,
				(angle) -> String.valueOf((int)(Math.round( Math.toDegrees(-angle) / 10.0))),
				(angle, textItem) -> {
						return new TransformBuilder()
						.setPosition(new Vec3f(0, textItem.getWidth(), 0.15f))
						.setRotation(QuaternionF.RotationX(Math.PI/2))
						.setScale(Vec3f.ONE.scale(2))
						.build();
				});


		// thrust indicator

		Transform thrustIndicatorTransform = transformBuilder
				.reset()
				.setPosition(Vec3f.Y.scale(4))
				.setRotation(QuaternionF.RotationZ(Math.PI/2))
				.setScale(Vec3f.ONE.scale(2))
				.build();

		TransformSceneGraph thrustIndicatorTransformGraph = new TransformSceneGraph(fboViewTransformGraph, thrustIndicatorTransform);

		MeshSceneGraph meshGameObject = new MeshSceneGraph(
				thrustIndicatorTransformGraph,
				whiteMarkers
		);
	}

	private Transform createDataCylinder(Vec3f origin,
	                                     SceneGraphNode parent,
	                                     double stepSize,
	                                     double radius,
	                                     MeshObject markerMesh,
	                                     QuaternionF cylinderPersistentRotation,
	                                     boolean hasText,
	                                     int from,
	                                     int to,
	                                     Function<Double, String> stringFunction,
	                                     BiFunction<Double, TextItem, Transform> textTransformFunction) {

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
		for (int i = from; i < to; i++) {
			double angleRad = i * stepSize;

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
						.setText(stringFunction.apply(angleRad))
						.build();
				Transform textTransform = textTransformFunction.apply(angleRad, text);

				TransformSceneGraph textSceneGraph = new TransformSceneGraph(meshSceneGraph, textTransform);
				MeshSceneGraph textMeshObject = new MeshSceneGraph(textSceneGraph, text);
			}
		}

		return cylindricalTransform;
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

	public Transform getFobCameraTransform() {
		return fobCameraTransform;
	}

	public Transform getSkyboxTransform() {
		return skyboxTransform;
	}
}
