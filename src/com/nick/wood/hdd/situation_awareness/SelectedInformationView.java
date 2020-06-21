package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.game_objects.MeshGameObject;
import com.nick.wood.graphics_library.objects.game_objects.TransformObject;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class SelectedInformationView {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final TextItem idText;
	private final TextItem typeText;
	private final TextItem bearingText;
	private final TextItem rangeText;
	private final TextItem altText;
	private final TextItem enemyHeadingText;
	private final TextItem enemyPitchText;
	private final TextItem enemyRollText;
	private final TextItem allegianceText;

	public SelectedInformationView(TransformObject parent) {

		this.idText = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/verandaGreenBold.png")
				.build();

		this.typeText = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/verandaGreenBold.png")
				.build();

		this.bearingText = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/verandaGreenBold.png")
				.build();

		this.rangeText = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/verandaGreenBold.png")
				.build();

		this.altText = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/verandaGreenBold.png")
				.build();

		this.enemyHeadingText = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/verandaGreenBold.png")
				.build();

		this.enemyPitchText = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/verandaGreenBold.png")
				.build();

		this.enemyRollText = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/verandaGreenBold.png")
				.build();

		this.allegianceText = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/verandaGreenBold.png")
				.build();


		createTextLine(new Vec3f(0, 0.25f, 0), parent, "ID", idText);
		createTextLine(new Vec3f(0, 0.25f, -0.1f), parent, "TYPE", typeText);
		createTextLine(new Vec3f(0, 0.25f, -0.2f), parent, "BEARING", bearingText);
		createTextLine(new Vec3f(0, 0.25f, -0.3f), parent, "RANGE", rangeText);
		createTextLine(new Vec3f(0, 0.25f, -0.4f), parent, "ALTITUDE", altText);
		createTextLine(new Vec3f(0, 0.25f, -0.5f), parent, "HEADING", enemyHeadingText);
		createTextLine(new Vec3f(0, 0.25f, -0.6f), parent, "PITCH", enemyPitchText);
		createTextLine(new Vec3f(0, 0.25f, -0.7f), parent, "ROLL", enemyRollText);
		createTextLine(new Vec3f(0, 0.25f, -0.8f), parent, "ALLEGIANCE", allegianceText);

	}

	private void createTextLine(Vec3f position, TransformObject parent, String label, TextItem textItem) {

		TextItem labelTextItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setText(label)
				.setFontFile("/font/verandaGreenBold.png")
				.build();

		Transform labelTransform = transformBuilder
				.reset()
				.setPosition(position)
				.build();

		TransformObject labelTextTransformGraph = new TransformObject(parent, labelTransform);

		new MeshGameObject(labelTextTransformGraph, labelTextItem);

		Transform textTransform = transformBuilder
				.reset()
				.setPosition(position.add(new Vec3f(0, -1.25f, 0)))
				.build();

		TransformObject textTransformGraph = new TransformObject(parent, textTransform);

		new MeshGameObject(textTransformGraph, textItem);
	}

	public TextItem getIdText() {
		return idText;
	}

	public TextItem getTypeText() {
		return typeText;
	}

	public TextItem getBearingText() {
		return bearingText;
	}

	public TextItem getRangeText() {
		return rangeText;
	}

	public TextItem getAltText() {
		return altText;
	}

	public TextItem getEnemyHeadingText() {
		return enemyHeadingText;
	}

	public TextItem getEnemyPitchText() {
		return enemyPitchText;
	}

	public TextItem getEnemyRollText() {
		return enemyRollText;
	}

	public TextItem getAllegianceText() {
		return allegianceText;
	}
}
