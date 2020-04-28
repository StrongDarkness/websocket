package cn.qxl;

import cn.qxl.chat.MyWebSocket;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@MapperScan(basePackages = {"cn.qxl.mapper"})
@EnableCaching
@EnableConfigurationProperties
@PropertySource("application.yml")
public class WebsocketApplication {

	public static void main(String[] args) {
		ApplicationContext context=SpringApplication.run(WebsocketApplication.class, args);
		MyWebSocket.setApplicationContext(context);
	}

}
