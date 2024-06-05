package farmhub.clients;

import com.fasterxml.jackson.databind.JsonNode;
import farmhub.FarmProperties;
import farmhub.clients.model.SpecieClientModel;
import farmhub.controller.models.InputSpecieModel;
import farmhub.exception.BadRequestException;
import farmhub.exception.InternalServerErrorException;
import farmhub.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class SpeciesClient extends AbstractClient {

    @Autowired
    public SpeciesClient(FarmProperties properties) {
        super(properties.getSpeciesHost(), "specie");
    }

    public List<SpecieClientModel> getAllSpecies() {
        String url_suffix = "/species";
        return executeGetRequest(url_suffix, new ParameterizedTypeReference<>() {
        });
    }

    public Optional<SpecieClientModel> getSpecieById(String specieId) {
        String url_suffix = "/species/" + specieId;
        return executeGetRequest(url_suffix, SpecieClientModel.class);
    }

    public SpecieClientModel createSpecie(InputSpecieModel inputSpecieModel) {
        String url_suffix = "/species";
        return executePostRequest(url_suffix, inputSpecieModel, SpecieClientModel.class);
    }

    public Optional<SpecieClientModel> updateSpecie(String specieId, InputSpecieModel inputSpecieModel) {
        String url_suffix = "/species/" + specieId;
        return executePutRequest(url_suffix, inputSpecieModel, SpecieClientModel.class);
    }

    public Optional<SpecieClientModel> patchSpecie(String specieId, JsonNode node) {
        String url_suffix = "/species/" + specieId;
        return executePatchRequest(url_suffix, node, SpecieClientModel.class);
    }

    public Optional<SpecieClientModel> deleteSpecie(String specieId) {
        String url_suffix = "/species/" + specieId;
        return executeDeleteRequest(url_suffix, SpecieClientModel.class);
    }

    public void reset() {
        String url_suffix = "/reset";
        executePostRequest(url_suffix, null, Void.class);
    }

    private SpecieClientModel getSpecieOrThrow(String specieId, Supplier<? extends RuntimeException> supplier) {
        if (specieId == null)
            return null;

        return this.getSpecieById(specieId)
                .orElseThrow(supplier);
    }

    public SpecieClientModel getSpecieOrBadRequest(String specieId) {
        return getSpecieOrThrow(specieId, () -> {
            String message = badRequestMessage(specieId);
            logger.debug(message);
            return new BadRequestException(message);
        });
    }

    public SpecieClientModel getSpecieOrNotFound(String specieId) {
        return getSpecieOrThrow(specieId, () -> {
            String message = notFoundMessage(specieId);
            logger.debug(message);
            return new NotFoundException(message);
        });
    }

    public SpecieClientModel getSpecieOrInternalServerError(String specieId) {
        return getSpecieOrThrow(specieId, () -> {
            String message = internalServerErrorMessage(specieId);
            logger.debug(message);
            return new InternalServerErrorException();
        });
    }

    public SpecieClientModel getSpecieOrNull(String specieId) {
        if (specieId == null)
            return null;

        return this.getSpecieById(specieId)
                .orElse(null);
    }

    public SpecieClientModel updateSpecieOrNotFound(String specieId, InputSpecieModel inputSpecieModel) {
        return updateSpecie(specieId, inputSpecieModel)
                .orElseThrow(() -> {
                    String message = notFoundMessage(specieId);
                    logger.debug(message);
                    return new NotFoundException(message);
                });
    }

    public SpecieClientModel patchSpecieOrNotFound(String specieId, JsonNode node) {
        return patchSpecie(specieId, node)
                .orElseThrow(() -> {
                    String message = notFoundMessage(specieId);
                    logger.debug(message);
                    return new NotFoundException(message);
                });
    }

    public SpecieClientModel deleteSpecieOrNotFound(String specieId) {
        return deleteSpecie(specieId)
                .orElseThrow(() -> {
                    String message = notFoundMessage(specieId);
                    logger.debug(message);
                    return new NotFoundException(message);
                });
    }
}
