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
import farmhub.controller.models.InputPictureModel;
import farmhub.exception.BadRequestException;
import farmhub.service.models.PictureModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PicturesService {
    private final PicturesClient picturesClient;
    private final FarmersClient farmersClient;
    private final AnimalsClient animalsClient;
    private final Logger logger;

    @Autowired
    public PicturesService(PicturesClient picturesClient, FarmersClient farmersClient, AnimalsClient animalsClient) {
        this.picturesClient = picturesClient;
        this.farmersClient = farmersClient;
        this.animalsClient = animalsClient;

        this.logger = LoggerFactory.getLogger(PicturesClient.class);
    }

    public List<PictureModel> listAllPictures() {
        return picturesClient.getAllPictures().stream()
                .map(Mapper::toPictureModel)
                .collect(Collectors.toList());
    }

    public PictureModel getPictureById(String pictureId) {
        PictureClientModel pictureClientModel = picturesClient.getPictureOrNotFound(pictureId);

        return Mapper.toPictureModel(pictureClientModel);
    }

    public PictureModel createPicture(InputPictureModel inputPictureModel) {
        PictureClientModel pictureClientModel = picturesClient.createPicture(inputPictureModel);
        return Mapper.toPictureModel(pictureClientModel);
    }

    public PictureModel updatePicture(String pictureId, InputPictureModel inputPictureModel) {
        PictureClientModel pictureClientModel = picturesClient.updatePictureOrNotFound(pictureId, inputPictureModel);
        return Mapper.toPictureModel(pictureClientModel);
    }

    public PictureModel patchPicture(String pictureId, JsonNode inputNode) {
        List<String> validKeys = List.of("name", "type", "url");

        ObjectNode node = new ObjectMapper().createObjectNode();
        inputNode.fields().forEachRemaining(
                element -> {
                    if (validKeys.contains(element.getKey())) {
                        node.set(element.getKey(), element.getValue());
                    }
                }
        );

        PictureClientModel pictureClientModel = picturesClient.patchPictureOrNotFound(pictureId, node);
        return Mapper.toPictureModel(pictureClientModel);
    }

    public PictureModel deletePicture(String pictureId) {

        List<AnimalClientModel> animalsUsingPicture = animalsClient.getAllAnimals().stream()
                .filter(animalClientModel -> pictureId.equals(animalClientModel.getPicture()))
                .toList();

        if (!animalsUsingPicture.isEmpty()) {
            String message = String.format("The picture %s is used by at least 1 animal", pictureId);
            logger.warn(message);
            throw new BadRequestException(message);
        }

        List<FarmerClientModel> farmersUsingPicture = farmersClient.getAllFarmers().stream()
                .filter(farmerClientModel -> pictureId.equals(farmerClientModel.getPicture()))
                .toList();

        if (!farmersUsingPicture.isEmpty()) {
            String message = String.format("The picture %s is used by at least 1 farmer", pictureId);
            logger.warn(message);
            throw new BadRequestException(message);
        }

        PictureClientModel pictureClientModel = picturesClient.deletePictureOrNotFound(pictureId);
        return Mapper.toPictureModel(pictureClientModel);
    }
}
