package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class SelectedInformationView {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final TextItem idText;
	private final TextItem bearingText;
	private final TextItem rangeText;
	private final TextItem altText;
	private final TextItem enemyHeadingText;
	private final TextItem enemyPitchText;
	private final TextItem enemyRollText;
	private final TextItem allegianceText;

	public SelectedInformationView(TransformSceneGraph parent) {

		this.idText = (TextItem) new MeshBuilder()
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


		createTextLine(new Vec3f(0, 0, 0), parent, "ID", idText);
		createTextLine(new Vec3f(0, 0, -0.1f), parent, "BEARING", bearingText);
		createTextLine(new Vec3f(0, 0, -0.2f), parent, "RANGE", rangeText);
		createTextLine(new Vec3f(0, 0, -0.3f), parent, "ALTITUDE", altText);
		createTextLine(new Vec3f(0, 0, -0.4f), parent, "HEADING", enemyHeadingText);
		createTextLine(new Vec3f(0, 0, -0.5f), parent, "PITCH", enemyPitchText);
		createTextLine(new Vec3f(0, 0, -0.6f), parent, "ROLL", enemyRollText);
		createTextLine(new Vec3f(0, 0, -0.7f), parent, "ALLEGIANCE", allegianceText);

	}

	private void createTextLine(Vec3f position, TransformSceneGraph parent, String label, TextItem textItem) {

		TextItem labelTextItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setText(label)
				.setFontFile("/font/verandaGreenBold.png")
				.build();

		Transform labelTransform = transformBuilder
				.reset()
				.setPosition(position)
				.build();

		TransformSceneGraph labelTextTransformGraph = new TransformSceneGraph(parent, labelTransform);

		new MeshSceneGraph(labelTextTransformGraph, labelTextItem);

		Transform textTransform = transformBuilder
				.reset()
				.setPosition(position.add(new Vec3f(0, -1, 0)))
				.build();

		TransformSceneGraph textTransformGraph = new TransformSceneGraph(parent, textTransform);

		new MeshSceneGraph(textTransformGraph, textItem);
	}

	public TextItem getIdText() {
		return idText;
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
