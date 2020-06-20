package com.nick.wood.hdd.altimeter;

import com.nick.wood.graphics_library.lighting.PointLight;
import com.nick.wood.graphics_library.objects.Camera;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.game_objects.*;
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
	private final LinearInfiniteReadout altitudeReadout;
	private final ChangeIndicator pitchChangeIndicator;
	private final ChangeIndicator yawChangeIndicator;
	private final CameraObject fboCameraObject;
	private final LinearInfiniteReadout speedReadout;
	private TransformBuilder transformBuilder = new TransformBuilder();


	private final CylindricalReadout pitchReadout;
	private final CylindricalReadout headingReadout;

	public AltimeterSceneView(GameObject fboViewTransformGraph) {

		TransformBuilder transformBuilder = new TransformBuilder();

		// camera for viewing thing rendered to fbo
		Camera fboCamera = new Camera(CameraType.FBO_CAMERA, 1024, 1024, 1.5708f, 0.01f, 100f);
		Transform persistentFobCameraTransform = transformBuilder
				.setPosition(new Vec3f(0, 0, 0))
				.setScale(Vec3f.ONE)
				.setRotation(Main.CAMERA_ROTATION)
				.build();
		TransformObject persistentFboCameraTransformGameObject = new TransformObject(fboViewTransformGraph, persistentFobCameraTransform);
		this.fobCameraTransform = transformBuilder
				.resetRotation()
				.build();
		TransformObject fboCameraTransformGameObject = new TransformObject(persistentFboCameraTransformGameObject, fobCameraTransform);
		this.fboCameraObject = new CameraObject(fboCameraTransformGameObject, fboCamera);


		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject levelBlackMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/gunMetalTexture.jpg")
				.setNormalTexture("/textures/gunMetalNormal.jpg")
				.setTransform(transformBuilder
						.setRotation(QuaternionF.RotationZ(Math.PI/2.0))
						.setScale(new Vec3f(0.03f, 0.5f, 0.03f)).build()).build();

		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject whiteMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.resetPosition()
						.setScale(new Vec3f(0.01f, 0.01f, 0.2f))
						.build()).build();

		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject aimMarker = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("D:\\Software\\Programming\\projects\\Java\\GraphicsLibrary\\src\\main\\resources\\models\\aimMarker.obj")
				.setTexture("/textures/gunMetalTexture.jpg")
				.setNormalTexture("/textures/gunMetalNormal.jpg")
				.setTransform(transformBuilder
						.resetPosition()
						.setRotation(
								QuaternionF.RotationZ(Math.PI/2)
								.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.setScale(new Vec3f(0.1f, 0.1f, 0.1f))
						.build()).build();

		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject pinMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("D:\\Software\\Programming\\projects\\Java\\GraphicsLibrary\\src\\main\\resources\\models\\pin.obj")
				.setTexture("/textures/gunMetalTexture.jpg")
				.setNormalTexture("/textures/gunMetalNormal.jpg")
				.setTransform(transformBuilder
						.resetPosition()
						.setRotation(
								QuaternionF.RotationZ(Math.PI/2)
										.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.setScale(new Vec3f(0.2f, 0.2f, 0.2f))
						.build()).build();


		// Level marker
		double angleToRotate = Math.atan2(1, 1);
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(1, 0, 0)), QuaternionF.RotationY(-angleToRotate), fboCameraTransformGameObject, pinMesh);
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(0, 0, 0)), fboCameraTransformGameObject, aimMarker);
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(-1, 0, 0)), QuaternionF.RotationY(angleToRotate), fboCameraTransformGameObject, pinMesh);
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(0, -1f, 0)), QuaternionF.RotationZ(Math.PI/2).multiply(QuaternionF.RotationY(angleToRotate)), fboCameraTransformGameObject, pinMesh);

		// skybox
		this.skyboxTransform = transformBuilder
				.setScale(Vec3f.ONE)
				.setPosition(new Vec3f(0, 0, 0))
				.setRotation(QuaternionF.Identity)
				.build();
		TransformObject sphereTransformObject = new TransformObject(fboViewTransformGraph, skyboxTransform);
		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject sphere = new MeshBuilder()
				.setMeshType(MeshType.SPHERE)
				.setTexture("/textures/altimeterSphere.png")
				.setTriangleNumber(10)
				.setInvertedNormals(true)
				.setTransform(transformBuilder
						.setPosition(Vec3f.ZERO)
						.setRotation(QuaternionF.RotationY(Math.PI))
						.setScale(Vec3f.ONE.scale(100)).build())
				.build();
		MeshObject sphereMeshObject = new MeshObject(sphereTransformObject, sphere);

		PointLight pointLight = new PointLight(
				Vec3f.ONE,
				1
		);
		Creation.CreateLight(pointLight, fboCameraTransformGameObject, transformBuilder
				.setPosition(Vec3f.ZERO)
				.setRotation(QuaternionF.Identity).build());// roll text

		this.pitchChangeIndicator = new ChangeIndicator(fboViewTransformGraph, new Vec3f(0.5f, 0.15f, 0), QuaternionF.RotationZ(Math.atan2(0.15, 0.5)));

		this.yawChangeIndicator = new ChangeIndicator(fboViewTransformGraph, new Vec3f(0.5f, 0, -0.35f), QuaternionF.RotationY(Math.atan2(0.35, 0.5)).multiply(QuaternionF.RotationX(Math.PI/2)));

		this.rollReadout = new RollReadout(fboViewTransformGraph, new Vec3f(1, 0, 0.7f));

		this.pitchReadout = new CylindricalReadout(
				36,
				Vec3f.ZERO,
				fboViewTransformGraph,
				2.5,
				whiteMarkers,
				QuaternionF.RotationX(-Math.PI/2),
				true,
				(angle) -> String.valueOf((int)(Math.round( Math.toDegrees(-angle) / 10.0) * 10)),
				(angle, textItem) -> transformBuilder
						.setScale(2)
						.setPosition(new Vec3f(0, 0, -0.2f-textItem.getWidth()/2))
						.setRotation(QuaternionF.RotationX(Math.PI/2))
						.build());

		this.headingReadout = new CylindricalReadout(
				36,
				new Vec3f(0, 0, -1.2f),
				fboViewTransformGraph,
				2,
				whiteMarkers,
				QuaternionF.Identity,
				true,
				(angle) -> String.valueOf((int)(Math.round( Math.toDegrees(-angle) / 10.0) * 10)),
				(angle, textItem) -> new TransformBuilder()
						.setPosition(new Vec3f(0, -textItem.getWidth()/4, 0))
						.resetRotation()
						.resetScale()
				.build());

		this.throttleReadout = new LinearReadout(fboViewTransformGraph, new Vec3f(1, 0.95f, 0.5f));

		this.speedReadout = new LinearInfiniteReadout(fboViewTransformGraph, new Vec3f(1, -0.95f, -0.3f), 10);
		this.altitudeReadout = new LinearInfiniteReadout(fboViewTransformGraph, new Vec3f(1, -0.95f, 0.5f), 100);

	}

	public LinearInfiniteReadout getSpeedReadout() {
		return speedReadout;
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

	public LinearInfiniteReadout getAltitudeReadout() {
		return altitudeReadout;
	}

	public ChangeIndicator getPitchChangeIndicator() {
		return pitchChangeIndicator;
	}

	public ChangeIndicator getYawChangeIndicator() {
		return yawChangeIndicator;
	}

	public CameraObject getFboCameraObject() {
		return fboCameraObject;
	}
}
