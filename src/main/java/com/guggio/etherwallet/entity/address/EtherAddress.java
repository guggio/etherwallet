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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ether_address_seq")
  @SequenceGenerator(name = "ether_address_seq", sequenceName = "ether_address_seq", allocationSize = 1)
  @Column(nullable = false)
  private Long id;
  @Column(unique = true)
  @NotEmpty
  private String address;
  @OneToMany(mappedBy = "etherAddress", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @ToString.Exclude
  private Set<EtherBalance> etherBalances;
  @OneToMany(mappedBy = "etherAddress", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @ToString.Exclude
  private Set<Erc20Balance> erc20Balances;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EtherAddress)) return false;
    EtherAddress that = (EtherAddress) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
