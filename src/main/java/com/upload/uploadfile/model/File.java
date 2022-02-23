package com.upload.uploadfile.model;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "FILES")
//datbase name
@Data
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String contentType;

    private Long size;

    @Lob
    private byte[] data;

}
