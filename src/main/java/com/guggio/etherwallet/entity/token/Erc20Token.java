package com.guggio.etherwallet.entity.token;

import com.guggio.etherwallet.core.asset.Asset;
import com.guggio.etherwallet.entity.balance.Erc20Balance;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Erc20Token implements Asset {

  @Id
  @NotNull
  private String contractAddress;
  @NotNull
  private String name;
  @NotNull
  private String symbol;
  @NotNull
  private Integer decimalPlaces;
  @OneToMany(mappedBy = "erc20Token", fetch = FetchType.LAZY)
  @ToString.Exclude
  private Set<Erc20Balance> er20Balances;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Erc20Token)) return false;
    Erc20Token that = (Erc20Token) o;
    return contractAddress.equals(that.contractAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contractAddress);
  }
}
