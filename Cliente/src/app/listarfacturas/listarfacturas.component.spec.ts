import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarFacturasComponent } from './listarfacturas.component';

describe('ListarFacturasComponent', () => {
  let component: ListarFacturasComponent;
  let fixture: ComponentFixture<ListarFacturasComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListarFacturasComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListarFacturasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
