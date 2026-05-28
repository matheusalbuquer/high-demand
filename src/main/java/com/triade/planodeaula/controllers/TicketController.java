package com.triade.planodeaula.controllers;

import com.triade.planodeaula.dtos.ticket.TicketResponseDTO;
import com.triade.planodeaula.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entidades")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponseDTO> create(){
        TicketResponseDTO salvo = ticketService.create();
        return ResponseEntity.status(201).body(salvo);
    }
}
