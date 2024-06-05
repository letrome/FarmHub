package farmhub.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import farmhub.controller.models.InputFarmModel;
import farmhub.repository.models.FarmRepositoryModel;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;

@Repository
public class FarmRepository {
    private static final String FILE_PATH = "resources/db.txt";

    private FarmRepositoryModel farmRepositoryModel;

    private final Logger logger;

    public FarmRepository() {
        this.logger = LoggerFactory.getLogger(FarmRepository.class);
        loadDataFromFile();
    }

    public FarmRepositoryModel getFarm() {
        return farmRepositoryModel;
    }

    public FarmRepositoryModel updateFarm(InputFarmModel inputFarmModel) {
        this.farmRepositoryModel = FarmRepositoryModel.builder()
                .farmName(inputFarmModel.getFarmName())
                .beginningDate(inputFarmModel.getBeginningDate())
                .build();
        saveDataToFile();
        return this.farmRepositoryModel;
    }

    public FarmRepositoryModel patchFarm(ObjectNode node) {
        if (node.has("farm_name"))
            this.farmRepositoryModel.setFarmName(node.get("farm_name").textValue());
        if (node.has("beginning_date"))
            this.farmRepositoryModel.setBeginningDate(node.get("beginning_date").textValue());

        saveDataToFile();

        return this.farmRepositoryModel;
    }

    public void reset(){
        loadDefaultData();
        saveDataToFile();
    }

    @SneakyThrows
    private void loadDataFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.farmRepositoryModel = objectMapper.readValue(new File(FILE_PATH), FarmRepositoryModel.class);
        } catch (IOException e) {
            logger.error("Error when loading data from file", e);
        }

        if (this.farmRepositoryModel == null) {
            logger.info("Loading default data");
            loadDefaultData();
            saveDataToFile();
        } else {
            logger.info("Data successfully loaded");
        }
    }

    private void loadDefaultData() {
        this.farmRepositoryModel = FarmRepositoryModel.builder()
                .farmName("Farmville")
                .beginningDate("2009-06-19")
                .build();
    }

    private void saveDataToFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(FILE_PATH), this.farmRepositoryModel);
        } catch (IOException e) {
            logger.error("Error when saving data to file", e);
        }
    }
}
