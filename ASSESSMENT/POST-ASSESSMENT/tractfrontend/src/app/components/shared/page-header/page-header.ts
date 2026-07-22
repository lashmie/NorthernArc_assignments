import { Component, input } from '@angular/core';

@Component({
  selector: 'app-page-header',
  template: `
    <div class="d-flex align-items-center mb-4">
      <div>
        <h2 class="mb-1 fw-bold">
          @if (icon()) {
            <i class="bi {{ icon() }} me-2 text-primary"></i>
          }
          {{ title() }}
        </h2>
        @if (subtitle()) {
          <p class="text-muted mb-0">{{ subtitle() }}</p>
        }
      </div>
    </div>
  `
})
export class PageHeader {
  readonly title = input('');
  readonly subtitle = input('');
  readonly icon = input('');
}
