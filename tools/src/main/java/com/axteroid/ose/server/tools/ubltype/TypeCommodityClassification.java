package com.axteroid.ose.server.tools.ubltype;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)

public class TypeCommodityClassification {

    protected String natureCode;
    protected String cargoTypeCode;
    protected String commodityCode;
    protected String itemClassificationCode;
    
	public String getNatureCode() {
		return natureCode;
	}
	public void setNatureCode(String natureCode) {
		this.natureCode = natureCode;
	}
	public String getCargoTypeCode() {
		return cargoTypeCode;
	}
	public void setCargoTypeCode(String cargoTypeCode) {
		this.cargoTypeCode = cargoTypeCode;
	}
	public String getCommodityCode() {
		return commodityCode;
	}
	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}
	public String getItemClassificationCode() {
		return itemClassificationCode;
	}
	public void setItemClassificationCode(String itemClassificationCode) {
		this.itemClassificationCode = itemClassificationCode;
	}
    
}
