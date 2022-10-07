package com.guggio.etherwallet.entity.balance;

import com.guggio.etherwallet.entity.address.EtherAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueAddressAndBlock", columnNames = {"owner_address", "blockNumber"})})
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
  @JoinColumn(name = "owner_address", nullable = false, foreignKey = @ForeignKey(name = "eth_balance_owner_address"))
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
