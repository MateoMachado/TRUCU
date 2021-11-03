import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Publication } from '../models/Publication';
import { PublicationFilter } from '../models/PublicationFilter';
import { Page } from '../models/Page';
import { Account } from '../models/Account';

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

  Login(emailAndPassword : any){
    return <Observable<Account>> this.Get('account/login',true, emailAndPassword);
  }

}