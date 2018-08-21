package com.whittle.logit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whittle.logit.dto.ItemDTO;

public class Harness {

	public Harness() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		
		
		ObjectMapper mapper = new ObjectMapper();
		ItemDTO itemDTO = new ItemDTO();
		itemDTO.setCost(20.00);
		itemDTO.setImageFileName("ImageName.jpg");
		itemDTO.setDescription("This is my first one!");
		itemDTO.setImageFile(Files.readAllBytes(new File("C:\\Users\\ldev077\\workspace-july-2018\\LogIt\\src\\main\\resources\\Tulips.jpg").toPath()));
		String jsonInString = mapper.writeValueAsString(itemDTO);
		System.out.println(jsonInString);
		
		
		URL url = new URL("http://localhost:8085/admin/save-item");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        os.write(jsonInString.getBytes("UTF-8"));
        os.close();

        // read the response
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String text = null;
        try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            text = scanner.useDelimiter("\\A").next();
        }
        
        System.out.println(text);

        in.close();
        conn.disconnect();
	}

}
