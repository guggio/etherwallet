package com.guggio.etherwallet.service.external.moralis;

import com.guggio.etherwallet.core.address.ValidatedAddress;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class MoralisService {

  private static final String DEFAULT_KEY = "test";

  public Optional<NativeBalance> getNativeBalanceOf(ValidatedAddress address) {
    return createWebClient()
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{address}")
            .path("/balance")
            .queryParamIfPresent("chain", Optional.of("eth"))
            .queryParamIfPresent("providerUrl", Optional.empty())
            .queryParamIfPresent("to_block", Optional.empty())
            .build(address.getAddress()))
        .header("X-API-Key", getApiKey())
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(NativeBalance.class)
        .blockOptional();
  }

  public Optional<List<WalletTokenBalance>> getErc20BalancesOf(ValidatedAddress address) {
    return createWebClient()
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{address}")
            .path("/erc20")
            .queryParamIfPresent("chain", Optional.of("eth"))
            .queryParamIfPresent("subdomain", Optional.empty())
            .queryParamIfPresent("to_block", Optional.empty())
            .queryParamIfPresent("token_addresses", Optional.empty())
            .build(address.getAddress()))
        .header("X-API-Key", getApiKey())
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToFlux(WalletTokenBalance.class)
        .collectList()
        .blockOptional();
  }

  private WebClient createWebClient() {
    return WebClient.builder()
        .baseUrl("https://deep-index.moralis.io/api/v2")
        .build();
  }

  private String getApiKey() {
    final String key = System.getenv("X-API-Key");
    return (key == null || key.isEmpty())
        ? DEFAULT_KEY
        : key;
  }
}
