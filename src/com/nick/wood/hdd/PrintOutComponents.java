package com.nick.wood.hdd;

import com.nick.wood.game_object_serialiser.IO;
import com.nick.wood.graphics_library.objects.game_objects.RootObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.hdd.gui_components.CylindricalReadout;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

import java.util.ArrayList;

public class PrintOutComponents {

	public static void main(String[] args) {
		new PrintOutComponents();
	}

	public PrintOutComponents() {

		ArrayList<RootObject> sceneGraphNodes = new ArrayList<>();

		RootObject rootObject = new RootObject();

		sceneGraphNodes.add(rootObject);

		MeshObject whiteMarkers = new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/white.png")
				.setTransform(new TransformBuilder()
						.setScale(new Vec3f(0.01f, 0.01f, 0.2f))
						.build())
				.build();

		CylindricalReadout cylindricalReadout = new CylindricalReadout(
				36,
				Vec3f.ZERO,
				rootObject,
				2.5,
				whiteMarkers,
				QuaternionF.RotationX(-Math.PI/2),
				true,
				(angle) -> String.valueOf((int)(Math.round( Math.toDegrees(-angle) / 10.0) * 10)),
				(angle, textItem) -> new TransformBuilder()
						.setScale(2)
						.setPosition(new Vec3f(0, 0, -0.2f-textItem.getWidth()/2))
						.setRotation(QuaternionF.RotationX(Math.PI/2))
						.build()
		);

		IO io = new IO();
		io.apply(sceneGraphNodes, "CylindricalReadout.json");

	}
}
