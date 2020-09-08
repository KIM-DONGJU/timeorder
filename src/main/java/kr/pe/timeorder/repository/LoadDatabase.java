package kr.pe.timeorder.repository;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.pe.timeorder.model.Member;
import kr.pe.timeorder.model.Review;
import kr.pe.timeorder.model.Store;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LoadDatabase {
	//private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Bean
	CommandLineRunner initDatabase(MemberRepository repository) {
		return args -> {
			log.info("Preloading " + repository.save(Member.builder().phone("01011112222").author(0).name("이름").pw("zxasqw12!@").build()));
			log.info("Preloading " + repository.save(Member.builder().phone("01000001111").author(2).name("이름이름").pw("zxasqw12!@").build()));
		};
	}
	
}
