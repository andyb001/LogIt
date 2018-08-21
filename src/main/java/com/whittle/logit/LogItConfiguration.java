package com.whittle.logit;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.whittle.logit.dto.ItemDTO;
import com.whittle.logit.dto.ItemDTOList;
import com.whittle.logit.dto.ItemTypeDTO;
import com.whittle.logit.dto.ItemTypeDTOList;

public class LogItConfiguration {

	private static LogItConfiguration logItConfiguration;
	private static final String USER_HOME = System.getProperty("user.home");
	private static final String LOGIT_DIR = USER_HOME + File.separator + ".LogIt";
	private static final String LOGIT_ITEM_TYPES = LOGIT_DIR + File.separator + "item_types.xml";
	private static final String LOGIT_ALL_ITEMS = LOGIT_DIR + File.separator + "all_items.xml";
	private List<ItemTypeDTO> itemTypes;
	private List<ItemDTO> allItems;

	private LogItConfiguration() {
		try {
			initialise();
		} catch (IOException | JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initialise() throws IOException, JAXBException {
		File logItDirectory = new File(LOGIT_DIR);
		if (!logItDirectory.exists()) {
			logItDirectory.mkdirs();
		}

		File itemTypesFile = new File(LOGIT_ITEM_TYPES);
		if (!itemTypesFile.exists()) {
			createDefaultItemTypes();
		} else {
			loadItemTypes();
		}
		
		File allItemsFile = new File(LOGIT_ALL_ITEMS);
		if (allItemsFile.exists()) {
			loadAllAitems();
		}
	}

	private void createDefaultItemTypes() throws IOException, JAXBException {
		itemTypes = new ArrayList<>();
		ItemTypeDTO itemTypeDTO = new ItemTypeDTO();
		itemTypeDTO.setName("Book");
		itemTypeDTO.setDescription("Any book type object.");
		itemTypeDTO.setId(generateUniqueKeyUsingUUID());
		itemTypes.add(itemTypeDTO);
		itemTypeDTO = new ItemTypeDTO();
		itemTypeDTO.setName("Furniture");
		itemTypeDTO.setDescription("Any item of furniture.");
		itemTypeDTO.setId(generateUniqueKeyUsingUUID());
		itemTypes.add(itemTypeDTO);
		itemTypeDTO = new ItemTypeDTO();
		itemTypeDTO.setName("Appliance");
		itemTypeDTO.setDescription("Any household appliance.");
		itemTypeDTO.setId(generateUniqueKeyUsingUUID());
		itemTypes.add(itemTypeDTO);
		saveItemTypes(itemTypes);
	}

	private void saveItemTypes(List<ItemTypeDTO> itemTypes2) throws IOException, JAXBException {
		JAXBContext context = JAXBContext.newInstance(ItemTypeDTOList.class);
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		ItemTypeDTOList itemTypeDTOList = new ItemTypeDTOList();
		itemTypeDTOList.setItemTypeDTOs(itemTypes);
		marshaller.marshal(itemTypeDTOList, sw);
		FileWriter fileWriter = new FileWriter(LOGIT_ITEM_TYPES);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.print(sw.toString());
		printWriter.close();
	}

	private void loadItemTypes() throws JAXBException, IOException {
		String xml = readFile(LOGIT_ITEM_TYPES, Charset.defaultCharset());
		JAXBContext context = JAXBContext.newInstance(ItemTypeDTOList.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ItemTypeDTOList itemTypeDTOList = (ItemTypeDTOList) unmarshaller
				.unmarshal(new ByteArrayInputStream(xml.getBytes()));
		itemTypes = itemTypeDTOList.getItemTypeDTOs();
		System.out.println("ItemTypes: " + itemTypes);
	}
	
	private void loadAllAitems() throws IOException, JAXBException {
		String xml = readFile(LOGIT_ALL_ITEMS, Charset.defaultCharset());
		JAXBContext context = JAXBContext.newInstance(ItemDTOList.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ItemDTOList itemDTOList = (ItemDTOList) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
		allItems = itemDTOList.getItemDTOs();
	}
	
	public void saveAllItems() throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(ItemDTOList.class);
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		ItemDTOList itemDTOList = new ItemDTOList();
		if (allItems == null) {
			allItems = new ArrayList<>();
		}
		itemDTOList.setItemDTOs(allItems);
		marshaller.marshal(itemDTOList, sw);
		FileWriter fileWriter = new FileWriter(LOGIT_ALL_ITEMS);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.print(sw.toString());
		printWriter.close();
	}

	public static LogItConfiguration getInstance() {
		if (logItConfiguration == null) {
			logItConfiguration = new LogItConfiguration();
		}
		return logItConfiguration;
	}

	public static String generateUniqueKeyUsingUUID() {
		return UUID.randomUUID().toString();
	}

	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public List<ItemTypeDTO> getItemTypes() {
		return itemTypes;
	}

	public void setItemTypes(List<ItemTypeDTO> itemTypes) {
		this.itemTypes = itemTypes;
	}

	public List<ItemDTO> getAllItems() {
		return allItems;
	}

	public void setAllItems(List<ItemDTO> allItems) {
		this.allItems = allItems;
	}

	
}
