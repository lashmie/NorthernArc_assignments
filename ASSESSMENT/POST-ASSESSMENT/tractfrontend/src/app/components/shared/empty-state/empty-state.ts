import { Component, input } from '@angular/core';

@Component({
  selector: 'app-empty-state',
  template: `
    <div class="text-center my-5 py-4">
      <i class="bi bi-inbox display-1 text-secondary"></i>
      <h4 class="mt-3">{{ title() }}</h4>
      <p class="text-muted">{{ message() }}</p>
    </div>
  `
})
export class EmptyState {
  readonly title = input('Nothing here yet');
  readonly message = input('There is no data to display.');
}
