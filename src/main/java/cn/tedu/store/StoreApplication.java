package cn.tedu.store;

import javax.servlet.MultipartConfigElement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
@SpringBootApplication
@MapperScan("cn.tedu.store.mapper")
public class StoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}
	
	@Bean
	public MultipartConfigElement getMultipartConfigElement() {
		MultipartConfigFactory factory
			= new MultipartConfigFactory();
		
		DataSize dataSize = DataSize.ofMegabytes(100);
		factory.setMaxFileSize(dataSize);
		factory.setMaxRequestSize(dataSize);
		
		return factory.createMultipartConfig();
	}

}









