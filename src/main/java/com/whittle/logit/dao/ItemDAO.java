package com.whittle.logit.dao;

import java.util.List;

import com.whittle.logit.dto.ItemDTO;
import com.whittle.logit.dto.ItemTypeDTO;
import com.whittle.logit.exception.LogItException;

public interface ItemDAO {

	ItemDTO saveItemDTO(ItemDTO itemDTO) throws LogItException;

	List<ItemDTO> getAllItemDTOs() throws LogItException;

	List<ItemTypeDTO> getItemTypes() throws LogItException;
	
	void saveItemTypes(List<ItemTypeDTO> itemTypes) throws LogItException;

	List<ItemDTO> getItemDTOsByType(String typeId) throws LogItException;

	void saveItemTypeDTO(ItemTypeDTO itemTypeDTO) throws LogItException;

	void deleteItemTypeDTO(ItemTypeDTO itemTypeDTO) throws LogItException;

	void deleteItemDTO(ItemDTO itemDTO) throws LogItException;

}
