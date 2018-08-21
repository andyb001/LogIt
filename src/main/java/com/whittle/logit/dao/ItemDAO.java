package com.whittle.logit.dao;

import java.util.List;

import com.whittle.logit.dto.ItemDTO;
import com.whittle.logit.exception.LogItException;

public interface ItemDAO {

	ItemDTO saveItemDTO(ItemDTO itemDTO) throws LogItException;

	List<ItemDTO> getAllItemDTOs() throws LogItException;

}
