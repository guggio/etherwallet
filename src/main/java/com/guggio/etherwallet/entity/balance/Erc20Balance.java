package com.guggio.etherwallet.entity.balance;

import com.guggio.etherwallet.entity.address.EtherAddress;
import com.guggio.etherwallet.entity.token.Erc20Token;
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
@Table(name = "Erc20Balance", uniqueConstraints = {
    @UniqueConstraint(name = "uc_erc20balance_owner_address", columnNames = {"owner_address", "erc20_token_id", "timestamp"})
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Erc20Balance {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "erc20_balance_seq")
  @SequenceGenerator(name = "erc20_balance_seq", sequenceName = "erc20_balance_seq", allocationSize = 1)
  @Column(nullable = false)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "owner_address", nullable = false, foreignKey = @ForeignKey(name = "erc20_balance_owner_address"))
  private EtherAddress etherAddress;
  @ManyToOne
  @JoinColumn(name = "erc20_token_id", nullable = false, foreignKey = @ForeignKey(name = "erc20_balance_token_id"))
  private Erc20Token erc20Token;
  @NotNull
  private Long timestamp;
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
    if (!(o instanceof Erc20Balance)) return false;
    Erc20Balance that = (Erc20Balance) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
