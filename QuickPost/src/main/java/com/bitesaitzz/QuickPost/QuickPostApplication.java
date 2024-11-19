package com.bitesaitzz.QuickPost;

import com.bitesaitzz.QuickPost.config.KafkaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication
public class QuickPostApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickPostApplication.class, args);
	}

}
