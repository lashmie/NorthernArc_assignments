import { Injectable, Service } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PeopleSservice {
    private names: string[] = ['Alice', 'Bob', 'Charlie'];
   private names$ = new BehaviorSubject<string[]>(this.names);

    getPeople(): BehaviorSubject<string[]> {
        return this.names$;
    }//async pipe (how we subscribe to the observable in the template)
   
    addPerson(name: string) :void{
       this.names$.next([...this.names, name]);
    }
    removePerson(name: string){
        const currentNames = this.names$.getValue();
        this.names$.next(currentNames.filter(n => n !== name));
    }
    // updatePerson(oldName: string, newName: string){
    //     const currentNames = this.names$.getValue();
    //     const index = currentNames.indexOf(oldName);
    //     if (index !== -1) {
    //         currentNames[index] = newName;
    //         this.names$.next([...currentNames]);
    //     }
    // }
    //update the names array and emit the new value to the BehaviorSubject
    //update by the map 
    updatePerson(oldName: string, newName: string){
        const currentNames = this.names$.getValue();
        const updatedNames = currentNames.map(n => n === oldName ? newName : n);
        this.names$.next(updatedNames);
    }

}
