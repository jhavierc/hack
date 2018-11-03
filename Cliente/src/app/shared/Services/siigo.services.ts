import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import 'rxjs/Rx';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../../environments/environment';
import { InvoiceModel } from '../models/InvoiceModel.model';
import { Mensaje } from '../models/Mensaje.model';
import { ConstantesSiigo } from '../constantes';


@Injectable()
export class SiigoService {
    
    api: string;

    constructor(private httpClient: HttpClient) {
        this.api = environment.api;
    }

    registrarFactura(invoice:InvoiceModel):Observable<Mensaje>{
        return this.httpClient.post<Mensaje>(this.api + ConstantesSiigo.WS_REGISTRAR_FACTURA, invoice);
    }

}