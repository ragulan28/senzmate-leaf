package com.ragul.leaf.model;


import com.ragul.leaf.model.audit.UserDateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "leafs")
public class Leaf extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 140)
    private String name;

    @Size(max = 140)
    private String description;
    @NotBlank
    @Size(max = 140)
    private String fileDownloadUri;

    public Leaf() {
    }

    public Leaf(String name, String description,  String fileDownloadUri) {
        this.name = name;
        this.description = description;
        this.fileDownloadUri = fileDownloadUri;
    }

    public Leaf(String name, String fileDownloadUri) {
        this.name = name;
        this.fileDownloadUri = fileDownloadUri;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }
}
