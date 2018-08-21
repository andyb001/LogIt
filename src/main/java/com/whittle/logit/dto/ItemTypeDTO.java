package com.whittle.logit.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ItemTypeDTO {
	
	String id;
	String name;
	String description;
	
	public ItemTypeDTO() {
		
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

}
