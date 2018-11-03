import { Component, OnInit } from '@angular/core';
import { InvoiceModel } from '../shared/models/InvoiceModel.model';
import { SiigoService } from '../shared/Services/siigo.services';
import { Alerta } from '../shared/models/Alerta.model';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { Log } from '../shared/models/Log.model';


@Component({
  selector: 'app-listar-facturas',
  templateUrl: './listarfacturas.component.html',
  styleUrls: ['./listarfacturas.component.scss']
})
export class ListarFacturasComponent implements OnInit {

  listaFacturas: InvoiceModel[] = [];
  showMessage: boolean = false;
  alerta: Alerta;
  closeResult: string;
  listaLog: Log[] = [];
  showDetail: boolean = false;
  facturaSelected: InvoiceModel;

  constructor(private _siigoServices: SiigoService, private modalService: NgbModal) {
    this.alerta = new Alerta();
    this.showMessage = true;
    this._siigoServices.consultarFacturas().subscribe(
      (data) => {
        if (data.codigo == 'OK') {
          this.listaFacturas = data.object;
        } else {
          this.alerta.type = 'warning';
          this.alerta.strong = 'Warning!';
          this.alerta.message = data.mensaje;
          this.alerta.icon = 'ui-1_bell-53';
        }
      }, (error) => {
        this.alerta.type = 'danger';
        this.alerta.strong = 'Oh snap!';
        this.alerta.message = 'Error al consultar la información de las facturas del sistema';
        this.alerta.icon = 'ui-2_like';
      }
    );

  }

  ngOnInit() {
  }

  downloadAdjunto(id) {
    this._siigoServices.descargarArchivoAdjunto(id);
  }

  verDetalleLog(data) {
    this.showMessage = true;
    this.facturaSelected = data;
    this._siigoServices.consultarLogs(data.id).subscribe(
      (data) => {
        if (data.codigo == 'OK') {
          this.listaLog = data.object;
          //this.open("Log Facturacion", null, null);
          this.showDetail = true;

        } else {
          this.alerta.type = 'warning';
          this.alerta.strong = 'Warning!';
          this.alerta.message = data.mensaje;
          this.alerta.icon = 'ui-1_bell-53';
        }
      },
      (error) => {
        this.alerta.type = 'danger';
        this.alerta.strong = 'Oh snap!';
        this.alerta.message = 'Error al consultar la información de las facturas del sistema';
        this.alerta.icon = 'ui-2_like';
      }
    )

  }

  volverLista() {
    this.facturaSelected = null;
    this.listaLog = [];
    this.showDetail = false;
  }

  open(content, type, modalDimension) {
    if (modalDimension === 'sm' && type === 'modal_mini') {
      this.modalService.open(content, { windowClass: 'modal-mini modal-primary', size: 'sm' }).result.then((result) => {
        this.closeResult = `Closed with: ${result}`;
      }, (reason) => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      });
    } else if (modalDimension == undefined && type === 'Login') {
      this.modalService.open(content, { windowClass: 'modal-login modal-primary' }).result.then((result) => {
        this.closeResult = `Closed with: ${result}`;
      }, (reason) => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      });
    } else {
      this.modalService.open(content).result.then((result) => {
        this.closeResult = `Closed with: ${result}`;
      }, (reason) => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      });
    }

  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  reprocesarDian() {
    this.showMessage = true;
    this._siigoServices.reprocesarFacturasDian().subscribe(
      (data) => {
        if (data.codigo == 'OK') {
          this.alerta.type = 'success';
          this.alerta.strong = 'Warning!';
          this.alerta.message = data.mensaje;
        } else {
          this.alerta.type = 'warning';
          this.alerta.strong = 'Warning!';
          this.alerta.icon = 'ui-1_bell-53';
        }
      }, (error) => {
        this.alerta.type = 'danger';
        this.alerta.strong = 'Oh snap!';
        this.alerta.message = 'Error al consultar la información de las facturas del sistema';
        this.alerta.icon = 'ui-2_like';
      }
    );
  }
}
