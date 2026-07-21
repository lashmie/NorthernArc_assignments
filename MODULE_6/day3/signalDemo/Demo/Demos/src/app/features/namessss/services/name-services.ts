import { Injectable, Service, signal, WritableSignal } from '@angular/core';


@Injectable({
    providedIn: 'root'
    })
export class NameServices {
    // we are using signals over here...
private names:WritableSignal<string[]> = signal(['John', 'Jane', 'Alice', 'Bob']);

getNames():WritableSignal<string[]> {
    return this.names;
}

addName(name:string) {
    this.names.set([...this.names(), name]);
}
removeName(name:string):void {
    this.names.set(this.names().filter(n => n !== name));
}
updateName(oldName:string, newName:string):void {
    this.names.set(this.names().map(n => n === oldName ? newName : n)); 
}
}
