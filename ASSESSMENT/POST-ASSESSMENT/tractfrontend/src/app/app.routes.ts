import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { TrackList } from './components/track-list/track-list';
import { AddTrack } from './components/add-track/add-track';
import { SearchTrack } from './components/search-track/search-track';
import { TrackDetails } from './components/track-details/track-details';
import { NotFound } from './not-found/not-found';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'tracks', component: TrackList },
  { path: 'tracks/add', component: AddTrack },
  { path: 'tracks/search', component: SearchTrack },
  { path: 'tracks/details/:title', component: TrackDetails },
  { path: '**', component: NotFound }
];
