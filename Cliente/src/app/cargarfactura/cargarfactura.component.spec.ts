import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CargarfacturaComponent } from './cargarfactura.component';

describe('CargarfacturaComponent', () => {
  let component: CargarfacturaComponent;
  let fixture: ComponentFixture<CargarfacturaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CargarfacturaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CargarfacturaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
