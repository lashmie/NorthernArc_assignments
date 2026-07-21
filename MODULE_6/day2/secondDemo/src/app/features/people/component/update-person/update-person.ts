import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PersonDTO } from '../../../../types/PersonDTO';

@Component({
  selector: 'app-update-person',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './update-person.html',
  styleUrl: './update-person.css'
})
export class UpdatePerson {

  private _person?: PersonDTO;

  @Input()
  set person(value: PersonDTO | undefined) {

    if (value) {
      this._person = value;

      this.id = value.id;
      this.name = value.name;
      this.age = value.age;
      this.email = value.email;
    }

  }

  @Output()
  onUpdate = new EventEmitter<PersonDTO>();

  id = 0;
  name = '';
  age = 0;
  email = '';

  updatePerson() {

    if (this.id <= 0) {
      alert("Please select a person to edit.");
      return;
    }

    if (this.name.trim() === "") {
      alert("Name is required");
      return;
    }

    if (this.age <= 0) {
      alert("Age must be greater than 0");
      return;
    }

    if (this.email.trim() === "") {
      alert("Email is required");
      return;
    }

    this.onUpdate.emit({
      id: this.id,
      name: this.name,
      age: this.age,
      email: this.email
    });

    this.clearForm();
  }

  clearForm() {
    this.id = 0;
    this.name = "";
    this.age = 0;
    this.email = "";
  }

}