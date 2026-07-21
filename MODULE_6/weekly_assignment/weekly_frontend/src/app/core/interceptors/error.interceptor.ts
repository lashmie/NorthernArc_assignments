import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

/**
 * Global HTTP error interceptor.
 * Converts backend and connection errors into user-friendly messages.
 */
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const message = buildUserMessage(error);
      // Attach a friendly message so components can display it directly.
      return throwError(() => ({ ...error, userMessage: message }));
    })
  );
};

function buildUserMessage(error: HttpErrorResponse): string {
  // Connection / network error (server unreachable, CORS, etc.)
  if (error.status === 0) {
    return 'Unable to reach the server. Please make sure the backend is running at http://localhost:8080.';
  }

  // Prefer a message coming from the backend body when available.
  const backendMessage = extractBackendMessage(error);
  if (backendMessage) {
    return backendMessage;
  }

  switch (error.status) {
    case 400:
      return 'The request was invalid. Please check your input and try again.';
    case 404:
      return 'The requested scan could not be found.';
    case 409:
      return 'A scan with the same details already exists.';
    case 500:
      return 'Something went wrong on the server. Please try again later.';
    default:
      return `Unexpected error (status ${error.status}). Please try again.`;
  }
}

function extractBackendMessage(error: HttpErrorResponse): string | null {
  const body = error.error;
  if (!body) {
    return null;
  }
  if (typeof body === 'string') {
    return body.trim() ? body : null;
  }
  if (typeof body === 'object') {
    return body.message || body.error || null;
  }
  return null;
}
