package com.nick.wood.hdd.gui_components;

import com.nick.wood.graphics_library.objects.game_objects.GameObject;
import com.nick.wood.graphics_library.objects.game_objects.MeshGameObject;
import com.nick.wood.graphics_library.objects.game_objects.TransformObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class CircleGrid {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final int stepWidthsReal;
	private final TransformObject editableTransformGraph;

	// player is in the middle
	// width and height increments are either side
	// make a grid with dimensions of 2 in y and z plane, center at 0
	public CircleGrid(GameObject parent,
	                  Vec3f position,
	                  QuaternionF rotation,
	                  int widthIncrements,
	                  double widthMaxValue) {

		stepWidthsReal = (int) (widthMaxValue / widthIncrements);

		MeshObject circle = new MeshBuilder()
				.setMeshType(MeshType.CIRCLE)
				.setTriangleNumber(50)
				.setTexture("/textures/circleSADisplayTexture.png")
				.setTransform(transformBuilder
						.setScale(2.0f/widthIncrements)
						.build())
				.build();

		Transform gridTransform = transformBuilder
				.resetScale()
				.setPosition(position)
				.setRotation(rotation)
				.build();

		TransformObject gridTransformGraph = new TransformObject(parent, gridTransform);

		Transform editableGridTransform = transformBuilder
				.reset()
				.build();

		this.editableTransformGraph = new TransformObject(gridTransformGraph, editableGridTransform);

		float widthStepSize = 1.0f/widthIncrements;

		for (int i = -widthIncrements; i <= widthIncrements; i++) {

			if (i > 0) {
				Transform circleTransform = transformBuilder
						.reset()
						.setScale(i)
						.build();

				TransformObject circleTransformGraph = new TransformObject(editableTransformGraph, circleTransform);

				MeshGameObject circleMeshGameObj = new MeshGameObject(circleTransformGraph, circle);
			}

			TextItem textItem = (TextItem) new MeshBuilder()
					.setMeshType(MeshType.TEXT)
					.setText(String.valueOf(i * stepWidthsReal))
					.setFontFile("/fonts/verandaGreenBold.png")
					.build();

			Transform textTransform = transformBuilder
					.reset()
					.setPosition(new Vec3f(0,  1 - 0.02f, i * widthStepSize - textItem.getHeight()/2))
					.build();

			TransformObject textTransformGraph = new TransformObject(gridTransformGraph, textTransform);

			MeshGameObject textSceneGraph = new MeshGameObject(textTransformGraph, textItem);

		}


	}

	public TransformObject getEditableTransformGraph() {
		return editableTransformGraph;
	}
}
