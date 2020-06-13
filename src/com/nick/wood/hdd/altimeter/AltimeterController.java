package com.nick.wood.hdd.altimeter;

import com.nick.wood.graphics_library.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshType;
import com.nick.wood.graphics_library.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.hdd.event_bus.busses.RenderBus;
import com.nick.wood.hdd.event_bus.data.RenderUpdateData;
import com.nick.wood.hdd.event_bus.event_types.RenderUpdateEventType;
import com.nick.wood.hdd.event_bus.events.AltimeterChangeEvent;
import com.nick.wood.hdd.event_bus.events.RenderUpdateEvents;
import com.nick.wood.hdd.event_bus.interfaces.Event;
import com.nick.wood.hdd.event_bus.interfaces.Subscribable;
import com.nick.wood.maths.objects.QuaternionF;
import com.nick.wood.maths.objects.srt.Transform;
import com.nick.wood.maths.objects.srt.TransformBuilder;
import com.nick.wood.maths.objects.vector.Vec3f;

import java.util.HashSet;
import java.util.Set;

public class AltimeterController implements Subscribable {

	private final Set<Class<?>> supports = new HashSet<>();
	private final AltimeterView altimeterView;
	private final RenderBus renderBus;

	public AltimeterController(AltimeterView altimeterView, RenderBus renderBus) {

		supports.add(AltimeterChangeEvent.class);

		this.altimeterView = altimeterView;
		this.renderBus = renderBus;

	}

	@Override
	public void handle(Event<?> event) {

	}

	@Override
	public boolean supports(Class<? extends Event> aClass) {
		return supports.contains(aClass);
	}
}
