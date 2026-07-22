import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
      <div class="container">
        <a class="navbar-brand fw-bold" routerLink="/">
          <i class="bi bi-music-note-beamed me-2"></i>MusicTrack
        </a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navContent"
          aria-controls="navContent"
          aria-expanded="false"
          aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navContent">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <a class="nav-link" routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{ exact: true }">
                <i class="bi bi-house-door me-1"></i>Home
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/tracks" routerLinkActive="active">
                <i class="bi bi-list-ul me-1"></i>Tracks
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/tracks/add" routerLinkActive="active">
                <i class="bi bi-plus-circle me-1"></i>Add Track
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/tracks/search" routerLinkActive="active">
                <i class="bi bi-search me-1"></i>Search
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  `
})
export class Navbar {}
