
package domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebsiteRegister {
    private String username;
    private String name;
    private String url;
    private TypeInterval intervalType;
    private int interval;
    private boolean isActive=true;
}