import { Injectable, Service, signal, WritableSignal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CountService {
private count:WritableSignal<number> = signal(0);

getCount():WritableSignal<number> {
  return this.count;    
}
incrementCount():void { 
    this.count.set(this.count() + 1);  
 }
    decrementCount():void {
    this.count.set(this.count() - 1);   
}
    incrementBy(amount:number):void{
        this.count.set(this.count() + amount);
    }
}