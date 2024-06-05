package farmhub.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import farmhub.controller.models.InputFarmModel;
import farmhub.service.FarmService;
import farmhub.service.models.FarmModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/farm")
public class FarmController {
    private final FarmService service;
    private final ObjectMapper mapper;

    @Autowired
    public FarmController(FarmService service) {
        this.service = service;
        this.mapper = new ObjectMapper();
    }

    @GetMapping
    public FarmModel getFarm(@RequestParam(value = "include_gone", defaultValue = "false") boolean includeGone) {
        return service.getFarm(includeGone);
    }

    @PutMapping
    public FarmModel updateFarm(@RequestBody InputFarmModel inputFarmModel) {
        return service.updateFarm(inputFarmModel);
    }

    @PatchMapping
    public FarmModel patchFarm(@RequestBody JsonNode inputNode) {
        return service.patchFarm(inputNode);
    }

    @PostMapping("/reset")
    public JsonNode reset() {
        service.reset();
        return mapper.createObjectNode().put("applied", true);
    }
}