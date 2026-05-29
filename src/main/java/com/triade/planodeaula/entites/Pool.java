package com.triade.planodeaula.entites;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "tb_pool")
public class Pool {

  @Id
  private Long id;

  private Integer vagasDisponiveis;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getVagasDisponiveis() {
    return vagasDisponiveis;
  }

  public void setVagasDisponiveis(Integer vagasDisponiveis) {
    this.vagasDisponiveis = vagasDisponiveis;
  }
}
