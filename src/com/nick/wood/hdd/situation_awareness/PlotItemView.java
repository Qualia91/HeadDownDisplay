package com.nick.wood.hdd.situation_awareness;

import com.nick.wood.graphics_library.objects.mesh_objects.*;
import com.nick.wood.graphics_library.objects.game_objects.MeshObject;
import com.nick.wood.graphics_library.objects.game_objects.GameObject;
import com.nick.wood.graphics_library.objects.game_objects.TransformObject;
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
	private final com.nick.wood.graphics_library.objects.mesh_objects.MeshObject neutralTrackMesh;
	private final com.nick.wood.graphics_library.objects.mesh_objects.MeshObject friendlyTrackMesh;
	private final com.nick.wood.graphics_library.objects.mesh_objects.MeshObject enemyTrackMesh;
	private final com.nick.wood.graphics_library.objects.mesh_objects.MeshObject unknownTrackMesh;
	private final MeshObject trackMeshGraph;

	public PlotItemView(Vec3f position, QuaternionF orientation, GameObject parent, float maxWidth, float maxHeight) {

		com.nick.wood.graphics_library.objects.mesh_objects.MeshObject selectedOutline = new MeshBuilder()
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

		TransformObject trackLocationTransformGraph = new TransformObject(parent, trackLocationTransform);

		this.trackRotationTransform = transformBuilder
				.reset()
				.setRotation(orientation)
				.build();

		TransformObject trackRotationTransformGraph = new TransformObject(trackLocationTransformGraph, trackRotationTransform);

		this.trackMeshGraph = new MeshObject(trackRotationTransformGraph, unknownTrackMesh);

		this.selectedMeshTransform = transformBuilder
				.reset()
				.setScale(0.5f)
				.setPosition(outOfTheWayVec)
				.build();

		TransformObject selectedMeshTransformGraph = new TransformObject(trackMeshGraph, selectedMeshTransform);

		MeshObject selectedMeshObject = new MeshObject(selectedMeshTransformGraph, selectedOutline);

		this.textItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.setFontFile("/font/verandaGreenBold.png")
				.build();

		this.textTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(-1, 0, 0))
				.setScale(5)
				.build();

		TransformObject textTransformGraph = new TransformObject(trackLocationTransformGraph, textTransform);

		MeshObject textSceneGraph = new MeshObject(textTransformGraph, textItem);

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
