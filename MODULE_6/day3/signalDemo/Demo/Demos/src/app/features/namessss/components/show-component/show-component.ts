import { Component, inject } from '@angular/core';
import { NameServices } from '../../services/name-services';

@Component({
  selector: 'app-show-component',
  imports: [],
  templateUrl: './show-component.html',
  styleUrl: './show-component.css',
})
export class ShowComponent {
  nameservices: NameServices = inject(NameServices);
}
