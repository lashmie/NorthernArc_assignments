import { Component, inject } from '@angular/core';
import { NameServices } from '../../services/name-services';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-component',
  imports: [FormsModule],
  templateUrl: './add-component.html',
  styleUrl: './add-component.css',
})
export class AddComponent {

  // nameservices: NameServices = new NameServices();
nameservices: NameServices = inject(NameServices);
  newName: string = '';

  addName(name: string) {
      console.log(name);
    this.nameservices.addName(name);
    this.newName = '';
    
  }
}