package com.sozinx.questionnaire.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "STATUSES")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long statusId;
    private String statusName;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Range> ranges;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

}
