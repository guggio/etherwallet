package com.guggio.etherwallet.service.balance;

import com.guggio.etherwallet.core.address.ValidatedAddress;
import com.guggio.etherwallet.core.asset.Balance;
import com.guggio.etherwallet.entity.address.EtherAddress;
import com.guggio.etherwallet.entity.balance.EtherBalance;
import com.guggio.etherwallet.repository.balance.EtherBalanceRepository;
import com.guggio.etherwallet.service.address.EtherAddressService;
import com.guggio.etherwallet.service.external.etherscan.EtherscanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
@Transactional
public class EtherBalanceService {

  private final EtherscanService etherscanService;
  private final EtherBalanceRepository etherBalanceRepository;
  private final EtherAddressService etherAddressService;

  public Balance getBalanceOf(ValidatedAddress address) {
    EtherAddress etherAddress = etherAddressService.loadOrCreateNew(address);
    EtherBalance savedBalance = saveEtherBalance(etherAddress);
    return Balance.of(savedBalance);
  }

  private EtherBalance saveEtherBalance(EtherAddress etherAddress) {
    EtherBalance etherBalance = createEtherBalance(etherAddress);
    return etherBalanceRepository.save(etherBalance);
  }

  private EtherBalance createEtherBalance(EtherAddress etherAddress) {
    BigInteger balance = getBalanceFromEtherscan(etherAddress);
    EtherBalance etherBalance = new EtherBalance();
    etherBalance.setEtherAddress(etherAddress);
    etherBalance.setTimestamp(getCurrentTimestamp());
    etherBalance.setValue(balance);
    return etherBalance;
  }

  private BigInteger getBalanceFromEtherscan(EtherAddress etherAddress) {
    return etherscanService.getBalanceOf(etherAddress.getAddress());
  }

  private long getCurrentTimestamp() {
    return LocalDateTime.now(ZoneId.of("UTC")).toEpochSecond(ZoneOffset.UTC);
  }
}
