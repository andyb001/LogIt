package com.whittle.logit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.whittle.logit.dto.ItemDTO;
import com.whittle.logit.dto.ItemDTOList;

public class MarshallTest {

	public MarshallTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		
		try {
			JAXBContext context = JAXBContext.newInstance(ItemDTOList.class);
			Marshaller marshaller = context.createMarshaller();
			ItemDTOList itemDTOList = new ItemDTOList();
			ItemDTO itemDTO = new ItemDTO();
			
			byte[] fileContent = Files.readAllBytes(new File("C:\\Users\\ldev077\\Downloads\\Fusion Failed Orders.png").toPath());
			itemDTO.setImageFile(fileContent);
			itemDTO.setImageFileName("Fusion Failed Orders.png");
			itemDTO.setCost(25.99);
			itemDTO.setId("5676-eaddfec-5676526765765");
			itemDTO.setName("My best Picture");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
