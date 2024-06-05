package farmhub;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class FarmProperties {

    @Value("${farmers.host}")
    private String farmersHost;

    @Value("${animals.host}")
    private String animalsHost;

    @Value("${pictures.host}")
    private String picturesHost;

    @Value("${species.host}")
    private String speciesHost;
}