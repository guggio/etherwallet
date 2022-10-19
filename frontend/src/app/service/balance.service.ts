import { Injectable } from '@angular/core';
import { catchError, map, Observable, throwError } from 'rxjs';
import { Balance } from '../model/balance';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { EtherBalanceResponse } from '../model/ether-balance-response';
import { Erc20BalancesResponse } from '../model/erc20-balances-response';

@Injectable({
  providedIn: 'root',
})
export class BalanceService {
  private readonly apiUrl = 'http://localhost:8080/balance';

  constructor(private http: HttpClient) {}

  public getEtherBalance(address: string): Observable<Balance> {
    return this.http
      .get<EtherBalanceResponse>(`${this.apiUrl}/ether/${address}`)
      .pipe(
        map((response) => response.balance),
        catchError(this.handleError)
      );
  }

  public getErc20Balances(address: string): Observable<Balance[]> {
    return this.http
      .get<Erc20BalancesResponse>(`${this.apiUrl}/erc20/all/${address}`)
      .pipe(
        map((response) => response.balances),
        catchError(this.handleError)
      );
  }
  handleError(handleError: HttpErrorResponse): Observable<never> {
    // TODO guggio -> intelligent error handling
    console.log(handleError);
    throw new Error('Method not implemented.');
  }
}
