package com.nick.wood.hdd;

import com.nick.wood.graphics_library.WindowInitialisationParametersBuilder;
import com.nick.wood.graphics_library.lighting.PointLight;
import com.nick.wood.graphics_library.objects.Camera;
import com.nick.wood.graphics_library.objects.scene_graph_objects.CameraSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.CameraType;
import com.nick.wood.graphics_library.objects.scene_graph_objects.SceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.graphics_library.utils.Creation;
import com.nick.wood.hdd.altimeter.AltimeterController;
import com.nick.wood.hdd.altimeter.AltimeterSceneController;
import com.nick.wood.hdd.altimeter.AltimeterSceneView;
import com.nick.wood.hdd.altimeter.AltimeterView;
import com.nick.wood.hdd.event_bus.busses.RenderBus;
import com.nick.wood.hdd.event_bus.data.AltimeterChangeData;
import com.nick.wood.hdd.event_bus.data.PlotsListChangeData;
import com.nick.wood.hdd.event_bus.data.RenderManagementInitData;
import com.nick.wood.hdd.event_bus.event_types.AltimeterChangeDataType;
import com.nick.wood.hdd.event_bus.event_types.PlotListChangeDataType;
import com.nick.wood.hdd.event_bus.event_types.RenderManagementEventType;
import com.nick.wood.hdd.event_bus.events.AltimeterChangeEvent;
import com.nick.wood.hdd.event_bus.events.PlotListChangeEvent;
import com.nick.wood.hdd.event_bus.events.RenderManagementEvents;
import com.nick.wood.hdd.event_bus.subscribables.RendererManager;
import com.nick.wood.hdd.situation_awareness.Allegiance;
import com.nick.wood.hdd.situation_awareness.Plot;
import com.nick.wood.hdd.situation_awareness.SAController;
import com.nick.wood.hdd.situation_awareness.SAView;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainFull {

	// this is to get world in sensible coordinate system to start with
	static private final QuaternionF quaternionX = QuaternionF.RotationX((float) Math.toRadians(-90));
	static private final QuaternionF quaternionY = QuaternionF.RotationY((float) Math.toRadians(180));
	static private final QuaternionF quaternionZ = QuaternionF.RotationZ((float) Math.toRadians(90));
	public static final QuaternionF CAMERA_ROTATION = quaternionZ.multiply(quaternionY).multiply(quaternionX);

	public static void main(String[] args) {
		new MainFull().launch();
	}

	private void launch() {

		TransformBuilder transformBuilder = new TransformBuilder();



		// main scene
		SceneGraph rootGameObject = new SceneGraph();
		Transform playerViewTransform = transformBuilder.build();
		TransformSceneGraph playerViewTransformGraph = new TransformSceneGraph(rootGameObject, playerViewTransform);



		// view wheel transform
		Transform wheelTransform = transformBuilder
				.reset()
				.build();
		TransformSceneGraph wheelTransformGraph = new TransformSceneGraph(playerViewTransformGraph, wheelTransform);



		// sa transform
		Transform saTransform = transformBuilder
				.setPosition(new Vec3f(5, 2.5f, 0))
				.setScale(2.5f)
				.build();
		TransformSceneGraph saTransformGraph = new TransformSceneGraph(wheelTransformGraph, saTransform);



		// altimeter transform
		Transform altimeterTransform = transformBuilder
				.resetScale()
				.setPosition(new Vec3f(5, -2.5f, 0))
				.build();
		TransformSceneGraph altimeterTransformGraph = new TransformSceneGraph(wheelTransformGraph, altimeterTransform);



		// fbo scene transform
		Transform fboViewTransform = transformBuilder
				.setPosition(new Vec3f(1000, 0, 0))
				.build();
		TransformSceneGraph fboViewTransformGraph = new TransformSceneGraph(rootGameObject, fboViewTransform);



		// create views
		SAView saView = new SAView(saTransformGraph);
		AltimeterView altimeterView = new AltimeterView(altimeterTransformGraph);
		AltimeterSceneView altimeterSceneView = new AltimeterSceneView(fboViewTransformGraph);



		// create camera
		Camera camera = new Camera(1.22173f, 0.001f, 100);
		TransformSceneGraph cameraTransformGameObject;

		// attach camera to fbo
		if (false) {
			CameraSceneGraph fboCameraGameObject = altimeterSceneView.getFboCameraGameObject();
			Transform cameraTransform = transformBuilder
					.setPosition(new Vec3f(0, 0, 0))
					.setScale(Vec3f.ONE)
					.build();
			cameraTransformGameObject = new TransformSceneGraph(fboCameraGameObject, cameraTransform);
		} else {
			// separate camera
			// player camera
			Transform cameraTransform = transformBuilder
					.setPosition(new Vec3f(0, 0, 0))
					.setScale(Vec3f.ONE)
					.setRotation(CAMERA_ROTATION)
					.build();
			cameraTransformGameObject = new TransformSceneGraph(playerViewTransformGraph, cameraTransform);
		}
		CameraSceneGraph cameraGameObject = new CameraSceneGraph(cameraTransformGameObject, camera, CameraType.PRIMARY);



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



		// create controllers and attach to Bus
		AltimeterSceneController altimeterSceneController = new AltimeterSceneController(altimeterSceneView, renderBus);
		renderBus.register(altimeterSceneController);
		AltimeterController altimeterController = new AltimeterController(altimeterView, renderBus);
		renderBus.register(altimeterController);
		SAController saSceneController = new SAController(saView, renderBus);
		renderBus.register(saSceneController);



		RendererManager renderer = new RendererManager();
		renderBus.register(renderer);

		executorService.submit(() -> {

			ArrayList<Plot> plots = new ArrayList<>();

			for (int j = 1; j < 3; j++) {
				plots.add(new Plot(
						j,
						new Vec3f((float) (Math.PI/8 * j), j * 100, 0),
						QuaternionF.RotationX((float) (Math.PI/8 * j)),
						j == 1,
						Allegiance.UNKNOWN
				));
			}

			for (int j = 3; j < 8; j++) {
				plots.add(new Plot(
						j,
						new Vec3f((float) (Math.PI/8 * j), j * 100, 0),
						QuaternionF.RotationX((float) (Math.PI/8 * j)),
						false,
						Allegiance.FRIENDLY
				));
			}

			for (int j = 8; j < 10; j++) {
				plots.add(new Plot(
						j,
						new Vec3f((float) (Math.PI/8 * j), j * 100, 0),
						QuaternionF.RotationX((float) (Math.PI/8 * j)),
						false,
						Allegiance.ENEMY
				));
			}

			for (int j = 10; j < 13; j++) {
				plots.add(new Plot(
						j,
						new Vec3f((float) (Math.PI/8 * j), j * 100, 0),
						QuaternionF.RotationX((float) (Math.PI/8 * j)),
						false,
						Allegiance.NEUTRAL
				));
			}

			float bearingIncrement = 0.01f;

			int i = 0;
			while (true) {
				Thread.sleep(100);

				ArrayList<Plot> newPlots = new ArrayList<>();

				for (Plot plot : plots) {
					newPlots.add(new Plot(
							plot.getId(),
							new Vec3f(plot.getBra().getX() + (i * bearingIncrement), plot.getBra().getY(), 0),
							plot.getOrientation(),
							plot.isSelected(),
							plot.getAllegiance()
					));
				}

				renderBus.dispatch(new PlotListChangeEvent(
						new PlotsListChangeData(
								newPlots
						),
						PlotListChangeDataType.CHANGE
				));
				i++;
			}
		});

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
								(float) i/50,
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
}
