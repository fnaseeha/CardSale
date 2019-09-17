package com.lk.lankabell.android.activity.tsr.sqlite;

public class FaultTickets {

	String teamCode;
	String FaultId;
	String ActionTakenCode;
	int signalStrength;
	int MeterReading;
	String vehicleNo;
	
	public FaultTickets(String teamCode,String FaultId,String ActionTakenCode,int signalStrength,int MeterReading,String vehicleNo) {
		super();
		this.teamCode = teamCode;
		this.FaultId = FaultId;
		this.ActionTakenCode = ActionTakenCode;
		this.signalStrength = signalStrength;
		this.MeterReading = MeterReading;
		this.vehicleNo = vehicleNo;
	}
	
	public String GetTeamCode() {
		return teamCode;
	}
	
	public String GetFaultId() {
		return FaultId;
	}
	
	public String GetActionTakenCode() {
		return ActionTakenCode;
	}
	
	public int GetsignalStrength() {
		return signalStrength;
	}
	
	public int GetMeterReading() {
		return MeterReading;
	}
	
	public String GetvehicleNo() {
		return vehicleNo;
	}
}
