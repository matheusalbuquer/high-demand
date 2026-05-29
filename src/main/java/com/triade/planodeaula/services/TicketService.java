package com.triade.planodeaula.services;


import com.triade.planodeaula.dtos.ticket.TicketRequestDTO;
import com.triade.planodeaula.dtos.ticket.TicketResponseDTO;
import com.triade.planodeaula.entites.Ticket;
import com.triade.planodeaula.entites.User;
import com.triade.planodeaula.enums.Status;
import com.triade.planodeaula.repositories.TicketRepository;
import com.triade.planodeaula.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


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

      System.out.println("LOGIN: " + login);

      User user = userRepository.findByEmail(login);


        Ticket ticket = new Ticket();
        ticket.setStatus(Status.RASCUNHO);
        ticket.setUser(user);

        Ticket salvo = ticketRepository.save(ticket);

        return new TicketResponseDTO(salvo.getId(), salvo.getStatus());
    }

  @Transactional
  public TicketResponseDTO submeter(Long id) {

    String login = SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getName();

    User user = userRepository.findByEmail(login);

    Ticket ticket = ticketRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));


    if (!ticket.getUser().getId().equals(user.getId())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


    long quantidadeUsuario =
      ticketRepository.countByUserAndStatus(
        user,
        Status.PROCESSANDO
      );

    if (quantidadeUsuario >= 2) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "COTA_PESSOAL"
      );
    }


    long totalProcessando =
      ticketRepository.countByStatus(Status.PROCESSANDO);

    if (totalProcessando >= 100) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "POOL_CHEIO"
      );
    }

    ticket.setStatus(Status.PROCESSANDO);

    ticketRepository.save(ticket);

    return new TicketResponseDTO(
      ticket.getId(),
      ticket.getStatus()
    );
  }



}
