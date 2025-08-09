package biojj.managerproductapi.config;

import biojj.managerproductapi.service.DBService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdConfig {

    private final DBService dbService;

    public ProdConfig(DBService dbService) {
        this.dbService = dbService;
    }

    @PostConstruct
    public void init() {
        dbService.instantiateDatabase();
    }
}