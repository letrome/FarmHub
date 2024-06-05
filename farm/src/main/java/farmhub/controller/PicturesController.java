package farmhub.controller;

import com.fasterxml.jackson.databind.JsonNode;
import farmhub.controller.models.InputPictureModel;
import farmhub.exception.BadRequestException;
import farmhub.service.PicturesService;
import farmhub.service.models.PictureModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pictures")
public class PicturesController {
    private final PicturesService service;

    private final Logger logger;

    @Autowired
    public PicturesController(PicturesService service) {
        this.service = service;
        this.logger = LoggerFactory.getLogger(PicturesController.class);
    }

    @GetMapping
    public List<PictureModel> listAllPictures() {
        return service.listAllPictures();
    }

    @GetMapping("/{id}")
    public PictureModel getPicture(@PathVariable(name = "id") String pictureId) {
        return service.getPictureById(pictureId);
    }

    @PostMapping
    public PictureModel createPicture(@RequestBody InputPictureModel inputPictureModel) {
        validate(inputPictureModel);
        return service.createPicture(inputPictureModel);
    }

    @PutMapping("/{id}")
    public PictureModel updatePicture(@PathVariable(name = "id") String pictureId, @RequestBody InputPictureModel inputPictureModel) {
        validate(inputPictureModel);
        return service.updatePicture(pictureId, inputPictureModel);
    }

    @PatchMapping("/{id}")
    public PictureModel patchPicture(@PathVariable(name = "id") String pictureId, @RequestBody JsonNode inputNode) {
        return service.patchPicture(pictureId, inputNode);
    }

    @DeleteMapping("/{id}")
    public PictureModel deletePicture(@PathVariable(name = "id") String pictureId) {
        return service.deletePicture(pictureId);
    }

    private void validate(InputPictureModel inputPictureModel) {
        checkNotNullOrEmpty("name", inputPictureModel.getName());
        checkNotNullOrEmpty("birth_date", inputPictureModel.getType());
        checkNotNullOrEmpty("specie", inputPictureModel.getUrl());
    }

    private void checkNotNullOrEmpty(String fieldName, String value) {
        if (value == null || value.isEmpty()) {
            String message = String.format("mandatory field '%s' not present or empty", fieldName);

            logger.debug(message);
            throw new BadRequestException(message);
        }
    }
}
