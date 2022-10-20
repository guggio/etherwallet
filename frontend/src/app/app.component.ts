import { ChangeDetectionStrategy, Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Balance } from './model/balance';
import { BalanceService } from './service/balance.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent {
  title = 'Eth Balances';
  address: string = '';
  ethBalance$: Observable<Balance> = new Observable();
  erc20Balances$: Observable<Balance[]> = new Observable();

  constructor(private balanceService: BalanceService) {}

  onSubmit() {
    this.updateBalances();
  }

  private updateBalances() {
    this.updateEthBalance();
    this.updateErc20Balances();
  }

  private updateEthBalance() {
    this.ethBalance$ = this.balanceService.getEtherBalance$(this.address);
  }

  private updateErc20Balances() {
    this.erc20Balances$ = this.balanceService.getErc20Balances$(this.address);
  }
}
