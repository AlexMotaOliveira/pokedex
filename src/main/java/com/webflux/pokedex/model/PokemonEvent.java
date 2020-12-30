package com.webflux.pokedex.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class PokemonEvent {

    public Long eventeId;
    public String eventType;

}
