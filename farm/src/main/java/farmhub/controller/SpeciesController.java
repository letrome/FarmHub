package farmhub.controller;

import com.fasterxml.jackson.databind.JsonNode;
import farmhub.controller.models.InputSpecieModel;
import farmhub.service.SpeciesService;
import farmhub.service.models.SpecieModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/species")
public class SpeciesController {
    private final SpeciesService service;

    @Autowired
    public SpeciesController(SpeciesService service) {
        this.service = service;
    }

    @GetMapping
    public List<SpecieModel> listSpecies() {
        return service.listSpecies();
    }

    @GetMapping("/{specieId}")
    public SpecieModel getSpecieById(@PathVariable String specieId) {
        return service.getSpecieById(specieId);
    }

    @PostMapping
    public SpecieModel createSpecie(@RequestBody InputSpecieModel inputSpecieModel) {
        return service.createSpecie(inputSpecieModel);
    }

    @PutMapping("/{specieId}")
    public SpecieModel updateSpecie(@PathVariable String specieId, @RequestBody InputSpecieModel inputSpecieModel) {
        return service.updateSpecie(specieId, inputSpecieModel);
    }

    @PatchMapping("/{specieId}")
    public SpecieModel patchSpecie(@PathVariable String specieId, @RequestBody JsonNode node) {
        return service.patchSpecie(specieId, node);
    }

    @DeleteMapping("/{specieId}")
    public SpecieModel deleteSpecie(@PathVariable String specieId) {
        return service.deleteSpecie(specieId);
    }
}
