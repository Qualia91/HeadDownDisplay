package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.graphics_library.objects.mesh_objects.*;
import com.nick.wood.graphics_library.objects.game_objects.MeshGameObject;
import com.nick.wood.graphics_library.objects.game_objects.GameObject;
import com.nick.wood.graphics_library.objects.game_objects.TransformObject;
import com.nick.wood.hdd.gui_components.ModelManager;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class PlotItemView {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final float maxWidth;
	private final float maxHeight;
	private final Transform textTransform;
	private final TextItem textItem;
	private final Transform trackLocationTransform;
	private final Transform trackRotationTransform;
	private final Transform selectedMeshTransform;

	private static final Vec3f OUT_OF_THE_WAY_VEC = new Vec3f(-100, 0, 0);
	private final com.nick.wood.graphics_library.objects.mesh_objects.MeshObject neutralTrackMesh;
	private final com.nick.wood.graphics_library.objects.mesh_objects.MeshObject friendlyTrackMesh;
	private final com.nick.wood.graphics_library.objects.mesh_objects.MeshObject enemyTrackMesh;
	private final com.nick.wood.graphics_library.objects.mesh_objects.MeshObject unknownTrackMesh;
	private final MeshGameObject trackMeshGraph;

	public PlotItemView(GameObject parent, float maxWidth, float maxHeight, ModelManager modelManager) {

		MeshObject selectedOutline = new MeshBuilder()
				.setMeshType(MeshType.SQUARE)
				.setTexture("/textures/selectedTexture.png")
				.build();

		this.enemyTrackMesh = modelManager.getModel("ENEMY_TRACK");

		this.unknownTrackMesh = modelManager.getModel("UNKNOWN_TRACK");

		this.friendlyTrackMesh = modelManager.getModel("FRIENDLY_TRACK");

		this.neutralTrackMesh = modelManager.getModel("NEUTRAL_TRACK");

		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;

		this.trackLocationTransform = transformBuilder
				.reset()
				.setScale(0.2f)
				.build();

		TransformObject trackLocationTransformGraph = new TransformObject(parent, trackLocationTransform);

		this.trackRotationTransform = transformBuilder
				.reset()
				.build();

		TransformObject trackRotationTransformGraph = new TransformObject(trackLocationTransformGraph, trackRotationTransform);

		this.trackMeshGraph = new MeshGameObject(trackRotationTransformGraph, unknownTrackMesh);

		this.selectedMeshTransform = transformBuilder
				.reset()
				.setScale(0.5f)
				.setPosition(OUT_OF_THE_WAY_VEC)
				.build();

		TransformObject selectedMeshTransformGraph = new TransformObject(trackMeshGraph, selectedMeshTransform);

		MeshGameObject selectedMeshGameObject = new MeshGameObject(selectedMeshTransformGraph, selectedOutline);

		this.textItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/fonts/verandaGreenBold.png")
				.build();

		this.textTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(-1, 0, 0))
				.setScale(5)
				.build();

		TransformObject textTransformGraph = new TransformObject(trackLocationTransformGraph, textTransform);

		MeshGameObject textSceneGraph = new MeshGameObject(textTransformGraph, textItem);

	}

	public void updateInformation(Plot plot, float playerHeading) {

		// calc position in grid
		// find "forward" and "side" locations
		float forward = (float) (plot.getBra().getY() * Math.cos(plot.getBra().getX())) / maxWidth;
		float side = (float) (plot.getBra().getY() * Math.sin(plot.getBra().getX())) / maxHeight;

		// check if in grid
		if (Math.abs(side) > 1) {
			side = Math.copySign(1, side);
		}
		if (Math.abs(forward) > 1) {
			forward = Math.copySign(1, forward);
		}
		this.trackLocationTransform.setPosition(new Vec3f(0, -side, forward));

		float targetRelativeHeading = plot.getHpr().getX() - playerHeading;

		this.trackRotationTransform.setRotation(QuaternionF.RotationX(targetRelativeHeading));

		this.textItem.changeText(String.valueOf(plot.getId()));

		selectedMeshTransform.setPosition(plot.isSelected() ? new Vec3f(0.01f, 0, 0) : OUT_OF_THE_WAY_VEC);

		switch (plot.getAllegiance()) {
			case NEUTRAL: trackMeshGraph.setMeshObject(neutralTrackMesh);
			break;
			case FRIENDLY: trackMeshGraph.setMeshObject(friendlyTrackMesh);
			break;
			case ENEMY: trackMeshGraph.setMeshObject(enemyTrackMesh);
			break;
			case UNKNOWN: trackMeshGraph.setMeshObject(unknownTrackMesh);
			break;
		}
	}

	public void hide() {
		this.trackLocationTransform.setPosition(new Vec3f(-100, 0, 0));
	}
}
