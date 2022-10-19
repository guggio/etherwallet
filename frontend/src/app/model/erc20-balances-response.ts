import { Balance } from './balance';

export interface Erc20BalancesResponse {
  message: string;
  balances: Balance[];
}
