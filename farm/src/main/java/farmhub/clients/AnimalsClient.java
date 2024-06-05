package farmhub.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import farmhub.FarmProperties;
import farmhub.clients.model.AnimalClientModel;
import farmhub.controller.models.InputAnimalModel;
import farmhub.exception.InternalServerErrorException;
import farmhub.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class AnimalsClient extends AbstractClient {

    @Autowired
    public AnimalsClient(FarmProperties properties) {
        super(properties.getAnimalsHost(), "animal");
    }

    public List<AnimalClientModel> getAllAnimals() {
        String url_suffix = "/animals";
        return executeGetRequest(url_suffix, new ParameterizedTypeReference<>() {
        });
    }

    public Optional<AnimalClientModel> getAnimalById(String animalId) {
        String url_suffix = "/animals/" + animalId;
        return executeGetRequest(url_suffix, AnimalClientModel.class);
    }

    public AnimalClientModel createAnimal(InputAnimalModel inputAnimalModel) {
        String url_suffix = "/animals";
        return executePostRequest(url_suffix, inputAnimalModel, AnimalClientModel.class);
    }

    public Optional<AnimalClientModel> updateAnimal(String animalId, InputAnimalModel inputAnimalModel) {
        String url_suffix = "/animals/" + animalId;
        return executePutRequest(url_suffix, inputAnimalModel, AnimalClientModel.class);
    }

    public Optional<AnimalClientModel> patchAnimal(String animalId, JsonNode inputNode) {
        String url_suffix = "/animals/" + animalId;
        return executePatchRequest(url_suffix, inputNode, AnimalClientModel.class);
    }

    public Optional<AnimalClientModel> deleteAnimal(String animalId) {
        String url_suffix = "/animals/" + animalId;
        return executeDeleteRequest(url_suffix, AnimalClientModel.class);
    }

    public void reset() {
        String url_suffix = "/reset";
        executePostRequest(url_suffix, null, Void.class);
    }

    public AnimalClientModel getAnimalOrThrow(String animalId, Supplier<? extends RuntimeException> supplier) {
        if (animalId == null)
            return null;

        return this.getAnimalById(animalId)
                .orElseThrow(supplier);
    }

    public AnimalClientModel getAnimalOrNotFound(String animalId) {
        return getAnimalOrThrow(animalId, () -> {
            String message = notFoundMessage(animalId);
            logger.debug(message);
            return new NotFoundException(message);
        });
    }

    public AnimalClientModel updateAnimalOrInternalServerError(String animalId, InputAnimalModel inputAnimalModel) {
        return this.updateAnimal(animalId, inputAnimalModel)
                .orElseThrow(() -> {
                    String message = internalServerErrorMessage(animalId);
                    logger.error(message);
                    return new InternalServerErrorException();
                });
    }

    public AnimalClientModel deleteAnimalOrNotFound(String animalId) {
        return this.deleteAnimal(animalId)
                .orElseThrow(() -> {
                    String message = notFoundMessage(animalId);
                    logger.debug(message);
                    return new NotFoundException(message);
                });
    }

    public AnimalClientModel patchAnimalOrNotFound(String animalId, ObjectNode node) {
        return patchAnimal(animalId, node)
                .orElseThrow(() -> {
                    String message = notFoundMessage(animalId);
                    logger.debug(message);
                    return new NotFoundException(message);
                });
    }
}
