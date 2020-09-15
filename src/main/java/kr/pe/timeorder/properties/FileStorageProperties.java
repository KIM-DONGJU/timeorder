package kr.pe.timeorder.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="file")
public class FileStorageProperties {
	private String uploadDir;
	
	public String getUploadDir() {
		return this.uploadDir;
	}
	
	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}
}
