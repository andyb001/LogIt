package com.whittle.logit.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemDTOList {

	@XmlElement(name = "item")
    private List<ItemDTO> list = new ArrayList<>();
	
	public ItemDTOList() {
		
	}

	public List<ItemDTO> getItemDTOs() {
		return list;
	}

	public void setItemDTOs(List<ItemDTO> list) {
		this.list = list;
	}

}
