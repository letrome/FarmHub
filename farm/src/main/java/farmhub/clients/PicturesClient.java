package farmhub.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import farmhub.FarmProperties;
import farmhub.clients.model.PictureClientModel;
import farmhub.controller.models.InputPictureModel;
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
public class PicturesClient extends AbstractClient {

    @Autowired
    public PicturesClient(FarmProperties properties) {
        super(properties.getPicturesHost(), "picture");
    }

    public List<PictureClientModel> getAllPictures() {
        String url_suffix = "/pictures";
        return executeGetRequest(url_suffix, new ParameterizedTypeReference<>() {
        });
    }

    public Optional<PictureClientModel> getPictureById(String pictureId) {
        String url_suffix = "/pictures/" + pictureId;
        return executeGetRequest(url_suffix, PictureClientModel.class);
    }

    public PictureClientModel createPicture(InputPictureModel inputPictureModel) {
        String url_suffix = "/pictures";
        return executePostRequest(url_suffix, inputPictureModel, PictureClientModel.class);
    }

    public Optional<PictureClientModel> updatePicture(String pictureId, InputPictureModel inputPictureModel) {
        String url_suffix = "/pictures/" + pictureId;
        return executePutRequest(url_suffix, inputPictureModel, PictureClientModel.class);
    }

    public Optional<PictureClientModel> patchPicture(String pictureId, JsonNode inputNode) {
        String url_suffix = "/pictures/" + pictureId;
        return executePatchRequest(url_suffix, inputNode, PictureClientModel.class);
    }

    public Optional<PictureClientModel> deletePicture(String pictureId) {
        String url_suffix = "/pictures/" + pictureId;
        return executeDeleteRequest(url_suffix, PictureClientModel.class);
    }

    public void reset() {
        String url_suffix = "/reset";
        executePostRequest(url_suffix, null, Void.class);
    }


    public PictureClientModel getPictureOrThrow(String pictureId, Supplier<? extends RuntimeException> supplier) {
        if (pictureId == null)
            return null;

        return this.getPictureById(pictureId)
                .orElseThrow(supplier);
    }

    public PictureClientModel getPictureOrBadRequest(String pictureId) {
        return getPictureOrThrow(pictureId, () -> {
            String message = badRequestMessage(pictureId);
            logger.debug(message);
            return new BadRequestException(message);
        });
    }

    public PictureClientModel getPictureOrNotFound(String pictureId) {
        return getPictureOrThrow(pictureId, () -> {
            String message = notFoundMessage(pictureId);
            logger.debug(message);
            return new NotFoundException(message);
        });
    }

    public PictureClientModel getPictureOrInternalServerError(String pictureId) {
        return getPictureOrThrow(pictureId, () -> {
            String message = internalServerErrorMessage(pictureId);
            logger.debug(message);
            return new InternalServerErrorException();
        });
    }

    public PictureClientModel getPictureOrNull(String pictureId) {
        if (pictureId == null)
            return null;

        return this.getPictureById(pictureId)
                .orElse(null);
    }

    public PictureClientModel updatePictureOrNotFound(String pictureId, InputPictureModel inputPictureModel) {
        return updatePicture(pictureId, inputPictureModel)
                .orElseThrow(() -> {
                    String message = notFoundMessage(pictureId);
                    logger.debug(message);
                    return new NotFoundException(message);
                });
    }

    public PictureClientModel deletePictureOrNotFound(String pictureId) {
        return deletePicture(pictureId)
                .orElseThrow(() -> {
                    String message = notFoundMessage(pictureId);
                    logger.debug(message);
                    return new NotFoundException(message);
                });
    }

    public PictureClientModel patchPictureOrNotFound(String pictureId, ObjectNode node) {
        return patchPicture(pictureId, node)
                .orElseThrow(() -> {
                    String message = notFoundMessage(pictureId);
                    logger.debug(message);
                    return new NotFoundException(message);
                });
    }
}
