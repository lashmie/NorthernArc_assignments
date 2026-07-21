import { Component } from '@angular/core';
import { PersonComponent } from '../person-component/person-component';
import { PersonDTO } from '../../../../types/PersonDTO';
import { AddPerson } from '../add-person/add-person';
import { UpdatePerson } from '../update-person/update-person';

@Component({
  selector: 'app-people',
  imports: [PersonComponent,AddPerson,UpdatePerson],
  templateUrl: './people.html',
  styleUrl: './people.css',
})
export class People {

  protected people = [
    {
      id: 1,
      name: 'John',
      age: 25,
      email: 'john@gmail.com'
    },
    {
      id: 2,
      name: 'Alice',
      age: 30,
      email: 'alice@gmail.com'
    }
  ];
  removePerson(pid: number): void {
    this.people = this.people.filter(person => person.id !== pid);
  }
  // addPerson($event:PersonDTO): void {
  //   this.people.push($event);
  // }
  //if it is signal: u can use only rest operator not this push
  addPerson(person: PersonDTO) {
    this.people.push(person);
  }

selectedPerson?: PersonDTO;

selectPerson(person: PersonDTO) {
  this.selectedPerson = { ...person };
}

updatePerson(updated: PersonDTO) {

  const index = this.people.findIndex(
    p => p.id === updated.id
  );

  if (index !== -1) {
    this.people[index] = { ...updated };
  }

  this.selectedPerson = undefined;
}
}
