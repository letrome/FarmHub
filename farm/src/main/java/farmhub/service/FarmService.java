package farmhub.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import farmhub.Mapper;
import farmhub.clients.AnimalsClient;
import farmhub.clients.FarmersClient;
import farmhub.clients.PicturesClient;
import farmhub.clients.SpeciesClient;
import farmhub.clients.model.AnimalClientModel;
import farmhub.clients.model.FarmerClientModel;
import farmhub.clients.model.PictureClientModel;
import farmhub.clients.model.SpecieClientModel;
import farmhub.controller.models.InputFarmModel;
import farmhub.repository.FarmRepository;
import farmhub.repository.models.FarmRepositoryModel;
import farmhub.service.models.FarmModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmService {
    private final FarmRepository repository;
    private final FarmersClient farmersClient;
    private final AnimalsClient animalsClient;
    private final SpeciesClient speciesClient;
    private final PicturesClient picturesClient;

    @Autowired
    public FarmService(FarmRepository repository,
                       FarmersClient farmersClient,
                       AnimalsClient animalsClient,
                       SpeciesClient speciesClient,
                       PicturesClient picturesClient
    ) {
        this.repository = repository;
        this.farmersClient = farmersClient;
        this.animalsClient = animalsClient;
        this.speciesClient = speciesClient;
        this.picturesClient = picturesClient;
    }

    public FarmModel getFarm(boolean includeGone) {
        FarmRepositoryModel farmRepositoryModel = repository.getFarm();

        List<FarmerClientModel> farmerClientModels = farmersClient.getAllFarmers();
        List<AnimalClientModel> animalClientModels = animalsClient.getAllAnimals();
        List<PictureClientModel> pictureClientModels = picturesClient.getAllPictures();
        List<SpecieClientModel> specieClientModels = speciesClient.getAllSpecies();

        return Mapper.toFarmModel(farmRepositoryModel, animalClientModels, specieClientModels, pictureClientModels, farmerClientModels, includeGone);
    }

    public FarmModel updateFarm(InputFarmModel inputFarmModel) {
        FarmRepositoryModel farmRepositoryModel = repository.updateFarm(inputFarmModel);

        return getFarmModel(farmRepositoryModel);
    }

    public FarmModel patchFarm(JsonNode inputNode) {
        List<String> validKeys = List.of("farm_name", "beginning_date");

        ObjectNode node = new ObjectMapper().createObjectNode();

        inputNode.fields().forEachRemaining(element -> {
            if (validKeys.contains(element.getKey()))
                node.set(element.getKey(), element.getValue());
        });

        FarmRepositoryModel farmRepositoryModel = repository.patchFarm(node);

        return getFarmModel(farmRepositoryModel);
    }

    public void reset() {
        farmersClient.reset();
        animalsClient.reset();
        picturesClient.reset();
        speciesClient.reset();
        repository.reset();
    }

    private FarmModel getFarmModel(FarmRepositoryModel farmRepositoryModel) {
        List<FarmerClientModel> farmerClientModels = farmersClient.getAllFarmers();
        List<AnimalClientModel> animalClientModels = animalsClient.getAllAnimals();
        List<PictureClientModel> pictureClientModels = picturesClient.getAllPictures();
        List<SpecieClientModel> specieClientModels = speciesClient.getAllSpecies();

        return Mapper.toFarmModel(farmRepositoryModel, animalClientModels, specieClientModels, pictureClientModels, farmerClientModels, true);
    }
}
