package com.nick.wood.hdd.event_bus.subscribables;

import com.nick.wood.graphics_library.Window;
import com.nick.wood.graphics_library.objects.game_objects.GameObject;
import com.nick.wood.hdd.event_bus.data.RenderManagementInitData;
import com.nick.wood.hdd.event_bus.events.RenderManagementEvents;
import com.nick.wood.hdd.event_bus.events.RenderUpdateEvents;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RendererManager implements Subscribable {

	private final Set<Class<?>> supports = new HashSet<>();

	private Window window;

	private final ConcurrentLinkedQueue<RenderUpdateEvents> renderUpdateEvents = new ConcurrentLinkedQueue<>();
	private final ArrayList<GameObject> gameObjects;
	private final ArrayList<GameObject> hudObjects;
	private UUID cameraUUID;

	public RendererManager() {

		this.gameObjects = new ArrayList<>();
		this.hudObjects = new ArrayList<>();
		this.cameraUUID = UUID.randomUUID();

		supports.add(RenderManagementEvents.class);
		supports.add(RenderUpdateEvents.class);
	}


	private void init(RenderManagementInitData data) {

		try {
			this.window = new Window();
			window.init(data.getWindowInitialisationParameters());
			this.gameObjects.addAll(data.getGameObjects());
			this.hudObjects.addAll(data.getHudObjects());
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
					case FUNCTION -> poll.getData().getRunnable().run();
					default -> {
						System.out.println("Wut?");
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

		if (event instanceof RenderManagementEvents renderManagementEvents) {
			switch (renderManagementEvents.getType()) {
				case START -> init((RenderManagementInitData) renderManagementEvents.getData());
			}
		} else if (event instanceof RenderUpdateEvents renderUpdateEvent) {
			renderUpdateEvents.add(renderUpdateEvent);
		}

	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return supports.contains(aClass);
	}
}
