package com.guggio.etherwallet.entity.address;

import com.guggio.etherwallet.entity.balance.Erc20Balance;
import com.guggio.etherwallet.entity.balance.EtherBalance;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class EtherAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(unique = true)
  @NotEmpty
  private String address;
  @OneToMany(mappedBy = "etherAddress", fetch = FetchType.LAZY)
  private Set<EtherBalance> etherBalances;
  @OneToMany(mappedBy = "etherAddress", fetch = FetchType.LAZY)
  private Set<Erc20Balance> erc20Balances;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EtherAddress that = (EtherAddress) o;
    return id.equals(that.id) && address.equals(that.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, address);
  }
}
