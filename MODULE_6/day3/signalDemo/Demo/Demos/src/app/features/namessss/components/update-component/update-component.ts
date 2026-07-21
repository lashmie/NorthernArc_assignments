import { Component, inject } from '@angular/core';
import { NameServices } from '../../services/name-services';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-component',
  imports: [FormsModule],
  templateUrl: './update-component.html',
  styleUrl: './update-component.css',
})
export class UpdateComponent {
  nameservices: NameServices = inject(NameServices);
  oldName: string = '';
  newName: string = '';

  updateName(oldName: string, newName: string) {
    this.nameservices.updateName(oldName, newName);
    this.oldName = '';
    this.newName = '';
  }
}
