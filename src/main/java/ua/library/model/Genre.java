package ua.library.model;

public enum Genre {
    FICTION(14), 
    SCIENCE(30), 
    HISTORY(21), 
    BIOGRAPHY(21);

    private final int maxLoanDays;

    Genre(int maxLoanDays) {
        this.maxLoanDays = maxLoanDays;
    }

    public int getMaxLoanDays() {
        return maxLoanDays;
    }
}