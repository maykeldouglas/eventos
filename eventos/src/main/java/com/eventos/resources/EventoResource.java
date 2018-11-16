package com.eventos.resources;

import com.eventos.models.Evento;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import com.eventos.repository.EventoRepository;

import javax.validation.Valid;
import java.util.ArrayList;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(value = "API REST Eventos")
@RestController
@RequestMapping("/evento")
public class EventoResource {

    @Autowired
    private EventoRepository eventoRepository;

    @ApiOperation(value = "Retorna um evento pelo código informado")
    @GetMapping(value = "/{codigo}", produces = "application/json")
    public @ResponseBody Evento obtemEvento(@PathVariable(value = "codigo") long codigo){
        Evento evento = eventoRepository.findByCodigo(codigo);
        evento.add(linkTo(methodOn(EventoResource.class).listaEventos()).withRel("Lista de Eventos"));
        return evento;
    }

    @ApiOperation(value = "Retorna a lista de eventos cadastrados")
    @GetMapping(produces = "application/json")
    public @ResponseBody Iterable<Evento> listaEventos(){
        Iterable<Evento> listaEventos = eventoRepository.findAll();
        ArrayList<Evento> eventos = new ArrayList<Evento>();
        for(Evento evento : listaEventos){
            long codigo = evento.getCodigo();
            evento.add(linkTo(methodOn(EventoResource.class).obtemEvento(codigo)).withSelfRel());
            eventos.add(evento);
        }
        return eventos;
    }

    @ApiOperation(value = "Cadastra um evento")
    @PostMapping()
    public Evento cadastraEvento(@RequestBody @Valid Evento evento){
        Evento eventoInserido = eventoRepository.save(evento);
        eventoInserido.add(linkTo(methodOn(EventoResource.class).obtemEvento(evento.getCodigo())).withRel("Retorna o evento pelo código informado"));
        return eventoInserido;
    }

    @ApiOperation(value = "Exclui um evento")
    @DeleteMapping()
    public Evento deletaEvento(@RequestBody @Valid Evento evento){
        eventoRepository.delete(evento);
        return evento;
    }

}
