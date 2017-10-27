package com.caoyong.autoconfig;



import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "autoconfiguration")
/**
 * @author caoyong
 * 2017年10月24日 下午2:53:03
 */
public class CustomerDatasourcePropertiesBean {
	private String packagePath="";
	private String aliasesPackage="";
	private String resourcesPath="";
	public String getResourcesPath() {
		return resourcesPath;
	}
	public void setResourcesPath(String resourcesPath) {
		this.resourcesPath = resourcesPath;
	}
	public String getPackagePath() {
		return packagePath;
	}
	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}
	public String getAliasesPackage() {
		return aliasesPackage;
	}
	public void setAliasesPackage(String aliasesPackage) {
		this.aliasesPackage = aliasesPackage;
	}
	
}
