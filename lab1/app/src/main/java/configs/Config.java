package configs;

import com.fasterxml.jackson.annotation.*;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HelicopterConfig.class, name = "Helicopter"),
        @JsonSubTypes.Type(value = LamborghiniConfig.class, name = "Lamborghini"),
        @JsonSubTypes.Type(value = PlaneConfig.class, name = "Plane"),
        @JsonSubTypes.Type(value = PoloConfig.class, name = "Polo")
})
public interface Config {
    @JsonIgnore
    boolean isRebuild();
}
