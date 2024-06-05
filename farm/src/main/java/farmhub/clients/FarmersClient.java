package farmhub.clients;

import com.fasterxml.jackson.databind.JsonNode;
import farmhub.FarmProperties;
import farmhub.clients.model.FarmerClientModel;
import farmhub.controller.models.InputFarmerModel;
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
public class FarmersClient extends AbstractClient {

    @Autowired
    public FarmersClient(FarmProperties properties) {
        super(properties.getFarmersHost(), "farmer");
    }

    public List<FarmerClientModel> getAllFarmers() {
        String url_suffix = "/farmers";
        return executeGetRequest(url_suffix, new ParameterizedTypeReference<>() {
        });
    }

    public Optional<FarmerClientModel> getFarmerById(String farmerId) {
        String url_suffix = "/farmers/" + farmerId;
        return executeGetRequest(url_suffix, FarmerClientModel.class);
    }

    public FarmerClientModel createFarmer(InputFarmerModel inputFarmerModel) {
        String url_suffix = "/farmers";
        return executePostRequest(url_suffix, inputFarmerModel, FarmerClientModel.class);
    }

    public Optional<FarmerClientModel> updateFarmer(String farmerId, InputFarmerModel inputFarmerModel) {
        String url_suffix = "/farmers/" + farmerId;
        return executePutRequest(url_suffix, inputFarmerModel, FarmerClientModel.class);
    }

    public Optional<FarmerClientModel> patchFarmer(String farmerId, JsonNode node) {
        String url_suffix = "/farmers/" + farmerId;
        return executePatchRequest(url_suffix, node, FarmerClientModel.class);
    }

    public Optional<FarmerClientModel> deleteFarmer(String farmerId) {
        String url_suffix = "/farmers/" + farmerId;
        return executeDeleteRequest(url_suffix, FarmerClientModel.class);
    }

    public void reset() {
        String url_suffix = "/reset";
        executePostRequest(url_suffix, null, Void.class);
    }


    public FarmerClientModel getFarmerOrThrow(String farmerId, Supplier<? extends RuntimeException> supplier) {
        if (farmerId == null)
            return null;

        return this.getFarmerById(farmerId)
                .orElseThrow(supplier);
    }

    public FarmerClientModel getFarmerOrBadRequest(String farmerId) {
        return getFarmerOrThrow(farmerId, () -> {
            String message = badRequestMessage(farmerId);
            logger.debug(message);
            return new BadRequestException(message);
        });
    }

    public FarmerClientModel getFarmerOrNotFound(String farmerId) {
        return getFarmerOrThrow(farmerId, () -> {
            String message = notFoundMessage(farmerId);
            logger.debug(message);
            return new NotFoundException(message);
        });
    }

    public FarmerClientModel getFarmerOrInternalServerError(String farmerId) {
        return getFarmerOrThrow(farmerId, () -> {
            String message = internalServerErrorMessage(farmerId);
            logger.debug(message);
            return new InternalServerErrorException();
        });
    }

    public FarmerClientModel getFarmerOrNull(String farmerId) {
        if (farmerId == null)
            return null;

        return this.getFarmerById(farmerId)
                .orElse(null);
    }

    public FarmerClientModel updateFarmerOrInternalServerError(String farmerId, InputFarmerModel inputFarmerModel) {
        return this.updateFarmer(farmerId, inputFarmerModel)
                .orElseThrow(() -> {
                    String message = internalServerErrorMessage(farmerId);
                    logger.error(message);
                    return new InternalServerErrorException();
                });
    }

    public FarmerClientModel patchFarmerOrNotFound(String farmerId, JsonNode node) {
        return this.patchFarmer(farmerId, node)
                .orElseThrow(() -> {
                    String message = notFoundMessage(farmerId);
                    logger.error(message);
                    return new NotFoundException(message);
                });
    }

    public FarmerClientModel deleteFarmerOrNotFound(String farmerId) {
        return this.deleteFarmer(farmerId)
                .orElseThrow(() -> {
                    String message = notFoundMessage(farmerId);
                    logger.debug(message);
                    return new NotFoundException(message);
                });
    }
}
