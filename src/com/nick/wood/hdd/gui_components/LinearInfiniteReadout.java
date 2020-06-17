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

import java.util.ArrayList;

public class LinearInfiniteReadout {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final Transform arrowTransform;

	private final ArrayList<Transform> stepTransforms = new ArrayList<>();

	private final double heightDiff = 0.25 + 0.24;
	private final TextItem textItem;
	private final double stepValue;
	private final double lastValue = 0;
	private final float stepWidth;
	private final Transform textTransform;
	private final QuaternionF rotation;

	public LinearInfiniteReadout(SceneGraphNode parent, Vec3f position, QuaternionF rotation, double stepValue) {

		this.stepValue = stepValue;
		stepWidth = (float) (0.05 / stepValue);
		this.rotation = rotation;

		MeshObject whiteMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.setScale(new Vec3f(0.01f, 0.01f, 0.5f))
						.build())
				.build();

		// indicator
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
				.setPosition(position)
				.setRotation(rotation)
				.build();

		TransformSceneGraph indicatorTransformGraph = new TransformSceneGraph(parent, indicatorTransform);


		// create steps
		for (int i = -5; i < 6; i++) {

			Transform transform = transformBuilder
					.reset()
					.setPosition(new Vec3f(0, 0, i * 0.05f))
					.setRotation(QuaternionF.RotationX(Math.PI / 2))
					.setScale(new Vec3f(1, 1, 0.1f))
					.build();

			stepTransforms.add(transform);

			TransformSceneGraph stepTransformGraph = new TransformSceneGraph(indicatorTransformGraph, transform);

			MeshSceneGraph stepMeshGraph = new MeshSceneGraph(stepTransformGraph, whiteMarkers);

		}

		this.arrowTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(0, -0.1f, 0))
				.build();

		TransformSceneGraph arrowTransformGraph = new TransformSceneGraph(indicatorTransformGraph, arrowTransform);

		MeshSceneGraph arrowGameObject = new MeshSceneGraph(
				arrowTransformGraph,
				arrowMesh
		);

		// text
		this.textItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/MontserratLightGreenBold.png")
				.build();
		this.textTransform = transformBuilder
				.reset()
				.setRotation(
						rotation
						.multiply(QuaternionF.RotationX(Math.PI/2))
				)
				.build();
		TransformSceneGraph transformSceneGraphThrottle = new TransformSceneGraph(indicatorTransformGraph, textTransform);
		MeshSceneGraph textMeshObjectThrottle = new MeshSceneGraph(transformSceneGraphThrottle, textItem);

	}

	public TextItem getTextItem() {
		return textItem;
	}

	public void moveToValue(double newValue) {

		// calc step size modulated by step value
		double stepSize = (newValue - lastValue) % stepValue;

		// find the distance the bar needs to move (-ve because a +ve change will move the markers down)
		float stepMove = -(float) (stepSize * stepWidth);

		if (stepSize != 0) {

			// loop through and move them all
			for (int i = -5; i < 6; i++) {

				Transform transform = stepTransforms.get(i + 5);

				// if stepSize is negative, take the first marker and move it to the to top
				if (stepSize < 0 && i == -5) {
					transform.setPosition(new Vec3f(0, 0, 5 * 0.05f + stepMove));
				}
				// if stepSize is positive, take the last marker and move it to the to bottom
				else if (stepSize > 0 && i == 5) {
					transform.setPosition(new Vec3f(0, 0, -5 * 0.05f + stepMove));
				} else {
					transform.setPosition(new Vec3f(0, 0, i * 0.05f + stepMove));
				}
			}

		}

		// calculate movement inwards of text
		//QuaternionF quaternionF = rotation.rotateVector(new Vec3f(0, textItem.getWidth(), textItem.getHeight()));
		this.textTransform.setPosition(new Vec3f(0, -textItem.getWidth() - 0.1f, -textItem.getHeight()/2));
	}
}
