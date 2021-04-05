package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataJpaApplication.class, args);
    }

    //등록자, 수정자 구현
    @Bean
    public AuditorAware<String> auditorProvider() {
        //인터페이스에 메소드가 하나면 람다로 바꿀수 있다(getCurrentAuditor() 메소드를 오버라이딩했다)
        //세션정보를 가져와서 그 아이디를 UUID.randowUUID().toString()에다가 넣어주면 된다)
        //여기선 그냥 예시로 구현
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
