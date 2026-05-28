package com.triade.planodeaula.dtos.ticket;

import com.triade.planodeaula.enums.Status;

public record TicketResponseDTO(Long id,
                                Status status) {
}
