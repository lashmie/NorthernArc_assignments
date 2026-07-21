export interface Scan {
  id?: number;
  domainName: string;
  numPages: number;
  numBrokenLinks: number;
  numMissingImages: number;
  deleted?: boolean;
}

export type ScanOrderBy =
  | 'id'
  | 'domainName'
  | 'numPages'
  | 'numBrokenLinks'
  | 'numMissingImages'
  | 'deleted';

export const SCAN_ORDER_BY_OPTIONS: { value: ScanOrderBy; label: string }[] = [
  { value: 'id', label: 'ID' },
  { value: 'domainName', label: 'Domain Name' },
  { value: 'numPages', label: 'Number of Pages' },
  { value: 'numBrokenLinks', label: 'Number of Broken Links' },
  { value: 'numMissingImages', label: 'Number of Missing Images' },
  { value: 'deleted', label: 'Deleted' }
];
