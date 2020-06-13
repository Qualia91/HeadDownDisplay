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
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class AltimeterSceneView {

	private final Transform fobCameraTransform;
	private float radiusOfPitchCylinder = 5;

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

/*
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
						.resetRotation()

						.build()).build();

		// Level marker
		Creation.CreateObject(Vec3f.Z.scale(-radiusOfPitchCylinder + 1).add(new Vec3f(1, 0, 0)), fboCameraTransformGameObject, levelBlackMarkers);
		Creation.CreateObject(Vec3f.Z.scale(-radiusOfPitchCylinder + 1).add(new Vec3f(0, 0, 0)), fboCameraTransformGameObject, levelBlackMarkers);
		Creation.CreateObject(Vec3f.Z.scale(-radiusOfPitchCylinder + 1).add(new Vec3f(-1, 0, 0)), fboCameraTransformGameObject, levelBlackMarkers);


		// pitch cylinder
		this.cylindricalPitchTransform = transformBuilder.reset().build();
		TransformSceneGraph cylindricalAltitudeTransformObject = new TransformSceneGraph(fboViewTransformGraph, cylindricalPitchTransform);

		// create boxes all around at regular angular intervals
		int pitchIntervals = 36;
		this.pitchAngleStepSize = (2 * Math.PI / pitchIntervals);
		for (int i = 0; i < pitchIntervals; i++) {
			double angleRad = i * pitchAngleStepSize;

			float posX = (float) (Math.sin(angleRad) * radiusOfPitchCylinder);
			float posZ = (float) (Math.cos(angleRad) * radiusOfPitchCylinder);

			MeshSceneGraph meshSceneGraph = Creation.CreateObjectAndGetSceneObject(new Vec3f(posX, 0, posZ),cylindricalAltitudeTransformObject, whiteMarkers);

			// text
			TextItem pitchText = (TextItem) new MeshBuilder()
					.setMeshType(MeshType.TEXT)
					.setText(String.valueOf((int) Math.toDegrees(angleRad)))
					.build();
			Transform headingNumberText = transformBuilder
					.setPosition(new Vec3f(0, pitchText.getWidth(), 0))
					.setRotation(QuaternionF.RotationX(Math.PI/2.0))
					.setScale(Vec3f.ONE.scale(2))
					.build();
			TransformSceneGraph headingNumberTextSceneGraph = new TransformSceneGraph(meshSceneGraph, headingNumberText);
			MeshSceneGraph headingTextMeshObject = new MeshSceneGraph(headingNumberTextSceneGraph, pitchText);
		}

*/
		// skybox
		Transform sphereTransform = transformBuilder
				.setScale(Vec3f.ONE)
				.setPosition(new Vec3f(0, 0, 0))
				.setRotation(QuaternionF.Identity)
				.build();
		TransformSceneGraph sphereTransformSceneGraph = new TransformSceneGraph(fboViewTransformGraph, sphereTransform);
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
				.setRotation(QuaternionF.Identity).build());
	}

	public Transform getFobCameraTransform() {
		return fobCameraTransform;
	}

}
