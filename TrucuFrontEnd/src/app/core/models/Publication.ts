import { Image } from "./Image";

export class Publication {
    idPublication : number;
    title : string;
    description : string;
    ucuCoinValue : number;
    publicationDate : Date;
    status : string;
    accountCI : string;
    images : Image[];
}