package com.whittle.logit.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ItemDTO {
	
	private String id;
	private String name;
	private String description;
	private String itemTypeId;
	private String imageUrl;
	private Date purchaseDate;
	private Double cost;
	private byte[] imageFile;
	private byte[] barCodeImageFile;
	private String imageFileStrBytes;
	private String barCodeImageFileStrBytes;
	private String imageFileName;
	private String barCode;
	
	public ItemDTO() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getItemTypeId() {
		return itemTypeId;
	}

	public void setItemTypeId(String itemTypeId) {
		this.itemTypeId = itemTypeId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public byte[] getImageFile() {
		return imageFile;
	}

	public void setImageFile(byte[] imageFile) {
		this.imageFile = imageFile;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public byte[] getBarCodeImageFile() {
		return barCodeImageFile;
	}

	public void setBarCodeImageFile(byte[] barCodeImageFile) {
		this.barCodeImageFile = barCodeImageFile;
	}

	public String getImageFileStrBytes() {
		return imageFileStrBytes;
	}

	public void setImageFileStrBytes(String imageFileStrBytes) {
		this.imageFileStrBytes = imageFileStrBytes;
	}

	public String getBarCodeImageFileStrBytes() {
		return barCodeImageFileStrBytes;
	}

	public void setBarCodeImageFileStrBytes(String barCodeImageFileStrBytes) {
		this.barCodeImageFileStrBytes = barCodeImageFileStrBytes;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

}
