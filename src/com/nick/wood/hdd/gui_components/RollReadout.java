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

public class RollReadout {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final TextItem rollTextItem;
	private final TransformObject rollTextTransformGraph;
	private final TransformObject circleTransformObject;

	public RollReadout(GameObject parent, Vec3f position) {

		Transform rollReadoutTransform = transformBuilder
				.reset()
				.setPosition(position)
				.build();
		TransformObject rollTransformGraph = new TransformObject(parent, rollReadoutTransform);

		// roll text
		this.rollTextItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/fonts/verandaGreenBold.png")
				.build();
		Transform rollTextTransformPers = transformBuilder
				.reset()
				.setPosition(new Vec3f(-0.005f, -0.075f, 0.15f))
				.build();
		TransformObject rollTextTransformGraphPers = new TransformObject(rollTransformGraph, rollTextTransformPers);
		Transform rollTextTransform = transformBuilder
				.reset()
				.build();
		this.rollTextTransformGraph = new TransformObject(rollTextTransformGraphPers, rollTextTransform);
		MeshGameObject textMeshGameObjectRoll = new MeshGameObject(rollTextTransformGraph, rollTextItem);

		// curve
		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject curveMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("\\models\\curve.obj")
				.setTexture("/textures/gunMetalTexture.jpg")
				.setNormalTexture("/normalMaps/gunMetalNormal.jpg")
				.setTransform(
						transformBuilder
								.reset()
								.setScale(0.2f)
								.setRotation(QuaternionF.RotationX(Math.PI/2))
								.build()
				)
				.build();

		TransformObject curveMeshTransformObject = new TransformObject(rollTransformGraph,
				transformBuilder
						.reset()
						.build());

		MeshGameObject curveMeshGameObject = new MeshGameObject(curveMeshTransformObject, curveMesh);

		// requested roll arrow
		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject arrowMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("\\models\\arrow.obj")
				.setTexture("/textures/black.png")
				.setTransform(
						transformBuilder
								.reset()
								.setScale(0.025f)
								.build()
				)
				.build();

		this.circleTransformObject = new TransformObject(curveMeshTransformObject,
				transformBuilder
						.reset().build());

		TransformObject arrowMeshTransformObject = new TransformObject(circleTransformObject,
				transformBuilder
						.reset()
						.setPosition(new Vec3f(0, 0, 0.1f))
						.setRotation(
								QuaternionF.RotationZ(Math.PI/2)
								.multiply(QuaternionF.RotationY(-Math.PI/2))
						).build());

		MeshGameObject arrowMeshGameObject = new MeshGameObject(arrowMeshTransformObject, arrowMesh);

	}

	public TransformBuilder getTransformBuilder() {
		return transformBuilder;
	}

	public TextItem getRollTextItem() {
		return rollTextItem;
	}

	public void setRoll(float roll) {
		rollTextItem.changeText(String.valueOf((int) Math.toDegrees(roll % (2 * Math.PI))));
		rollTextTransformGraph.setPosition(new Vec3f(0, -rollTextItem.getWidth()/2.0f, 0));
	}

	public void setRollStick(double rollStick) {
		circleTransformObject.setRotation(
				QuaternionF.RotationX(rollStick)
		);
	}
}
