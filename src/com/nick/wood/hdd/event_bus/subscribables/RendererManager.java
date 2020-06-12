package com.nick.wood.hdd.event_bus.subscribables;

import com.nick.wood.graphics_library.Window;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.SceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.SceneGraphNode;
import com.nick.wood.hdd.event_bus.data.RenderManagementInitData;
import com.nick.wood.hdd.event_bus.events.RenderManagementEvents;
import com.nick.wood.hdd.event_bus.events.RenderUpdateEvents;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;
import com.nick.wood.maths.objects.vector.Vec3f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public class RendererManager implements Subscribable {

	private final Set<Class<?>> supports = new HashSet<>();

	private Window window;

	private final ConcurrentLinkedQueue<RenderUpdateEvents> renderUpdateEvents = new ConcurrentLinkedQueue<>();
	private final HashMap<UUID, SceneGraph> gameObjects;
	private final HashMap<UUID, SceneGraph> hudObjects;
	private UUID cameraUUID;

	public RendererManager() {

		this.gameObjects = new HashMap<>();
		this.hudObjects = new HashMap<>();
		this.cameraUUID = UUID.randomUUID();

		supports.add(RenderManagementEvents.class);
		supports.add(RenderUpdateEvents.class);
	}



	private void init(RenderManagementInitData data) {

		try {
			this.window = new Window();
			window.init(data.getWindowInitialisationParameters());
			this.gameObjects.putAll(data.getGameObjects());
			this.hudObjects.putAll(data.getHudObjects());
			this.cameraUUID = data.getCameraUUID();
		} catch (Exception e) {
			System.out.println("Failed");
		}
		run();
	}

	private void run() {
		while (!window.shouldClose()) {
			applyUpdates();
			window.loop(gameObjects, hudObjects, cameraUUID);
		}
		stop();
	}

	private void applyUpdates() {
		int size = renderUpdateEvents.size();

		for (int i = 0; i < size; i++) {
			RenderUpdateEvents poll = renderUpdateEvents.poll();
			if (poll != null) {
				switch (poll.getType()) {
					case SCENE -> {
						gameObjects.forEach((uuid, sceneGraph) -> {
							for (SceneGraphNode child : sceneGraph.getSceneGraphNodeData().getChildren()) {
								findAndEdit(child, poll.getData().getText());
							}
						});
					}
					default -> {
						System.out.println("Wut?");
					}
				}
			}
		}
	}

	private void findAndEdit(SceneGraphNode child, String text) {
		for (SceneGraphNode sceneGraphNode : child.getSceneGraphNodeData().getChildren()) {
			if (sceneGraphNode instanceof MeshSceneGraph) {
				MeshSceneGraph meshSceneGraph = (MeshSceneGraph) sceneGraphNode;
				if (meshSceneGraph.getMeshObject() instanceof TextItem textItem) {
					textItem.changeText(text);
				} else {
					findAndEdit(sceneGraphNode, text);
				}
			}
		}
	}

	private void stop() {
		try {
			window.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void handle(Event<?> event) {

		if (event instanceof RenderManagementEvents renderManagementEvents) {
			switch (renderManagementEvents.getType()) {
				case START -> init((RenderManagementInitData) renderManagementEvents.getData());
			}
		}

	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return supports.contains(aClass);
	}
}
