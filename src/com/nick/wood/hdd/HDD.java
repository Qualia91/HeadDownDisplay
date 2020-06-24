package com.nick.wood.hdd;

import com.nick.wood.graphics_library.WindowInitialisationParametersBuilder;
import com.nick.wood.graphics_library.lighting.PointLight;
import com.nick.wood.graphics_library.objects.Camera;
import com.nick.wood.graphics_library.objects.game_objects.CameraObject;
import com.nick.wood.graphics_library.objects.game_objects.RootObject;
import com.nick.wood.graphics_library.objects.game_objects.TransformObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
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
import com.nick.wood.hdd.event_bus.events.RenderManagementEvent;
import com.nick.wood.hdd.event_bus.subscribables.RendererManager;
import com.nick.wood.hdd.gui_components.ModelManager;
import com.nick.wood.hdd.situation_awareness.*;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HDD {

	// this is to get world in sensible coordinate system to start with
	static private final QuaternionF quaternionX = QuaternionF.RotationX((float) Math.toRadians(-90));
	static private final QuaternionF quaternionY = QuaternionF.RotationY((float) Math.toRadians(180));
	static private final QuaternionF quaternionZ = QuaternionF.RotationZ((float) Math.toRadians(90));
	public static final QuaternionF CAMERA_ROTATION = quaternionZ.multiply(quaternionY).multiply(quaternionX);

	public static void main(String[] args) {
		new HDD().launch();

		System.exit(0);
	}

	private void launch() {

		TransformBuilder transformBuilder = new TransformBuilder();


		// main scene
		RootObject rootGameObject = new RootObject();
		Transform playerViewTransform = transformBuilder.build();
		TransformObject playerViewTransformGraph = new TransformObject(rootGameObject, playerViewTransform);


		// view wheel transform
		Transform wheelTransform = transformBuilder
				.reset()
				.build();
		TransformObject wheelTransformGraph = new TransformObject(playerViewTransformGraph, wheelTransform);


		// selected item information
		Transform siiTransform = transformBuilder
				.setPosition(new Vec3f(7, 6, 2.5f))
				.setScale(2.5f)
				.build();
		TransformObject siiTransformGraph = new TransformObject(wheelTransformGraph, siiTransform);


		// sa transform
		Transform saTransform = transformBuilder
				.setPosition(new Vec3f(7, 0, 0))
				.setScale(2.5f)
				.build();
		TransformObject saTransformGraph = new TransformObject(wheelTransformGraph, saTransform);


		// altimeter transform
		Transform altimeterTransform = transformBuilder
				.resetScale()
				.setPosition(new Vec3f(7, -5f, 0))
				.build();
		TransformObject altimeterTransformGraph = new TransformObject(wheelTransformGraph, altimeterTransform);


		// fbo scene transform
		Transform fboViewTransform = transformBuilder
				.setPosition(new Vec3f(1000, 0, 0))
				.build();
		TransformObject fboViewTransformGraph = new TransformObject(rootGameObject, fboViewTransform);


		// build all models used in app

		ModelManager modelManager = new ModelManager();


		// create views
		SAView saView = new SAView(saTransformGraph, 1000, 1000, modelManager);
		AltimeterView altimeterView = new AltimeterView(altimeterTransformGraph);
		AltimeterSceneView altimeterSceneView = new AltimeterSceneView(fboViewTransformGraph, modelManager);
		SelectedInformationView selectedInformationView = new SelectedInformationView(siiTransformGraph);


		// create camera
		Camera camera = new Camera(1.22173f, 1, 10);
		TransformObject cameraTransformGameObject;

		// attach camera to fbo
		if (false) {
			CameraObject fboCameraObject = altimeterSceneView.getFboCameraObject();
			Transform cameraTransform = transformBuilder
					.setPosition(new Vec3f(0, 0, 0))
					.setScale(Vec3f.ONE)
					.build();
			cameraTransformGameObject = new TransformObject(fboCameraObject, cameraTransform);
		} else {
			// separate camera
			// player camera
			Transform cameraTransform = transformBuilder
					.setPosition(new Vec3f(0, 0, 0))
					.setScale(Vec3f.ONE)
					.setRotation(CAMERA_ROTATION)
					.build();
			cameraTransformGameObject = new TransformObject(playerViewTransformGraph, cameraTransform);
		}
		CameraObject cameraObject = new CameraObject(cameraTransformGameObject, camera);


		// lights
		PointLight pointLight = new PointLight(
				Vec3f.ONE,
				1
		);
		Creation.CreateLight(pointLight, cameraTransformGameObject, transformBuilder
				.setPosition(Vec3f.ZERO)
				.setRotation(QuaternionF.Identity).build());


		// add roots to map
		ArrayList<RootObject> gameObjects = new ArrayList<>();
		gameObjects.add(rootGameObject);

		WindowInitialisationParametersBuilder windowInitialisationParametersBuilder = new WindowInitialisationParametersBuilder()
				.setDecorated(true)
				.setSceneAmbientLight(new Vec3f(.8f, .8f, .8f))
				.setLockCursor(false)
				.setPicking(true);

		RenderBus renderBus = new RenderBus();
		ExecutorService executorService = Executors.newFixedThreadPool(4);


		// create controllers and attach to Bus
		AltimeterSceneController altimeterSceneController = new AltimeterSceneController(altimeterSceneView, renderBus);
		renderBus.register(altimeterSceneController);
		AltimeterController altimeterController = new AltimeterController(altimeterView, renderBus);
		renderBus.register(altimeterController);
		SAController saSceneController = new SAController(saView, renderBus);
		renderBus.register(saSceneController);
		SelectedInformationController selectedInformationController = new SelectedInformationController(selectedInformationView, renderBus);
		renderBus.register(selectedInformationController);


		RendererManager renderer = new RendererManager(renderBus);
		renderBus.register(renderer);

		SisoEnum sisoEnum = new SisoEnum(1, 2, 227, 23, 42, 1, 1);

		executorService.submit(() -> {

			Plot[] plots = new Plot[13];

			for (int j = 0; j < 3; j++) {
				plots[j] = (new Plot(
						new TrackID(j, j),
						sisoEnum,
						new Vec3f((float) (Math.PI / 8 * j), j * 100, 0),
						new Vec3f((float) Math.toRadians(j * 10), 0, 0),
						j == 1,
						Allegiance.UNKNOWN
				));
			}

			for (int j = 3; j < 8; j++) {
				plots[j] = (new Plot(
						new TrackID(j, j),
						sisoEnum,
						new Vec3f((float) (Math.PI / 8 * j), j * 100, 0),
						new Vec3f((float) Math.toRadians(j * 10), 0, 0),
						false,
						Allegiance.FRIENDLY
				));
			}

			for (int j = 8; j < 10; j++) {
				plots[j] = (new Plot(
						new TrackID(j, j),
						sisoEnum,
						new Vec3f((float) (Math.PI / 8 * j), j * 100, 0),
						new Vec3f((float) Math.toRadians(j * 10), 0, 0),
						false,
						Allegiance.ENEMY
				));
			}

			for (int j = 10; j < 13; j++) {
				plots[j] = (new Plot(
						new TrackID(j, j),
						sisoEnum,
						new Vec3f((float) (Math.PI / 8 * j), j * 100, 0),
						new Vec3f((float) Math.toRadians(j * 10), 0, 0),
						false,
						Allegiance.NEUTRAL
				));
			}

			renderBus.dispatch(new PlotListChangeEvent(
					new PlotsListChangeData(
							plots
					),
					PlotListChangeDataType.CHANGE
			));
		});

		executorService.submit(() -> {
			int i = 0;
			while (true) {
				Thread.sleep(10);
				float angle = (float) ((i / 1000.0) % Math.PI);
				float angleRequest = (float) (-Math.PI / 2 + ((i / 100.0) % Math.PI));
				renderBus.dispatch(new AltimeterChangeEvent(
						new AltimeterChangeData(
								angle,
								angle,
								angle,
								(float) i,
								(float) i / 50,
								(float) ((i / 10) % 120) / 100,
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
				new RenderManagementEvent(
						new RenderManagementInitData(
								gameObjects,
								new ArrayList<>(),
								cameraObject.getGameObjectData().getUuid(),
								windowInitialisationParametersBuilder.build()
						),
						RenderManagementEventType.START));


	}
}
