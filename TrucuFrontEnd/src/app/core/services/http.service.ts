import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Publication } from '../models/Publication';
import { PublicationFilter } from '../models/PublicationFilter';
import { Page } from '../models/Page';
import { Account } from '../models/Account';
import { OfferWrapper } from '../models/OfferWrapper';
import { Reason } from '../models/Reason';
import { Report } from '../models/Report';

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  baseUrl='http://localhost:8080/trucu/';//Local

  constructor(private http:HttpClient) { }

  errorHandl(error:any) {
    let errorMessage = '';
    if(error.error instanceof ErrorEvent) {
      // Get client-side error
      errorMessage = error.error.message;
    } else {
      // Get server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(errorMessage);
  }

  Get(path: string, ignoreLoadingBar?: boolean, params?: HttpParams): Observable<Object> {
    if (ignoreLoadingBar) {
      return this.http.get<Object>(this.baseUrl + path, {
        headers: { ignoreLoadingBar: 'true' },
        params: params
      })
        .pipe(
          catchError(this.errorHandl)
        );
    } else {
      return this.http.get<Object>(this.baseUrl + path, {params: params})
        .pipe(
          catchError(this.errorHandl)
        );
    }
  }

  Post(path: string, obj: Object, ignoreLoadingBar?: boolean): Observable<Object> {
    if (ignoreLoadingBar) {
      return this.http.post<Object>(this.baseUrl + path, {
        headers: { ignoreLoadingBar: 'true' }
      }, obj)
        .pipe(
          catchError(this.errorHandl)
          );
    } else {
      return this.http.post<Object>(this.baseUrl + path, obj)
        .pipe(
          catchError(this.errorHandl)
          );
    }
  }

  Put(path: string, obj: Object, ignoreLoadingBar?: boolean): Observable<Object> {
    if (ignoreLoadingBar) {
      return this.http.put<Object>(this.baseUrl + path, {
        headers: { ignoreLoadingBar: 'true' }
      }, obj)
        .pipe(
          catchError(this.errorHandl)
          );
    } else {
      return this.http.put<Object>(this.baseUrl + path, obj)
        .pipe(
          catchError(this.errorHandl)
          );
    }
  }

  GetPublications(publicationFilter : any){
    return <Observable<Page>> this.Get('publication/filter',true, publicationFilter);
  }

  GetReportedPublications(publicationFilter : any){
    return <Observable<Page>> this.Get('report/getReportedPublications',true, publicationFilter);
  }

  GetReportReasons(idPublication : any){
    return <Observable<any>> this.Get('report/reportReasons?idPublication='+idPublication,true);
  }

  GetOffers(offerFilter : any){
    return <Observable<Page>> this.Get('offer/filter',true, offerFilter);
  }

  GetAccount(accountEmail : any){
    return <Observable<Account>> this.Get('account/get?email='+accountEmail,true);
  }

  Login(emailAndPassword : any){
    return <Observable<any>> this.Get('account/login',true, emailAndPassword);
  }

  Register(user : any){
    return <Observable<any>> this.http.post<Account>(this.baseUrl + 'account/create', user);
  }

  CreatePublication(publication : any){
    return <Observable<any>> this.http.post<Publication>(this.baseUrl + 'publication/create', publication);
  }

  CreateOffer(idPublication : number, idPublications :number[]){
    return <Observable<string>> this.http.post(this.baseUrl + 'offer/create?idPublication='+idPublication , idPublications);
  }

  AcceptOffer(idOffer : number){
    return <Observable<string>> this.http.post(this.baseUrl + 'offer/accept?idOffer='+idOffer , null);
  }

  RejectOffer(idOffer : number){
    return <Observable<string>> this.http.post(this.baseUrl + 'offer/reject?idOffer='+idOffer , null);
  }

  CancelOffer(idOffer : number){
    return <Observable<string>> this.http.post(this.baseUrl + 'offer/cancel?idOffer='+idOffer , null);
  }

  RevertOffer(idOffer : number){
    return <Observable<string>> this.http.post(this.baseUrl + 'offer/revert?idOffer='+idOffer , null);
  }

  CloseOffer(idOffer : number){
    return <Observable<string>> this.http.post(this.baseUrl + 'offer/close?idOffer='+idOffer , null);
  }

  MakeCounterOffer(idOffer : number, idPublications : number[]){
    return <Observable<string>> this.http.post(this.baseUrl + 'offer/counterOffer?idOffer='+idOffer , idPublications);
  }

  AcceptCounterOffer(idOffer : number){
    return <Observable<string>> this.http.post(this.baseUrl + 'offer/counterOffer/accept?idOffer='+idOffer , null);
  }

  RejectCounterOffer(idOffer : number){
    return <Observable<string>> this.http.post(this.baseUrl + 'offer/counterOffer/reject?idOffer='+idOffer , null);
  }
 
  GetReasons(){
    return <Observable<Reason[]>> this.Get('report/reasons',true);
  }

  CreateReport(report : Report){
    return <Observable<string>> this.http.post(this.baseUrl + 'report/create', report);
  }

  AcceptReport(idPublication : number){
    return <Observable<string>> this.http.post(this.baseUrl + 'report/acceptReport?idPublication='+idPublication, null);
  }

  CancelReport(idPublication : number){
    return <Observable<string>> this.http.post(this.baseUrl + 'report/cancelReport?idPublication='+idPublication, null);
  }

  CancelPublication(idPublication : number){
    return <Observable<string>> this.http.post(this.baseUrl + 'publication/cancel?idPublication='+idPublication, null);
  }

  HidePublication(idPublication : number){
    return <Observable<string>> this.http.post(this.baseUrl + 'publication/cancel?idPublication='+idPublication, null);
  }

  
}