import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  template: `
    <footer class="bg-dark text-light py-3 mt-5">
      <div class="container text-center">
        <small>
          <i class="bi bi-music-note-beamed me-1"></i>
          MusicTrack &copy; {{ year }} — Manage your music tracks with ease.
        </small>
      </div>
    </footer>
  `
})
export class Footer {
  readonly year = new Date().getFullYear();
}
