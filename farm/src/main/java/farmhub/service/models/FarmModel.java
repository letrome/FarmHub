package farmhub.service.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmModel {
    @JsonProperty("farm_name")
    private String farmName;

    @JsonProperty("beginning_date")
    private String beginningDate;

    @JsonProperty("nb_animals")
    private long nbAnimals;

    @JsonProperty("nb_farmers")
    private long nbFarmers;

    @JsonProperty("farmers")
    private List<FarmerModel> farmers;

    @JsonProperty("animals")
    private List<AnimalModel> animals;
}
