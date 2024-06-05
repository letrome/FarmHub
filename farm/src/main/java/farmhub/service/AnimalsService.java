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
import farmhub.controller.models.InputAnimalModel;
import farmhub.service.models.AnimalModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class AnimalsService {

    private final AnimalsClient animalsClient;
    private final SpeciesClient speciesClient;
    private final PicturesClient picturesClient;
    private final FarmersClient farmersClient;

    @Autowired
    public AnimalsService(AnimalsClient animalsClient,
                          SpeciesClient speciesClient,
                          PicturesClient picturesClient,
                          FarmersClient farmersClient
    ) {
        this.animalsClient = animalsClient;
        this.speciesClient = speciesClient;
        this.picturesClient = picturesClient;
        this.farmersClient = farmersClient;
    }

    public List<AnimalModel> listAnimals(boolean includeGone) {
        List<AnimalClientModel> animalClientModels = animalsClient.getAllAnimals();
        List<SpecieClientModel> specieClientModels = speciesClient.getAllSpecies();
        List<PictureClientModel> pictureClientModels = picturesClient.getAllPictures();
        List<FarmerClientModel> farmerClientModels = farmersClient.getAllFarmers();

        return Mapper.toAnimalModels(animalClientModels, specieClientModels, pictureClientModels, farmerClientModels, includeGone);
    }

    public AnimalModel getAnimalById(String animalId) {
        AnimalClientModel animalClientModel = animalsClient.getAnimalOrNotFound(animalId);

        SpecieClientModel specieClientModel = speciesClient.getSpecieOrNull(animalClientModel.getSpecie());
        FarmerClientModel farmerClientModel = farmersClient.getFarmerOrNull(animalClientModel.getFarmer());
        PictureClientModel pictureClientModel = picturesClient.getPictureOrNull(animalClientModel.getPicture());

        PictureClientModel farmerPictureClientModel = picturesClient.getPictureOrNull(
                farmerClientModel == null
                        ? null
                        : farmerClientModel.getPicture()
        );

        return Mapper.toAnimalModel(animalClientModel, specieClientModel, farmerClientModel, farmerPictureClientModel, pictureClientModel);
    }

    public AnimalModel createAnimal(InputAnimalModel inputAnimalModel) {

        SpecieClientModel specieClientModel = speciesClient.getSpecieOrBadRequest(inputAnimalModel.getSpecie());
        PictureClientModel pictureClientModel = picturesClient.getPictureOrBadRequest(inputAnimalModel.getPicture());
        FarmerClientModel farmerClientModel = farmersClient.getFarmerOrBadRequest(inputAnimalModel.getFarmer());

        AnimalClientModel animalClientModel = animalsClient.createAnimal(inputAnimalModel);

        PictureClientModel farmerPictureClientModel = picturesClient.getPictureOrNull(
                farmerClientModel.getPicture() == null
                        ? null
                        : farmerClientModel.getPicture()
        );

        return Mapper.toAnimalModel(animalClientModel, specieClientModel, farmerClientModel, farmerPictureClientModel, pictureClientModel);
    }

    public AnimalModel updateAnimal(String animalId, InputAnimalModel inputAnimalModel) {
        animalsClient.getAnimalOrNotFound(animalId);

        SpecieClientModel specieClientModel = speciesClient.getSpecieOrBadRequest(inputAnimalModel.getSpecie());
        PictureClientModel pictureClientModel = picturesClient.getPictureOrBadRequest(inputAnimalModel.getPicture());
        FarmerClientModel farmerClientModel = farmersClient.getFarmerOrBadRequest(inputAnimalModel.getFarmer());

        AnimalClientModel responseAnimalClientModel = animalsClient.updateAnimalOrInternalServerError(animalId, inputAnimalModel);

        PictureClientModel farmerPictureClientModel = picturesClient.getPictureOrNull(
                farmerClientModel.getPicture() == null
                        ? null
                        : farmerClientModel.getPicture()
        );

        return Mapper.toAnimalModel(responseAnimalClientModel, specieClientModel, farmerClientModel, farmerPictureClientModel, pictureClientModel);
    }

    public AnimalModel patchAnimal(String animalId, JsonNode inputNode) {
        List<String> validKeys = List.of("name", "birth_date", "specie", "farmer", "status", "picture");

        SpecieClientModel specieClientModel = null;
        PictureClientModel pictureClientModel = null;
        FarmerClientModel farmerClientModel = null;

        ObjectNode node = new ObjectMapper().createObjectNode();

        Iterator<Map.Entry<String, JsonNode>> it = inputNode.fields();
        while(it.hasNext()){
            Map.Entry<String, JsonNode> element = it.next();
            if(!validKeys.contains(element.getKey())){
                continue;
            }

            if("specie".equals(element.getKey())){
                specieClientModel = speciesClient.getSpecieOrBadRequest(element.getValue().textValue());
            }

            if("farmer".equals(element.getKey())){
                farmerClientModel = farmersClient.getFarmerOrBadRequest(element.getValue().textValue());
            }

            if("picture".equals(element.getKey())){
                pictureClientModel = picturesClient.getPictureOrBadRequest(element.getValue().textValue());
            }

            node.set(element.getKey(), element.getValue());
        }

        AnimalClientModel responseAnimalClientModel = animalsClient.patchAnimalOrNotFound(animalId, node);
        if(specieClientModel == null){
            specieClientModel = speciesClient.getSpecieOrInternalServerError(responseAnimalClientModel.getSpecie());
        }

        if(farmerClientModel == null){
            farmerClientModel = farmersClient.getFarmerOrInternalServerError(responseAnimalClientModel.getFarmer());
        }

        if(pictureClientModel == null){
            pictureClientModel = picturesClient.getPictureOrInternalServerError(responseAnimalClientModel.getPicture());
        }

        PictureClientModel farmerPictureClientModel = picturesClient.getPictureOrInternalServerError(farmerClientModel.getPicture());

        return Mapper.toAnimalModel(responseAnimalClientModel, specieClientModel, farmerClientModel, farmerPictureClientModel, pictureClientModel);
    }

    public AnimalModel deleteAnimal(String animalId) {
        AnimalClientModel animalClientModel = animalsClient.deleteAnimalOrNotFound(animalId);

        SpecieClientModel specieClientModel = speciesClient.getSpecieOrNull(animalClientModel.getSpecie());
        PictureClientModel pictureClientModel = picturesClient.getPictureOrNull(animalClientModel.getPicture());
        FarmerClientModel farmerClientModel = farmersClient.getFarmerOrNull(animalClientModel.getFarmer());
        PictureClientModel farmerPictureClientModel = picturesClient.getPictureOrNull(
                farmerClientModel == null
                        ? null
                        : farmerClientModel.getPicture()
        );

        return Mapper.toAnimalModel(animalClientModel, specieClientModel, farmerClientModel, farmerPictureClientModel, pictureClientModel);
    }
}
