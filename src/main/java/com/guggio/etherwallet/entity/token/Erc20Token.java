package com.guggio.etherwallet.entity.token;

import com.guggio.etherwallet.entity.balance.Erc20Balance;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Erc20Token {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(unique = true)
  @NotNull
  private String contractAddress;
  @NotNull
  private String name;
  @NotNull
  private String symbol;
  @NotNull
  private Integer decimalPlaces;
  @OneToMany(mappedBy = "erc20Token", fetch = FetchType.LAZY)
  private Set<Erc20Balance> er20Balances;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Erc20Token that = (Erc20Token) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
