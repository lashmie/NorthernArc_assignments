import { Component, input } from '@angular/core';

@Component({
  selector: 'app-loading',
  templateUrl: './loading.html',
  styleUrl: './loading.css'
})
export class Loading {
  /** Optional message shown under the spinner. */
  readonly message = input<string>('Loading...');
}
