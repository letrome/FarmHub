package farmhub.controller.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InputFarmerModel {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("birth_date")
    private String birthDate;

    @JsonProperty("specialties")
    private List<String> specialties;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("status")
    private String status;
}
