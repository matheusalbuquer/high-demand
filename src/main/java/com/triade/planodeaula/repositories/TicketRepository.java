package com.triade.planodeaula.repositories;

import com.triade.planodeaula.entites.Ticket;
import com.triade.planodeaula.entites.User;
import com.triade.planodeaula.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  long countByUserAndStatus(User user, Status status);

  long countByStatus(Status status);
}
