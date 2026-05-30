package com.triade.planodeaula.entites;

import jakarta.persistence.*;

@Table(name = "tb_pool")
@Entity
public class Pool {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
