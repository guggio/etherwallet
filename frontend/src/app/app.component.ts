import { Component, OnInit } from '@angular/core';
import { Balance } from './model/balance';
import { BalanceType } from './model/enum/balance-type';
import { BalanceService } from './service/balance.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'Eth Balances';
  address: string = '';
  ethBalance: Balance = {} as Balance;
  erc20Balances: Balance[] = [];
  balanceType: BalanceType = BalanceType.ALL;

  constructor(private balanceService: BalanceService) {}

  onSubmit() {
    this.updateBalances();
  }

  private updateBalances() {
    this.updateEthBalance();
    this.updateErc20Balances();
  }

  private updateEthBalance() {
    this.balanceService
      .getEtherBalance(this.address)
      .subscribe((data) => (this.ethBalance = data));
  }

  private updateErc20Balances() {
    this.balanceService
      .getErc20Balances(this.address)
      .subscribe((data) => (this.erc20Balances = data));
  }

  get allBalances(): Balance[] {
    if(this.balanceType == BalanceType.ALL) {
      return [this.ethBalance, ...this.erc20Balances];
    } else if (this.balanceType == BalanceType.ETH) {
      return [this.ethBalance];
    } else if (this.balanceType == BalanceType.ERC20) {
      return [...this.erc20Balances]
    } else {
      throw new Error(`Unknown Balancetype ${this.balanceType} selected!`);
    }
  }

  filterType($event: BalanceType) {
    this.balanceType = $event;
  }
}
