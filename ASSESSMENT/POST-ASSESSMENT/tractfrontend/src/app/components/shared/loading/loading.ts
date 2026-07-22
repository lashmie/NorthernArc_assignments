import { Component } from '@angular/core';

@Component({
  selector: 'app-loading',
  template: `
    <div class="text-center my-5">
      <div class="spinner-border text-primary" role="status" style="width: 3rem; height: 3rem;">
        <span class="visually-hidden">Loading...</span>
      </div>
      <p class="text-muted mt-3 mb-0">Loading, please wait...</p>
    </div>
  `
})
export class Loading {}
