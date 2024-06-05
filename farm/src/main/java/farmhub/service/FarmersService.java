package farmhub.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import farmhub.Mapper;
import farmhub.clients.AnimalsClient;
import farmhub.clients.FarmersClient;
import farmhub.clients.PicturesClient;
import farmhub.clients.model.AnimalClientModel;
import farmhub.clients.model.FarmerClientModel;
import farmhub.clients.model.PictureClientModel;
import farmhub.controller.models.InputFarmerModel;
import farmhub.exception.BadRequestException;
import farmhub.service.models.FarmerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class FarmersService {

    private final FarmersClient farmersClient;
    private final PicturesClient picturesClient;
    private final AnimalsClient animalsClient;

    protected Logger logger;

    @Autowired
    public FarmersService(FarmersClient farmersClient,
                          PicturesClient picturesClient,
                          AnimalsClient animalsClient
    ) {
        this.farmersClient = farmersClient;
        this.picturesClient = picturesClient;
        this.animalsClient = animalsClient;

        this.logger = LoggerFactory.getLogger(FarmersService.class);
    }

    public List<FarmerModel> listFarmers(boolean includeGone) {
        List<FarmerClientModel> farmerClientModels = farmersClient.getAllFarmers();
        List<PictureClientModel> pictureClientModels = picturesClient.getAllPictures();
        return Mapper.toFarmerModels(farmerClientModels, pictureClientModels, includeGone);
    }

    public FarmerModel getFarmerById(String farmerId) {
        FarmerClientModel farmerClientModel = farmersClient.getFarmerOrNotFound(farmerId);
        PictureClientModel pictureClientModel = picturesClient.getPictureOrInternalServerError(farmerClientModel.getPicture());
        return Mapper.toFarmerModel(farmerClientModel, pictureClientModel);
    }

    public FarmerModel createFarmer(InputFarmerModel inputFarmerModel) {
        PictureClientModel pictureClientModel = picturesClient.getPictureOrBadRequest(inputFarmerModel.getPicture());

        FarmerClientModel farmerClientModel = farmersClient.createFarmer(inputFarmerModel);
        return Mapper.toFarmerModel(farmerClientModel, pictureClientModel);
    }

    public FarmerModel updateFarmer(String farmerId, InputFarmerModel inputFarmerModel) {
        farmersClient.getFarmerOrNotFound(farmerId);

        PictureClientModel pictureClientModel = picturesClient.getPictureOrBadRequest(inputFarmerModel.getPicture());
        FarmerClientModel farmerClientModel = farmersClient.updateFarmerOrInternalServerError(farmerId, inputFarmerModel);
        return Mapper.toFarmerModel(farmerClientModel, pictureClientModel);
    }

    public FarmerModel patchFarmer(String farmerId, JsonNode inputNode) {
        PictureClientModel pictureClientModel = null;

        List<String> validKeys = List.of("first_name", "last_name", "birth_date", "specialties", "picture", "status");

        ObjectNode node = new ObjectMapper().createObjectNode();
        Iterator<Map.Entry<String, JsonNode>> it = inputNode.fields();
        while (it.hasNext()) {
            Map.Entry<String, JsonNode> element = it.next();
            if (!validKeys.contains(element.getKey())) {
                continue;
            }

            if (element.getKey().equals("specialties") && !element.getValue().isArray()) {
                continue;
            }

            if ("picture".equals(element.getKey())) {
                pictureClientModel = picturesClient.getPictureOrBadRequest(element.getValue().textValue());
            }

            node.put(element.getKey(), element.getValue().textValue());
        }

        FarmerClientModel responseFarmerClientModel = farmersClient.patchFarmerOrNotFound(farmerId, node);

        if (pictureClientModel == null) {
            pictureClientModel = picturesClient.getPictureOrInternalServerError(responseFarmerClientModel.getPicture());
        }
        return Mapper.toFarmerModel(responseFarmerClientModel, pictureClientModel);
    }

    public FarmerModel deleteFarmer(String farmerId) {
        List<AnimalClientModel> animalsUsingFarmer = animalsClient.getAllAnimals().stream()
                .filter(animalClientModel -> farmerId.equals(animalClientModel.getFarmer()))
                .toList();

        if (!animalsUsingFarmer.isEmpty()) {
            String message = String.format("The farmer %s is used by at least 1 animal", farmerId);
            logger.warn(message);
            throw new BadRequestException(message);
        }

        FarmerClientModel farmerClientModel = farmersClient.deleteFarmerOrNotFound(farmerId);

        PictureClientModel pictureClientModel = picturesClient.getPictureOrNull(farmerClientModel.getPicture());
        return Mapper.toFarmerModel(farmerClientModel, pictureClientModel);
    }
}
