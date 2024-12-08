package com.switchmaker.smpay.wave_ci;

public class EventObject {
	private String id;
	private String type;
	private EventEntity data;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public EventEntity getData() {
		return data;
	}
	public void setData(EventEntity data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "EventObject [id=" + id + ", type=" + type + ", data=" + data + "]";
	}
	
}
