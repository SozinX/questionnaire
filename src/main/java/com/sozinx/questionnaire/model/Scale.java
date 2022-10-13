package com.sozinx.questionnaire.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "SCALES")
public class Scale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scaleId;
    private String name;
    @OneToMany(mappedBy = "scale", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Question> questions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
