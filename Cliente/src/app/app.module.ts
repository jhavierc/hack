import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // this is needed!
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app.routing';
import { ComponentsModule } from './components/components.module';
import { ExamplesModule } from './examples/examples.module';
import { HttpModule } from '@angular/http';
import { AppComponent } from './app.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { SiigoService } from './shared/Services/siigo.services';
import { HttpClientModule,HttpClient } from '@angular/common/http';

@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent
    ],
    imports: [ReactiveFormsModule,
        BrowserAnimationsModule,
        NgbModule.forRoot(),
        FormsModule,
        HttpModule,
        RouterModule,
        AppRoutingModule,
        ComponentsModule,
        ExamplesModule,
        HttpClientModule,
    ],
    providers: [SiigoService],
    bootstrap: [AppComponent]
})
export class AppModule { }
