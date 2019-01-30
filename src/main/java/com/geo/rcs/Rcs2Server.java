package com.geo.rcs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ServletComponentScan
public class Rcs2Server extends SpringBootServletInitializer{


	public static void main(String[] args) {
		/*if(loggingPath == null){
			throw new RcsException("日志路径未配置或配置错误");
		}*/
		ConfigurableApplicationContext run = SpringApplication.run(Rcs2Server.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Rcs2Server.class);
	}



}
