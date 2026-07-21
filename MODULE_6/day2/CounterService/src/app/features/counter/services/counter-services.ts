import { Injectable, Service } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';

@Injectable({
  providedIn: 'root'
})

export class CounterServices {
    private counter$= new BehaviorSubject<number>(0);
    increment(): void {
        const currentValue = this.counter$.getValue();
        alert("Current Value: " + currentValue);
        this.counter$.next(currentValue + 1);
  
    }

    decrement(): void {
        const currentValue = this.counter$.getValue();
        this.counter$.next(currentValue - 1);
      
    }

    getCount(): BehaviorSubject<number> {
        //alert("Current Value: " + this.counter$.value);
      return this.counter$;
    }
    incrementByValue(value: number): void {
        const currentValue = this.counter$.getValue();
        this.counter$.next(currentValue + value);
    }

    decrementByValue(value: number): void {
        const currentValue = this.counter$.getValue();
        this.counter$.next(currentValue - value);
    }
}
