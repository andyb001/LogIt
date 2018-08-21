package com.whittle.logit.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Repository;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.whittle.logit.LogItConfiguration;
import com.whittle.logit.dao.ItemDAO;
import com.whittle.logit.dto.ItemDTO;
import com.whittle.logit.dto.ItemTypeDTO;
import com.whittle.logit.dto.ItemTypeDTOList;
import com.whittle.logit.exception.LogItException;

@Repository
public class ItemDAOImpl implements ItemDAO {

	private static final String ITEM_TYPE_DT_OS_XML = "itemTypeDTOs.xml";
	private static final String IMAGE_JPEG = "image/jpeg";
	private static final String INVENTORY_IMAGES = "Inventory Images";
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = System.getProperty("user.home") + java.io.File.separator
			+ "tokens";

	public ItemDAOImpl() {

	}

	@Override
	public ItemDTO saveItemDTO(ItemDTO itemDTO) throws LogItException {
		

		String uniqueId = LogItConfiguration.generateUniqueKeyUsingUUID();
		itemDTO.setId(uniqueId);

		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

			Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME).build();

			File inventoryImagesFolder = null;
			
			List<File> inventoryFolders = getGoogleSubFolderByName(driveService, null, INVENTORY_IMAGES);
			if (inventoryFolders == null || inventoryFolders.isEmpty()) {
				inventoryImagesFolder = createGoogleFolder(driveService, null, INVENTORY_IMAGES);
			} else {
				inventoryImagesFolder = inventoryFolders.get(0);
			}

			String first2Chars = uniqueId.substring(0, 2);
			System.out.println(first2Chars);

			List<File> subFolder = getGoogleSubFolderByName(driveService, inventoryImagesFolder.getId(), first2Chars);
			System.out.println("sub: " + subFolder);

			File subFolderFile;
			if (subFolder.isEmpty()) {
				System.out.println("Create: " + first2Chars);
				subFolderFile = createGoogleFolder(driveService, inventoryImagesFolder.getId(), first2Chars);
			} else {
				subFolderFile = subFolder.get(0);
			}

			System.out.println("subFolderFile.getId(): " + subFolderFile.getId());

			File googleImageFile = createGoogleFile(driveService, subFolderFile.getId(), IMAGE_JPEG,
					uniqueId + ".jpg", itemDTO.getImageFile());
			
			createPublicPermission(driveService, googleImageFile.getId());

			System.out.println(googleImageFile.getWebViewLink());
			itemDTO.setImageUrl(googleImageFile.getWebViewLink());
			
			itemDTO.setImageFile(null);
			
