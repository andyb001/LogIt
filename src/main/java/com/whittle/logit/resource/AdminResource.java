package com.whittle.logit.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.whittle.logit.dao.ItemDAO;
import com.whittle.logit.dto.ItemDTO;
import com.whittle.logit.dto.ItemTypeDTO;
import com.whittle.logit.exception.LogItException;

import io.swagger.annotations.Api;

@RestController
@Api(value = "/admin")
@RequestMapping(path = "/admin")
public class AdminResource {
	
	@Autowired
	private ItemDAO itemDAO;

	public AdminResource() {
		
	}
	
	@RequestMapping(path = "hello-world", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public String getHelloWorld() {
		return "Hello World.";
	}
	
	@RequestMapping(path = "save-item", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemDTO saveItemDTO(@RequestBody ItemDTO itemDTO) throws LogItException {
		return itemDAO.saveItemDTO(itemDTO);
	}
	
	@RequestMapping(path = "delete-item", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void deleteItemDTO(@RequestBody ItemDTO itemDTO) throws LogItException {
		itemDAO.deleteItemDTO(itemDTO);
	}
	
	@RequestMapping(path = "get-all-items", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ItemDTO> getAllItemDTOs() throws LogItException {
		return itemDAO.getAllItemDTOs();
	}
	
	@RequestMapping(path = "get-items-by-type/{typeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ItemDTO> getItemDTOsByType(@PathVariable(value = "type") String typeId) throws LogItException {
		return itemDAO.getItemDTOsByType(typeId);
	}
	
	@RequestMapping(path = "get-item-types", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ItemTypeDTO> getItemTypes() throws LogItException {
		return itemDAO.getItemTypes();
	}
	
	@RequestMapping(path = "save-item-type", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveItemDTO(@RequestBody ItemTypeDTO itemTypeDTO) throws LogItException {
		itemDAO.saveItemTypeDTO(itemTypeDTO);
	}
	
	@RequestMapping(path = "delete-item-type", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void deleteItemTypeDTO(@RequestBody ItemTypeDTO itemTypeDTO) throws LogItException {
		itemDAO.deleteItemTypeDTO(itemTypeDTO);
	}
}
