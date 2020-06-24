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

public class SquareGrid {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final int stepWidthsReal;
	private final int stepHeightsReal;

	// player is in the middle
	// width and height increments are either side
	// make a grid with dimensions of 2 in y and z plane, center at 0
	public SquareGrid(GameObject parent,
	                  Vec3f position,
	                  QuaternionF rotation,
	                  int widthIncrements,
	                  int heightIncrements,
	                  double widthMaxValue,
	                  double heightMaxValue) {

		stepWidthsReal = (int) (widthMaxValue / widthIncrements);
		stepHeightsReal = (int) (heightMaxValue / heightIncrements);

		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject whiteMarkers = new MeshBuilder()
				.setMeshType(MeshType.SQUARE)
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.build())
				.build();

		Transform gridTransform = transformBuilder
				.resetScale()
				.setPosition(position)
				.setRotation(rotation)
				.build();

		TransformObject gridTransformGraph = new TransformObject(parent, gridTransform);

		float widthStepSize = 1.0f/widthIncrements;
		float heightStepSize = 1.0f/heightIncrements;

		for (int i = -widthIncrements; i <= widthIncrements; i++) {

			Transform transform = transformBuilder
					.reset()
					.setPosition(new Vec3f(0, i * widthStepSize, 0))
					.setScale(new Vec3f(0.01f, 0.01f, 2))
					.build();

			TransformObject stepTransformGraph = new TransformObject(gridTransformGraph, transform);

			MeshGameObject stepMeshGraph = new MeshGameObject(stepTransformGraph, whiteMarkers);

			TextItem textItem = (TextItem) new MeshBuilder()
					.setMeshType(MeshType.TEXT)
					.setText(String.valueOf(-i * stepWidthsReal))
					.setFontFile("/fonts/verandaGreenBold.png")
					.build();

			Transform textTransform = transformBuilder
					.reset()
					.setPosition(new Vec3f(0, (i * widthStepSize) - 0.07f - textItem.getWidth()/2, 1.1f))
					.build();

			TransformObject textTransformGraph = new TransformObject(gridTransformGraph, textTransform);

			MeshGameObject textSceneGraph = new MeshGameObject(textTransformGraph, textItem);

		}

		for (int i = -heightIncrements; i <= heightIncrements; i++) {

			Transform transform = transformBuilder
					.reset()
					.setPosition(new Vec3f(0, 0, i * heightStepSize))
					.setScale(new Vec3f(0.01f, 2, 0.01f))
					.build();

			TransformObject stepTransformGraph = new TransformObject(gridTransformGraph, transform);

			MeshGameObject stepMeshGraph = new MeshGameObject(stepTransformGraph, whiteMarkers);

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

}
