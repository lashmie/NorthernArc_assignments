import { Component, EventEmitter, Input, input, Output } from '@angular/core';
import { PersonDTO } from '../../../../types/PersonDTO';

@Component({
  selector: 'app-person-component',
  imports: [],
  templateUrl: './person-component.html',
  styleUrl: './person-component.css',
})
export class PersonComponent {
//   @Input() 
//   public p!: PersonDTO;
//   @Output() 
//   public onRemovePerson: EventEmitter<number> = new EventEmitter<number>();

//   public removePerson(pid: number): void {
//     this.onRemovePerson.emit(pid);
//   }


// @Output()
// public onEditPerson = new EventEmitter<PersonDTO>();

// editPerson() {
//     this.onEditPerson.emit(this.p);
// }
@Input()
p!: PersonDTO;

@Output()
onRemovePerson = new EventEmitter<number>();

@Output()
onEditPerson = new EventEmitter<PersonDTO>();

removePerson() {
  this.onRemovePerson.emit(this.p.id);
}

editPerson() {
  this.onEditPerson.emit(this.p);
}
}
