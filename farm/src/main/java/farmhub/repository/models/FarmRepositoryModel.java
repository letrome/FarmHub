package farmhub.repository.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FarmRepositoryModel {
    @JsonProperty("farm_name")
    private String farmName;

    @JsonProperty("beginning_date")
    private String beginningDate;
}
