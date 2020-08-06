package ir.sam.hearthstone.server.model.main;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class HasAction {
    @Id
    @Getter
    @Setter
    @EqualsAndHashCode.Include
    protected String name;
    @Column
    @Getter
    @Setter
    protected String description;
    @Getter
    @Setter
    @Column
    protected String className;
    @Setter
    @Getter
    @Column
    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    protected Map<ActionType, String> methods;

    public HasAction() {
    }

    @PostLoad
    private void postLoad() {
        this.methods = new HashMap<>(this.methods);
    }
}
