package com.lk.lankabell.android.activity.tsr.sqlite;

public class Serials {
	public Serials(Integer startSerial, Integer endSerial) {
		super();
		this.startSerial = startSerial;
		this.endSerial = endSerial;
	}

	public Serials(Integer startSerial, Integer endSerial, Integer nextSerial) {
		super();
		this.startSerial = startSerial;
		this.endSerial = endSerial;
		this.nextSerial = nextSerial;
	}

	private Integer startSerial;
	private Integer endSerial;
	private Integer nextSerial;

	public Integer getStartSerial() {
		return startSerial;
	}

	public void setStartSerial(Integer startSerial) {
		this.startSerial = startSerial;
	}

	public Integer getEndSerial() {
		return endSerial;
	}

	public void setEndSerial(Integer endSerial) {
		this.endSerial = endSerial;
	}

	public Integer getNextSerial() {
		return nextSerial;
	}

	public void setNextSerial(Integer nextSerial) {
		this.nextSerial = nextSerial;
	}

}
