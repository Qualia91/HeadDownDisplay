package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.graphics_library.objects.mesh_objects.*;
import com.nick.wood.graphics_library.objects.game_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.game_objects.SceneGraphNode;
import com.nick.wood.graphics_library.objects.game_objects.TransformSceneGraph;
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

	private static final Vec3f outOfTheWayVec = new Vec3f(-100, 0, 0);
	private final MeshObject neutralTrackMesh;
	private final MeshObject friendlyTrackMesh;
	private final MeshObject enemyTrackMesh;
	private final MeshObject unknownTrackMesh;
	private final MeshSceneGraph trackMeshGraph;

	public PlotItemView(Vec3f position, QuaternionF orientation, SceneGraphNode parent, float maxWidth, float maxHeight) {

		MeshObject selectedOutline = new MeshBuilder()
				.setMeshType(MeshType.SQUARE)
				.setTexture("/textures/selectedTexture.png")
				.build();

		this.enemyTrackMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("D:\\Software\\Programming\\projects\\Java\\HeadDownDisplay\\deps\\flatCone.obj")
				.setTexture("/textures/red.png")
				.setTransform(transformBuilder
						.setScale(0.2f)
						.setRotation(
								QuaternionF.RotationY(-Math.PI/2)
										.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.build()).build();

		this.unknownTrackMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("D:\\Software\\Programming\\projects\\Java\\HeadDownDisplay\\deps\\flatCone.obj")
				.setTexture("/textures/yellow.png")
				.setTransform(transformBuilder
						.setScale(0.2f)
						.setRotation(
								QuaternionF.RotationY(-Math.PI/2)
										.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.build()).build();

		this.friendlyTrackMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("D:\\Software\\Programming\\projects\\Java\\HeadDownDisplay\\deps\\flatCone.obj")
				.setTexture("/textures/blue.png")
				.setTransform(transformBuilder
						.setScale(0.2f)
						.setRotation(
								QuaternionF.RotationY(-Math.PI/2)
										.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.build()).build();

		this.neutralTrackMesh = new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("D:\\Software\\Programming\\projects\\Java\\HeadDownDisplay\\deps\\flatCone.obj")
				.setTexture("/textures/green.png")
				.setTransform(transformBuilder
						.setScale(0.2f)
						.setRotation(
								QuaternionF.RotationY(-Math.PI/2)
										.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.build()).build();

		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;

		// calc position in grid
		// find "forward" and "side" locations
		float forward = (float) (position.getY() * Math.cos(position.getX())) / maxWidth;
		float side = (float) (position.getY() * Math.sin(position.getX())) / maxHeight;

		this.trackLocationTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(0, forward, side))
				.setScale(0.2f)
				.build();

		TransformSceneGraph trackLocationTransformGraph = new TransformSceneGraph(parent, trackLocationTransform);

		this.trackRotationTransform = transformBuilder
				.reset()
				.setRotation(orientation)
				.build();

		TransformSceneGraph trackRotationTransformGraph = new TransformSceneGraph(trackLocationTransformGraph, trackRotationTransform);

		this.trackMeshGraph = new MeshSceneGraph(trackRotationTransformGraph, unknownTrackMesh);

		this.selectedMeshTransform = transformBuilder
				.reset()
				.setScale(0.5f)
				.setPosition(outOfTheWayVec)
				.build();

		TransformSceneGraph selectedMeshTransformGraph = new TransformSceneGraph(trackMeshGraph, selectedMeshTransform);

		MeshSceneGraph selectedMeshSceneGraph = new MeshSceneGraph(selectedMeshTransformGraph, selectedOutline);

		this.textItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/verandaGreenBold.png")
				.build();

		this.textTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(-1, 0, 0))
				.setScale(5)
				.build();

		TransformSceneGraph textTransformGraph = new TransformSceneGraph(trackLocationTransformGraph, textTransform);

		MeshSceneGraph textSceneGraph = new MeshSceneGraph(textTransformGraph, textItem);

	}

	public void updateInformation(Plot plot) {

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
		this.trackRotationTransform.setRotation(plot.getOrientation());

		this.textItem.changeText(String.valueOf(plot.getId()));

		selectedMeshTransform.setPosition(plot.isSelected() ? new Vec3f(0.01f, 0, 0) : outOfTheWayVec);

		switch (plot.getAllegiance()) {
			case NEUTRAL -> trackMeshGraph.setMeshObject(neutralTrackMesh);
			case FRIENDLY -> trackMeshGraph.setMeshObject(friendlyTrackMesh);
			case ENEMY -> trackMeshGraph.setMeshObject(enemyTrackMesh);
			case UNKNOWN -> trackMeshGraph.setMeshObject(unknownTrackMesh);
		}
	}

	public void hide() {
		this.trackLocationTransform.setPosition(new Vec3f(-100, 0, 0));
	}
}
