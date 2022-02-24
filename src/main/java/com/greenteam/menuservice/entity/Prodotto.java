package com.greenteam.menuservice.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Document("prodotti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prodotto {
    @Id
    private String id;
    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String nome;
    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String descrizione;
    @Positive
    private float prezzo;
}
