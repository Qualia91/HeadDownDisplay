package com.nick.wood.hdd.gui_components;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.SceneGraphNode;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

public class AltitudeReadout {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final TextItem altTextItem;
	private final Transform altTextTransform;

	public AltitudeReadout(SceneGraphNode parent) {
		// alt text
		this.altTextItem = (TextItem) new MeshBuilder()
				.setMeshType(MeshType.TEXT)
				.build();
		Transform persistentAltTextTransform = transformBuilder
				.reset()
				.setRotation(QuaternionF.RotationX(Math.PI/2))
				.setPosition(new Vec3f(1, 1, 0.05f))
				.build();
		this.altTextTransform = transformBuilder
				.reset()
				.setPosition(new Vec3f(0, 0, 0))
				.build();
		TransformSceneGraph textTransformSceneGraphPersistentAlt = new TransformSceneGraph(parent, persistentAltTextTransform);
		TransformSceneGraph textTransformSceneGraphAlt = new TransformSceneGraph(textTransformSceneGraphPersistentAlt, altTextTransform);
		MeshSceneGraph textMeshObjectAlt = new MeshSceneGraph(textTransformSceneGraphAlt, altTextItem);

	}

	public TransformBuilder getTransformBuilder() {
		return transformBuilder;
	}

	public TextItem getAltTextItem() {
		return altTextItem;
	}

	public Transform getAltTextTransform() {
		return altTextTransform;
	}

	public void setAltitude(float altitude) {
		altTextItem.changeText(String.valueOf((int) altitude));
	}
}

