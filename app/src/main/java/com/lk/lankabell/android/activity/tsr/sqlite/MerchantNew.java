package com.lk.lankabell.android.activity.tsr.sqlite;


public class MerchantNew {

	private Long id;
	private String name;
	private String address;
	private String reloadNo;
	private Double latitude;
	private Double longitude;
	private String registeredDate;
	private Integer registeredBy;
	private String telephoneNo;
	private String city;
	private Integer isUpdate;
	private Integer isAssigned;
	private Integer isEdited;
	private Integer isRegistered;
	private Integer isActive;

	
	
	
	public MerchantNew(Long id, String name, String address,String reloadNo, Double latitude, Double longitude,String registeredDate, Integer registeredBy, String telephoneNo,String city, Integer isAssigned,Integer isEdited, Integer isRegistered, Integer isActive, Integer isUpdate) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.reloadNo = reloadNo;
		this.latitude = latitude;
		this.longitude = longitude;
		this.registeredDate = registeredDate;
		this.registeredBy = registeredBy;
		this.telephoneNo = telephoneNo;
		this.city = city;
		this.isAssigned = isAssigned;
		this.isEdited = isEdited;
		this.isRegistered = isRegistered;
		this.isActive = isActive;
		this.isUpdate = isUpdate;		
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReloadNo() {
		return reloadNo;
	}

	public void setReloadNo(String reloadNo) {
		this.reloadNo = reloadNo;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(String registeredDate) {
		this.registeredDate = registeredDate;
	}

	public Integer getRegisteredBy() {
		return registeredBy;
	}

	public void setRegisteredBy(Integer registeredBy) {
		this.registeredBy = registeredBy;
	}

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getIsAssigned() {
		return isAssigned;
	}

	public void setIsAssigned(Integer isAssigned) {
		this.isAssigned = isAssigned;
	}

	public Integer getIsEdited() {
		return isEdited;
	}

	public void setIsEdited(Integer isEdited) {
		this.isEdited = isEdited;
	}

	public Integer getIsRegistered() {
		return isRegistered;
	}

	public void setIsRegistered(Integer isRegistered) {
		this.isRegistered = isRegistered;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(Integer isUpdate) {
		this.isUpdate = isUpdate;
	}

}