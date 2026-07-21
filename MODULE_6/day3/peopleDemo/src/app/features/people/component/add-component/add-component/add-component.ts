import { Component, inject } from '@angular/core';
import { PeopleSservice } from '../../../services/people-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-component',
  imports: [FormsModule],
  templateUrl: './add-component.html',
  styleUrl: './add-component.css',
})
export class AddComponent {
protected name: string = '';
  peopleService: PeopleSservice = inject(PeopleSservice);

  addPeople() {
    if (this.name.trim() !== '') {
      this.peopleService.addPerson(this.name);
      this.name = ''; // Clear the input field after adding
    }
  }
}
