package farmhub.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import farmhub.Mapper;
import farmhub.clients.AnimalsClient;
import farmhub.clients.SpeciesClient;
import farmhub.clients.model.AnimalClientModel;
import farmhub.clients.model.SpecieClientModel;
import farmhub.controller.models.InputSpecieModel;
import farmhub.exception.BadRequestException;
import farmhub.service.models.SpecieModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpeciesService {
    private final SpeciesClient speciesClient;
    private final AnimalsClient animalsClient;
    private final Logger logger;

    public SpeciesService(SpeciesClient speciesClient, AnimalsClient animalsClient) {
        this.speciesClient = speciesClient;
        this.animalsClient = animalsClient;

        this.logger = LoggerFactory.getLogger(SpeciesService.class);
    }


    public List<SpecieModel> listSpecies() {
        return speciesClient.getAllSpecies().stream()
                .map(Mapper::toSpecieModel)
                .collect(Collectors.toList());
    }

    public SpecieModel getSpecieById(String specieId) {
        SpecieClientModel specieClientModel = speciesClient.getSpecieOrNotFound(specieId);
        return Mapper.toSpecieModel(specieClientModel);
    }

    public SpecieModel createSpecie(InputSpecieModel inputSpecieModel) {
        SpecieClientModel specieClientModel = speciesClient.createSpecie(inputSpecieModel);
        return Mapper.toSpecieModel(specieClientModel);
    }

    public SpecieModel updateSpecie(String specieId, InputSpecieModel inputSpecieModel) {
        SpecieClientModel specieClientModel = speciesClient.updateSpecieOrNotFound(specieId, inputSpecieModel);
        return Mapper.toSpecieModel(specieClientModel);
    }

    public SpecieModel patchSpecie(String specieId, JsonNode inputNode) {
        List<String> validKeys = List.of("name", "description", "diet", "natural_habitat", "conservation_status");

        ObjectNode node = new ObjectMapper().createObjectNode();
        inputNode.fields().forEachRemaining(
                element -> {
                    if (validKeys.contains(element.getKey())) {
                        node.set(element.getKey(), element.getValue());
                    }
                }
        );

        SpecieClientModel specieClientModel = speciesClient.patchSpecieOrNotFound(specieId, node);
        return Mapper.toSpecieModel(specieClientModel);
    }

    public SpecieModel deleteSpecie(String specieId) {

        List<AnimalClientModel> animalsUsingSpecie = animalsClient.getAllAnimals().stream()
                .filter(animalClientModel -> specieId.equals(animalClientModel.getSpecie()))
                .toList();

        if (!animalsUsingSpecie.isEmpty()) {
            String message = String.format("The specie %s is used by at least 1 animal", specieId);
            logger.warn(message);
            throw new BadRequestException(message);
        }

        SpecieClientModel specieClientModel = speciesClient.deleteSpecieOrNotFound(specieId);
        return Mapper.toSpecieModel(specieClientModel);
    }
}
