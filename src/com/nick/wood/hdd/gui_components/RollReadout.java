package com.nick.wood.hdd.gui_components;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.SceneGraphNode;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class RollReadout {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final TextItem rollTextItem;
	private final TransformSceneGraph rollTextTransformGraph;
	private final TransformSceneGraph circleTransformSceneGraph;

	public RollReadout(SceneGraphNode parent) {
		// roll text
		this.rollTextItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/MontserratLightGreenBold.png")
				.build();
		Transform rollTextTransformPers = transformBuilder
				.reset()
				.setRotation(QuaternionF.RotationX(Math.PI/2))
				.setPosition(new Vec3f(0.8f, 0, 0.525f))
				.setScale(new Vec3f(1, 1, 0.6f))
				.build();
		TransformSceneGraph rollTextTransformGraphPers = new TransformSceneGraph(parent, rollTextTransformPers);
		Transform rollTextTransform = transformBuilder
				.reset()
				.build();
		this.rollTextTransformGraph = new TransformSceneGraph(rollTextTransformGraphPers, rollTextTransform);
		MeshSceneGraph textMeshObjectRoll = new MeshSceneGraph(rollTextTransformGraph, rollTextItem);

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

		TransformSceneGraph curveMeshTransformSceneGraph = new TransformSceneGraph(parent,
				transformBuilder
						.reset()
						.setPosition(new Vec3f(1, 0, 0.5f)).build());

		MeshSceneGraph curveMeshSceneGraph = new MeshSceneGraph(curveMeshTransformSceneGraph, curveMesh);

		// requested roll arrow
		MeshObject arrowMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("D:\\Software\\Programming\\projects\\Java\\GraphicsLibrary\\src\\main\\resources\\models\\arrow.obj")
				.setTexture("/textures/black.png")
				.setTransform(
						transformBuilder
								.reset()
								.setScale(0.025f)
								.build()
				)
				.build();

		this.circleTransformSceneGraph = new TransformSceneGraph(curveMeshTransformSceneGraph,
				transformBuilder
						.reset().build());

		TransformSceneGraph arrowMeshTransformSceneGraph = new TransformSceneGraph(circleTransformSceneGraph,
				transformBuilder
						.reset()
						.setPosition(new Vec3f(0, 0, 0.1f))
						.setRotation(
								QuaternionF.RotationZ(Math.PI/2)
								.multiply(QuaternionF.RotationY(-Math.PI/2))
						).build());

		MeshSceneGraph arrowMeshSceneGraph = new MeshSceneGraph(arrowMeshTransformSceneGraph, arrowMesh);

	}

	public TransformBuilder getTransformBuilder() {
		return transformBuilder;
	}

	public TextItem getRollTextItem() {
		return rollTextItem;
	}

	public void setRoll(float roll) {
		rollTextItem.changeText(String.valueOf((int) Math.toDegrees(roll % (2 * Math.PI))));
		rollTextTransformGraph.setPosition(new Vec3f(0, 0, -rollTextItem.getWidth()/2.0f));
	}

	public void setRollStick(double rollStick) {
		circleTransformSceneGraph.setRotation(
				QuaternionF.RotationX(rollStick)
		);
	}
}
