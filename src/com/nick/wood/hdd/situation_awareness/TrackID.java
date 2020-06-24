package com.nick.wood.hdd.situation_awareness;

public class TrackID {

	private final long source;
	private final long id;

	public TrackID(long source, long id) {
		this.source = source;
		this.id = id;
	}

	public long getSource() {
		return source;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "TrackID:" + source +
				", " + id;
	}
}
