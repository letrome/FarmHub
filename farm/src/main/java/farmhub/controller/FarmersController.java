package farmhub.controller;

import com.fasterxml.jackson.databind.JsonNode;
import farmhub.controller.models.InputFarmerModel;
import farmhub.service.FarmersService;
import farmhub.service.models.FarmerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/farmers")
public class FarmersController {
    private final FarmersService service;

    @Autowired
    public FarmersController(FarmersService service) {
        this.service = service;
    }

    @GetMapping
    public List<FarmerModel> listFarmers(@RequestParam(value = "include_gone", defaultValue = "false") boolean includeGone) {
        return service.listFarmers(includeGone);
    }

    @GetMapping("/{farmerId}")
    public FarmerModel getFarmerById(@PathVariable String farmerId) {
        return service.getFarmerById(farmerId);
    }

    @PostMapping
    public FarmerModel createFarmer(@RequestBody InputFarmerModel inputFarmerModel) {
        return service.createFarmer(inputFarmerModel);
    }

    @PutMapping("/{farmerId}")
    public FarmerModel updateFarmer(@PathVariable String farmerId, @RequestBody InputFarmerModel inputFarmerModel) {
        return service.updateFarmer(farmerId, inputFarmerModel);
    }

    @PatchMapping("/{farmerId}")
    public FarmerModel patchFarmer(@PathVariable String farmerId, @RequestBody JsonNode inputNode) {
        return service.patchFarmer(farmerId, inputNode);
    }

    @DeleteMapping("/{farmerId}")
    public FarmerModel deleteFarmer(@PathVariable String farmerId) {
        return service.deleteFarmer(farmerId);
    }
}
