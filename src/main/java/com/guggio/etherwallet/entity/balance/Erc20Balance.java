package com.guggio.etherwallet.entity.balance;

import com.guggio.etherwallet.entity.address.EtherAddress;
import com.guggio.etherwallet.entity.token.Erc20Token;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueAddressTokenAndBlock", columnNames = {"address_id", "erc20_token_id", "blockNumber"})})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Erc20Balance {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "address_id", nullable = false, foreignKey = @ForeignKey(name = "erc20_balance_address_id"))
  private EtherAddress etherAddress;
  @ManyToOne
  @JoinColumn(name = "erc20_token_id", nullable = false, foreignKey = @ForeignKey(name = "erc20_balance_token_id"))
  private Erc20Token erc20Token;
  @NotNull
  private Long blockNumber;
  private String value;

  public BigInteger getValue() {
    return new BigInteger(value);
  }

  public void setValue(BigInteger value) {
    this.value = value.toString();
  }
}
