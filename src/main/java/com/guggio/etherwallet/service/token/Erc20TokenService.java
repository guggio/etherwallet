package com.guggio.etherwallet.service.token;

import com.guggio.etherwallet.entity.token.Erc20Token;
import com.guggio.etherwallet.repository.token.Erc20TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class Erc20TokenService {

  private final Erc20TokenRepository erc20TokenRepository;

  public Erc20Token loadOrCreateNew(Erc20Token erc20Token) {
    return erc20TokenRepository.findById(erc20Token.getContractAddress())
        .orElseGet(() -> createNew(erc20Token));
  }

  private Erc20Token createNew(Erc20Token erc20Token) {
    return erc20TokenRepository.save(erc20Token);
  }
}
