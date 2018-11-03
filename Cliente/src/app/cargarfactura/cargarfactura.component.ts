import { Component, OnInit, ViewChild } from '@angular/core';
import { SiigoService } from '../shared/Services/siigo.services';
import { InvoiceModel } from '../shared/models/InvoiceModel.model';
import { Mensaje } from '../shared/models/Mensaje.model';
import { NgForm } from '@angular/forms';
import { Alert } from 'selenium-webdriver';
import { Alerta } from '../shared/models/Alerta.model';
import { Cliente } from '../shared/models/Cliente.model';

@Component({
  selector: 'app-cargarfactura',
  templateUrl: './cargarfactura.component.html',
  styleUrls: ['./cargarfactura.component.scss']
})
export class CargarfacturaComponent implements OnInit {

  @ViewChild('formFactura') formFactura: NgForm;
  invoice: InvoiceModel;
  mensaje: Mensaje;
  showMessage: boolean = false;
  infoUser: string = '';
  cliente:Cliente;

  alerta: Alerta;
  sendForm: boolean=false;

  constructor(private _siigoServices: SiigoService) {
    this.invoice = new InvoiceModel();
    this.cliente = new Cliente();
    this.cliente.id='2';
    this.cliente.nit='777';
  }

  ngOnInit() {
  }

  crearFactura() {

  }

  onSubmit() {
    this.sendForm = true;
    this.showMessage = true;
    if (this.formFactura.valid) {
      this.alerta = new Alerta();
      this.invoice.cliente = this.cliente;
      console.log(JSON.stringify(this.invoice));
      this._siigoServices.registrarFactura(this.invoice).subscribe(
        (data)=>{
          console.log(JSON.stringify(data));
          if(data.codigo=='OK'){
            this.alerta.type = 'success';
            this.alerta.strong = 'Well done!';
            this.alerta.message = data.mensaje;
            this.alerta.icon = 'ui-2_like';
          } else{
            this.alerta.type= 'warning';
            this.alerta.strong= 'Warning!';
            this.alerta.message= data.mensaje;
            this.alerta.icon= 'ui-1_bell-53';
          }
          this.sendForm = false;
        },(error)=>{
          console.log(JSON.stringify(error));
          this.alerta.type = 'danger';
          this.alerta.strong = 'Oh snap!';
          this.alerta.message = 'Error al registrar la Factura, los servicios no están disponibles';
          this.alerta.icon = 'ui-2_like';
        }
      );

     
      

      this.invoice = new InvoiceModel();

    } else {

      this.infoUser = "Debe ingresar la información marcada como obligatoria";
      this.alerta = new Alerta();
      this.alerta.type = 'danger';
      this.alerta.strong = 'Oh snap!';
      this.alerta.message = this.infoUser;
      this.alerta.icon = 'ui-2_like';
    }
  }

  onFileChange(event) {
    let reader = new FileReader();
    if (event.target.files && event.target.files.length > 0) {
      let file = event.target.files[0];
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.invoice.pdfFile = reader.result.split(',')[1];
        console.log("Nombre del archivo cargado " + file.name);
        console.log("Typo del archivo cargado " + file.type);
      };
    }
  }

  async delay(ms: number) {
    await new Promise(resolve => setTimeout(() => resolve(), ms)).then(() => console.log("fired"));
  }
}
