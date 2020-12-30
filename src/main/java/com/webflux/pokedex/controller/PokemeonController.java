package com.webflux.pokedex.controller;

import com.webflux.pokedex.model.Pokemon;
import com.webflux.pokedex.model.PokemonEvent;
import com.webflux.pokedex.repository.PokemonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/pokemons")
public class PokemeonController {

    private PokemonRepository pokemonRepository;

    public PokemeonController(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @GetMapping
    public Flux<Pokemon> getAllPokemos() {
        return pokemonRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Pokemon> savePokemo(@RequestBody Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Pokemon>> getPokemo(@PathVariable String id) {
        return pokemonRepository.findById(id)
                .map(pokemon -> ResponseEntity.ok(pokemon))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }



    @PutMapping("/{id}")
    public Mono<ResponseEntity<Pokemon>> updatePokemo(
            @PathVariable(value = "id") String id,
            @RequestBody Pokemon pokemon) {

        return pokemonRepository.findById(id)
                    .flatMap(existingPokemon -> {
                        existingPokemon.setNome(pokemon.getNome());
                        existingPokemon.setCategoria(pokemon.getCategoria());
                        existingPokemon.setHabilidade(pokemon.getHabilidade());
                        existingPokemon.setPeso(pokemon.getPeso());
                        return pokemonRepository.save(existingPokemon);
                    })
                .map(updatePokemo -> ResponseEntity.ok(updatePokemo))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deletePokemon (@PathVariable(value = "id") String id){
        return pokemonRepository.findById(id)
                .flatMap(existingPokemon ->
                        pokemonRepository.delete(existingPokemon)
                        .then(Mono.just(ResponseEntity.ok().<Void>build()))
                    )
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @DeleteMapping
    public Mono<Void> deleteAllPokemon (){
        return pokemonRepository.deleteAll();
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PokemonEvent> getPokemonEvents() {
        return Flux.interval(Duration.ofSeconds(5))
                .map(val ->
                        new PokemonEvent(val, "Product Event")
                );
    }
}
