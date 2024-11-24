package AISL.ADizzi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0") //버전
                .title("ADizzi API") //이름
                .description("AISL ADizzi API"); //설명

        // SecurityScheme 설정
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization");

        // SecurityRequirement 추가
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

        List<Tag> tags = Arrays.asList(
                new Tag().name("회원 관련 API"),
                new Tag().name("방 관련 API"),
                new Tag().name("수납장 관련 API"),
                new Tag().name("수납칸 관련 API"),
                new Tag().name("물건 관련 API"),
                new Tag().name("이미지 관련 API")
        );

        return new OpenAPI().info(info)
                .addSecurityItem(securityRequirement)
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("BearerAuth", securityScheme))
                .tags(tags); // 태그 추가
    }
}
