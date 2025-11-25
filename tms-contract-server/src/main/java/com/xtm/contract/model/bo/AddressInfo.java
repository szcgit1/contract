package com.xtm.contract.model.bo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class AddressInfo implements Serializable
{
	/**
	 * 
	 */
	@ApiModelProperty(value = "") private static final long serialVersionUID = -8657159942841802215L;
	@ApiModelProperty(value = "地址ID") private String addressID;
	@ApiModelProperty(value = "地址") private String address;

	@ApiModelProperty(value = "城市编号") private Integer cityCode;
	@ApiModelProperty(value = "城市") private String cityName;
	@ApiModelProperty(value = "城市简称") private String cityShortName;
    @ApiModelProperty(value = "距离") private Double realDistance;
	//经纬度  纬度
	@ApiModelProperty(value = "经度") private String lon;
	@ApiModelProperty(value = "纬度") private String lat;
	//省、区
	private String provincesName;
	private String districtsName;

	public Integer getCityCode()
	{
		return cityCode;
	}

	public void setCityCode(Integer cityCode)
	{
		this.cityCode = cityCode;
	}

	public String getCityName()
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public String getCityShortName()
	{
		return cityShortName;
	}

	public void setCityShortName(String cityShortName)
	{
		this.cityShortName = cityShortName;
	}

	public String getAddressID()
	{
		return addressID;
	}

	public void setAddressID(String addressID)
	{
		this.addressID = addressID;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

    public Double getRealDistance() {
        return realDistance;
    }

    public void setRealDistance(Double realDistance) {
        this.realDistance = realDistance;
    }

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getProvincesName() {
		return provincesName;
	}

	public void setProvincesName(String provincesName) {
		this.provincesName = provincesName;
	}

	public String getDistrictsName() {
		return districtsName;
	}

	public void setDistrictsName(String districtsName) {
		this.districtsName = districtsName;
	}
}
