export class PublicationFilter{
    pageNumber : number; 
    pageSize : number; 
    status : string[];
    title : string;
    accountEmail : string;
    minUcuCoins : number;
    maxUcuCoins : number;
    afterDate : Date;
    beforeDate : Date;
    description : string;
    idPublication : number;
}