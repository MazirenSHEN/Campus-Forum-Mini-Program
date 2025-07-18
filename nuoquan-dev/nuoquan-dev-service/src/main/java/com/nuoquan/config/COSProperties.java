package com.nuoquan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * COS properties
 *
 * @author linux_china
 */
@Configuration
@ConfigurationProperties(
        prefix = "spring.cos"
)
public class COSProperties {
    /**
     * secretID
     */
    private String secretId;
    /**
     * secret key
     */
    private String secretKey;
    /**
     * bucket name
     */
    private String bucketName;
    /**
     * region
     */
    private String region;
    /**
     * 根目录
     * @return
     */
    private String rootDir;
    /**
     * 域名
     */
    private String domain;

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}
