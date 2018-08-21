package com.whittle.logit.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "item-types")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemTypeDTOList {
	
	@XmlElement(name = "item-type")
    private List<ItemTypeDTO> list = new ArrayList<>();

	public ItemTypeDTOList() {
		
	}

	public List<ItemTypeDTO> getItemTypeDTOs() {
		return list;
	}

	public void setItemTypeDTOs(List<ItemTypeDTO> list) {
		this.list = list;
	}

}
