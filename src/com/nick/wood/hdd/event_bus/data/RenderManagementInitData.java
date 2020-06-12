package com.nick.wood.hdd.event_bus.data;

import com.nick.wood.graphics_library.WindowInitialisationParameters;
import com.nick.wood.graphics_library.objects.scene_graph_objects.SceneGraph;
import com.nick.wood.hdd.event_bus.interfaces.RenderManagementData;

import java.util.HashMap;
import java.util.UUID;

public class RenderManagementInitData implements RenderManagementData {

	private final HashMap<UUID, SceneGraph> gameObjects;
	private final HashMap<UUID, SceneGraph> hudObjects;
	private final UUID cameraUUID;
	private final WindowInitialisationParameters windowInitialisationParameters;

	public RenderManagementInitData(HashMap<UUID, SceneGraph> gameObjects,
	                                HashMap<UUID, SceneGraph> hudObjects,
	                                UUID cameraUUID,
	                                WindowInitialisationParameters windowInitialisationParameters) {

		this.gameObjects = gameObjects;
		this.hudObjects = hudObjects;
		this.cameraUUID = cameraUUID;
		this.windowInitialisationParameters = windowInitialisationParameters;

	}

	public HashMap<UUID, SceneGraph> getGameObjects() {
		return gameObjects;
	}

	public HashMap<UUID, SceneGraph> getHudObjects() {
		return hudObjects;
	}

	public UUID getCameraUUID() {
		return cameraUUID;
	}

	public WindowInitialisationParameters getWindowInitialisationParameters() {
		return windowInitialisationParameters;
	}
}
