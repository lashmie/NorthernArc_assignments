import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PersonDTO } from '../../../../types/PersonDTO';

@Component({
  selector: 'app-add-person',
  imports: [FormsModule],
  templateUrl: './add-person.html',
  styleUrl: './add-person.css',
})

export class AddPerson {

  @Output()
  public onAdd = new EventEmitter<PersonDTO>();
  public newid!: number;
  public newname!: string;
  public newage!: number;
  public newemail!: string;

  addPerson() {
    const newPerson: PersonDTO = {
      id: this.newid,
      name: this.newname,
      age: this.newage,
      email: this.newemail
    };
    this.onAdd.emit(newPerson);
  }
}
 