package com.guggio.etherwallet.entity.address;

import com.guggio.etherwallet.entity.balance.Erc20Balance;
import com.guggio.etherwallet.entity.balance.EtherBalance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    return address.equals(that.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address);
  }
}
