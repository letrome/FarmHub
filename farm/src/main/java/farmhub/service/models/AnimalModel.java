package farmhub.service.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalModel {
    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("birth_date")
    public String birthDate;

    @JsonProperty("specie")
    public SpecieModel specie;

    @JsonProperty("farmer")
    public FarmerModel farmer;

    @JsonProperty("status")
    public String status;

    @JsonProperty("picture")
    public PictureModel picture;
}
