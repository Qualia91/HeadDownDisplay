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
import com.nick.wood.hdd.gui_components.*;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class AltimeterSceneView {

	private final Transform fobCameraTransform;
	private final Transform skyboxTransform;
	private final LinearReadout throttleReadout;
	private final RollReadout rollReadout;
	private final AltitudeReadout altitudeReadout;
	private final ChangeIndicator pitchChangeIndicator;
	private final ChangeIndicator yawChangeIndicator;
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
						.setScale(new Vec3f(0.03f, 0.5f, 0.03f)).build()).build();

		MeshObject whiteMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.resetPosition()
						.setScale(new Vec3f(0.01f, 0.01f, 0.25f))
						.build()).build();


		// Level marker
		double angleToRotate = Math.atan2(1, 1);
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(1, 0, 0)), QuaternionF.RotationY(-angleToRotate), fboCameraTransformGameObject, levelBlackMarkers);
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(0, 0, 0)), fboCameraTransformGameObject, levelBlackMarkers);
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(-1, 0, 0)), QuaternionF.RotationY(angleToRotate), fboCameraTransformGameObject, levelBlackMarkers);
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(0, -0.8f, 0)), QuaternionF.RotationZ(Math.PI/2).multiply(QuaternionF.RotationY(angleToRotate)), fboCameraTransformGameObject, levelBlackMarkers);

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

		this.altitudeReadout = new AltitudeReadout(fboViewTransformGraph);

		this.pitchChangeIndicator = new ChangeIndicator(fboViewTransformGraph, new Vec3f(0.5f, 0.15f, 0), QuaternionF.RotationZ(Math.atan2(0.15, 0.5)));

		this.yawChangeIndicator = new ChangeIndicator(fboViewTransformGraph, new Vec3f(0.5f, 0, -0.35f), QuaternionF.RotationY(Math.atan2(0.35, 0.5)).multiply(QuaternionF.RotationX(Math.PI/2)));

		this.rollReadout = new RollReadout(fboViewTransformGraph);

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
				(angle, textItem) -> new TransformBuilder()
				.setPosition(new Vec3f(0, textItem.getWidth(), 0.15f))
				.setRotation(QuaternionF.RotationX(Math.PI/2))
				.setScale(Vec3f.ONE.scale(2))
				.build());

		this.throttleReadout = new LinearReadout(fboViewTransformGraph);

	}

	public LinearReadout getThrottleReadout() {
		return throttleReadout;
	}

	public RollReadout getRollReadout() {
		return rollReadout;
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

	public AltitudeReadout getAltitudeReadout() {
		return altitudeReadout;
	}

	public ChangeIndicator getPitchChangeIndicator() {
		return pitchChangeIndicator;
	}

	public ChangeIndicator getYawChangeIndicator() {
		return yawChangeIndicator;
	}
}
