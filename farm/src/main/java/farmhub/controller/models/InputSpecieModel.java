package farmhub.controller.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InputSpecieModel {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("diet")
    private String diet;

    @JsonProperty("natural_habitat")
    private String naturalHabitat;

    @JsonProperty("conservation_status")
    private String conservationStatus;
}
