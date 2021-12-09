package org.starrier.imperator.content.config.profile;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.starrier.imperator.content.component.constant.ProfileConstant;
import org.starrier.imperator.content.component.profile.ConfigSupportProperties;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.starrier.imperator.content.component.constant.ProfileConstant.CONFIG_SERVICE;

/**
 * @author Starrier
 * @date 2019/05/09
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ConfigSupportProperties.class)
public class ConfigSupportConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    private final ConfigSupportProperties configSupportProperties;
    private List<PropertySourceLocator> propertySourceLocators = Collections.EMPTY_LIST;

    public ConfigSupportConfiguration(List<PropertySourceLocator> propertySourceLocators, ConfigSupportProperties configSupportProperties) {
        this.propertySourceLocators = propertySourceLocators;
        this.configSupportProperties = configSupportProperties;
    }


    @Override
    @SneakyThrows(NullPointerException.class)
    public void initialize(@Nullable ConfigurableApplicationContext configurableApplicationContext) {

        if (!isHasCloudConfigLocator(this.propertySourceLocators)) {
            log.info("未启用Config Server管理配置");
            return;
        }
        log.info("检查Config Service配置资源");
        @CheckForNull ConfigurableEnvironment environment = configurableApplicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        log.info("加载PropertySources源：" + propertySources.size() + "个");
        if (!configSupportProperties.isEnable()) {
            log.warn("未启用配置备份功能，可使用{}.enable打开", ProfileConstant.CONFIG_PREFIX);
            return;
        }
        if (isCloudConfigLoaded(propertySources)) {
            PropertySource cloudConfigSource = getLoadedCloudPropertySource(propertySources);
            log.info("成功获取ConfigService配置资源");
            //备份
            Map<String, Object> backupPropertyMap = makeBackupPropertyMap(cloudConfigSource);
            doBackup(backupPropertyMap, configSupportProperties.getFallbackLocation());
        } else {
            log.error("获取ConfigService配置资源失败");
            Properties backupProperty = loadBackupProperty(configSupportProperties.getFallbackLocation());
            if (backupProperty != null) {
                HashMap backupSourceMap = Maps.newHashMap(backupProperty);
                PropertySource backupSource = new MapPropertySource("backupSource", backupSourceMap);
                propertySources.addFirst(backupSource);
                log.warn("使用备份的配置启动：{}", configSupportProperties.getFallbackLocation());
            }
        }

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 11;
    }

    /**
     * 是否启用了Spring Cloud Config获取配置资源
     *
     * @param propertySourceLocators
     * @return
     */
    private boolean isHasCloudConfigLocator(List<PropertySourceLocator> propertySourceLocators) {
        for (PropertySourceLocator sourceLocator : propertySourceLocators) {
            if (sourceLocator instanceof ConfigServicePropertySourceLocator) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否启用Cloud Config
     *
     * @param propertySources
     * @return
     */
    private boolean isCloudConfigLoaded(MutablePropertySources propertySources) {
        return getLoadedCloudPropertySource(propertySources) != null;
    }

    /**
     * 获取加载的Cloud Config 配置项
     *
     * @param propertySources
     * @return
     */
    private PropertySource getLoadedCloudPropertySource(MutablePropertySources propertySources) {
        if (!propertySources.contains(PropertySourceBootstrapConfiguration.BOOTSTRAP_PROPERTY_SOURCE_NAME)) {
            return null;
        }

        PropertySource propertySource = propertySources.get(PropertySourceBootstrapConfiguration.BOOTSTRAP_PROPERTY_SOURCE_NAME);
        if (propertySource instanceof CompositePropertySource) {
            for (PropertySource<?> source : ((CompositePropertySource) propertySource).getPropertySources()) {
                if (CONFIG_SERVICE.equals(source.getName())) {
                    return source;
                }
            }
        }
        return null;
    }


    /**
     * 生成备份的配置数据
     *
     * @param propertySource
     * @return
     */
    private Map<String, Object> makeBackupPropertyMap(PropertySource propertySource) {

        Map<String, Object> backupSourceMap = new HashMap<>();
        if (propertySource instanceof CompositePropertySource) {
            CompositePropertySource composite = (CompositePropertySource) propertySource;
            for (PropertySource<?> source : composite.getPropertySources()) {
                if (source instanceof MapPropertySource) {
                    MapPropertySource mapSource = (MapPropertySource) source;
                    for (String propertyName : mapSource.getPropertyNames()) {
                        // 前面的配置覆盖后面的配置
                        if (!backupSourceMap.containsKey(propertyName)) {
                            backupSourceMap.put(propertyName, mapSource.getProperty(propertyName));
                        }
                    }
                }
            }
        }
        return backupSourceMap;
    }

    /**
     * 生成备份文件
     *
     * @param backupPropertyMap
     * @param filePath
     */
    private void doBackup(Map<String, Object> backupPropertyMap, String filePath) {
        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        File backupFile = fileSystemResource.getFile();
        try {
            if (!backupFile.exists()) {
                backupFile.createNewFile();
            }
            if (!backupFile.canWrite()) {
                log.error("无法读写文件：{}", fileSystemResource.getPath());
            }
            Properties properties = new Properties();
            for (String key : backupPropertyMap.keySet()) {
                properties.setProperty(key, String.valueOf(backupPropertyMap.get(key)));
            }

            FileOutputStream fos = new FileOutputStream(fileSystemResource.getFile());
            properties.store(fos, "Backup Cloud Config");
        } catch (IOException e) {
            log.error("文件操作失败：{}", fileSystemResource.getPath());
        }
    }

    /**
     * 加载本地文件
     *
     * @param filePath
     * @return
     */
    private Properties loadBackupProperty(String filePath) {
        PropertiesFactoryBean propertiesFactory = new PropertiesFactoryBean();
        Properties props = new Properties();
        try {
            FileSystemResource fileSystemResource = new FileSystemResource(filePath);
            propertiesFactory.setLocation(fileSystemResource);
            propertiesFactory.afterPropertiesSet();
            props = propertiesFactory.getObject();
        } catch (IOException e) {
            log.error("load local file error:[{}]", e.getMessage(), e);
            return null;
        }
        return props;
    }
}
