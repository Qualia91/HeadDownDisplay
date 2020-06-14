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

public class LinearReadout {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final Transform arrowTransform;

	private final double heightDiff = 0.25 + 0.24;
	private final TextItem textItem;

	public LinearReadout(SceneGraphNode parent) {

		MeshObject whiteMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.setScale(new Vec3f(0.01f, 0.01f, 0.5f))
						.build())
				.build();

		MeshObject redMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/red.png")
				.setTransform(transformBuilder
						.setScale(new Vec3f(0.01f, 0.01f, 0.1f))
						.build())
				.build();

		// thrust indicator
		MeshObject arrowMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("D:\\Software\\Programming\\projects\\Java\\GraphicsLibrary\\src\\main\\resources\\models\\arrow.obj")
				.setTexture("/textures/gunMetalTexture.jpg")
				.setNormalTexture("/textures/gunMetalNormal.jpg")
				.setTransform(
						transformBuilder
								.reset()
								.setScale(0.025f)
								.setRotation(QuaternionF.RotationZ(Math.PI/2))
								.build()
				)
				.build();

		Transform indicatorTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(1, 0.95f, 0.5f))
				.build();

		TransformSceneGraph indicatorTransformGraph = new TransformSceneGraph(parent, indicatorTransform);

		MeshSceneGraph meshGameObject = new MeshSceneGraph(
				indicatorTransformGraph,
				whiteMarkers
		);

		Transform redIndicatorTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(0, 0, 0.3f))
				.build();

		TransformSceneGraph redIndicatorTransformGraph = new TransformSceneGraph(indicatorTransformGraph, redIndicatorTransform);

		MeshSceneGraph redMeshGameObject = new MeshSceneGraph(
				redIndicatorTransformGraph,
				redMarkers
		);

		this.arrowTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(0, -0.05f, -0.24f))
				.build();

		TransformSceneGraph arrowTransformGraph = new TransformSceneGraph(indicatorTransformGraph, arrowTransform);

		MeshSceneGraph arrowGameObject = new MeshSceneGraph(
				arrowTransformGraph,
				arrowMesh
		);

		// throttle text
		this.textItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.build();
		Transform textTransform = transformBuilder
				.reset()
				.setRotation(QuaternionF.RotationX(Math.PI/2))
				.setPosition(new Vec3f(0, -0.05f, -textItem.getHeight()/2))
				.build();
		TransformSceneGraph transformSceneGraphThrottle = new TransformSceneGraph(arrowTransformGraph, textTransform);
		MeshSceneGraph textMeshObjectThrottle = new MeshSceneGraph(transformSceneGraphThrottle, textItem);

	}

	public TextItem getTextItem() {
		return textItem;
	}

	public void setPercent(float percent) {
		Vec3f position = this.arrowTransform.getPosition();
		this.arrowTransform.setPosition(new Vec3f(position.getX(), position.getY(), -0.24f + (float) (percent * heightDiff)));
	}
}
