import { Cliente } from "./Cliente.model";

export class InvoiceModel{
    public id:number;
    public prefix:string;
    public consecutive:number;
    public ubl:string;
    public pdfFile:string;
    public createddate: string;
    public cliente:Cliente;
    public state:String;
    public tieneAdjunto:boolean;
}