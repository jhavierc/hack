import { Component, OnInit } from '@angular/core';
import { InvoiceModel } from '../shared/models/InvoiceModel.model';
import { SiigoService } from '../shared/Services/siigo.services';
import { Alerta } from '../shared/models/Alerta.model';

@Component({
  selector: 'app-listar-facturas',
  templateUrl: './listarfacturas.component.html',
  styleUrls: ['./listarfacturas.component.scss']
})
export class ListarFacturasComponent implements OnInit {

  listaFacturas:InvoiceModel[]=[];
  showMessage: boolean = false;
  alerta: Alerta;

  constructor(private _siigoServices:SiigoService) { 
    this.alerta = new Alerta();
    this._siigoServices.consultarFacturas().subscribe(
      (data)=>{
        if(data.codigo=='OK'){
          this.listaFacturas = data.object;
        } else {
          this.alerta.type= 'warning';
          this.alerta.strong= 'Warning!';
          this.alerta.message= data.mensaje;
          this.alerta.icon= 'ui-1_bell-53';
        }
      },(error)=>{
        this.alerta.type = 'danger';
        this.alerta.strong = 'Oh snap!';
        this.alerta.message = 'Error al consultar la información de las facturas del sistema';
        this.alerta.icon = 'ui-2_like';
      }
    );

  }

  ngOnInit() {
  }

  downloadAdjunto(id){
    this._siigoServices.descargarArchivoAdjunto(id);
  }
}
