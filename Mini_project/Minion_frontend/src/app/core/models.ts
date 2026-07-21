// ---- Auth ----
export type Role = 'ADMIN' | 'MANAGER' | 'USER';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  expiresAtEpochMillis: number;
  role: Role;
}

export interface RegisterRequest {
  customerName: string;
  email: string;
  password: string;
  phoneNumber: string;
  city: string;
}

// ---- Customer ----
export interface Customer {
  customerId?: number;
  customerName: string;
  email: string;
  password?: string;
  phoneNumber: string;
  city: string;
  creditScore?: number;
  role?: Role;
}

// ---- Loans ----
export interface LoanCreateRequest {
  loanType: string;
  principalAmount: number;
  annualInterestRate: number;
  tenureMonths: number;
  customerId: number | null;
}

export interface LoanSummaryDTO {
  loanId: number;
  loanType: string;
  principalAmount: number;
  annualInterestRate: number;
  tenureMonths: number;
  emiAmount: number;
  disbursementDate: string;
  loanStatus: string;
  customerName: string;
  city: string;
}

export interface Loan {
  loanId: number;
  loanType: string;
  principalAmount: number;
  annualInterestRate: number;
  tenureMonths: number;
  emiAmount: number;
  disbursementDate: string;
  loanStatus: string;
}

// Spring Data Page wrapper
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}

// ---- EMI ----
export interface EmiPaymentRequest {
  amount: number;
  paymentMode: string;
  referenceNumber?: string;
}

export interface EmiSchedule {
  emiScheduleId?: number;
  dueDate?: string;
  amountDue?: number;
  amountPaid?: number;
  penaltyAmount?: number;
  daysPastDue?: number;
  status?: string;
  paymentDate?: string;
}

export interface EmiPayment {
  paymentId?: number;
  amount: number;
  paymentMode: string;
  paymentDate: string;
  referenceNumber: string;
}

// ---- Dashboards ----
export interface DashboardDTO {
  totalCustomers: number;
  totalLoans: number;
  activeLoans: number;
  closedLoans: number;
  overdueEMIs: number;
  totalEMICollected: number;
  totalPenaltyCollected: number;
  averageInterestRate: number;
  highestOutstandingLoan: LoanSummaryDTO | null;
  highestPayingCustomer: string | null;
  npaAccounts: number;
}

export interface LoanDashboardDTO {
  activeLoans: number;
  zeroOverdueLoans: number;
  averageInterestRate: number;
  totalPenaltyCollected: number;
  totalEmiCollected: number;
  highestOutstandingLoan: LoanSummaryDTO | null;
  emiCollectionByCity: { [city: string]: number };
}
