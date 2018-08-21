package com.whittle.logit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogItApplication {
	private static final String LOCAL_PORT = "3129";
	private static final String LOCALHOST = "localhost";
	
	public static void main(String[] args) {
		
		if ("Windows 7".equals(System.getProperty("os.name"))) {
			System.setProperty("http.proxyHost", LOCALHOST);
			System.setProperty("http.proxyPort", LOCAL_PORT);
			System.setProperty("https.proxyHost", LOCALHOST);
			System.setProperty("https.proxyPort", LOCAL_PORT);
		}
		LogItConfiguration.getInstance();
		SpringApplication.run(LogItApplication.class, args);
	}
	
}
