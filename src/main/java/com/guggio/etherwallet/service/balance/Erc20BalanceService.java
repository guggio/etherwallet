package com.guggio.etherwallet.service.balance;

import com.guggio.etherwallet.core.address.ValidatedAddress;
import com.guggio.etherwallet.core.asset.Balance;
import com.guggio.etherwallet.entity.address.EtherAddress;
import com.guggio.etherwallet.entity.balance.Erc20Balance;
import com.guggio.etherwallet.entity.token.Erc20Token;
import com.guggio.etherwallet.repository.balance.Erc20BalanceRepository;
import com.guggio.etherwallet.service.address.EtherAddressService;
import com.guggio.etherwallet.service.external.moralis.MoralisService;
import com.guggio.etherwallet.service.external.moralis.WalletTokenBalance;
import com.guggio.etherwallet.service.token.Erc20TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class Erc20BalanceService {

  private final EtherAddressService etherAddressService;
  private final Erc20TokenService erc20TokenService;
  private final Erc20BalanceRepository erc20BalanceRepository;
  private final MoralisService moralisService;

  public List<Balance> getBalancesOf(ValidatedAddress validatedAddress) {
    EtherAddress etherAddress = etherAddressService.loadOrCreateNew(validatedAddress);

    List<WalletTokenBalance> walletTokenBalances = moralisService.getErc20BalancesOf(etherAddress.getAddress()).orElseThrow();

    return walletTokenBalances.stream()
        .map(balance -> saveAsErc20Balance(etherAddress, balance))
        .collect(Collectors.toList());
  }

  private Balance saveAsErc20Balance(EtherAddress etherAddress, WalletTokenBalance balance) {
    Erc20Balance erc20Balance = createErc20Balance(etherAddress, balance);
    Erc20Balance savedBalance = saveErc20Balance(erc20Balance);
    return Balance.of(savedBalance);
  }

  private Erc20Balance createErc20Balance(EtherAddress etherAddress, WalletTokenBalance balance) {
    Erc20Balance erc20Balance = new Erc20Balance();
    erc20Balance.setEtherAddress(etherAddress);
    erc20Balance.setErc20Token(erc20TokenService.loadOrCreateNew(Erc20Token.of(balance)));
    erc20Balance.setValue(balance.getBalance());
    erc20Balance.setTimestamp(getCurrentTimestamp());
    return erc20Balance;
  }

  private Erc20Balance saveErc20Balance(Erc20Balance erc20Balance) {
    return erc20BalanceRepository.save(erc20Balance);
  }

  private long getCurrentTimestamp() {
    return LocalDateTime.now(ZoneId.of("UTC")).toEpochSecond(ZoneOffset.UTC);
  }

}
