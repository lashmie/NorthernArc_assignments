import { Component, input } from '@angular/core';

@Component({
  selector: 'app-empty-state',
  templateUrl: './empty-state.html',
  styleUrl: './empty-state.css'
})
export class EmptyState {
  readonly icon = input<string>('bi-inbox');
  readonly title = input<string>('Nothing here yet');
  readonly message = input<string>('There is no data to display.');
}
