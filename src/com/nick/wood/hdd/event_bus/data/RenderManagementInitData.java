package com.nick.wood.hdd.event_bus.data;

import com.nick.wood.graphics_library.WindowInitialisationParameters;
import com.nick.wood.graphics_library.objects.game_objects.RootObject;
import com.nick.wood.hdd.event_bus.interfaces.RenderManagementData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RenderManagementInitData implements RenderManagementData {

	private final ArrayList<RootObject> gameObjects;
	private final ArrayList<RootObject> hudObjects;
	private final UUID cameraUUID;
	private final WindowInitialisationParameters windowInitialisationParameters;

	public RenderManagementInitData(ArrayList<RootObject> gameObjects,
	                                ArrayList<RootObject> hudObjects,
	                                UUID cameraUUID,
	                                WindowInitialisationParameters windowInitialisationParameters) {

		this.gameObjects = gameObjects;
		this.hudObjects = hudObjects;
		this.cameraUUID = cameraUUID;
		this.windowInitialisationParameters = windowInitialisationParameters;

	}

	public ArrayList<RootObject> getGameObjects() {
		return gameObjects;
	}

	public ArrayList<RootObject> getHudObjects() {
		return hudObjects;
	}

	public UUID getCameraUUID() {
		return cameraUUID;
	}

	public WindowInitialisationParameters getWindowInitialisationParameters() {
		return windowInitialisationParameters;
	}
}
