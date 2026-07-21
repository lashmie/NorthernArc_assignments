import { Component, inject } from '@angular/core';
import { PeopleSservice } from '../../services/people-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-component',
  imports: [FormsModule],
  templateUrl: './update-component.html',
  styleUrl: './update-component.css',
})
export class UpdateComponent {
  //updated component is used to update the name of the person in the list
  protected oldName: string = '';
  protected newName: string = '';
  peopleService: PeopleSservice = inject(PeopleSservice);
  updatePeople(oldName: string, newName: string) {
    if (oldName.trim() !== '' && newName.trim() !== '') {
      this.peopleService.updatePerson(oldName, newName);
    }
  }
}
