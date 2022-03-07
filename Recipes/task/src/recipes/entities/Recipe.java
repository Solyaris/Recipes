package recipes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recipe {
    @Id
    @JsonIgnore
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @UpdateTimestamp
    private LocalDateTime date;

    @NotBlank
    private String category;

    @ElementCollection
    @Size(min = 1)
    @NotNull
    private List<String> ingredients;

    @ElementCollection
    @NotNull
    @Size(min = 1)
    private List<String> directions;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;



}
