package com.guggio.etherwallet.entity.balance;

import com.guggio.etherwallet.entity.address.EtherAddress;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueAddressAndBlock", columnNames = {"address_id", "blockNumber"})})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class EtherBalance {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ether_balance_seq")
  @SequenceGenerator(name = "ether_balance_seq", sequenceName = "ether_balance_seq", allocationSize = 1)
  @Column(nullable = false)
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EtherBalance)) return false;
    EtherBalance that = (EtherBalance) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
