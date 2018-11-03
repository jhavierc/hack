import { Component, OnInit } from '@angular/core';
import { SiigoService } from '../shared/Services/siigo.services';
import { InvoiceModel } from '../shared/models/InvoiceModel.model';
import { Mensaje } from '../shared/models/Mensaje.model';

@Component({
  selector: 'app-cargarfactura',
  templateUrl: './cargarfactura.component.html',
  styleUrls: ['./cargarfactura.component.scss']
})
export class CargarfacturaComponent implements OnInit {

  invoice:InvoiceModel;
  mensaje:Mensaje;

  constructor(private _siigoServices:SiigoService) {
    this.invoice = new InvoiceModel();
  }

  ngOnInit() {
  }

  crearFactura(){

  }

  onSubmit(){
    
  }

  onFileChange(event) {
    let reader = new FileReader();
    if (event.target.files && event.target.files.length > 0) {
      let file = event.target.files[0];
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.invoice.pdfFile= reader.result.split(',')[1];
        console.log("Nombre del archivo cargado "+file.name);
        console.log("Typo del archivo cargado "+file.type);
      };
    }
  }
}
