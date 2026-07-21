import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Footer } from './component/footer/footer';
import { SideNavigation } from './component/side-navigation/side-navigation';
import { WebsideHeader } from './component/webside-header/webside-header';
import { MainBody } from './component/main-body/main-body';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('static_Components');
  name ="lavanyaElavarasan";
  func(event: any) {
    this.name = event.target.value;
  }
}