			save(itemDTO);
			
		} catch (GeneralSecurityException | IOException e) {
			throw new LogItException(e);
		}
		return itemDTO;
	}

	private void save(ItemDTO itemDTO) throws LogItException {
		LogItConfiguration logItConf = LogItConfiguration.getInstance();
		if (logItConf.getAllItems() == null) {
			logItConf.setAllItems(new ArrayList<>());
		}
		logItConf.getAllItems().add(itemDTO);
		
		try {
			logItConf.saveAllItems();
		} catch (JAXBException | IOException e) {
			throw new LogItException(e);
		}
	}

	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = ItemDAOImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	private static final List<File> getGoogleSubFolderByName(Drive driveService, String googleFolderIdParent,
			String subFolderName) throws IOException {

		String pageToken = null;
		List<File> list = new ArrayList<>();

		String query = getFolderNameQuery(subFolderName, googleFolderIdParent);
		/*if (googleFolderIdParent == null) {
			query = " name = '" + subFolderName + "' " 
					+ " and mimeType = 'application/vnd.google-apps.folder' " 
					+ " and 'root' in parents";
		} else {
			query = " name = '" + subFolderName + "' " 
					+ " and mimeType = 'application/vnd.google-apps.folder' " 
					+ " and '" + googleFolderIdParent + "' in parents";
		}*/

		do {
			FileList result = driveService.files().list().setQ(query).setSpaces("drive") 
					.setFields("nextPageToken, files(id, name, createdTime)")
					.setPageToken(pageToken).execute();
			for (File file : result.getFiles()) {
				list.add(file);
			}
			pageToken = result.getNextPageToken();
		} while (pageToken != null);
		//
		return list;
	}
	
	private static String getFolderNameQuery(String subFolderName, String googleFolderIdParent) {
		StringBuilder s = new StringBuilder();
		s.append(" name = '");
		s.append(subFolderName);
		s.append(" and mimeType = 'application/vnd.google-apps.folder' ");
		s.append(" and '");
		if (googleFolderIdParent == null) {
			s.append("root");
		} else {
			s.append(googleFolderIdParent);
		}
		s.append("' in parents");
		return s.toString();
	}

	private static final File createGoogleFolder(Drive driveService, String folderIdParent, String folderName)
			throws IOException {

		File fileMetadata = new File();

		fileMetadata.setName(folderName);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");

		if (folderIdParent != null) {
			List<String> parents = Arrays.asList(folderIdParent);
			fileMetadata.setParents(parents);
		}

		// Create a Folder.
		// Returns File object with id & name fields will be assigned values
		return driveService.files().create(fileMetadata).setFields("id, name").execute();
	}

	private static File createGoogleFile(Drive driveService, String googleFolderIdParent, String contentType, 
			String customFileName, byte[] uploadData) throws IOException {
		AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);
		return _createGoogleFile(driveService, googleFolderIdParent, contentType, customFileName, uploadStreamContent);
	}
	
	public static File createGoogleFile(Drive driveService, String googleFolderIdParent, String contentType,
            String customFileName, InputStream inputStream) throws IOException {
        AbstractInputStreamContent uploadStreamContent = new InputStreamContent(contentType, inputStream);
        return _createGoogleFile(driveService, googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

	private static File _createGoogleFile(Drive driveService, String googleFolderIdParent, String contentType, 
			String customFileName, AbstractInputStreamContent uploadStreamContent) throws IOException {

		File fileMetadata = new File();
		fileMetadata.setName(customFileName);

		List<String> parents = Arrays.asList(googleFolderIdParent);
		fileMetadata.setParents(parents);

		File file = driveService.files().create(fileMetadata, uploadStreamContent)
				.setFields("id, webContentLink, webViewLink, parents").execute();

		return file;
	}

	private static Permission createPublicPermission(Drive driveService, String googleFileId) throws IOException {
		// All values: user - group - domain - anyone
		String permissionType = "anyone";
		// All values: organizer - owner - writer - commenter - reader
		String permissionRole = "reader";

		Permission newPermission = new Permission();
		newPermission.setType(permissionType);
		newPermission.setRole(permissionRole);

		return driveService.permissions().create(googleFileId, newPermission).execute();
	}
	
	public static final List<File> getGoogleFilesByName(Drive driveService, String fileNameLike) throws IOException {
        String pageToken = null;
        List<File> list = new ArrayList<File>();
        String query = " name contains '" + fileNameLike + "' " //
                + " and mimeType != 'application/vnd.google-apps.folder' ";
        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    // Fields will be assigned values: id, name, createdTime, mimeType
                    .setFields("nextPageToken, files(id, name, createdTime, mimeType)")//
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }

	@Override
	public List<ItemDTO> getAllItemDTOs() throws LogItException {
		return LogItConfiguration.getInstance().getAllItems();
	}

	@Override
	public List<ItemTypeDTO> getItemTypes() throws LogItException {
		List<File> list = null;
		ItemTypeDTOList itemTypeDTOList = null;
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

			Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME).build();
			
			list = getGoogleFilesByName(driveService, ITEM_TYPE_DT_OS_XML);
			if (list.isEmpty()) {
				return new ArrayList<>();
			}
			File file = list.get(0);
			
			String fileId = file.getId();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            driveService.files().get(fileId).executeMediaAndDownloadTo(out);
            
            //String fileContents = out.toByteArray()
            //System.out.println(fileContents);
            JAXBContext context = JAXBContext.newInstance(ItemTypeDTOList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            itemTypeDTOList = (ItemTypeDTOList) unmarshaller.unmarshal(new ByteArrayInputStream(out.toByteArray()));
			
			
		} catch (GeneralSecurityException | IOException | JAXBException e) {
			throw new LogItException(e);
		}
		
		
		return itemTypeDTOList.getItemTypeDTOs();
	}

	@Override
	public void saveItemTypes(List<ItemTypeDTO> itemTypes) throws LogItException {
		ItemTypeDTOList itemTypeDTOList = new ItemTypeDTOList();
		itemTypeDTOList.setItemTypeDTOs(itemTypes);
		try {
			
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

			Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME).build();
			
			JAXBContext context = JAXBContext.newInstance(ItemTypeDTOList.class);
			Marshaller marshaller = context.createMarshaller();
			StringWriter sw = new StringWriter();
			marshaller.marshal(itemTypeDTOList, sw);
			
			createGoogleFile(driveService, null, "text/xml", ITEM_TYPE_DT_OS_XML, new ByteArrayInputStream(sw.toString().getBytes()));
			
			
		} catch (JAXBException | IOException | GeneralSecurityException e) {
			throw new LogItException(e);
		}
		
	}

	@Override
	public List<ItemDTO> getItemDTOsByType(String typeId) throws LogItException {
		List<ItemDTO> all = LogItConfiguration.getInstance().getAllItems();
		List<ItemDTO> byType = new ArrayList<>();
		for (ItemDTO item : all) {
			if (typeId.equalsIgnoreCase(item.getItemTypeId())) {
				byType.add(item);
			}
		}
		return byType;
	}

	@Override
	public void saveItemTypeDTO(ItemTypeDTO itemTypeDTO) throws LogItException {
		List<ItemTypeDTO> allItemTypes = LogItConfiguration.getInstance().getItemTypes();
		if (itemTypeDTO.getId() == null) {
			itemTypeDTO.setId(LogItConfiguration.generateUniqueKeyUsingUUID());
		}
		allItemTypes.add(itemTypeDTO);
		saveItemTypes(allItemTypes);
	}

	@Override
	public void deleteItemDTO(ItemTypeDTO itemTypeDTO) throws LogItException {
		List<ItemTypeDTO> allItemTypes = LogItConfiguration.getInstance().getItemTypes();
		allItemTypes.remove(itemTypeDTO);
		saveItemTypes(allItemTypes);
	}
}
