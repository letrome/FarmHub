package farmhub.controller.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InputFarmModel {
    @JsonProperty("farm_name")
    private String farmName;

    @JsonProperty("beginning_date")
    private String beginningDate;
}
