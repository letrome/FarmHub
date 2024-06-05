package farmhub.clients.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AnimalClientModel {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("birth_date")
    private String birthDate;
    
    @JsonProperty("specie")
    private String specie;
    
    @JsonProperty("farmer")
    private String farmer;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("picture")
    private String picture;
}
