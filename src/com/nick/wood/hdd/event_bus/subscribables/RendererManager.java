package com.nick.wood.hdd.event_bus.subscribables;

import com.nick.wood.graphics_library.Picking;
import com.nick.wood.graphics_library.Window;
import com.nick.wood.graphics_library.objects.game_objects.GameObject;
import com.nick.wood.graphics_library.objects.game_objects.MeshGameObject;
import com.nick.wood.graphics_library.utils.GameObjectUtils;
import com.nick.wood.hdd.event_bus.busses.RenderBus;
import com.nick.wood.hdd.event_bus.data.GameObjectSelectedEventData;
import com.nick.wood.hdd.event_bus.data.RenderManagementInitData;
import com.nick.wood.hdd.event_bus.event_types.GameObjectSelectedEventType;
import com.nick.wood.hdd.event_bus.events.GameObjectSelectedEvent;
import com.nick.wood.hdd.event_bus.events.RenderManagementEvent;
import com.nick.wood.hdd.event_bus.events.RenderUpdateEvent;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RendererManager implements Subscribable {

	private final Set<Class<?>> supports = new HashSet<>();
	private final RenderBus renderBus;
	private Picking picking;

	private Window window;

	private final ConcurrentLinkedQueue<RenderUpdateEvent> renderUpdateEvents = new ConcurrentLinkedQueue<>();
	private final ArrayList<GameObject> gameObjects;
	private final ArrayList<GameObject> hudObjects;
	private UUID cameraUUID;

	public RendererManager(RenderBus renderBus) {

		this.renderBus = renderBus;
		this.gameObjects = new ArrayList<>();
		this.hudObjects = new ArrayList<>();
		this.cameraUUID = UUID.randomUUID();

		supports.add(RenderManagementEvent.class);
		supports.add(RenderUpdateEvent.class);

	}


	private void init(RenderManagementInitData data) {

		try {
			this.window = new Window();
			window.init(data.getWindowInitialisationParameters());
			this.gameObjects.addAll(data.getGameObjects());
			this.hudObjects.addAll(data.getHudObjects());
			this.cameraUUID = data.getCameraUUID();
			// create class to take mouse positions in when mouse clicked and make ray
			if (data.getWindowInitialisationParameters().isPicking()) {
				this.picking = new Picking(window.getGraphicsLibraryInput());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		run();
	}

	private void run() {
		while (!window.shouldClose()) {
			applyUpdates();
			window.loop(gameObjects, hudObjects, cameraUUID);
			if (picking != null) {
				picking.iterate(window.getScene(), window.getHeight()).ifPresent(uuid -> {
					MeshGameObject selectedMeshGameObject = (MeshGameObject) GameObjectUtils.FindGameObjectByID(gameObjects, uuid);
					if (selectedMeshGameObject != null) {
						renderBus.dispatch(new GameObjectSelectedEvent(
								new GameObjectSelectedEventData(selectedMeshGameObject.getGameObjectData().getUuid()),
								GameObjectSelectedEventType.SELECTED
						));
					}
				});
			}
		}
		stop();
	}

	private void applyUpdates() {
		int size = renderUpdateEvents.size();

		for (int i = 0; i < size; i++) {
			RenderUpdateEvent poll = renderUpdateEvents.poll();
			if (poll != null) {
				switch (poll.getType()) {
					case FUNCTION: poll.getData().getRunnable().run();
					break;
					default: {
						System.out.println("Wut?");
						break;
					}
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

		if (event instanceof RenderManagementEvent) {
			RenderManagementEvent renderManagementEvent = (RenderManagementEvent) event;
			switch (renderManagementEvent.getType()) {
				case START: {
					init((RenderManagementInitData) renderManagementEvent.getData());
					break;
				}
			}
		} else if (event instanceof RenderUpdateEvent) {
			RenderUpdateEvent renderUpdateEvent = (RenderUpdateEvent) event;
			renderUpdateEvents.add(renderUpdateEvent);
		}

	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return supports.contains(aClass);
	}
}
