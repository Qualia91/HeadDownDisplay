package com.nick.wood.hdd;

import com.nick.wood.graphics_library.WindowInitialisationParametersBuilder;
import com.nick.wood.graphics_library.lighting.Attenuation;
import com.nick.wood.graphics_library.lighting.PointLight;
import com.nick.wood.graphics_library.objects.Camera;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.scene_graph_objects.*;
import com.nick.wood.graphics_library.utils.Creation;
import com.nick.wood.hdd.altimeter.AltimeterController;
import com.nick.wood.hdd.altimeter.AltimeterSceneController;
import com.nick.wood.hdd.altimeter.AltimeterSceneView;
import com.nick.wood.hdd.event_bus.busses.RenderBus;
import com.nick.wood.hdd.event_bus.data.AltimeterChangeData;
import com.nick.wood.hdd.event_bus.data.RenderManagementInitData;
import com.nick.wood.hdd.event_bus.data.RenderUpdateData;
import com.nick.wood.hdd.event_bus.event_types.AltimeterChangeDataType;
import com.nick.wood.hdd.event_bus.event_types.RenderManagementEventType;
import com.nick.wood.hdd.event_bus.event_types.RenderUpdateEventType;
import com.nick.wood.hdd.event_bus.events.AltimeterChangeEvent;
import com.nick.wood.hdd.event_bus.events.RenderManagementEvents;
import com.nick.wood.hdd.event_bus.events.RenderUpdateEvents;
import com.nick.wood.hdd.event_bus.subscribables.RendererManager;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;
import com.nick.wood.hdd.altimeter.AltimeterView;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	// this is to get world in sensible coordinate system to start with
	static private final QuaternionF quaternionX = QuaternionF.RotationX((float) Math.toRadians(-90));
	static private final QuaternionF quaternionY = QuaternionF.RotationY((float) Math.toRadians(180));
	static private final QuaternionF quaternionZ = QuaternionF.RotationZ((float) Math.toRadians(90));
	public static final QuaternionF CAMERA_ROTATION = quaternionZ.multiply(quaternionY).multiply(quaternionX);

	public static void main(String[] args) {
		new Main().launch();
	}

	private void launch() {

		TransformBuilder transformBuilder = new TransformBuilder();

		// main scene
		SceneGraph rootGameObject = new SceneGraph();
		Transform playerViewTransform = transformBuilder.build();
		TransformSceneGraph playerViewTransformGraph = new TransformSceneGraph(rootGameObject, playerViewTransform);


		Transform fboViewTransform = transformBuilder
				.setPosition(new Vec3f(1000, 0, 0)).build();
		TransformSceneGraph fboViewTransformGraph = new TransformSceneGraph(rootGameObject, fboViewTransform);

		transformBuilder
				.setPosition(Vec3f.ZERO).build();

		Transform altimeterTransform = transformBuilder
				.setPosition(new Vec3f(5, 0, 0)).build();
		TransformSceneGraph altimeterTransformGraph = new TransformSceneGraph(playerViewTransformGraph, altimeterTransform);


		// player camera
		Camera camera = new Camera(1.22173f, 0.001f, 100);
		Transform cameraTransform = transformBuilder
				.setPosition(new Vec3f(0, 0, 0))
				.setScale(Vec3f.ONE)
				.setRotation(CAMERA_ROTATION)
				.build();
		TransformSceneGraph cameraTransformGameObject = new TransformSceneGraph(playerViewTransformGraph, cameraTransform);
		CameraSceneGraph cameraGameObject = new CameraSceneGraph(cameraTransformGameObject, camera, CameraType.PRIMARY);

		AltimeterView altimeterView = new AltimeterView(altimeterTransformGraph);
		AltimeterSceneView altimeterSceneView = new AltimeterSceneView(fboViewTransformGraph);

		// lights
		PointLight pointLight = new PointLight(
				Vec3f.ONE,
				1
		);
		Creation.CreateLight(pointLight, cameraTransformGameObject, transformBuilder
						.setPosition(Vec3f.ZERO)
						.setRotation(QuaternionF.Identity).build());


		// add roots to map
		HashMap<UUID, SceneGraph> gameObjects = new HashMap<>();
		gameObjects.put(rootGameObject.getSceneGraphNodeData().getUuid(), rootGameObject);

		WindowInitialisationParametersBuilder windowInitialisationParametersBuilder = new WindowInitialisationParametersBuilder();
		windowInitialisationParametersBuilder.setDecorated(true);
		windowInitialisationParametersBuilder.setSceneAmbientLight(new Vec3f(.8f, .8f, .8f));

		RenderBus renderBus = new RenderBus();
		ExecutorService executorService = Executors.newFixedThreadPool(4);

		AltimeterSceneController altimeterSceneController = new AltimeterSceneController(altimeterSceneView, renderBus);
		renderBus.register(altimeterSceneController);

		AltimeterController altimeterController = new AltimeterController(altimeterView, renderBus);
		renderBus.register(altimeterController);

		RendererManager renderer = new RendererManager();
		renderBus.register(renderer);

		executorService.submit(() -> {
			int i = 0;
			while (true) {
				Thread.sleep(10);
				float angle = (float) ((i/1000.0) % Math.PI);
				float angleRequest = (float) (-Math.PI/2 + ((i/100.0) % Math.PI));
				renderBus.dispatch(new AltimeterChangeEvent(
						new AltimeterChangeData(
								angle,
								angle,
								angle,
								(float) i,
								(float)i,
								(float)((i/10) % 120)/ 100,
								angleRequest,
								angleRequest,
								angleRequest
								),
						AltimeterChangeDataType.CHANGE
				));
				i++;
			}
		});


		renderBus.dispatch(
				new RenderManagementEvents(
						new RenderManagementInitData(
								gameObjects,
								new HashMap<>(),
								cameraGameObject.getSceneGraphNodeData().getUuid(),
								windowInitialisationParametersBuilder.build()
						),
						RenderManagementEventType.START));

	}

	public Main() {
	}
}
