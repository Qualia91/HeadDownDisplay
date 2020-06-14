package com.nick.wood.hdd.altimeter;

import com.nick.wood.graphics_library.lighting.PointLight;
import com.nick.wood.graphics_library.objects.Camera;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.scene_graph_objects.*;
import com.nick.wood.graphics_library.utils.Creation;
import com.nick.wood.hdd.Main;
import com.nick.wood.hdd.gui_components.CylindricalReadout;
import com.nick.wood.hdd.gui_components.LinearReadout;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class AltimeterSceneView {

	private final Transform fobCameraTransform;
	private final TextItem rollTextItem;
	private final Transform rollTextTransform;
	private final TextItem altTextItem;
	private final Transform altTextTransform;
	private final Transform skyboxTransform;
	private final LinearReadout throttleReadout;
	private TransformBuilder transformBuilder = new TransformBuilder();


	private final CylindricalReadout pitchReadout;
	private final CylindricalReadout headingReadout;

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
				.setTexture("/textures/gunMetalTexture.jpg")
				.setNormalTexture("/textures/gunMetalNormal.jpg")
				.setTransform(transformBuilder
						.setRotation(QuaternionF.RotationZ(Math.PI/2.0))
						.setScale(new Vec3f(0.05f, 0.5f, 0.05f)).build()).build();


		// Level marker
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(1, 0, 0)), fboCameraTransformGameObject, levelBlackMarkers);
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(0, 0, 0)), fboCameraTransformGameObject, levelBlackMarkers);
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(-1, 0, 0)), fboCameraTransformGameObject, levelBlackMarkers);

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

		// alt text
		this.altTextItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.build();
		Transform persistentAltTextTransform = transformBuilder
				.reset()
				.setRotation(QuaternionF.RotationX(Math.PI/2))
				.setPosition(new Vec3f(1, 1, 0.05f))
				.build();
		this.altTextTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(0, 0, 0))
				.build();
		TransformSceneGraph textTransformSceneGraphPersistentAlt = new TransformSceneGraph(fboViewTransformGraph, persistentAltTextTransform);
		TransformSceneGraph textTransformSceneGraphAlt = new TransformSceneGraph(textTransformSceneGraphPersistentAlt, altTextTransform);
		MeshSceneGraph textMeshObjectAlt = new MeshSceneGraph(textTransformSceneGraphAlt, altTextItem);

		// roll text
		this.rollTextItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.build();
		Transform persistentRollTextTransform = transformBuilder
				.reset()
				.setRotation(QuaternionF.RotationX(Math.PI/2))
				.setPosition(new Vec3f(0.8f, 0, 0.525f))
				.build();
		this.rollTextTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(0, 0, 0))
				.build();
		TransformSceneGraph textTransformSceneGraphPersistentRoll = new TransformSceneGraph(fboViewTransformGraph, persistentRollTextTransform);
		TransformSceneGraph textTransformSceneGraphRoll = new TransformSceneGraph(textTransformSceneGraphPersistentRoll, rollTextTransform);
		MeshSceneGraph textMeshObjectRoll = new MeshSceneGraph(textTransformSceneGraphRoll, rollTextItem);

		// curve
		MeshObject curveMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("D:\\Software\\Programming\\projects\\Java\\GraphicsLibrary\\src\\main\\resources\\models\\curve.obj")
				.setTexture("/textures/gunMetalTexture.jpg")
				.setNormalTexture("/textures/gunMetalNormal.jpg")
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

		this.pitchReadout = new CylindricalReadout(
				36,
				Vec3f.ZERO,
				fboViewTransformGraph,
				2.5,
				whiteMarkers,
				QuaternionF.RotationX(Math.PI/2),
				true,
				(angle) -> String.valueOf((int)(Math.round( Math.toDegrees(angle) / 10.0) * 10)),
				(angle, textItem) -> transformBuilder
						.setPosition(new Vec3f(0, 0, -textItem.getWidth()))
						.resetRotation()
						.setScale(Vec3f.ONE.scale(2))
						.build());

		this.headingReadout = new CylindricalReadout(
				36,
				new Vec3f(0, 0, -1.2f),
				fboViewTransformGraph,
				2,
				whiteMarkers,
				QuaternionF.Identity,
				true,
				(angle) -> String.valueOf((int)(Math.round( Math.toDegrees(-angle) / 10.0))),
				(angle, textItem) -> {
						return new TransformBuilder()
						.setPosition(new Vec3f(0, textItem.getWidth(), 0.15f))
						.setRotation(QuaternionF.RotationX(Math.PI/2))
						.setScale(Vec3f.ONE.scale(2))
						.build();
				});

		this.throttleReadout = new LinearReadout(fboViewTransformGraph);

	}

	public LinearReadout getThrottleReadout() {
		return throttleReadout;
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

	public Transform getFobCameraTransform() {
		return fobCameraTransform;
	}

	public Transform getSkyboxTransform() {
		return skyboxTransform;
	}

	public CylindricalReadout getPitchReadout() {
		return pitchReadout;
	}

	public CylindricalReadout getHeadingReadout() {
		return headingReadout;
	}
}
