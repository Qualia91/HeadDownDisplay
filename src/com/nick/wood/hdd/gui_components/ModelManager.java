package com.nick.wood.hdd.gui_components;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

import java.util.HashMap;

public class ModelManager {

	private final TransformBuilder transformBuilder = new TransformBuilder();
	private final HashMap<String, MeshObject> modelMap;

	public ModelManager() {
		modelMap = new HashMap<>();

		modelMap.put("WHITE_MARKER", new MeshBuilder()
				.setMeshType(MeshType.CUBOID)
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.resetPosition()
						.setScale(new Vec3f(0.01f, 0.01f, 0.2f))
						.build()).build());

		modelMap.put("AIM_MARKER", new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("\\models\\aimMarker.obj")
				.setTexture("/textures/gunMetalTexture.jpg")
				.setNormalTexture("/normalMaps/gunMetalNormal.jpg")
				.setTransform(transformBuilder
						.resetPosition()
						.setRotation(
								QuaternionF.RotationZ(Math.PI/2)
										.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.setScale(new Vec3f(0.1f, 0.1f, 0.1f))
						.build()).build());

		modelMap.put("PIN", new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("\\models\\pin.obj")
				.setTexture("/textures/gunMetalTexture.jpg")
				.setNormalTexture("/normalMaps/gunMetalNormal.jpg")
				.setTransform(transformBuilder
						.resetPosition()
						.setRotation(
								QuaternionF.RotationZ(Math.PI/2)
										.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.setScale(new Vec3f(0.2f, 0.2f, 0.2f))
						.build()).build());

		modelMap.put("ENEMY_TRACK", new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("\\models\\flatCone.obj")
				.setTexture("/textures/red.png")
				.setTransform(transformBuilder
						.setScale(0.2f)
						.setRotation(
								QuaternionF.RotationY(-Math.PI/2)
										.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.build()).build());

		modelMap.put("UNKNOWN_TRACK", new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("\\models\\flatCone.obj")
				.setTexture("/textures/yellow.png")
				.setTransform(transformBuilder
						.setScale(0.2f)
						.setRotation(
								QuaternionF.RotationY(-Math.PI/2)
										.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.build()).build());

		modelMap.put("FRIENDLY_TRACK", new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("\\models\\flatCone.obj")
				.setTexture("/textures/blue.png")
				.setTransform(transformBuilder
						.setScale(0.2f)
						.setRotation(
								QuaternionF.RotationY(-Math.PI/2)
										.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.build()).build());

		modelMap.put("NEUTRAL_TRACK", new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("\\models\\flatCone.obj")
				.setTexture("/textures/green.png")
				.setTransform(transformBuilder
						.setScale(0.2f)
						.setRotation(
								QuaternionF.RotationY(-Math.PI/2)
										.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.build()).build());

		modelMap.put("PLAYER_TRACK", new MeshBuilder()
				.setMeshType(MeshType.MODEL)
				.setModelFile("\\models\\flatCone.obj")
				.setTexture("/textures/white.png")
				.setTransform(transformBuilder
						.setScale(0.2f)
						.setRotation(
								QuaternionF.RotationY(-Math.PI/2)
										.multiply(QuaternionF.RotationX(Math.PI/2))
						)
						.build()).build());

		modelMap.put("ARROW", new MeshBuilder()
						.setMeshType(MeshType.MODEL)
						.setModelFile("\\models\\arrow.obj")
						.setTexture("/textures/gunMetalTexture.jpg")
						.setNormalTexture("/normalMaps/gunMetalNormal.jpg")
						.setTransform(
								transformBuilder
										.reset()
										.setScale(0.025f)
										.setRotation(QuaternionF.RotationZ(Math.PI/2))
										.build()
						)
						.build());
	}

	public MeshObject getModel(String name) {
		return modelMap.get(name);
	}
}
