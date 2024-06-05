package farmhub.controller;

import com.fasterxml.jackson.databind.JsonNode;
import farmhub.clients.AbstractClient;
import farmhub.controller.models.InputAnimalModel;
import farmhub.exception.BadRequestException;
import farmhub.service.AnimalsService;
import farmhub.service.models.AnimalModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalsController {
    private final AnimalsService service;

    private final Logger logger;

    @Autowired
    public AnimalsController(AnimalsService service) {
        this.service = service;
        this.logger = LoggerFactory.getLogger(AnimalsController.class);
    }

    @GetMapping
    public List<AnimalModel> listAnimals(@RequestParam(value = "include_gone", defaultValue = "false") boolean includeGone) {
        return service.listAnimals(includeGone);
    }

    @GetMapping("/{animalId}")
    public AnimalModel getAnimalById(@PathVariable String animalId) {
        return service.getAnimalById(animalId);
    }

    @PostMapping
    public AnimalModel createAnimal(@RequestBody InputAnimalModel inputAnimalModel) {
        validate(inputAnimalModel);
        return service.createAnimal(inputAnimalModel);
    }

    @PutMapping("/{animalId}")
    public AnimalModel updateAnimal(@PathVariable String animalId, @RequestBody InputAnimalModel inputAnimalModel) {
        validate(inputAnimalModel);
        return service.updateAnimal(animalId, inputAnimalModel);
    }

    @PatchMapping("/{animalId}")
    public AnimalModel patchAnimal(@PathVariable String animalId, @RequestBody JsonNode inputNode) {
        return service.patchAnimal(animalId, inputNode);
    }

    @DeleteMapping("/{animalId}")
    public AnimalModel deleteAnimal(@PathVariable String animalId) {
        return service.deleteAnimal(animalId);
    }

    private void validate(InputAnimalModel inputAnimalModel) {
        checkNotNullOrEmpty("name", inputAnimalModel.getName());
        checkNotNullOrEmpty("birth_date", inputAnimalModel.getBirthDate());
        checkNotNullOrEmpty("specie", inputAnimalModel.getSpecie());
        checkNotNullOrEmpty("farmer", inputAnimalModel.getFarmer());
        checkNotNullOrEmpty("status", inputAnimalModel.getStatus());
        checkNotNullOrEmpty("picture", inputAnimalModel.getPicture());
    }

    private void checkNotNullOrEmpty(String fieldName, String value) {
        if (value == null || value.isEmpty()) {
            String message = String.format("mandatory field '%s' not present or empty", fieldName);

            logger.debug(message);
            throw new BadRequestException(message);
        }
    }
}
