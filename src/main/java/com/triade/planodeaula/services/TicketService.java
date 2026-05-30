package com.triade.planodeaula.services;


import com.triade.planodeaula.dtos.ticket.TicketRequestDTO;
import com.triade.planodeaula.dtos.ticket.TicketResponseDTO;
import com.triade.planodeaula.entites.Pool;
import com.triade.planodeaula.entites.Ticket;
import com.triade.planodeaula.entites.User;
import com.triade.planodeaula.enums.Status;
import com.triade.planodeaula.repositories.PoolRepository;
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
    private final PoolRepository poolRepository;

    public TicketService (TicketRepository ticketRepository,PoolRepository poolRepository, UserRepository userRepository){
        this.ticketRepository = ticketRepository;
        this.poolRepository = poolRepository;
        this.userRepository = userRepository;
    }

    public TicketResponseDTO create(){

      String login = SecurityContextHolder.getContext()
        .getAuthentication()
        .getName();


        User user = userRepository.findByEmail(login);

        System.out.println("USER: " + user);

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

    Pool pool = poolRepository.buscarPoolComLock();

    if (pool.getVagasDisponiveis() <= 0) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "POOL_CHEIO"
      );
    }

    pool.setVagasDisponiveis(
      pool.getVagasDisponiveis() - 1
    );

    ticket.setStatus(Status.PROCESSANDO);

    poolRepository.save(pool);
    ticketRepository.save(ticket);

    return new TicketResponseDTO(
      ticket.getId(),
      ticket.getStatus()
    );
  }


  @Transactional
  public TicketResponseDTO finalizar(Long id) {

    String login = SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getName();

    User user = userRepository.findByEmail(login);

    Ticket ticket = ticketRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Nao encontrado"));

    if (!ticket.getUser().getId().equals(user.getId())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    if (ticket.getStatus() != Status.PROCESSANDO) {
      throw new RuntimeException("Status inválido");
    }

    Pool pool = poolRepository.buscarPoolComLock();

    ticket.setStatus(Status.CONCLUIDO);

    pool.setVagasDisponiveis(
      pool.getVagasDisponiveis() + 1
    );

    ticketRepository.save(ticket);
    poolRepository.save(pool);

    return new TicketResponseDTO(
      ticket.getId(),
      ticket.getStatus()
    );
  }



}
