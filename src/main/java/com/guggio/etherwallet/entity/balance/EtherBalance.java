package com.guggio.etherwallet.entity.balance;

import com.guggio.etherwallet.entity.address.EtherAddress;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueAddressAndBlock", columnNames = {"address_id", "blockNumber"})})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class EtherBalance {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "address_id", nullable = false, foreignKey = @ForeignKey(name = "eth_balance_address_id"))
  private EtherAddress etherAddress;
  @NotNull
  private Long blockNumber;
  @NotNull
  private String value;

  public BigInteger getValue() {
    return new BigInteger(value);
  }

  public void setValue(BigInteger value) {
    this.value = value.toString();
  }
}
