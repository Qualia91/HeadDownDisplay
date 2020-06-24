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

public class LinearReadout {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final Transform arrowTransform;

	private final double heightDiff = 0.25 + 0.24;
	private final TextItem textItem;

	public LinearReadout(GameObject parent, Vec3f position) {

		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject whiteMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.build())
				.build();

		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject redMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/red.png")
				.setTransform(transformBuilder
						.build())
				.build();

		// thrust indicator
		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject arrowMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("\\models\\arrow.obj")
				.setTexture("/textures/gunMetalTexture.jpg")
				.setNormalTexture("/normalMaps/gunMetalNormal.jpg")
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
				.setPosition(position)
				.build();

		TransformObject indicatorTransformGraph = new TransformObject(parent, indicatorTransform);

		Transform whiteLineTransform = transformBuilder
				.reset()
				.setScale(new Vec3f(1, 1, 2))
				.build();

		TransformObject whiteLineTransformGraph = new TransformObject(indicatorTransformGraph, whiteLineTransform);

		MeshGameObject meshGameObject = new MeshGameObject(
				whiteLineTransformGraph,
				whiteMarkers
		);

		Transform redIndicatorTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(0, 0, 0.3f))
				.setScale(new Vec3f(0.01f, 0.01f, 0.1f))
				.build();

		TransformObject redIndicatorTransformGraph = new TransformObject(indicatorTransformGraph, redIndicatorTransform);

		MeshGameObject redMeshGameObject = new MeshGameObject(
				redIndicatorTransformGraph,
				redMarkers
		);

		this.arrowTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(0, -0.05f, -0.24f))
				.build();

		TransformObject arrowTransformGraph = new TransformObject(indicatorTransformGraph, arrowTransform);

		MeshGameObject arrowGameObject = new MeshGameObject(
				arrowTransformGraph,
				arrowMesh
		);

		// text
		this.textItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/fonts/verandaGreenBold.png")
				.build();
		Transform textTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(0, -0.25f, -textItem.getHeight()/2))
				.build();
		TransformObject transformObjectThrottle = new TransformObject(arrowTransformGraph, textTransform);
		MeshGameObject textMeshGameObjectThrottle = new MeshGameObject(transformObjectThrottle, textItem);

	}

	public TextItem getTextItem() {
		return textItem;
	}

	public void setPercent(float percent) {
		Vec3f position = this.arrowTransform.getPosition();
		this.arrowTransform.setPosition(new Vec3f(position.getX(), position.getY(), -0.24f + (float) (percent * heightDiff)));
	}
}
