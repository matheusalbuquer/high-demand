package com.triade.planodeaula.services;


import com.triade.planodeaula.dtos.ticket.TicketRequestDTO;
import com.triade.planodeaula.dtos.ticket.TicketResponseDTO;
import com.triade.planodeaula.entites.Ticket;
import com.triade.planodeaula.entites.User;
import com.triade.planodeaula.enums.Status;
import com.triade.planodeaula.repositories.TicketRepository;
import com.triade.planodeaula.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;



@Service
public class TicketService {


    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService (TicketRepository ticketRepository, UserRepository userRepository){
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public TicketResponseDTO create(){

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(login);

        if (user == null){
            throw new RuntimeException("User not found");
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(Status.RASCUNHO);
        ticket.setUser(user);

        Ticket salvo = ticketRepository.save(ticket);

        return new TicketResponseDTO(salvo.getId(), salvo.getStatus());
    }


}
