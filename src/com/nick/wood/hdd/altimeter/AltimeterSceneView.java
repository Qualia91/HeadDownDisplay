package com.nick.wood.hdd.altimeter;

import com.nick.wood.graphics_library.lighting.PointLight;
import com.nick.wood.graphics_library.objects.Camera;
import com.nick.wood.graphics_library.objects.game_objects.*;
import com.nick.wood.graphics_library.objects.mesh_objects.Mesh;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.utils.Creation;
import com.nick.wood.hdd.gui_components.*;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

import static com.nick.wood.hdd.HDD.CAMERA_ROTATION;

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

	public AltimeterSceneView(GameObject fboViewTransformGraph, ModelManager modelManager) {

		TransformBuilder transformBuilder = new TransformBuilder();

		// camera for viewing thing rendered to fbo
		Camera fboCamera = new Camera(CameraType.FBO_CAMERA, 1024, 1024, 1.5708f, 0.01f, 100f, 1);
		Transform persistentFobCameraTransform = transformBuilder
				.setPosition(new Vec3f(0, 0, 0))
				.setScale(Vec3f.ONE)
				.setRotation(CAMERA_ROTATION)
				.build();
		TransformObject persistentFboCameraTransformGameObject = new TransformObject(fboViewTransformGraph, persistentFobCameraTransform);
		this.fobCameraTransform = transformBuilder
				.resetRotation()
				.build();
		TransformObject fboCameraTransformGameObject = new TransformObject(persistentFboCameraTransformGameObject, fobCameraTransform);
		this.fboCameraObject = new CameraObject(fboCameraTransformGameObject, fboCamera);

		// Level marker
		double angleToRotate = Math.atan2(1, 1);
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(1, 0, 0)), QuaternionF.RotationY(-angleToRotate), fboCameraTransformGameObject, modelManager.getModel("PIN"));
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(0, 0, 0)), fboCameraTransformGameObject, modelManager.getModel("AIM_MARKER"));
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(-1, 0, 0)), QuaternionF.RotationY(angleToRotate), fboCameraTransformGameObject, modelManager.getModel("PIN"));
		Creation.CreateObject(Vec3f.Z.scale(-1).add(new Vec3f(0, -1f, 0)), QuaternionF.RotationZ(Math.PI/2).multiply(QuaternionF.RotationY(angleToRotate)), fboCameraTransformGameObject, modelManager.getModel("PIN"));

		// skybox
		this.skyboxTransform = transformBuilder
				.setScale(Vec3f.ONE)
				.setPosition(new Vec3f(0, 0, 0))
				.setRotation(QuaternionF.Identity)
				.build();
		TransformObject sphereTransformObject = new TransformObject(fboViewTransformGraph, skyboxTransform);
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
		MeshGameObject sphereMeshGameObject = new MeshGameObject(sphereTransformObject, sphere);

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
				modelManager.getModel("WHITE_MARKER"),
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
				modelManager.getModel("WHITE_MARKER"),
				QuaternionF.Identity,
				true,
				(angle) -> String.valueOf((int)(Math.round( Math.toDegrees(-angle) / 10.0) * 10)),
				(angle, textItem) -> new TransformBuilder()
						.setPosition(new Vec3f(0, -textItem.getWidth()/4, 0))
						.resetRotation()
						.resetScale()
				.build());

		this.throttleReadout = new LinearReadout(fboViewTransformGraph, new Vec3f(1, 0.95f, 0.5f), modelManager);

		this.speedReadout = new LinearInfiniteReadout(fboViewTransformGraph, new Vec3f(1, -0.95f, -0.3f), 10, modelManager);
		this.altitudeReadout = new LinearInfiniteReadout(fboViewTransformGraph, new Vec3f(1, -0.95f, 0.5f), 100, modelManager);

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
