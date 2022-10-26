package com.guggio.etherwallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guggio.etherwallet.core.address.ValidatedAddress;
import com.guggio.etherwallet.core.asset.Balance;
import com.guggio.etherwallet.core.asset.BalanceImpl;
import com.guggio.etherwallet.entity.address.EtherAddress;
import com.guggio.etherwallet.entity.token.Erc20Token;
import com.guggio.etherwallet.repository.address.EtherAddressRepository;
import com.guggio.etherwallet.repository.token.Erc20TokenRepository;
import com.guggio.etherwallet.service.external.moralis.MoralisService;
import com.guggio.etherwallet.service.external.moralis.NativeBalance;
import com.guggio.etherwallet.service.external.moralis.WalletTokenBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.lang.NonNull;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BalanceControllerTest {

  private static final String GET_ETH_BALANCE_URI_TEMPLATE = "/balance/ether/{address}";
  private static final String GET_ERC_20_BALANCES_TEMPLATE = "/balance/erc20/all/{address}";

  @Autowired
  MockMvc mockMvc;

  @MockBean
  MoralisService moralisService;

  @Autowired
  EtherAddressRepository etherAddressRepository;

  @Nested
  class TestGetEthBalance {

    @Test
    void shouldRetrieveEtherBalanceAndCreateNewEtherAddressRecordGivenValidNewAddress() throws Exception {
      String address = "0xe1cc2f34832C7023254A11e76eB91Be0d316262a";
      BigInteger valueInWei = new BigInteger("1000000000000000000");
      when(moralisService.getNativeBalanceOf(any())).thenReturn(Optional.of(createNativeBalance(valueInWei)));

      // precondition, that EtherAddress does not yet exist in db
      assertFalse(etherAddressRepository.existsById(address));

      BalanceImpl expectedBalance = new BalanceImpl("Ethereum", "ETH", valueInWei, new BigDecimal("1.00000"));
      mockMvc.perform(MockMvcRequestBuilders.get(GET_ETH_BALANCE_URI_TEMPLATE, address))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(result -> assertEthBalanceResponse(result, expectedBalance));
      assertTrue(etherAddressRepository.existsById(address));
      verifyGetNativeBalanceCalled(address);
    }

    @Test
    void shouldRetrieveEtherBalanceAndNotCreateNewEtherAddressRecordGivenValidExistingAddress() throws Exception {
      String address = "0xe1cc2f34832C7023254A11e76eB91Be0d316262a";
      BigInteger valueInWei = new BigInteger("1000000000000000000");
      when(moralisService.getNativeBalanceOf(any())).thenReturn(Optional.of(createNativeBalance(valueInWei)));

      // precondition, that EtherAddress does exist in db
      etherAddressRepository.save(createEtherAddress(address));
      assertTrue(etherAddressRepository.existsById(address));

      BalanceImpl expectedBalance = new BalanceImpl("Ethereum", "ETH", valueInWei, new BigDecimal("1.00000"));
      mockMvc.perform(MockMvcRequestBuilders.get(GET_ETH_BALANCE_URI_TEMPLATE, address))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(result -> assertEthBalanceResponse(result, expectedBalance));
      assertTrue(etherAddressRepository.existsById(address));
      verifyGetNativeBalanceCalled(address);
    }

    @Test
    void shouldRespondWithErrorWhenGettingEtherBalanceGivenInvalidAddress() throws Exception {
      String address = "0xe1cc2f34832C7023254A11e76eB91Be0d316262";

      // precondition, that EtherAddress does not yet exist in db
      assertFalse(etherAddressRepository.existsById(address));

      mockMvc.perform(MockMvcRequestBuilders.get(GET_ETH_BALANCE_URI_TEMPLATE, address))
          .andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(this::assertInvalidEthBalanceResponse);

      // no record was created for invalid address
      assertFalse(etherAddressRepository.existsById(address));
      verify(moralisService, never()).getNativeBalanceOf(any());
    }


    private void assertEthBalanceResponse(@NonNull MvcResult result, @NonNull Balance expectedBalance) throws UnsupportedEncodingException, JsonProcessingException {
      EtherBalanceResponse etherBalanceResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), EtherBalanceResponse.class);
      assertNotNull(etherBalanceResponse);
      assertEtherBalance(etherBalanceResponse, expectedBalance);
    }

    private void assertEtherBalance(@NonNull EtherBalanceResponse etherBalanceResponse, @NonNull Balance expectedBalance) {
      assertNotNull(etherBalanceResponse);
      assertEquals("ETH balance retrieved", etherBalanceResponse.getMessage());
      assertEquals(expectedBalance, etherBalanceResponse.getBalance());
    }

    private void assertInvalidEthBalanceResponse(@NonNull MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
      EtherBalanceResponse etherBalanceResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), EtherBalanceResponse.class);
      assertNotNull(etherBalanceResponse);
      assertEquals("Invalid address format", etherBalanceResponse.getMessage());
      assertNull(etherBalanceResponse.getBalance());
    }

    private void verifyGetNativeBalanceCalled(@NonNull String address) {
      ArgumentCaptor<ValidatedAddress> argumentCaptor = ArgumentCaptor.forClass(ValidatedAddress.class);
      verify(moralisService).getNativeBalanceOf(argumentCaptor.capture());
      ValidatedAddress validatedAddress = argumentCaptor.getValue();
      assertNotNull(validatedAddress);
      assertEquals(address, validatedAddress.getAddress());
    }

    @NonNull
    private NativeBalance createNativeBalance(@NonNull BigInteger balance) {
      NativeBalance nativeBalance = new NativeBalance();
      nativeBalance.setBalance(balance);
      return nativeBalance;
    }

  }

  @Nested
  class TestGetErc20Balances {

    @Autowired
    Erc20TokenRepository erc20TokenRepository;

    private int wethDecimals;
    private int usdcDecimals;
    private String wethAddress;
    private String usdcAddress;
    private String wethName;
    private String wethSymbol;
    private String usdcName;
    private String usdcSymbol;

    @BeforeEach
    void setUp() {
      wethDecimals = 18;
      usdcDecimals = 6;
      wethAddress = "0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2";
      usdcAddress = "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48";
      wethName = "Wrapped Ether";
      wethSymbol = "WETH";
      usdcName = "USDC Coin";
      usdcSymbol = "USDC";
    }

    @Test
    void shouldRetrieveErc20BalancesAndCreateNewEtherAddressRecordGivenValidNewAddress() throws Exception {
      String address = "0xe1cc2f34832C7023254A11e76eB91Be0d316262a";

      BigInteger wethBalance = new BigInteger("1000000000000000000");
      BigInteger usdcBalance = new BigInteger("1000000000");

      List<WalletTokenBalance> walletTokenBalances = List.of(
          createWalletTokenBalance(wethAddress, wethName, wethSymbol, wethDecimals, wethBalance),
          createWalletTokenBalance(usdcAddress, usdcName, usdcSymbol, usdcDecimals, usdcBalance)
      );
      when(moralisService.getErc20BalancesOf(any())).thenReturn(Optional.of(walletTokenBalances));

      // precondition, that neither EtherAddress nor Tokens exist in db
      assertFalse(etherAddressRepository.existsById(address));
      assertFalse(erc20TokenRepository.existsById(wethAddress));
      assertFalse(erc20TokenRepository.existsById(usdcAddress));

      mockMvc.perform(MockMvcRequestBuilders.get(GET_ERC_20_BALANCES_TEMPLATE, address))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(result -> assertValidErc20BalancesResponse(result, List.of(
              new BalanceImpl(wethName, wethSymbol, wethBalance, new BigDecimal("1.00000")),
              new BalanceImpl(usdcName, usdcSymbol, usdcBalance, new BigDecimal("1000.00000"))
          )));

      assertTrue(etherAddressRepository.existsById(address));
      assertTrue(erc20TokenRepository.existsById(wethAddress));
      assertTrue(erc20TokenRepository.existsById(usdcAddress));
      verifyGetErc20BalancesCalled(address);
    }

    @Test
    void shouldRetrieveErc20BalancesAndNotCreateNewEtherAddressRecordGivenValidExistingAddress() throws Exception {
      String address = "0xe1cc2f34832C7023254A11e76eB91Be0d316262a";

      BigInteger wethBalance = new BigInteger("1000000000000000000");
      BigInteger usdcBalance = new BigInteger("1000000000");

      List<WalletTokenBalance> walletTokenBalances = List.of(
          createWalletTokenBalance(wethAddress, wethName, wethSymbol, wethDecimals, wethBalance),
          createWalletTokenBalance(usdcAddress, usdcName, usdcSymbol, usdcDecimals, usdcBalance)
      );
      when(moralisService.getErc20BalancesOf(any())).thenReturn(Optional.of(walletTokenBalances));

      // precondition, that EtherAddress and Tokens exist in db
      etherAddressRepository.save(createEtherAddress(address));
      assertTrue(etherAddressRepository.existsById(address));
      erc20TokenRepository.save(createErc20Token(wethAddress, wethName, wethSymbol, wethDecimals));
      assertTrue(erc20TokenRepository.existsById(wethAddress));
      erc20TokenRepository.save(createErc20Token(usdcAddress, usdcName, usdcSymbol, usdcDecimals));
      assertTrue(erc20TokenRepository.existsById(usdcAddress));

      mockMvc.perform(MockMvcRequestBuilders.get(GET_ERC_20_BALANCES_TEMPLATE, address))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(result -> assertValidErc20BalancesResponse(result, List.of(
              new BalanceImpl(wethName, wethSymbol, wethBalance, new BigDecimal("1.00000")),
              new BalanceImpl(usdcName, usdcSymbol, usdcBalance, new BigDecimal("1000.00000"))
          )));

      assertTrue(etherAddressRepository.existsById(address));
      assertTrue(erc20TokenRepository.existsById(wethAddress));
      assertTrue(erc20TokenRepository.existsById(usdcAddress));
      verifyGetErc20BalancesCalled(address);
    }

    @Test
    void shouldRespondWithErrorGivenInvalidAddress() throws Exception {
      String address = "0xe1cc2f34832C7023254A11e76eB91Be0d316262";

      // precondition, that EtherAddress does not yet exist in db
      assertFalse(etherAddressRepository.existsById(address));

      mockMvc.perform(MockMvcRequestBuilders.get(GET_ERC_20_BALANCES_TEMPLATE, address))
          .andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(this::assertInvalidErc20BalancesResponse);

      // no record was created for invalid address
      assertFalse(etherAddressRepository.existsById(address));
      verify(moralisService, never()).getErc20BalancesOf(any());
    }

    private void assertValidErc20BalancesResponse(@NonNull MvcResult result, @NonNull List<Balance> expectedBalances) throws UnsupportedEncodingException, JsonProcessingException {
      Erc20BalancesResponse erc20BalancesResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Erc20BalancesResponse.class);
      assertNotNull(erc20BalancesResponse);
      assertEquals("Erc20 balances retrieved", erc20BalancesResponse.getMessage());
      List<Balance> balances = erc20BalancesResponse.getBalances();
      assertEquals(expectedBalances.size(), balances.size());
      assertIterableEquals(expectedBalances, balances);
    }

    private void assertInvalidErc20BalancesResponse(@NonNull MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
      Erc20BalancesResponse erc20BalancesResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Erc20BalancesResponse.class);
      assertNotNull(erc20BalancesResponse);
      assertEquals("Invalid address format", erc20BalancesResponse.getMessage());
      assertNull(erc20BalancesResponse.getBalances());
    }

    private void verifyGetErc20BalancesCalled(@NonNull String address) {
      ArgumentCaptor<ValidatedAddress> argumentCaptor = ArgumentCaptor.forClass(ValidatedAddress.class);
      verify(moralisService).getErc20BalancesOf(argumentCaptor.capture());
      ValidatedAddress validatedAddress = argumentCaptor.getValue();
      assertNotNull(validatedAddress);
      assertEquals(address, validatedAddress.getAddress());
    }

    @NonNull
    private Erc20Token createErc20Token(@NonNull String contractAddress, @NonNull String name, @NonNull String symbol, int decimals) {
      Erc20Token erc20Token = new Erc20Token();
      erc20Token.setContractAddress(contractAddress);
      erc20Token.setName(name);
      erc20Token.setSymbol(symbol);
      erc20Token.setDecimalPlaces(decimals);
      return erc20Token;
    }

    @NonNull
    private WalletTokenBalance createWalletTokenBalance(@NonNull String tokenAddress, @NonNull String name, @NonNull String symbol, int decimals, @NonNull BigInteger balance) {
      WalletTokenBalance walletTokenBalance = new WalletTokenBalance();
      walletTokenBalance.setToken_address(tokenAddress);
      walletTokenBalance.setName(name);
      walletTokenBalance.setSymbol(symbol);
      walletTokenBalance.setDecimals(decimals);
      walletTokenBalance.setBalance(balance);
      return walletTokenBalance;
    }

  }

  @NonNull
  private EtherAddress createEtherAddress(String address) {
    return EtherAddress.of(ValidatedAddress.of(address).orElseThrow());
  }

}