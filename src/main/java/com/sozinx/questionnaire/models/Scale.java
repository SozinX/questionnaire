package com.sozinx.questionnaire.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "SCALES")
public class Scale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scaleId;
    private String scaleName;
    @OneToMany(mappedBy = "scale", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Question> questions;
    @OneToMany(mappedBy = "scale", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Range> ranges;

    public String getScaleName() {
        return scaleName;
    }

    public void setScaleName(String scaleName) {
        this.scaleName = scaleName;
    }


}
