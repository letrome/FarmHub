package farmhub.service.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecieModel {

    @JsonProperty("id")
    private String id;

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
